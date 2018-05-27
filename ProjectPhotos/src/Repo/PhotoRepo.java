package Repo;

import Model.Photo;
import Model.User;
import org.hibernate.query.Query;
import java.util.ArrayList;
import java.util.List;

public class PhotoRepo {

    public List<Photo> findByAlbum(String album, DBConnector con)
    {
        List<Photo> photos = new ArrayList<>();
        Query<Photo> query = con.getSession().createQuery("from Photo p where :frmAlbum in elements (albums) ");
        query.setParameter("frmAlbum",album);

        photos = query.list();
        return photos;
    }


    public List<Photo> findById(Photo photo,DBConnector con)
    {

        Query<Photo> query= con.getSession().createQuery("from Photo where idPhoto = :frmid");
        query.setParameter("frmid",photo.getIdPhoto());

        List list=query.list();
        List<Photo> returnPhoto = null;
        returnPhoto = list;

        return returnPhoto;

    }
    public List<Photo> findByName(Photo photo,DBConnector con)
    {

        Query<Photo> query= con.getSession().createQuery("from Photo where namePhoto = :frmname");
        query.setParameter("frmname",photo.getNamePhoto());

        List list=query.list();
        List<Photo> returnPhoto = null;
        if(list.size() != 0)
        {
            returnPhoto = list;
        }
        return returnPhoto;

    }
    public List<Photo> findByUser(User user,DBConnector con)
    {
        Query query= con.getSession().createQuery("from Photo p inner join User u on p.user.userid = u.userid where p.user.userid = :frmuserid");
        query.setParameter("frmuserid",user.getUserid());

        List<Object[]> list=query.list();
        List<Photo> returnPhoto = new ArrayList<>();
        if(list.size() != 0)
        {
            for(Object[] obj : list)
            {
                returnPhoto.add((Photo)obj[0]);
            }
        }
        return returnPhoto;

    }

    public void dbDeletePhoto(Photo photo, DBConnector con)
    {
        con.getSession().delete(photo);
        con.getSession().getTransaction().commit();

    }

    public void dbInsertPhoto(Photo photo, DBConnector con)
    {
        con.getSession().save(photo);
        con.getSession().getTransaction().commit();

    }
}
