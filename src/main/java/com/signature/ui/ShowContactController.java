package com.signature.ui;

import com.jfoenix.controls.JFXButton;
import com.signature.model.Contact;
import com.signature.model.DataSource;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Optional;

public class ShowContactController {

    @FXML
    private VBox rootNode;
    @FXML
    private JFXButton back;
    @FXML
    private JFXButton favourite;
    @FXML
    private VBox detailBox;
    @FXML
    private Circle profile;
    @FXML
    private Label name;
    @FXML
    private Label phoneNumber;
    @FXML
    private HBox emailHB;
    @FXML
    private Label email;
    @FXML
    private HBox addressHB;
    @FXML
    private Label address;
    @FXML
    private HBox companyHB;
    @FXML
    private Label company;
    @FXML
    private HBox websiteHB;
    @FXML
    private Label website;
    @FXML
    private HBox dateHB;
    @FXML
    private Label date;
    @FXML
    private HBox relationHB;
    @FXML
    private Label relation;
    @FXML
    private HBox sipHB;
    @FXML
    private Label sip;
    @FXML
    private HBox notesHB;
    @FXML
    private Label notes;
    @FXML
    protected BorderPane parent;
    @FXML
    protected VBox parentCenter;
    @FXML
    protected VBox messageNode;
    
    protected static Contact contactToView = null;

    public void initialize() {
        rootNode.widthProperty().addListener((observable, oldValue, newValue) -> back.setVisible(parent.getRight() == null));
    }

    public void loadData(Contact contact) {
        contactToView = contact;
        String Profile = contact.getProfile();
        String firstName = contact.getFirstName();
        String lastName = contact.getLastName();
        String phone = contact.getPhoneNumber();
        String phoneType = contact.getPhoneType();
        String e_mail = contact.getEmail();
        String emailType = contact.getEmailType();
        String Address = contact.getAddress();
        String Company = contact.getCompany();
        String Website = contact.getWebsite();
        String dateField = contact.getDate();
        String dateType = contact.getDateType();
        String Relation = contact.getRelation();
        String relationType = contact.getRelationType();
        String SIP = contact.getSip();
        String Notes = contact.getNotes();

        try {
            if (Profile == null) {
                profile.setFill(new ImagePattern(new Image(getClass().getResource("/icons/ic_account_circle_black_48dp.png").toString())));
            } else {
                String testPath = (new URI(Profile)).normalize().getPath();
                if (Main.isWindows) {
                    testPath = testPath.substring(1);
                }
                if (Files.exists(FileSystems.getDefault().getPath(testPath))) {
                    profile.setFill(new ImagePattern(new Image(Profile)));
                } else {
                    profile.setFill(new ImagePattern(new Image(getClass().getResource("/icons/ic_account_circle_black_48dp.png").toString())));
                }
            }
        } catch (IllegalArgumentException | NullPointerException | URISyntaxException e) {
            System.err.println(e.getMessage());
        }

        if (lastName == null || lastName.isEmpty()) {
            name.setText(firstName);
        } else {
            name.setText(firstName + " " + lastName);
        }

        if (phoneType.equals("No Label")) {
            phoneNumber.setText(phone);
        } else {
            phoneNumber.setText(phoneType + ": " + phone);
        }

        if (e_mail == null || e_mail.isEmpty()) {
            detailBox.getChildren().remove(emailHB);
        } else {
            if (emailType.equals("No Label")) {
                email.setText(e_mail);
            } else {
                email.setText(emailType + ": " + e_mail);
            }
        }

        if (Address == null || Address.isEmpty()) {
            detailBox.getChildren().remove(addressHB);
        } else {
            address.setText(Address);
        }

        if (Company == null || Company.isEmpty()) {
            detailBox.getChildren().remove(companyHB);
        } else {
            company.setText(Company);
        }

        if (Website == null || Website.isEmpty()) {
            detailBox.getChildren().remove(websiteHB);
        } else {
            website.setText(Website);
        }

        if (dateField == null || dateField.isEmpty()) {
            detailBox.getChildren().remove(dateHB);
        } else {
            if (dateType.equals("No Label")) {
                date.setText(dateField);
            } else {
                date.setText(dateType + ": " + dateField);
            }
        }

        if (Relation == null || Relation.isEmpty()) {
            detailBox.getChildren().remove(relationHB);
        } else {
            if (relationType.equals("No Label")) {
                relation.setText(Relation);
            } else {
                relation.setText(relationType + ": " + Relation);
            }
        }

        if (SIP == null || SIP.isEmpty()) {
            detailBox.getChildren().remove(sipHB);
        } else {
            sip.setText(SIP);
        }

        if (Notes == null || Notes.isEmpty()) {
            detailBox.getChildren().remove(notesHB);
        } else {
            notes.setText(Notes);
        }

        if (contactToView.isFavourite()) {
            SVGPath yFavourite = new SVGPath();
            yFavourite.setContent("M 18 32.023438 L 15.824219 30.050781 C 8.101562 23.039062 3 18.414062 3 12.75 C 3 8.121094 6.621094 4.5 11.25 4.5 C 13.859375 4.5 16.363281 5.714844 18 7.628906 C 19.636719 5.714844 22.140625 4.5 24.75 4.5 C 29.378906 4.5 33 8.121094 33 12.75 C 33 18.414062 27.898438 23.039062 20.175781 30.050781 Z M 18 32.023438 ");
            yFavourite.setFill(Color.rgb(219, 50, 54, 0.8));
            favourite.setGraphic(yFavourite);
        }
    }

    @FXML
    public void handleBackOperation(ActionEvent actionEvent) {
        parent.setCenter(parentCenter);
    }

    @FXML
    public void handleSetFavourite(ActionEvent actionEvent) {
        if (contactToView.isFavourite()) {
            SVGPath nFavourite = new SVGPath();
            nFavourite.setContent("M 24.75 4.5 C 22.140625 4.5 19.636719 5.714844 18 7.628906 C 16.363281 5.714844 13.859375 4.5 11.25 4.5 C 6.621094 4.5 3 8.121094 3 12.75 C 3 18.414062 8.101562 23.039062 15.824219 30.050781 L 18 32.023438 L 20.175781 30.050781 C 27.898438 23.039062 33 18.414062 33 12.75 C 33 8.121094 29.378906 4.5 24.75 4.5 Z M 18.15625 27.832031 L 18 27.976562 L 17.84375 27.832031 C 10.710938 21.359375 6 17.085938 6 12.75 C 6 9.757812 8.257812 7.5 11.25 7.5 C 13.558594 7.5 15.808594 8.992188 16.597656 11.039062 L 19.394531 11.039062 C 20.191406 8.992188 22.441406 7.5 24.75 7.5 C 27.742188 7.5 30 9.757812 30 12.75 C 30 17.085938 25.289062 21.359375 18.15625 27.832031 Z M 18.15625 27.832031");
            nFavourite.setFill(Color.rgb(72, 133, 237));
            favourite.setGraphic(nFavourite);
            contactToView.setFavourite(false);
        } else {
            SVGPath yFavourite = new SVGPath();
            yFavourite.setContent("M 18 32.023438 L 15.824219 30.050781 C 8.101562 23.039062 3 18.414062 3 12.75 C 3 8.121094 6.621094 4.5 11.25 4.5 C 13.859375 4.5 16.363281 5.714844 18 7.628906 C 19.636719 5.714844 22.140625 4.5 24.75 4.5 C 29.378906 4.5 33 8.121094 33 12.75 C 33 18.414062 27.898438 23.039062 20.175781 30.050781 Z M 18 32.023438 ");
            yFavourite.setFill(Color.rgb(219, 50, 54, 0.8));
            favourite.setGraphic(yFavourite);
            contactToView.setFavourite(true);
        }
//        Main.controller.refreshFilteredList(contactToView);
        Platform.runLater(() -> DataSource.getInstance().setFavourite(contactToView));
    }

    @FXML
    public void handleEditContact(ActionEvent actionEvent) {
        try {
            AddEditController.contactToEdit = contactToView;
            AddEditController.fromShowContact = true;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddEditPane.fxml"));
            VBox addEditPane = loader.load();
            AddEditController controller = loader.getController();
            controller.parent = parent;
            controller.parentCenter = parentCenter;
            controller.messageNode = messageNode;
            if (parent.getScene().getWindow().getWidth() <= 1000.0) {
                parent.setCenter(addEditPane);
                ((Stage) parent.getScene().getWindow()).setMinWidth(500.0);
            } else {
                parent.setRight(addEditPane);
            }
        } catch (IOException e) {
            AddEditController.contactToEdit = null;
            AddEditController.fromShowContact = false;
            System.out.println(e.getMessage());
        }
    }

    @FXML
    public void handleDeleteContact(ActionEvent actionEvent) {
        Alert deleteAlert = new Alert(Alert.AlertType.CONFIRMATION);
        deleteAlert.setTitle("Delete Contact?");
        String firstName = contactToView.getFirstName();
        String lastName = contactToView.getLastName();
        if (lastName == null) {
            lastName = "";
        }
        deleteAlert.setHeaderText("Contact : " + firstName + " " + lastName);
        deleteAlert.setContentText("Are you sure to delete the contact ?\nPress OK to Delete, Cancel to go back!");

        Optional<ButtonType> response = deleteAlert.showAndWait();
        if (response.isPresent() && response.get().equals(ButtonType.OK)) {
            Platform.runLater(() -> {
                if (DataSource.getInstance().deleteContact(contactToView)) {
                    Main.controller.removeContactFromList(contactToView);
                    if (parent.getScene().getWindow().getWidth() <= 1000.0) {
                        parent.setCenter(parentCenter);
                    } else {
                        parent.setRight(messageNode);
                    }
                }
            });
        }
    }
}
