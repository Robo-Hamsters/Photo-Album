package Controller;

import Controller.Services.CountryAlbumCreator;
import Controller.Services.ImageUploadService;
import Controller.Services.ModelAlbumCreator;
import Controller.Services.NewAlbumService;
import Model.Album;
import Model.FileManager;
import Model.User;
import Repo.LocationParser;
import Model.Photo;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
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
    private  List<Album> albumsCombo =new ArrayList<>();
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
                metadata += photo.getLongitude()+" "+photo.getLatitude();
            }
            else
                metadata += "No GPS info!";
            metadata += "\nLocation: ";
            if(photo.hasCountry())
                metadata +=photo.getCountry();
            else
                metadata += "No locational Data!";

            if(photo.hasModel())
                metadata += "\nModel: "+photo.getModel();
            else
                metadata += "\nModel: No model found!";

            lbl_metadata.setText(metadata);

            List<String> albumStr = new ArrayList<>();

            for(Album album : albumsCombo)
            {
                if(!album.isAutoGenerate())
                    albumStr.add(album.getAlbumName());
            }
            albumComboBox.setItems(FXCollections.observableArrayList(albumStr));
        }
    }

    @FXML
    private void imageUpload(ActionEvent event) throws IOException {
            Album returnAlbum = null;
            ImageUploadService service = new ImageUploadService();
            NewAlbumService newAlbumService = new NewAlbumService();
            if(albumComboBox.getValue() != null) {
                returnAlbum = service.chooseFromCombo(new Album(albumComboBox.getValue()));
                returnAlbum.setUser(user);
            }

            service.registerSmartAlbumCreator(new ModelAlbumCreator());
            service.registerSmartAlbumCreator(new CountryAlbumCreator());

            album.addAll(service.createAlbumsFromMetadata(photo,user));
            for(Album album : album)
            {
                photo.getAlbums().add(album.getAlbumName());
                newAlbumService.createNewAlbum(album);
            }

            if(returnAlbum != null)
            {
                photo.getAlbums().add(returnAlbum.getAlbumName());
            }
            photo.setUser(user);
            image.saveFile(photo);


        Node source = (Node)event.getSource();
        Stage stage = (Stage)source.getScene().getWindow();

        stage.close();
    }





    public void setUser(User user) { this.user = user; }
    public List<Album> getAlbum() {
        return albumsCombo;
    }

    public void setAlbum(List<Album> album) {
        this.albumsCombo = album;
    }


}