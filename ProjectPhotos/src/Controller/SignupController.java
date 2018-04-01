package Controller;


import Authentication.EncryptService;
import Model.User;
import Repo.DBConnector;
import Repo.UserRepo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.UUID;

public class SignupController {

    private String message="";

    @FXML
    private TextField textName;
    @FXML
    private TextField textEmail;
    @FXML
    private TextField textPassword;
    @FXML
    private TextField textConPassword;

    @FXML
    public void createAccount(ActionEvent event)
    {
        if(checkValidation(textName, textEmail, textPassword, textConPassword))
        {
            User user=new User();
            user.setName(textName.getText());
            user.setEmail(textEmail.getText());
            user.setUserid(UUID.randomUUID());
            user.setEmail(textEmail.getText());
            user.setPassword(EncryptService.encryptPassword(textPassword.getText()));

            DBConnector con=new DBConnector();
            con.databaseConnect();
            UserRepo repo=new UserRepo();
            repo.dbInsertUser(user,con);
            con.databaseDisconnect();

            Node source = (Node)event.getSource();
            Stage stage = (Stage)source.getScene().getWindow();
            stage.close();

        }
        else
        {
            String messageBox= checkWarningStatus("");
            Alert alert=new Alert(Alert.AlertType.WARNING);
            alert.setContentText(messageBox);
            alert.setTitle("Tuxedo View");
            alert.showAndWait();
            message = "";
        }
    }

    private boolean checkValidation(TextField textName, TextField textEmail, TextField textPassword, TextField textConPassword){
        boolean allClear = true;

        SignUpService service = new SignUpService();

        if(service.chechIfUserNameIsNull(textName)){
            allClear = false;
            checkWarningStatus("Name is Null");
        }
        if(service.chechIfPasswordIsNull(textPassword)) {
            allClear = false;
            checkWarningStatus("Password is Null");
        }
        if(!service.checkIfPasswordIsStrong(textPassword)){
            allClear = false;
            checkWarningStatus("Password is Weak");
        }
        if(!service.checkIfPasswordsMatch(textPassword,textConPassword)) {
            allClear = false;
            checkWarningStatus("Passwords does not match");
        }
        if(!service.checkIfEmailIsValid(textEmail)){
            allClear = false;
            checkWarningStatus("Email is not valid");
        }

        return allClear;
    }

    private String checkWarningStatus(String code){
        message+=code+"\n";
        return  message;
    }


}
