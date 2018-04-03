package Controller;

import Model.Album;
import Model.User;
import Repo.AlbumRepo;
import Repo.DBConnector;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.UUID;

public class NewAlbumController {

    @FXML
    private TextField textAlbumName;

    private User user;

    @FXML
    void createAlbum(ActionEvent event)
    {
        DBConnector con = new DBConnector();
        con.databaseConnect();
        AlbumRepo albumRepo = new AlbumRepo();
        Album album = new Album();
        album.setAlbumID(UUID.randomUUID());
        album.setAlbumName(textAlbumName.getText());
        album.setUser(user);

        if(!textAlbumName.getText().isEmpty())
        {

        albumRepo.dbInsertAlbum(album,con);

        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Tuxedo View");
        alert.setContentText("You create an album");
        alert.showAndWait();

        Node source = (Node)event.getSource();
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close();
        con.databaseDisconnect();
        }
        else
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Tuxedo View");
            alert.setContentText("Invalid name");
            alert.showAndWait();
        }

    }
    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }
}
