package Controller;

import Model.Album;
import Model.User;
import Repo.AlbumRepo;
import Repo.DBConnector;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.UUID;

public class NewAlbumController {

    @FXML
    private TextField textAlbumName;

    private User user;

    @FXML
    void createAlbum()
    {
        DBConnector con = new DBConnector();
        con.databaseConnect();
        AlbumRepo albumRepo = new AlbumRepo();
        Album album = new Album();
        album.setAlbumID(UUID.randomUUID());
        album.setAlbumName(textAlbumName.getText());
        album.setUser(user);

        albumRepo.dbInsertAlbum(album,con);

    }
    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }
}
