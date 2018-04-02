package Controller;

import Model.User;
import Repo.DBConnector;
import Repo.UserRepo;

import java.util.UUID;

public class LoginService {


    private   User returnedUser;

    public  boolean loginUser(User user)
    {
        DBConnector con=new DBConnector();
        con.databaseConnect();
        con.setSession(con.getFactory().getCurrentSession()) ;
        con.getSession().beginTransaction();


        UserRepo userRepo=new UserRepo();
        returnedUser = userRepo.findUserByUsernameAndPassword(user,con);

        return (returnedUser != null);

    }

    public  User getReturnedUser()
    {
        return returnedUser;
    }
}
