package com.signature.ui;

import com.jfoenix.controls.*;
import com.signature.model.Contact;
import com.signature.model.DataSource;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class AddEditController {

    @FXML
    private VBox addEditPane;
    @FXML
    private VBox contactForm;
    @FXML
    private JFXButton back;
    @FXML
    private SVGPath headingIcon;
    @FXML
    private Label headingLabel;
    @FXML
    protected BorderPane parent;
    @FXML
    protected VBox parentCenter;
    @FXML
    protected VBox messageNode;
    @FXML
    private Circle profile;
    @FXML
    private SVGPath addProfile;
    @FXML
    private JFXButton removeProfile;
    @FXML
    private JFXTextField firstName;
    @FXML
    private JFXTextField lastName;
    @FXML
    private JFXTextField phoneNumber;
    @FXML
    private JFXComboBox<String> phoneType;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXComboBox<String> emailType;
    @FXML
    private JFXTextField address;
    @FXML
    private Label moreFields;
    @FXML
    private GridPane moreFieldsPane;
    @FXML
    private JFXTextField company;
    @FXML
    private JFXTextField website;
    @FXML
    private JFXDatePicker date;
    @FXML
    private JFXComboBox<String> dateType;
    @FXML
    private JFXTextField relation;
    @FXML
    private JFXComboBox<String> relationType;
    @FXML
    private JFXTextField sip;
    @FXML
    private JFXTextField notes;
    @FXML
    private JFXButton save, cancel;

    private String profileSourcePath = null;
    protected static Contact contactToEdit = null;
    protected static boolean fromShowContact = false;

    public void initialize() {
        addEditPane.widthProperty().addListener(((observable, oldValue, newValue) -> back.setVisible(parent.getRight() == null)));
        profile.setFill(new ImagePattern(new Image(getClass().getResource("/icons/ic_account_circle_black_48dp.png").toString())));
        setKeyBinding();
        contactForm.getChildren().remove(moreFieldsPane);
        if (contactToEdit != null) {
            loadContact();
        }

    }

    private void setKeyBinding() {
        firstName.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER) || keyEvent.getCode().equals(KeyCode.DOWN)) {
                lastName.requestFocus();
//                Platform.runLater(() -> {scrollView.setVvalue(0.0);});
            }
        });

        lastName.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER) || keyEvent.getCode().equals(KeyCode.DOWN)) {
                phoneNumber.requestFocus();
//                Platform.runLater(() -> {scrollView.setVvalue(0.0);});
            } else if (keyEvent.getCode().equals(KeyCode.UP)) {
                firstName.requestFocus();
//                Platform.runLater(() -> {scrollView.setVvalue(0.0);});
            }
        });

        phoneNumber.setOnKeyPressed(keyEvent -> {
            phoneNumber.setEditable(enableNumericField(keyEvent));
            if (keyEvent.getCode().equals(KeyCode.ENTER) || keyEvent.getCode().equals(KeyCode.DOWN)) {
                phoneType.requestFocus();
//                Platform.runLater(() -> {scrollView.setVvalue(0.0);});
            } else if (keyEvent.getCode().equals(KeyCode.UP)) {
                lastName.requestFocus();
//                Platform.runLater(() -> {scrollView.setVvalue(0.0);});
            }
        });

        phoneType.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                email.requestFocus();
//                Platform.runLater(() -> {scrollView.setVvalue(0.0);});
            }
        });

        email.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER) || keyEvent.getCode().equals(KeyCode.DOWN)) {
                emailType.requestFocus();
//                Platform.runLater(() -> {scrollView.setVvalue(0.0);});
            } else if (keyEvent.getCode().equals(KeyCode.UP)) {
                phoneType.requestFocus();
//                Platform.runLater(() -> {scrollView.setVvalue(0.0);});
            }
        });

        emailType.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                if (emailType.isFocused()) {
                    address.requestFocus();
                    if (contactForm.getChildren().lastIndexOf(moreFields) == 7) {
//                        Platform.runLater(() -> {scrollView.setVvalue(0.546875);});
                    } else {
//                        Platform.runLater(() -> {scrollView.setVvalue(0.09275);});
                    }
                }
            }
        });

        address.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER) || keyEvent.getCode().equals(KeyCode.DOWN)) {
                if (contactForm.getChildren().lastIndexOf(moreFields) == 1) {
                    if (save.isDisable()) {
                        cancel.requestFocus();
                    } else {
                        save.requestFocus();
                    }
//                    Platform.runLater(() -> {scrollView.setVvalue(1.0);});
                } else {
                    company.requestFocus();
//                    Platform.runLater(() -> {scrollView.setVvalue(0.2);});
                }
            } else if (keyEvent.getCode().equals(KeyCode.UP)) {
                emailType.requestFocus();
//                Platform.runLater(() -> {scrollView.setVvalue(0.0);});
            }
        });

        company.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER) || keyEvent.getCode().equals(KeyCode.DOWN)) {
                website.requestFocus();
//                Platform.runLater(() -> {scrollView.setVvalue(0.3);});
            } else if (keyEvent.getCode().equals(KeyCode.UP)) {
                address.requestFocus();
//                Platform.runLater(() -> {scrollView.setVvalue(0.2);});
            }
        });

        website.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER) || keyEvent.getCode().equals(KeyCode.DOWN)) {
                date.requestFocus();
//                Platform.runLater(() -> {scrollView.setVvalue(0.41285);});
            } else if (keyEvent.getCode().equals(KeyCode.UP)) {
                company.requestFocus();
//                Platform.runLater(() -> {scrollView.setVvalue(0.4);});
            }
        });

        date.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                dateType.requestFocus();
//                Platform.runLater(() -> {scrollView.setVvalue(0.5);});
            }
        });

        dateType.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                relation.requestFocus();
//                Platform.runLater(() -> {scrollView.setVvalue(0.62);});
            }
        });

        relation.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER) || keyEvent.getCode().equals(KeyCode.DOWN)) {
                relationType.requestFocus();
//                Platform.runLater(() -> {scrollView.setVvalue(0.7145);});
            } else if (keyEvent.getCode().equals(KeyCode.UP)) {
                dateType.requestFocus();
//                Platform.runLater(() -> {scrollView.setVvalue(0.6);});
            }
        });

        relationType.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER)) {
                sip.requestFocus();
//                Platform.runLater(() -> {scrollView.setVvalue(0.825);});
            }
        });

        sip.setOnKeyPressed(keyEvent -> {
            sip.setEditable(enableNumericField(keyEvent));
            if (keyEvent.getCode().equals(KeyCode.ENTER) || keyEvent.getCode().equals(KeyCode.DOWN)) {
                notes.requestFocus();
//                Platform.runLater(() -> {scrollView.setVvalue(0.94);});
            } else if (keyEvent.getCode().equals(KeyCode.UP)) {
                relationType.requestFocus();
//                Platform.runLater(() -> {scrollView.setVvalue(1.0);});
            }
        });

        notes.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.ENTER) || keyEvent.getCode().equals(KeyCode.DOWN)) {
                if (save.isDisable()) {
                    cancel.requestFocus();
                } else {
                    save.requestFocus();
                }
//                Platform.runLater(() -> {scrollView.setVvalue(1.0);});
            } else if (keyEvent.getCode().equals(KeyCode.UP)) {
                sip.requestFocus();
//                Platform.runLater(() -> {scrollView.setVvalue(1.0);});
            }
        });

        save.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.UP)) {
                if (contactForm.getChildren().lastIndexOf(moreFields) == 1) {
                    address.requestFocus();
                } else {
                    notes.requestFocus();
                }
            } else if (keyEvent.getCode().equals(KeyCode.RIGHT)) {
                cancel.requestFocus();
            }
        });

        cancel.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.UP)) {
                if (contactForm.getChildren().lastIndexOf(moreFields) == 1) {
                    address.requestFocus();
                } else {
                    notes.requestFocus();
                }
            } else if (keyEvent.getCode().equals(KeyCode.LEFT)) {
                if (!save.isDisable()) {
                    save.requestFocus();
                }
            }
        });

        contactForm.setOnKeyReleased(keyEvent -> {
            BooleanBinding buttonStatus = firstName.textProperty().isEmpty()
                    .or(phoneNumber.textProperty().isEmpty());
            save.setDisable(buttonStatus.get());
        });

    }

    @FXML
    private boolean enableNumericField(KeyEvent keyEvent) {
//        final KeyCombination keyCombination = new KeyCodeCombination(KeyCode.DIGIT0, KeyCombination.SHIFT_DOWN);
        return keyEvent.getCode().isDigitKey()
                || keyEvent.getCode().equals(KeyCode.PLUS)
                || keyEvent.getCode().equals(KeyCode.BACK_SPACE)
                || keyEvent.getCode().equals(KeyCode.RIGHT)
                || keyEvent.getCode().equals(KeyCode.LEFT)
                || keyEvent.getCode().equals(KeyCode.UP)
                || keyEvent.getCode().equals(KeyCode.DOWN)
                || keyEvent.getCode().equals(KeyCode.HOME)
                || keyEvent.getCode().equals(KeyCode.END);
    }

    @FXML
    public void handleShowFields(MouseEvent mouseEvent) {
        if (moreFields.getText().equals("More fields")) {
            contactForm.getChildren().remove(moreFields);
            contactForm.getChildren().addAll(moreFieldsPane);
            contactForm.getChildren().add(moreFields);
            moreFields.setText("Less fields");
        } else if (moreFields.getText().equals("Less fields")) {
            contactForm.getChildren().removeAll(moreFieldsPane);
            moreFields.setText("More fields");
        }
    }

    @FXML
    public void handleBackAction(ActionEvent actionEvent) {
        handleCancelAction(null);
    }

    @FXML
    public void showAddProfile(MouseEvent mouseEvent) {
        profile.setOpacity(0.5);
        addProfile.setOpacity(1.0);
    }

    @FXML
    public void hideAddProfile(MouseEvent mouseEvent) {
        addProfile.setOpacity(0.0);
        profile.setOpacity(1.0);
    }

    @FXML
    public void handleAddProfile(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Images", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.tif"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + File.separator + "Pictures"));
        File imageFile = fileChooser.showOpenDialog(parent.getScene().getWindow());
        if (imageFile != null) {
            profileSourcePath = imageFile.toURI().toString();
            Image image = new Image(profileSourcePath);
            profile.setFill(new ImagePattern(image));
            profile.setStroke(Color.rgb(60, 186, 84));
            removeProfile.setVisible(true);
        }

        if (contactToEdit != null) {
            save.setDisable(false);
        }
    }

    @FXML
    public void handleRemoveProfile(ActionEvent actionEvent) {
        profile.setFill(new ImagePattern(new Image(getClass().getResource("/icons/ic_account_circle_black_48dp.png").toString())));
        profile.setStroke(Color.rgb(242, 193, 15));
        removeProfile.setVisible(false);
        profileSourcePath = null;
        if (contactToEdit != null) {
            save.setDisable(false);
        }
    }

    @FXML
    public void handleCancelAction(ActionEvent actionEvent) {
        if (save.isDisable()) {
            cancelOperation();
        } else {
            int result = cancelConfirmationDialog();
            if (result == 1) {
                handleSaveAction(null);
            } else if (result == -1) {
                cancelOperation();
            }
        }
    }

    private void cancelOperation() {
        if (contactToEdit != null && fromShowContact) {
            showContact();
        } else {
            if (parent.getRight() == null) {
                parent.setCenter(parentCenter);
            } else {
                parent.setRight(messageNode);
            }
        }
        contactToEdit = null;
        fromShowContact = false;
    }

    private int cancelConfirmationDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(parent.getScene().getWindow());
        dialog.setTitle("Save changes?");
        dialog.setContentText("You can save your changes now or discard them.");
        ButtonType save = new ButtonType("Save");
        dialog.getDialogPane().getButtonTypes().add(save);
        ButtonType discard = new ButtonType("Discard");
        dialog.getDialogPane().getButtonTypes().add(discard);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);


        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get().equals(save)) {
            return 1;
        } else if (result.isPresent() && result.get().equals(discard)) {
            return -1;
        } else {
            return 0;
        }
    }

    @FXML
    public void handleSaveAction(ActionEvent actionEvent) {
        if (contactToEdit == null) {
            Contact contact = getData();
            Platform.runLater(() -> {
                if (DataSource.getInstance().addContact(contact)) {
                    Main.controller.addContactToList(contact);
                    contactToEdit = contact;
                    showContact();
                    Main.controller.refreshFilteredList(contactToEdit);
                }
            });
        } else {
            Platform.runLater(this::updateContact);
        }
    }

    private Contact getData() {
        String profile = profileSourcePath;
        String FirstName = firstName.getText().trim().replaceAll("\\s+", " ");
        String LastName = lastName.getText() == null ? "" : lastName.getText().trim().replaceAll("\\s+", " ");
        String PhoneNumber = phoneNumber.getText() == null ? "" : phoneNumber.getText().trim();
        String PhoneLabel = phoneType.getSelectionModel().getSelectedItem();
        String Email = email.getText() == null ? "" : email.getText().trim().replaceAll("\\s+", "");
        String EmailLabel = emailType.getSelectionModel().getSelectedItem();
        String Address = address.getText() == null ? "" : address.getText().trim();
        String Company = company.getText() == null ? "" : company.getText().trim();
        String Website = website.getText() == null ? "" : website.getText().trim();
        String dateField;
        try {
            dateField = date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (NullPointerException e) {
            dateField = "";
        }
        String dateLabel = dateType.getSelectionModel().getSelectedItem();
        String Relation = relation.getText() == null ? "" : relation.getText().trim();
        String relationLabel = relationType.getSelectionModel().getSelectedItem();
        String SIP = sip.getText() == null ? "" : sip.getText().trim();
        String Notes = notes.getText() == null ? "" : notes.getText().trim();

        return new Contact(profile,
                FirstName,
                LastName,
                PhoneNumber,
                PhoneLabel,
                Email,
                EmailLabel,
                Address,
                Company,
                Website,
                dateField,
                dateLabel,
                Relation,
                relationLabel,
                SIP,
                Notes);
    }

    private void updateContact() {
        Contact newContact = getData();
        newContact.setId(contactToEdit.getId());
        if (DataSource.getInstance().updateContact(newContact)) {
            Main.controller.updateContactInList(contactToEdit, newContact);
            contactToEdit = newContact;
            showContact();
        }
    }

    private void loadContact() {
        headingLabel.setText("Edit Contact");
        headingIcon.setContent("M6 34.5V42h7.5l22.13-22.13-7.5-7.5L6 34.5zm35.41-20.41c.78-.78.78-2.05 0-2.83l-4.67-4.67c-.78-.78-2.05-.78-2.83 0l-3.66 3.66 7.5 7.5 3.66-3.66z");
        if (contactToEdit.getProfile() != null) {
            if (!contactToEdit.getProfile().isEmpty()) {
                try {
                    profileSourcePath = contactToEdit.getProfile();
                    String testPath = (new URI(profileSourcePath)).normalize().getPath();
                    if (Main.isWindows) {
                        testPath = testPath.substring(1);
                    }
                    if (Files.exists(FileSystems.getDefault().getPath(testPath))) {
                        profile.setFill(new ImagePattern(new Image(profileSourcePath)));
                        removeProfile.setVisible(true);
                    } else {
                        profile.setFill(new ImagePattern(new Image(getClass().getResource("/icons/ic_account_circle_black_48dp.png").toString())));
                    }
                } catch (IllegalArgumentException | NullPointerException | URISyntaxException e) {
                    System.err.println(e.getMessage());
                }
            }
        }

        String Company = contactToEdit.getCompany();
        String Website = contactToEdit.getWebsite();
        String dateField = contactToEdit.getDate();
        String Relation = contactToEdit.getRelation();
        String SIP = contactToEdit.getSip();
        String Notes = contactToEdit.getNotes();

        firstName.setText(contactToEdit.getFirstName());
        lastName.setText(contactToEdit.getLastName());
        phoneNumber.setText(contactToEdit.getPhoneNumber());
        phoneType.getSelectionModel().select(contactToEdit.getPhoneType());
        email.setText(contactToEdit.getEmail());
        String EmailType = contactToEdit.getEmailType();
        if (EmailType == null) {
            emailType.getSelectionModel().select(0);
        } else if (EmailType.isEmpty()) {
            emailType.getSelectionModel().select(0);
        } else {
            emailType.getSelectionModel().select(EmailType);
        }
        address.setText(contactToEdit.getAddress());
        company.setText(Company);
        website.setText(Website);
        try {
            LocalDate localDate = LocalDate.parse(contactToEdit.getDate());
            date.setValue(localDate);
        } catch (Exception e) {
//            System.out.println("Empty date exception!");
        }
        String DateType = contactToEdit.getDateType();
        if (DateType == null) {
            dateType.getSelectionModel().select(0);
        } else if (DateType.isEmpty()) {
            dateType.getSelectionModel().select(0);
        } else {
            dateType.getSelectionModel().select(DateType);
        }
        relation.setText(Relation);
        String RelationType = contactToEdit.getRelationType();
        if (RelationType == null) {
            relationType.getSelectionModel().select(0);
        } else if (RelationType.isEmpty()) {
            relationType.getSelectionModel().select(0);
        } else {
            relationType.getSelectionModel().select(RelationType);
        }
        sip.setText(SIP);
        notes.setText(Notes);


        try {
            if ((Company != null) || (Website != null)
            || (dateField != null) || (Relation != null)
            || (SIP != null) || (Notes != null)) {

                assert Company != null;
                if (!Company.isEmpty() || !Website.isEmpty()
                        || !dateField.isEmpty() || !Relation.isEmpty()
                        || !SIP.isEmpty() || !Notes.isEmpty()) {
                    throw new NullPointerException();
                }
            }
        } catch (Exception e) {
            contactForm.getChildren().remove(moreFields);
            contactForm.getChildren().addAll(moreFieldsPane);
            contactForm.getChildren().add(moreFields);
            moreFields.setText("Less fields");
        }
    }

    private void showContact() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ShowContact.fxml"));
            VBox showContactPane = loader.load();
            ShowContactController controller = loader.getController();
            controller.parent = parent;
            controller.parentCenter = parentCenter;
            controller.messageNode = messageNode;
            controller.loadData(contactToEdit);
            if (parent.getScene().getWindow().getWidth() <= 1000.0) {
                parent.setCenter(showContactPane);
            } else {
                parent.setRight(showContactPane);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
