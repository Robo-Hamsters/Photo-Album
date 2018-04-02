package Controller;

import Model.Album;
import Repo.AlbumRepo;
import Repo.DBConnector;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.UUID;

public class NewAlbumController {

    @FXML
    private TextField textAlbumName;

    @FXML
    void createAlbum()
    {
        DBConnector con = new DBConnector();
        con.databaseConnect();
        AlbumRepo albumRepo = new AlbumRepo();
        Album album = new Album();
        album.setAlbumID(UUID.randomUUID());
        album.setAlbumName(textAlbumName.getText());

        albumRepo.dbInsertAlbum(album,con);

    }
}
