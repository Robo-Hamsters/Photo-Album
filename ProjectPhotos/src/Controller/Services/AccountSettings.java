package Controller.Services;

import Model.User;
import Repo.DBConnector;
import Repo.UserRepo;

public class AccountSettings extends TransactionHandler {

    private User user;

    public void saveAccountSettings(User user)
    {
        this.user = user;
        createTransaction();
    }

    @Override
    public void task(DBConnector con) {

        UserRepo userRepo = new UserRepo();
        userRepo.editUser(user,con);

    }
}
