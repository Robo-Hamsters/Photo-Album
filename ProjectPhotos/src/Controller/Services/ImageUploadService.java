package Controller.Services;

import Model.Album;
import Repo.AlbumRepo;
import Repo.DBConnector;

public class ImageUploadService extends TransactionHandler {

    Album album;

    public Album chooseFromCombo(Album album)
    {
        this.album = album;

        createTransaction();

        return this.album;

    }

    @Override
    public void task(DBConnector con) {

        AlbumRepo albumRepo = new AlbumRepo();
        album=albumRepo.findByName(album,con);

    }
}
