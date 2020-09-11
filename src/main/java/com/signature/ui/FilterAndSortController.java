package com.signature.ui;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXRadioButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;

public class FilterAndSortController {

    @FXML
    private JFXRadioButton firstName;
    @FXML
    private JFXRadioButton lastName;
    @FXML
    private JFXCheckBox withoutLastName;
    @FXML
    private JFXCheckBox withoutPhoneNo;
    @FXML
    private JFXCheckBox favourite;

    public void initialize() {
        Platform.runLater(() -> {
            firstName.setSelectedColor(Color.rgb(72, 133, 237));
            lastName.setSelectedColor(Color.rgb(72, 133, 237));
        });
    }

    public void setPreviousStatus(boolean[] previousStatus) {
        firstName.setSelected(previousStatus[0]);
        lastName.setSelected(previousStatus[1]);
        if (!firstName.isSelected() && !lastName.isSelected()) {
            firstName.setSelected(true);
        }
        withoutLastName.setSelected(previousStatus[2]);
        withoutPhoneNo.setSelected(previousStatus[3]);
        favourite.setSelected(previousStatus[4]);
    }

    public boolean byFirstName() {
        return firstName.isSelected();
    }

    public boolean byLastName() {
        return lastName.isSelected();
    }

    public boolean byWithoutLastName() {
        return withoutLastName.isSelected();
    }

    public boolean byWithoutPhoneNo() {
        return withoutPhoneNo.isSelected();
    }

    public boolean byFavouriteContact() {
        return favourite.isSelected();
    }
}
