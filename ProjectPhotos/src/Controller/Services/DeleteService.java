package Controller.Services;

import Model.Photo;
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
        photoRepo.dbDeletePhoto(photo,con);

    }
}
