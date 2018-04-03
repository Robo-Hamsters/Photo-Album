package Controller;

import Model.FileManager;
import Model.User;
import Repo.DBConnector;
import Repo.LocationParser;
import Model.Photo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.UUID;


public class ImageUploadController {

    @FXML
    private ImageView imageViewer;
    @FXML
    private Button btn_newFolder;
    @FXML
    private TitledPane btn_selectFolder;
    @FXML
    private TextField tBox_tagImg;
    @FXML
    private Button btn_upload;
    @FXML
    private Label lbl_metadata;

    private Photo photo;

    private  User user;

    private FileManager image;


    @FXML
    private void selectImage(ActionEvent event) {
        image = new FileManager();
        image.chooseImage();
        if (image.getFile() != null) {

            Image imageObj = new Image(image.getFile().toURI().toString());
            imageViewer.setImage(imageObj);

            btn_newFolder.disableProperty().setValue(false);
            btn_selectFolder.disableProperty().setValue(false);
            tBox_tagImg.disableProperty().setValue(false);
            btn_upload.disableProperty().setValue(false);

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
        }
    }

    @FXML
    private void imageUpload(ActionEvent event) throws IOException {

        photo.setUser(user);

        image.saveFile(photo);

        Node source = (Node)event.getSource();
        Stage stage = (Stage)source.getScene().getWindow();
        stage.close();

    }

    public void setUser(User user) { this.user = user; }
}