package Controller;

import Model.Photo;
import Repo.DBConnector;
import Repo.PhotoRepo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class AlbumController {

    @FXML
    private ImageView imageViewer;

    @FXML
    private Label labelUserName;

    public void loadImageView(){
        DBConnector con= new DBConnector();
        con.databaseConnect();
        con.setSession(con.getFactory().getCurrentSession()) ;

        con.getSession().beginTransaction();
        PhotoRepo photoRepo=new PhotoRepo();
        Photo photo = photoRepo.dbSelectPhoto(photoRepo,con);
        Image img = new Image(new ByteArrayInputStream(photo.getImage()));
        imageViewer.setImage(img);
        con.databaseDisconnect();
    }
    @FXML
    public void openImageUploadForm(ActionEvent event) throws IOException
    {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("../UI/ImageUploadForm.fxml"));
        Parent imageUploadOpen=loader.load();
        ImageUploadController controller=loader.getController();
        Stage stage = new Stage();
        stage.setTitle("Upload Image");
        stage.setScene(new Scene(imageUploadOpen));
        stage.showAndWait();

        loadImageView();

       /* Stage window=(Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(AlbumScene);
        window.show(); */
    }
    
}
