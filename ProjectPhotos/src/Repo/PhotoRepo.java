package Repo;

import Model.Photo;
import Model.User;
import org.hibernate.query.Query;

import java.util.List;

public class PhotoRepo {

    public Photo finById(Photo photo,DBConnector con)
    {

        Query query= con.getSession().createQuery("SELECT image from Photo ");
        //TODO CONNECT KEYS AT DATABASE TO WRITE THE RIGHT QUERY
        return new Photo();
    }
    public Photo findByName(Photo photo,DBConnector con)
    {


        Query query= con.getSession().createQuery("SELECT image from Photo ");
        //TODO CONNECT KEYS AT DATABASE TO WRITE THE RIGHT QUERY
        return new Photo();
    }
    public Photo dbSelectPhoto(PhotoRepo photo,DBConnector con)
    {

        Query query=con.getSession().createQuery("SELECT image from Photo ");

        List list=query.list();
        Photo returnPhoto=null;
        if(list.size() != 0)
        {
            returnPhoto = new Photo();
            byte[] bytes =(byte[])list.get(0);
            returnPhoto.setImage(bytes);
        }
        return returnPhoto;
    }

    public void dbInsertPhoto(Photo photo, DBConnector con)
    {
        con.setSession(con.getFactory().getCurrentSession()) ;
        con.getSession().beginTransaction();
        con.getSession().save(photo);
        con.getSession().getTransaction().commit();
    }
}
