package Controller.Services;

import javafx.scene.control.TextField;

public class SignUpService {

    private String message="";

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

    public boolean checkIfPasswordIsNull(TextField pass){
        boolean nullPass = false;
        if(pass.getText().isEmpty()) nullPass = true;
        return  nullPass;
    }

    public boolean checkIfUserNameIsNull(TextField name){
        boolean nullName = false;
        if(name.getText().isEmpty()) nullName = true;
        return  nullName;
    }

    public boolean checkIfEmailIsValid(TextField email){
        boolean validEmail = false;
        if(email.getText().contains("@")) validEmail = true;
        return  validEmail;
    }

    public boolean checkValidation(TextField textName, TextField textEmail, TextField textPassword, TextField textConPassword){
        boolean allClear = true;
        int passCode = 0;
        int matchCode = 0;

        SignUpService service = new SignUpService();

        if(service.checkIfUserNameIsNull(textName)){
            allClear = false;
            checkWarningStatus("Name field is empty...");
        }

        if(service.checkIfPasswordIsNull(textPassword)) {
            allClear = false;
            passCode = 1;
            checkWarningStatus("You must set a password");
        }

        if(!service.checkIfPasswordsMatch(textPassword,textConPassword)) {
            allClear = false;
            matchCode = 1;
            checkWarningStatus("Passwords does not match");
        }

        if(!service.checkIfPasswordIsStrong(textPassword)){
            allClear = false;
            if(passCode != 1 && matchCode !=1){
            checkWarningStatus("Your password is weak...");}
        }

        if(!service.checkIfEmailIsValid(textEmail)){
            allClear = false;
            checkWarningStatus("Email is not valid");
        }

        return allClear;
    }

    public String checkWarningStatus(String alert){
        message+=alert+"\n";
        return  message;
    }

    public String clearMessage(){
        message="";
        return message;
    }

}
