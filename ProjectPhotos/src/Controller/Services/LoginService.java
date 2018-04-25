package Controller.Services;

import Model.User;
import Repo.DBConnector;
import Repo.UserRepo;


public class LoginService extends TransactionHandler {


    private   User returnedUser;
    private  User user;

    public  boolean loginUser(User user)
    {
        this.user = user;
        createTransaction();


        return (returnedUser != null);


    }


    public  User getReturnedUser()
    {
        return returnedUser;
    }

    @Override
    public void task(DBConnector con) {
        UserRepo userRepo=new UserRepo();
        returnedUser = userRepo.findUserByUsernameAndPassword(user,con);


    }
}
