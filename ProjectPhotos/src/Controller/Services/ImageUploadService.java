package Controller.Services;

import Model.Album;
import Model.Photo;
import Model.User;
import Repo.AlbumRepo;
import Repo.DBConnector;

import java.util.ArrayList;
import java.util.List;

public class ImageUploadService extends TransactionHandler {

    private Album album;

    private List<SmartAlbumCreator> countryAlbumCreators = new ArrayList<>();

    private NewAlbumService newAlbumService = new NewAlbumService();

    public Album chooseFromCombo(Album album)
    {
        this.album = album;
        createTransaction();
        return this.album;
    }

    public void registerSmartAlbumCreator(SmartAlbumCreator creator) {
        countryAlbumCreators.add(creator);
    }
    public List<Album> createAlbumsFromMetadata(Photo photo, User user)
    {
        List<String> albumNames = new ArrayList<>();
        List<Album> albums = new ArrayList<>();
        for (SmartAlbumCreator creator: countryAlbumCreators) {
            String name = creator.getAlbumName(photo);
            if (!name.isEmpty()) {
                photo.getAlbums().add(name);
                albumNames.add(name);
            }
        }

        for (String albumName: albumNames) {
            Album alb = new Album(albumName,true, user);
            albums.add(alb);
            newAlbumService.createNewAlbum(alb);
        }
        return albums;
//        if(photo.hasCountry())
//            returnAlbums.add(new Album(photo.getCountry(), true, user));
//
//        if(photo.hasModel())
//            returnAlbums.add(new Album(photo.getModel(), true, user));
//
//        return returnAlbums;
    }


    @Override
    public void task(DBConnector con) {

        AlbumRepo albumRepo = new AlbumRepo();
        album=albumRepo.findByName(album,con);

    }
}
