package Controller.Services;

import Model.Photo;
import Repo.AlbumRepo;
import Repo.DBConnector;
import Repo.PhotoRepo;

public class DeleteService extends TransactionHandler {

    private Photo photo;
    public void deteleItem(Photo photo)
    {
        this.photo = photo;
        createTransaction();
    }

    @Override
    public void task(DBConnector con) {

        PhotoRepo photoRepo = new PhotoRepo();
        AlbumRepo albumRepo = new AlbumRepo();


        for(String album: photo.getAlbums()) {
            if(photoRepo.findByAlbum(album, con).size() <= 1)
            {
                albumRepo.dbDeleteAlbumByName(album,con);
                System.out.println("del "+ album);
            }
            System.out.println("ok");
        }

        DBConnector connector = new DBConnector();
        connector.databaseConnect();
        con.setSession(con.getFactory().getCurrentSession());
        con.getSession().beginTransaction();
        photoRepo.dbDeletePhoto(photo,con);
        connector.databaseDisconnect();
    }
}
