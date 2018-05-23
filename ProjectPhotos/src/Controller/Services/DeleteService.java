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
            }
        }
        photoRepo.dbDeletePhoto(photo,con);
    }
}
