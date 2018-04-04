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
    public List dbSelectAlbum(DBConnector con, User user)
    {
        //TODO write the right query
        Query query=con.getSession().createQuery("SELECT albumName from Album as albumName JOIN albumName.user as user");
      //  query.setParameter("frmUser",user.getUserid());
        List list=query.list();
        List<String> returnAlbum = new ArrayList<String>();
        if(list.size() != 0)
        {
            for(int i=0;i<list.size();i++)
            {
                returnAlbum = (List<String>) list.get(i);
            }
        }
        return returnAlbum;
    }
}
