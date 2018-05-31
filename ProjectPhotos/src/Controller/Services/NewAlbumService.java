package Controller.Services;

import Controller.NewAlbumController;
import Model.Album;
import Repo.AlbumRepo;
import Repo.DBConnector;
import javafx.scene.control.Alert;

import java.util.UUID;

public class NewAlbumService extends TransactionHandler{


    private Album returnAlbum;

    private Album album;


    private NewAlbumController controller;

    public void createNewAlbum(Album album)
    {
        this.album = album;
        createTransaction();
    }

    private boolean checkValidNames()
    {
        boolean isValid = false;
        if (returnAlbum == null)
        {
            album.setAlbumID(UUID.randomUUID());

            if(album.getAlbumName().equals("All"))
            {
                Alert alert=new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Tuxedo View");
                alert.setContentText("You can't create an Album with this name");
                alert.showAndWait();


            }
            else if(!album.getAlbumName().isEmpty())
            {
                isValid = true;
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
        return isValid;
    }


    @Override
    public void task(DBConnector con) {
        AlbumRepo albumRepo = new AlbumRepo();
        returnAlbum = albumRepo.findByNameAndUser(album,con);

        if(returnAlbum == null)
        {
            if(album.isAutoGenerate())
            {
                albumRepo.dbInsertAlbum(album, con);
            }
            else if(checkValidNames()){
                albumRepo.dbInsertAlbum(album, con);
            }

        }


    }
    public NewAlbumController getController() { return controller; }

    public void setController(NewAlbumController controller) { this.controller = controller; }

}
