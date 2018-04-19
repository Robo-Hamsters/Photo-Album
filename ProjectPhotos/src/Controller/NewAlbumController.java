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
        DBConnector con1 = new DBConnector();
        con1.databaseConnect();
        con1.setSession(con1.getFactory().getCurrentSession()) ;
        con1.getSession().beginTransaction();
        AlbumRepo albumRepo = new AlbumRepo();
        Album album = new Album();
        Album returnAlbum;
        album.setAlbumName(textAlbumName.getText());
        album.setUser(user);
        returnAlbum = albumRepo.findByName(album,con1);
        con1.databaseDisconnect();

        if (returnAlbum == null)
        {
            album.setAlbumID(UUID.randomUUID());

            if(!textAlbumName.getText().isEmpty())
            {
                DBConnector con2 = new DBConnector();
                con2.databaseConnect();

                albumRepo.dbInsertAlbum(album, con2);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Tuxedo View");
                alert.setContentText("You create an album");
                alert.showAndWait();

                Node source = (Node) event.getSource();
                Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
                con2.databaseDisconnect();
            }
            else
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Tuxedo View");
                alert.setContentText("You have to put an Album name");
                alert.showAndWait();

            }
        }
        else
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Tuxedo View");
            alert.setContentText("This album already exists!");
            alert.showAndWait();

        }



    }
    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }
}
