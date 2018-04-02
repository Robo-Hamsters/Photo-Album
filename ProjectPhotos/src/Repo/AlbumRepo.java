package Repo;

import Model.Album;

public class AlbumRepo {

    public void dbInsertAlbum(Album album, DBConnector con)
    {
        con.setSession(con.getFactory().getCurrentSession()) ;
        con.getSession().beginTransaction();
        con.getSession().save(album);
        con.getSession().getTransaction().commit();
    }
}
