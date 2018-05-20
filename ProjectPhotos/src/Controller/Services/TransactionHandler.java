package Controller.Services;

import Repo.DBConnector;

public abstract class TransactionHandler {

    private DBConnector con;

    public void createTransaction()
    {
        con = new DBConnector();
        con.databaseConnect();
        con.setSession(con.getFactory().getCurrentSession());
        con.getSession().beginTransaction();

        task(con);

        con.databaseDisconnect();
    }

    abstract public void task(DBConnector con);
}
