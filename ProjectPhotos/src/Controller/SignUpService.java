package Controller;

import javafx.scene.control.TextField;

public class SignUpService {

    public boolean checkIfPasswordsMatch(TextField pass , TextField conPass){
        boolean match = false;
        if(pass.getText().equals(conPass.getText())) match = true;
        return match;
    }

    public boolean checkIfPasswordIsStrong(TextField pass){
        boolean strongPass = false;
        if(pass.getText().length() >= 6) strongPass = true;
        return strongPass;
    }

    public boolean chechIfPasswordIsNull(TextField pass){
        boolean nullPass = false;
        if(pass.getText().isEmpty()) nullPass = true;
        return  nullPass;
    }

    public boolean chechIfUserNameIsNull(TextField name){
        boolean nullName = false;
        if(name.getText().isEmpty()) nullName = true;
        return  nullName;
    }

    public boolean checkIfEmailIsValid(TextField email){
        boolean validEmail = false;
        if(email.getText().contains("@")) validEmail = true;
        return  validEmail;
    }

}
