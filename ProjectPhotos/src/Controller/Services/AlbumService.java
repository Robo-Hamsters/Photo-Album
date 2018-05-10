package Controller.Services;

import Model.Album;
import Model.Photo;
import Model.User;
import Repo.AlbumRepo;
import Repo.DBConnector;
import Repo.PhotoRepo;

import java.util.List;

public class AlbumService extends TransactionHandler {

   private User user;
   private List<Photo> photos;
   private List<Album> albums;

    public AlbumService(User user)
    {
        this.user = user;
        createTransaction();

    }

    public List<Photo> getPhotos() { return photos; }
    public List<Album> getAlbums() {
        return albums;
    }

    @Override
    public void task(DBConnector con) {

        PhotoRepo photoRepo = new PhotoRepo();
        AlbumRepo albumRepo = new AlbumRepo();

        photos = photoRepo.findByUser(user, con);
        albums = albumRepo.findByUser(user, con);
    }
}
