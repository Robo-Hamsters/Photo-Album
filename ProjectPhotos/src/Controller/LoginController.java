package Controller;


import Authentication.EncryptService;
import Controller.Services.LoginService;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;



public class LoginController {

    @FXML
    private TextField textLoginEmail;
    @FXML
    private TextField textLoginPswd;
    @FXML
    private ProgressIndicator progressIndicator;


    public void signIn(ActionEvent event) throws IOException
    {
        setProgressIndicatorON();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../UI/AlbumForm.fxml"));
        Parent albumOpen = loader.load();
        Scene albumScene = new Scene(albumOpen);
        albumOpen.getStylesheets().add(AlbumController.class.getResource("../UI/Styles/AlbumForm.css").toExternalForm());

        AlbumController controller=loader.getController();

        User user = new User();
        user.setEmail(textLoginEmail.getText());
        user.setPassword(EncryptService.encryptPassword(textLoginPswd.getText()));

        LoginService login = new LoginService();

        if(login.loginUser(user)) {
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(albumScene);
            window.setMaximized(true);
            window.setResizable(true);
            controller.setLabelTextUsername(login.getReturnedUser().getName());
            controller.setUser(login.getReturnedUser());
            controller.loadImageView();
            loader.setController(controller);
            window.show();
            setProgressIndicatorOFF();
        }
        else
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Tuxedo View");
            alert.setContentText("Wrong email or password");
            alert.showAndWait();
            setProgressIndicatorOFF();

        }

    }

    public void openSignUpForm(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../UI/SignUpForm.fxml"));
        Parent signupOpen = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Create new Account");
        stage.setScene(new Scene(signupOpen));
        signupOpen.getStylesheets().add(SignupController.class.getResource("../UI/Styles/SignUpForm.css").toExternalForm());
        stage.setResizable(false);
        stage.showAndWait();
    }

    public void forgotPass(ActionEvent event) throws IOException
    {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("../UI/ForgotPassForm.fxml"));
        Parent forgotPassLoader=loader.load();
        Stage stage = new Stage();
        stage.setTitle("Forgot Password");
        stage.setScene(new Scene(forgotPassLoader));
        forgotPassLoader.getStylesheets().add(ForgotPassController.class.getResource("../UI/Styles/ForgotPassForm.css").toExternalForm());
        stage.showAndWait();
    }

    public void setProgressIndicatorON(){
            progressIndicator.setVisible(true);
    }

    public void setProgressIndicatorOFF(){
        progressIndicator.setVisible(false);
    }

}



