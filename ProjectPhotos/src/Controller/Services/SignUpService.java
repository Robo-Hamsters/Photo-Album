package Controller.Services;

import Model.User;
import Repo.DBConnector;
import Repo.UserRepo;

public class SignUpService extends TransactionHandler {

    private  User user;

    public  void createUser(User user)
    {
        this.user =user;
        createTransaction();
    }

    @Override
    public void task(DBConnector con) {
        UserRepo repo=new UserRepo();
        repo.dbInsertUser(user,con);
    }
}
