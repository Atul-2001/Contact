package com.signature.ui;

import com.signature.model.Contact;
import com.signature.model.DataSource;
import com.signature.util.SearchUtility;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Optional;

public class Controller {

    @FXML
    private BorderPane root;
    @FXML
    private TextField search;
    @FXML
    private ImageView searchButton;
    @FXML
    private TableView<Contact> dataTable;
    @FXML
    private TableColumn<Contact, Circle> profileColumn;
    @FXML
    private TableColumn<Contact, String> nameColumn;
    @FXML
    private TableColumn<Contact, String> emailColumn;
    @FXML
    private TableColumn<Contact, String> phoneColumn;
    @FXML
    private VBox mainPanel;
    @FXML
    private VBox messageNode;

    private boolean fullScreen = false;
    private ObservableList<Contact> contacts;
    public FilteredList<Contact> filteredContacts;
    private SortedList<Contact> sortedContacts;
    private final boolean[] previousStatus = new boolean[5];

    @FXML
    public void initialize() {
        final ContextMenu contextMenu = new ContextMenu();
        final MenuItem editMenu = new MenuItem("Edit");
        final MenuItem deleteMenu = new MenuItem("Delete");

        editMenu.setOnAction(actionEvent -> editContact());

        deleteMenu.setOnAction(actionEvent -> deleteContact());

        contextMenu.getItems().addAll(editMenu, deleteMenu);

        Task<SortedList<Contact>> showContactsTask = new Task<>() {
            @Override
            protected SortedList<Contact> call() {
                contacts = DataSource.getInstance().getContacts();
                filteredContacts = new FilteredList<>(contacts);
                sortedContacts = new SortedList<>(filteredContacts, (o1, o2) -> {
                    if (o1.getFirstName().compareToIgnoreCase(o2.getFirstName()) == 0) {
                        if (o1.getLastName() != null && o2.getLastName() != null) {
                            return o1.getLastName().compareToIgnoreCase(o2.getLastName());
                        } else {
                            if (o1.getPhoneNumber().compareToIgnoreCase(o2.getPhoneNumber()) == 0) {
                                return o1.getEmail().compareToIgnoreCase(o2.getEmail());
                            } else {
                                return o1.getPhoneNumber().compareToIgnoreCase(o2.getPhoneNumber());
                            }
                        }
                    } else {
                        return o1.getFirstName().compareToIgnoreCase(o2.getFirstName());
                    }
                });
                return sortedContacts;
            }
        };

        dataTable.itemsProperty().bind(showContactsTask.valueProperty());
        new Thread(showContactsTask).start();

        profileColumn.setCellValueFactory(contact -> {
            Circle imageView = new Circle();
            String image = contact.getValue().getProfile();

            try {
                if (image != null) {
                    if (!image.isEmpty()) {
                        String testPath = (new URI(image)).normalize().getPath();
                        if (Main.isWindows) {
                            testPath = testPath.substring(1);
                        }
                        if (Files.exists(FileSystems.getDefault().getPath(testPath))) {
                            imageView.setFill(new ImagePattern(new Image(image)));
                        } else {
                            imageView.setFill(new ImagePattern(new Image(getClass().getResource("/icons/ic_account_circle_black_48dp.png").toString())));
                        }
                    } else {
                        imageView.setFill(new ImagePattern(new Image(getClass().getResource("/icons/ic_account_circle_black_48dp.png").toString())));
                    }
                } else {
                    imageView.setFill(new ImagePattern(new Image(getClass().getResource("/icons/ic_account_circle_black_48dp.png").toString())));
                }
            } catch (IllegalArgumentException | NullPointerException | URISyntaxException e) {
                System.err.println(e.getMessage());
            }

            imageView.setRadius(18);
            imageView.setSmooth(true);
            return new SimpleObjectProperty<>(imageView);
        });

        nameColumn.setCellValueFactory(contact -> {
            String firstName = contact.getValue().getFirstName();
            String lastName = contact.getValue().getLastName();
            if (lastName == null) {
                lastName = "";
            }
            return new SimpleStringProperty(firstName + " " + lastName);
        });

        dataTable.setRowFactory(param -> {
            TableRow<Contact> row = new TableRow<>();
            row.emptyProperty().addListener((observable, wasEmpty, isEmpty) -> {
                if (isEmpty) {
                    row.setContextMenu(null);
                } else {
                    row.setContextMenu(contextMenu);
                }
            });
            return row;
        });

        root.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.DELETE)) {
                deleteContact();
            } else if (keyEvent.getCode().equals(KeyCode.F2)) {
                editContact();
            } else {
                final KeyCombination keyCombination = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);
                if (keyCombination.match(keyEvent)) {
                    showAddContact(null);
                }
            }
        });
    }

    protected void constructResponsiveUI() {
        Platform.runLater(() -> root.getScene().getWindow().widthProperty().addListener((observableValue, oldValue, newValue) -> {
                    if (newValue.doubleValue() <= 600.0 /*&& root.getCenter() == mainPanel*/) {
                        if (oldValue.doubleValue() > newValue.doubleValue()) {
                            if (dataTable.getColumns().size() == 3) {
                                dataTable.getColumns().remove(phoneColumn);
                            }
                            if (fullScreen && root.getRight() == messageNode) {
                                root.setRight(null);
                                dataTable.getColumns().removeAll(emailColumn, phoneColumn);
                            } else if (fullScreen) {
                                Node node = root.getRight();
                                root.setRight(null);
                                root.setCenter(node);
                                ((Stage) root.getScene().getWindow()).setMinWidth(500.0);
                            }
                        }
                        fullScreen = false;
                    } else if (newValue.doubleValue() <= 820.0 /*&& root.getCenter() == mainPanel*/) {
                        if (oldValue.doubleValue() > newValue.doubleValue()) {
                            if (dataTable.getColumns().size() == 4) {
                                dataTable.getColumns().remove(emailColumn);
                            }
                            if (fullScreen && root.getRight() == messageNode) {
                                root.setRight(null);
                            } else if (fullScreen) {
                                Node node = root.getRight();
                                root.setRight(null);
                                root.setCenter(node);
                                ((Stage) root.getScene().getWindow()).setMinWidth(500.0);
                            }
                        } else if (oldValue.doubleValue() < newValue.doubleValue() && dataTable.getColumns().size() == 2) {
                            dataTable.getColumns().add(phoneColumn);
                        }
                        fullScreen = false;
                    } else if (newValue.doubleValue() <= 1000.0) {
                        if (oldValue.doubleValue() > newValue.doubleValue()) {
                            if (dataTable.getColumns().size() == 2 || fullScreen) {
                                if (root.getRight() != null && root.getRight().getId().equalsIgnoreCase("messageNode")) {
                                    root.setRight(null);
                                    if (!fullScreen) {
                                        dataTable.getColumns().addAll(emailColumn, phoneColumn);
                                    }
                                } else if (root.getRight() != null) {
                                    Node node = root.getRight();
                                    root.setRight(null);
                                    root.setCenter(node);
                                    ((Stage) root.getScene().getWindow()).setMinWidth(500.0);
                                }
                            }
                        } else if (oldValue.doubleValue() < newValue.doubleValue() && dataTable.getColumns().size() == 3) {
                            dataTable.getColumns().add(2, emailColumn);
                        }
                        fullScreen = false;
                    } else if (newValue.doubleValue() <= 1100.0) {
                        if (oldValue.doubleValue() > newValue.doubleValue()) {
                            if (dataTable.getColumns().size() == 3) {
                                dataTable.getColumns().remove(phoneColumn);
                            }
                            if (fullScreen) {
                                dataTable.getColumns().removeAll(emailColumn, phoneColumn);
                            }
                        } else if (oldValue.doubleValue() < newValue.doubleValue() && dataTable.getColumns().size() == 4 && root.getCenter() == mainPanel) {
                            root.setRight(messageNode);
                            dataTable.getColumns().removeAll(emailColumn, phoneColumn);
                        } else if (oldValue.doubleValue() < newValue.doubleValue() && (dataTable.getColumns().size() == 2 || dataTable.getColumns().size() == 4) && root.getCenter() != mainPanel) {
                            Node node = root.getCenter();
                            root.setCenter(null);
                            root.setRight(node);
                            root.setCenter(mainPanel);
                            ((Stage) root.getScene().getWindow()).setMinWidth(460.0);
                            if (dataTable.getColumns().size() == 4) {
                                dataTable.getColumns().removeAll(emailColumn, phoneColumn);
                            }
                        }
                        fullScreen = false;
                    } else if (newValue.doubleValue() <= 1200.0) {
                        if (fullScreen || ((oldValue.doubleValue() > newValue.doubleValue()) && (dataTable.getColumns().size() == 4))) {
                            dataTable.getColumns().remove(emailColumn);
                        } else if ((oldValue.doubleValue() < newValue.doubleValue()) && (dataTable.getColumns().size() == 2)) {
                            dataTable.getColumns().add(phoneColumn);
                        }
                        fullScreen = false;
                    } else if ((newValue.doubleValue() > 1200.0) && oldValue.doubleValue() < newValue.doubleValue()) {
                        fullScreen = true;
                        if (root.getCenter() != mainPanel) {
                            Node node = root.getCenter();
                            root.setCenter(null);
                            root.setRight(node);
                            root.setCenter(mainPanel);
                        } else if (root.getRight() == null) {
                            root.setRight(messageNode);
                        }

                        if (dataTable.getColumns().size() == 2) {
                            dataTable.getColumns().addAll(emailColumn, phoneColumn);
                        } else if (dataTable.getColumns().size() == 3) {
                            dataTable.getColumns().add(2, emailColumn);
                        }
                    }
                })
        );
    }

    private void loadAddEditPane() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddEditPane.fxml"));
        VBox addEditPane = loader.load();
        AddEditController controller = loader.getController();
        controller.parent = root;
        controller.parentCenter = mainPanel;
        controller.messageNode = messageNode;
        if (root.getScene().getWindow().getWidth() <= 1000.0) {
            root.setCenter(addEditPane);
        } else {
            root.setRight(addEditPane);
        }
    }

    @FXML
    public void showAddContact(ActionEvent actionEvent) {
        Contact tempContact = AddEditController.contactToEdit;
        boolean tempStatus = AddEditController.fromShowContact;
        try {
            AddEditController.contactToEdit = null;
            AddEditController.fromShowContact = false;
            loadAddEditPane();
        } catch (IOException e) {
            AddEditController.contactToEdit = tempContact;
            AddEditController.fromShowContact = tempStatus;
            System.out.println(e.getMessage());
        }
    }

    public void addContactToList(Contact contact) {
        contacts.addAll(contact);
        dataTable.refresh();
        dataTable.getSelectionModel().select(contact);
    }

    @FXML
    public void handleShowContactM(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            handleShowContact(null);
        }
    }

    @FXML
    public void handleShowContact(KeyEvent keyEvent) {
        Contact contact = dataTable.getSelectionModel().getSelectedItem();
        if (contact != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowContact.fxml"));
                VBox showContactPane = loader.load();
                ShowContactController controller = loader.getController();
                controller.parent = root;
                controller.parentCenter = mainPanel;
                controller.messageNode = messageNode;
                controller.loadData(contact);
                if (root.getScene().getWindow().getWidth() <= 1000.0) {
                    root.setCenter(showContactPane);
                } else {
                    root.setRight(showContactPane);
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void editContact() {
        Contact contact = dataTable.getSelectionModel().getSelectedItem();
        if (contact != null) {
            try {
                AddEditController.contactToEdit = dataTable.getSelectionModel().getSelectedItem();
                AddEditController.fromShowContact = false;
                loadAddEditPane();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                AddEditController.contactToEdit = null;
                AddEditController.fromShowContact = false;
            }
        }
    }

    public void updateContactInList(Contact oldContact, Contact newContact) {

        int index = contacts.indexOf(oldContact);
        contacts.set(index, newContact);
        refreshFilteredList(newContact);
    }

    private void deleteContact() {
        Contact contactToDelete = dataTable.getSelectionModel().getSelectedItem();

        if (contactToDelete != null) {
            Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
            deleteAlert.setTitle("Delete Contact?");
            String firstName = contactToDelete.getFirstName();
            String lastName = contactToDelete.getLastName();
            if (lastName == null) {
                lastName = "";
            }
            deleteAlert.setHeaderText("Contact : " + firstName + " " + lastName);
            deleteAlert.setContentText("Are you sure to delete the contact ?\nPress OK to Delete, Cancel to go back!");

            Optional<ButtonType> response = deleteAlert.showAndWait();
            if (response.isPresent() && response.get().equals(ButtonType.OK)) {
                Platform.runLater(() -> {
                    if (DataSource.getInstance().deleteContact(contactToDelete)) {
                        removeContactFromList(contactToDelete);
                    }
                });
            }
        }
    }

    public void removeContactFromList(Contact contact) {
        contacts.remove(contact);
        if (root.getScene().getWindow().getWidth() <= 1000.0) {
            root.setCenter(mainPanel);
        } else {
            root.setRight(messageNode);
        }
        dataTable.refresh();
    }

    @FXML
    public void handleFilterContact(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(root.getScene().getWindow());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FilterAndSortDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(loader.load());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        FilterAndSortController controller = loader.getController();
        controller.setPreviousStatus(previousStatus);
        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get().equals(ButtonType.OK)) {
            Contact contactToSelect = dataTable.getSelectionModel().getSelectedItem();
            Platform.runLater(() -> {
                if (controller.byFirstName()) {
                    previousStatus[0] = true;
                    previousStatus[1] = false;
                    sortedContacts.setComparator((o1, o2) -> {
                        if (o1.getFirstName().compareToIgnoreCase(o2.getFirstName()) == 0) {
                            if (!o1.getLastName().isEmpty() && !o2.getLastName().isEmpty()) {
                                return o1.getLastName().compareToIgnoreCase(o2.getLastName());
                            } else {
                                if (o1.getPhoneNumber().compareToIgnoreCase(o2.getPhoneNumber()) == 0) {
                                    return o1.getEmail().compareToIgnoreCase(o2.getEmail());
                                } else {
                                    return o1.getPhoneNumber().compareToIgnoreCase(o2.getPhoneNumber());
                                }
                            }
                        } else {
                            return o1.getFirstName().compareToIgnoreCase(o2.getFirstName());
                        }
                    });
                } else if (controller.byLastName()) {
                    previousStatus[1] = true;
                    previousStatus[0] = false;
                    sortedContacts.setComparator((o1, o2) -> {
                        if (!o1.getLastName().isEmpty() && !o2.getLastName().isEmpty()) {
                            if (o1.getLastName().compareToIgnoreCase(o2.getLastName()) == 0) {
                                return o1.getFirstName().compareToIgnoreCase(o2.getFirstName());
                            } else {
                                return o1.getLastName().compareToIgnoreCase(o2.getLastName());
                            }
                        } else {
                            if (o1.getFirstName().compareToIgnoreCase(o2.getFirstName()) == 0) {
                                if (o1.getPhoneNumber().compareToIgnoreCase(o2.getPhoneNumber()) == 0) {
                                    return o1.getEmail().compareToIgnoreCase(o2.getEmail());
                                } else {
                                    return o1.getPhoneNumber().compareToIgnoreCase(o2.getPhoneNumber());
                                }
                            } else {
                                return o1.getFirstName().compareToIgnoreCase(o2.getFirstName());
                            }
                        }
                    });
                }

                if (controller.byWithoutLastName()) {
                    previousStatus[2] = true;
                    filteredContacts.setPredicate(contact -> contact.getLastName().isEmpty());
                } else {
                    previousStatus[2] = false;
                }

                if (controller.byWithoutPhoneNo()) {
                    previousStatus[3] = true;
                    filteredContacts.setPredicate(contact -> contact.getPhoneNumber().isEmpty());
                } else {
                    previousStatus[3] = false;
                }

                if (controller.byFavouriteContact()) {
                    previousStatus[4] = true;
                    filteredContacts.setPredicate(Contact::isFavourite);
                } else {
                    previousStatus[4] = false;
                }

                if (!previousStatus[2] && !previousStatus[3] && !previousStatus[4]) {
                    filteredContacts.setPredicate(contact -> true);
                }

                if (contactToSelect != null) {
                    refreshFilteredList(contactToSelect);
                }

                searchButton.setImage(new Image(getClass().getResource("/icons/ic_search_black_18dp.png").toString()));
                search.clear();
            });
        }
    }

    public void refreshFilteredList(Contact contactToSelect) {

        if (contactToSelect != null) {
            if (sortedContacts.contains(contactToSelect)) {
                dataTable.getSelectionModel().select(contactToSelect);
            } else if (!sortedContacts.isEmpty()){
                dataTable.getSelectionModel().selectFirst();
                if (root.getScene().getWindow().getWidth() > 1000) {
                    handleShowContact(null);
                }
            } else {
                if (root.getScene().getWindow().getWidth() > 1000.0 && root.getRight() != messageNode) {
                    root.setRight(messageNode);
                }
            }
        }
    }

    @FXML
    public void openSetting(ActionEvent actionEvent) {
        Alert messageDialog = new Alert(Alert.AlertType.INFORMATION);
        messageDialog.initOwner(root.getScene().getWindow());
        messageDialog.setTitle("Setting");
        messageDialog.setHeaderText("Message");
        Label msgLabel = new Label("Coming Soon!");
        msgLabel.setFont(Font.font("Roboto", FontWeight.BOLD, 40));
        msgLabel.setTextFill(Color.RED);
        messageDialog.getDialogPane().setContent(msgLabel);
        messageDialog.showAndWait();
    }

    @FXML
    public void performLiveSearch(KeyEvent keyEvent) {
        if (search.textProperty().isEmpty().get()) {
            searchButton.setImage(new Image(getClass().getResource("/icons/ic_search_black_18dp.png").toString()));
            filteredContacts.setPredicate(contact -> true);
        } else {
            Platform.runLater(() -> {
                filteredContacts.setPredicate(contact -> true);
                SearchUtility.search(search.getText());
            });
            searchButton.setImage(new Image(getClass().getResource("/icons/ic_clear_black_18dp.png").toString()));
        }
    }

    @FXML
    public void clearSearch(MouseEvent mouseEvent) {
        if (!search.textProperty().isEmpty().get()) {
            search.clear();
            searchButton.setImage(new Image(getClass().getResource("/icons/ic_search_black_18dp.png").toString()));
            filteredContacts.setPredicate(contact -> true);

            Arrays.fill(previousStatus, false);
        }
    }
}