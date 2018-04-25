package Controller.Services;

import Model.Photo;
import Model.User;
import Repo.DBConnector;
import Repo.PhotoRepo;

import java.util.List;

public class AlbumService extends TransactionHandler {

   private User user;
   private List<Photo> photos;

    public List<Photo> retrievePhotos(User user)
    {
        this.user = user;
        createTransaction();

        return photos;
    }

    @Override
    public void task(DBConnector con) {
        PhotoRepo photoRepo = new PhotoRepo();

        photos = photoRepo.findByUser(user, con);
    }
}
