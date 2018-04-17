package Repo;

import Model.Album;
import Model.User;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class AlbumRepo {

    public void dbInsertAlbum(Album album, DBConnector con)
    {
        con.setSession(con.getFactory().getCurrentSession()) ;
        con.getSession().beginTransaction();
        con.getSession().save(album);
        con.getSession().getTransaction().commit();
    }
    public List findByUser(DBConnector con, User user)
    {
        Query<Album> query= con.getSession().createQuery("SELECT albumname from Album p inner join User u on p.user.userid = u.userid where p.user.userid = :frmuserid");
        query.setParameter("frmuserid",user.getUserid());

        List list=query.list();
       /* if(list.size() != 0)
        {
            for(int i=0;i<list.size();i++)
            {
                returnAlbum = (List<String>) list.get(i);
            }
        }
        return returnAlbum;*/
       return list;
    }
}
