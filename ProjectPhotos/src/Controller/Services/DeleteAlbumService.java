package Controller.Services;

import Model.Album;
import Repo.AlbumRepo;
import Repo.DBConnector;


public class DeleteAlbumService extends TransactionHandler{

    private Album album;
    public void deteleItem(Album album)
    {
        this.album = album;
        createTransaction();
    }

    @Override
    public void task(DBConnector con) {


        AlbumRepo albumRepo = new AlbumRepo();
        albumRepo.dbDeleteAlbum(album,con);

    }
}
