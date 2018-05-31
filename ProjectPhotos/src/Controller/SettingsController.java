package Controller;

import Authentication.EncryptService;
import Controller.Services.AccountSettings;
import Controller.Services.LoginService;
import Controller.Services.ValidationService;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;


public class SettingsController {


    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private PasswordField currentPasswordField;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnSave;
    @FXML
    private AnchorPane termsAnchor;

    private User user;

    @FXML
    public void editAccount(ActionEvent event)
    {
        User user = new User(this.user.getUserid(),this.user.getName(),this.user.getEmail(),EncryptService.encryptPassword(currentPasswordField.getText()));
        LoginService service = new LoginService();


        if(service.loginUser(user))
        {
            nameField.setEditable(true);
            passwordField.setEditable(true);
            confirmPasswordField.setEditable(true);
            btnSave.setVisible(true);
            btnEdit.setVisible(false);
            currentPasswordField.setEditable(false);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Tuxedo View");
            alert.setContentText("You have to put the right current password first before you make any change!");
            alert.showAndWait();
        }


    }

    @FXML
    public void saveAccount(ActionEvent event)
    {
        nameField.setEditable(false);
        passwordField.setEditable(false);
        btnSave.setVisible(false);
        btnEdit.setVisible(true);

        if(!nameField.getText().isEmpty())
            user.setName(nameField.getText());

        ValidationService service = new ValidationService();
        if(service.checkIfPasswordsMatch(passwordField.getText(),confirmPasswordField.getText()))
          user.setPassword(EncryptService.encryptPassword(passwordField.getText()));
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Tuxedo View");
            alert.setContentText("Passwords does not match!");
            alert.showAndWait();
            //TODO STOPS AND SAVE
        }

        AccountSettings settings = new AccountSettings();
        settings.saveAccountSettings(user);
    }

    public void setUser(User user) {
        this.user = user;
    }
    //TODO FIX THE TERMS
    public SettingsController()
    {
        WebView browser = new WebView();
        termsAnchor = new AnchorPane();
        WebEngine webEngine = browser.getEngine();
        webEngine.loadContent("<!DOCTYPE html><html> <head> <meta charset=\"utf-8\"> <meta name=\"viewport\" content=\"width=device-width\"> <title>Privacy Policy</title> <style>body{font-family: \"Helvetica Neue\", Helvetica, Arial, sans-serif; padding:1em;}</style></head> <body><h2>Terms &amp; Conditions</h2> <p>By downloading or using the app, these terms will automatically apply to you – you should make sure therefore that you read them carefully before using the app. You’re not allowed to copy, or modify the app, any part of the app, or our trademarks in any way. You’re not allowed to attempt to extract the source code of the app, and you also shouldn’t try to translate the app into other languages, or make derivative versions. The app itself, and all the trade marks, copyright, database rights and other intellectual property rights related to it, still belong to Zarogoulidis Anastasis, Zegos Thomas, Mitsou Pavlina,Tzouvaliakidis Alexios.</p> <p>Zarogoulidis Anastasis, Zegos Thomas, Mitsou Pavlina,Tzouvaliakidis Alexios is committed to ensuring that the app is as useful and efficient as possible. For that reason, we reserve the right to make changes to the app or to charge for its services, at any time and for any reason. We will never charge you for the app or its services without making it very clear to you exactly what you’re paying for.</p> <p>The Tuxedo-View app stores and processes personal data that you have provided to us, in order to provide my Service. It’s your responsibility to keep your phone and access to the app secure. We therefore recommend that you do not jailbreak or root your phone, which is the process of removing software restrictions and limitations imposed by the official operating system of your device. It could make your phone vulnerable to malware/viruses/malicious programs, compromise your phone’s security features and it could mean that the Tuxedo-View app won’t work properly or at all. </p> <p>You should be aware that there are certain things that Zarogoulidis Anastasis, Zegos Thomas, Mitsou Pavlina,Tzouvaliakidis Alexios will not take responsibility for. Certain functions of the app will require the app to have an active internet connection. The connection can be Wi-Fi, or provided by your mobile network provider, but Zarogoulidis Anastasis, Zegos Thomas, Mitsou Pavlina,Tzouvaliakidis Alexios cannot take responsibility for the app not working at full functionality if you don’t have access to Wi-Fi, and you don’t have any of your data allowance left.</p><p></p><p>If you’re using the app outside of an area with Wi-Fi, you should remember that your terms of the agreement with your mobile network provider will still apply. As a result, you may be charged by your mobile provider for the cost of data for the duration of the connection while accessing the app, or other third party charges. In using the app, you’re accepting responsibility for any such charges, including roaming data charges if you use the app outside of your home territory (i.e. region or country) without turning off data roaming. If you are not the bill payer for the device on which you’re using the app, please be aware that we assume that you have received permission from the bill payer for using the app.</p> <p>Along the same lines, Zarogoulidis Anastasis, Zegos Thomas, Mitsou Pavlina,Tzouvaliakidis Alexios cannot always take responsibility for the way you use the app i.e. You need to make sure that your device stays charged – if it runs out of battery and you can’t turn it on to avail the Service, Zarogoulidis Anastasis, Zegos Thomas, Mitsou Pavlina,Tzouvaliakidis Alexios cannot accept responsibility</p> <p>With respect to Zarogoulidis Anastasis, Zegos Thomas, Mitsou Pavlina,Tzouvaliakidis Alexios’s responsibility for your use of the app, when you’re using the app, it’s important to bear in mind that although we endeavour to ensure that it is updated and correct at all times, we do rely on third parties to provide information to us so that we can make it available to you. Zarogoulidis Anastasis, Zegos Thomas, Mitsou Pavlina,Tzouvaliakidis Alexios accepts no liability for any loss, direct or indirect, you experience as a result of relying wholly on this functionality of the app.</p> <p>At some point, we may wish to update the app. The app is currently available on Android and iOS – the requirements for both systems (and for any additional systems we decide to extend the availability of the app to) may change, and you’ll need to download the updates if you want to keep using the app. Zarogoulidis Anastasis, Zegos Thomas, Mitsou Pavlina,Tzouvaliakidis Alexios does not promise that it will always update the app so that it is relevant to you and/or works with the iOS/Android version that you have installed on your device. However, you promise to always accept updates to the application when offered to you, We may also wish to stop providing the app, and may terminate use of it at any time without giving notice of termination to you. Unless we tell you otherwise, upon any termination, (a) the rights and licenses granted to you in these terms will end; (b) you must stop using the app, and (if needed) delete it from your device.</p> <p><strong>Changes to This Terms and Conditions</strong></p> <p> I may update our Terms and Conditions from time to time. Thus, you are advised to review\n" +
                "                      this page periodically for any changes. I will notify you of any changes by posting\n" +
                "                      the new Terms and Conditions on this page. These changes are effective immediately after they are posted on\n" +
                "                      this page.\n" +
                "                    </p></body></html>");
        termsAnchor.getChildren().add(browser);
    }
}
