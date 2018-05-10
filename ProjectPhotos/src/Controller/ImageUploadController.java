package Controller;

import Controller.Services.GenerateAlbumService;
import Controller.Services.ImageUploadService;
import Model.Album;
import Model.FileManager;
import Model.User;
import Repo.AlbumRepo;
import Repo.DBConnector;
import Repo.LocationParser;
import Model.Photo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ImageUploadController {

    @FXML
    private ImageView imageViewer;
    @FXML
    private TextField tBox_tagImg;
    @FXML
    private Button btn_upload;
    @FXML
    private Label lbl_metadata;
    @FXML
    private ComboBox<String> albumComboBox;

    private Photo photo;

    private  User user;

    private List<Album> album = new ArrayList<>();

    private FileManager image;


    @FXML
    private void selectImage(ActionEvent event) {
        image = new FileManager();
        image.chooseImage();
        if (image.getFile() != null) {

            Image imageObj = new Image(image.getFile().toURI().toString());
            imageViewer.setImage(imageObj);

            tBox_tagImg.disableProperty().setValue(false);
            btn_upload.disableProperty().setValue(false);
            albumComboBox.disableProperty().setValue(false);

            photo = new Photo();
            photo.readImageMetadata(image.getFile());

            String metadata = "Date-time:\n" + photo.getDateTime()+"\nSize: "+String.format("%.3f", photo.getSize())+" MB\nGPS: ";

            if(photo.hasLocation()) {
                LocationParser gpsLocation = new LocationParser(photo.getLatitude(), photo.getLongitude());
                photo.setCity(gpsLocation.getCity());
                photo.setCountry(gpsLocation.getCountry());
                metadata += photo.getLongitude()+" "+photo.getLatitude()+"\nLocation: ";

            }

            else
                metadata += "No GPS info!\n";

            if(photo.hasCountry())
                metadata +=photo.getCountry();
            else
                metadata += "No locational Data!";

            if(photo.hasModel())
                metadata += "\nModel: "+photo.getModel();
            else
                metadata += "\nModel: No model found!";

            lbl_metadata.setText(metadata);

            GenerateAlbumService generateAlbumService = new GenerateAlbumService();
            generateAlbumService.fillComboBox(albumComboBox,user);

        }
    }

    @FXML
    private void imageUpload(ActionEvent event) throws IOException {


            Album returnAlbum = new Album();
            ImageUploadService service = new ImageUploadService();
            try {
                returnAlbum = service.chooseFromCombo(new Album(albumComboBox.getValue()));
                returnAlbum.setUser(user);


            }catch (NullPointerException e)
            {

            }
            AlbumRepo albumRepo = new AlbumRepo();
            DBConnector con = new DBConnector();

            album.addAll(service.createAlbumsFromMetadata(photo));

            for(Album album : album)
            {
                con.databaseConnect();
                con.setSession(con.getFactory().getCurrentSession());
                con.getSession().beginTransaction();
                photo.getAlbums().add(album.getAlbumName());
                albumRepo.dbInsertAlbum(album,con);
                con.databaseDisconnect();

            }
            album.add(returnAlbum);

            photo.setUser(user);
            image.saveFile(photo);


        Node source = (Node)event.getSource();
        Stage stage = (Stage)source.getScene().getWindow();

        stage.close();
    }





    public void setUser(User user) { this.user = user; }


}