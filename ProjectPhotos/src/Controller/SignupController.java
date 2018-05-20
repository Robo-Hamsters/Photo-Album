package Controller;


import Authentication.EncryptService;
import Controller.Services.SignUpService;
import Controller.Services.ValidationService;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.UUID;


public class SignupController  {

    ValidationService service = new ValidationService();

    @FXML
    private TextField textName;
    @FXML
    private TextField textEmail;
    @FXML
    private TextField textPassword;
    @FXML
    private TextField textConPassword;

    private User user;

    @FXML
    public void createAccount(ActionEvent event)
    {
        if(service.checkValidation(textName, textEmail, textPassword, textConPassword))
        {
            user=new User();
            user.setName(textName.getText());
            user.setEmail(textEmail.getText());
            user.setUserid(UUID.randomUUID());
            user.setEmail(textEmail.getText());
            user.setPassword(EncryptService.encryptPassword(textPassword.getText()));

            SignUpService signUpService = new SignUpService();

            signUpService.createUser(user);


            Node source = (Node)event.getSource();
            Stage stage = (Stage)source.getScene().getWindow();
            stage.close();

        }
        else
        {
            String messageBox= service.checkWarningStatus("");
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setContentText(messageBox);
            alert.setTitle("Tuxedo View");
            alert.showAndWait();
            service.clearMessage();
        }
    }



}
