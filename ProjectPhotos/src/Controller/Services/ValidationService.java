package Controller.Services;



public class ValidationService {

    private String message="";

    public boolean checkIfPasswordsMatch(String pass , String conPass){
        boolean match = false;
        if(pass.equals(conPass)) match = true;
        return match;
    }

    public boolean checkIfPasswordIsStrong(String pass){
        boolean strongPass = false;
        if(pass.length() >= 6) strongPass = true;
        return strongPass;
    }

    public boolean checkIfPasswordIsNull(String pass){
        boolean nullPass = false;
        if(pass.isEmpty()) nullPass = true;
        return  nullPass;
    }

    public boolean checkIfUserNameIsNull(String name){
        boolean nullName = false;
        if(name.isEmpty()) nullName = true;
        return  nullName;
    }

    public boolean checkIfEmailIsValid(String email){
        boolean validEmail = false;
        if(email.contains("@")) validEmail = true;
        return  validEmail;
    }

    public boolean checkValidation(String textName, String textEmail, String textPassword,String textConPassword){
        boolean allClear = true;
        int passCode = 0;
        int matchCode = 0;

        ValidationService service = new ValidationService();

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
