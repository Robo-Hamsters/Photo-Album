package Controller;


import Authentication.EncryptService;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

import static Controller.LoginService.loginUser;


public class LoginController {

    @FXML
    private TextField textLoginEmail;
    @FXML
    private TextField textLoginPswd;

    public void signIn(ActionEvent event) throws IOException
    {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("../UI/AlbumForm.fxml"));
        Parent albumOpen=loader.load();
        Scene AlbumScene=new Scene(albumOpen);
        AlbumController controller=loader.getController();

        User user=new User();
        user.setEmail(textLoginEmail.getText());
        user.setPassword(EncryptService.encryptPassword(textLoginPswd.getText()));


        if(loginUser(user)) {
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(AlbumScene);
            window.show();
        }
        else
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Tuxedo View");
            alert.setContentText("Wrong email or password");
            alert.showAndWait();
        }

    }

    public void openSignUpForm(ActionEvent event) throws IOException
    {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("../UI/SignUpForm.fxml"));
        Parent signupOpen=loader.load();

        Stage stage = new Stage();
        stage.setTitle("Create new Account");
        stage.setScene(new Scene(signupOpen));
        stage.showAndWait();
    }

}



