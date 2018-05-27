package Controller;

import Authentication.EncryptService;
import Controller.Services.AccountSettings;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class SettingsController {


    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;

    private User user;

    @FXML
    public void editAccount(ActionEvent event)
    {
        nameField.setEditable(true);
        passwordField.setEditable(true);
        btnSave.setVisible(true);
        btnEdit.setVisible(false);

    }

    @FXML
    public void saveAccount(ActionEvent event)
    {
        nameField.setEditable(false);
        passwordField.setEditable(false);
        btnSave.setVisible(false);
        btnEdit.setVisible(true);
        user.setName(nameField.getText());
        user.setPassword(EncryptService.encryptPassword(passwordField.getText()));

        AccountSettings settings = new AccountSettings();
        settings.saveAccountSettings(user);
    }

    public void setUser(User user) {
        this.user = user;
    }
}
