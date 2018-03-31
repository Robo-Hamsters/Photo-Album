package Repo;
import Model.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import org.hibernate.query.Query;

import java.util.List;
import java.util.UUID;


public class DBConnector {

    private SessionFactory factory;

    public SessionFactory getFactory() {
        return factory;
    }

    public void setFactory(SessionFactory factory) {
        this.factory = factory;
    }

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }

    private Session session;


    public void databaseConnect()
    {
             factory=new Configuration()
                .configure("Repo/hibernate.cfg.xml")
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Photo.class)
                .addAnnotatedClass(Album.class)
                .buildSessionFactory();

    }

    public void databaseDisconnect()
    {
        factory.close();
    }



}
