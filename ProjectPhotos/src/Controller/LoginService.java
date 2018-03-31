package Controller;

import Model.User;
import Repo.DBConnector;
import Repo.UserRepo;

public class LoginService {

    public static boolean loginUser(User user)
    {
        DBConnector con=new DBConnector();
        con.databaseConnect();
        con.setSession(con.getFactory().getCurrentSession()) ;
        con.getSession().beginTransaction();

        User returnedUser = null;
        UserRepo userRepo=new UserRepo();
        returnedUser = userRepo.findUserByUsernameAndPassword(user,con);

        return (returnedUser != null);

    }


}
