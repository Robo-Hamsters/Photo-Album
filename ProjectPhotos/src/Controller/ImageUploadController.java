package Controller;

import Controller.Services.GenerateAlbumService;
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
import java.util.*;


public class ImageUploadController {

    @FXML
    private ImageView imageViewer;
    @FXML
    private Button btn_newFolder;
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

    private Album album;

    private FileManager image;


    @FXML
    private void selectImage(ActionEvent event) {
        image = new FileManager();
        image.chooseImage();
        if (image.getFile() != null) {

            Image imageObj = new Image(image.getFile().toURI().toString());
            imageViewer.setImage(imageObj);

            btn_newFolder.disableProperty().setValue(false);
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

            album = new Album();
            chooseFromCombo();
            photo.setUser(user);
            image.saveFile(photo);


        Node source = (Node)event.getSource();
        Stage stage = (Stage)source.getScene().getWindow();

        stage.close();
    }


    @FXML
    private void generateByLocation()
    {
        albumComboBox.disableProperty().setValue(true);
        btn_newFolder.disableProperty().setValue(true);

        DBConnector con1 = new DBConnector();
        con1.databaseConnect();
        con1.setSession(con1.getFactory().getCurrentSession()) ;
        con1.getSession().beginTransaction();


        Album album = new Album();
        Album returnAlbum = new Album();
        AlbumRepo albumRepo = new AlbumRepo();

        if(!(photo.getCountry().equals(""))) {
            album.setAlbumName(photo.getCountry());
            album.setUser(user);
            returnAlbum = albumRepo.findByName(album, con1);
            con1.databaseDisconnect();

            if (returnAlbum == null) {
                DBConnector con2 = new DBConnector();
                con2.databaseConnect();
                album.setAlbumID(UUID.randomUUID());
                albumRepo.dbInsertAlbum(album, con2);
                con2.databaseDisconnect();

            }

            photo.setAlbum(album);

        }
        else
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Tuxedo View");
            alert.setContentText("You can't make an Album from this photo's location.\nTHIS PHOTO IS FROM SPACE!");
            alert.showAndWait();
            albumComboBox.disableProperty().setValue(false);
        }

    }
    private void chooseFromCombo()
    {

        album.setAlbumName(albumComboBox.getValue());
        album.setUser(user);

        DBConnector con = new DBConnector();
        con.databaseConnect();
        con.setSession(con.getFactory().getCurrentSession()) ;
        con.getSession().beginTransaction();

        AlbumRepo albumRepo = new AlbumRepo();
        album=albumRepo.findByName(album,con);

        photo.setAlbum(album);
        con.databaseDisconnect();

    }

    public void setUser(User user) { this.user = user; }
}