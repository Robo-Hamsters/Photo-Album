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
    public List<Album> findByUser(User user, DBConnector con)
    {
        Query query= con.getSession().createQuery("from Album a inner join User u on a.user.userid = u.userid where a.user.userid = :frmuserid");
        query.setParameter("frmuserid",user.getUserid());

        List<Object[]> list=query.list();
        List<Album> returnAlbums = new ArrayList<>();
        for(Object[] obj : list)
        {
            returnAlbums.add((Album)obj[0]);
        }

        return returnAlbums;
    }

    public Album findByName(Album album,DBConnector con)
    {

        Query query= con.getSession().createQuery("from Album a inner join User u on a.user.userid = u.userid where a.albumName = :frmname");
        query.setParameter("frmname",album.getAlbumName());

        List<Object[]> list=query.list();
        Album returnAlbum = null;
        if(list.size() != 0)
        {
            for(Object[] obj : list)
            {
                returnAlbum = ((Album)obj[0]);
            }
        }
        return returnAlbum;
    }
}
