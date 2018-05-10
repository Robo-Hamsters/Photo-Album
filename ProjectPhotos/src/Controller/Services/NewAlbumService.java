package Controller.Services;

import Controller.NewAlbumController;
import Model.Album;
import Model.User;
import Repo.AlbumRepo;
import Repo.DBConnector;
import javafx.scene.control.Alert;

import java.util.UUID;

public class NewAlbumService extends TransactionHandler{

    private String albumName;

    private User user;


    private NewAlbumController controller;

    public void createNewAlbum(String albumName, User user)
    {
        this.albumName = albumName;
        this.user = user;

        createTransaction();
    }


    @Override
    public void task(DBConnector con) {
        AlbumRepo albumRepo = new AlbumRepo();
        Album album = new Album("");
        Album returnAlbum;
        album.setAlbumName(albumName);
        album.setAutoGenerate(false);
        album.setUser(user);
        returnAlbum = albumRepo.findByName(album,con);

        if (returnAlbum == null)
        {
            album.setAlbumID(UUID.randomUUID());

            if(albumName.equals("All"))
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Tuxedo View");
                alert.setContentText("You can't create an Album with this name");
                alert.showAndWait();


            }
            else if(!albumName.isEmpty())
            {
                albumRepo.dbInsertAlbum(album, con);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Tuxedo View");
                alert.setContentText("You create an album");
                alert.showAndWait();
                controller.getStage().close();

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
    public NewAlbumController getController() { return controller; }

    public void setController(NewAlbumController controller) { this.controller = controller; }

}
