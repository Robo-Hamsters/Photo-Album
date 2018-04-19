package Controller;

import Model.Photo;
import Model.User;
import Repo.DBConnector;
import Repo.PhotoRepo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class AlbumController {

    @FXML
    private ImageView imageViewer;

    @FXML
    private Label labelUsername;

    private User user;


    public void loadImageView(){
        DBConnector con= new DBConnector();
        con.databaseConnect();
        con.setSession(con.getFactory().getCurrentSession()) ;
        con.getSession().beginTransaction();

        PhotoRepo photoRepo=new PhotoRepo();
        List<Photo> photos = photoRepo.findByUser(user,con);
        //Photo photo = photoRepo.dbSelectPhoto(con);

        //Image img = new Image(new ByteArrayInputStream(photo.getImage()));
        ImageView imageView = new ImageView();
        //imageView.setImage(img);
        con.databaseDisconnect();

    }
    @FXML
    public void openImageUploadForm(ActionEvent event) throws IOException
    {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("../UI/ImageUploadForm.fxml"));
        Parent imageUploadOpen=loader.load();
        ImageUploadController controller=loader.getController();
        controller.setUser(user);
        loader.setController(loader);
        Stage stage = new Stage();
        stage.setTitle("Upload Image");
        stage.setScene(new Scene(imageUploadOpen));
        stage.showAndWait();

        loadImageView();


    }
    @FXML
    public void openNewAlbumForm(ActionEvent event) throws IOException
    {

        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("../UI/NewAlbumForm.fxml"));
        Parent newAlbum=loader.load();
        NewAlbumController controller=loader.getController();
        Stage stage = new Stage();
        stage.setTitle("Create album");
        stage.setScene(new Scene(newAlbum));
        controller.setUser(user);
        loader.setController(controller);
        stage.showAndWait();
    }

    public Label getLabelUsername() {
        return labelUsername;
    }

    public void setLabelTextUsername(String Username) {
        this.labelUsername.setText("Welcome "+Username+"!");
    }
    public void setUser(User user) { this.user = user; }

    public void imageViewCreator(){


    }
}
