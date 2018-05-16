package Controller.Services;

import Model.Album;
import Model.Photo;
import Model.User;
import Repo.AlbumRepo;
import Repo.DBConnector;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class ImageUploadService extends TransactionHandler {

    Album album;

    public Album chooseFromCombo(Album album)
    {
        this.album = album;
        createTransaction();
        return this.album;

    }

    public List<Album> createAlbumsFromMetadata(Photo photo, User user)
    {
        List<Album> returnAlbums = new ArrayList<>();

        if(photo.hasCountry())
            returnAlbums.add(new Album(photo.getCountry(), true, user));

        if(photo.hasModel())
            returnAlbums.add(new Album(photo.getModel(), true, user));

        return returnAlbums;
    }


    @Override
    public void task(DBConnector con) {

        AlbumRepo albumRepo = new AlbumRepo();
        album=albumRepo.findByName(album,con);

    }
}
