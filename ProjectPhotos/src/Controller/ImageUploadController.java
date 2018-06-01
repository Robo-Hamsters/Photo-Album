package Controller;

import Controller.Services.*;
import Model.*;
import Model.Services.LocationParser;
import Model.Services.FileManager;
import Model.Services.MetaExtractor;
import Model.Services.ThumbnailGenerator;
import Model.SmartAlbum.*;
import javafx.collections.FXCollections;
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
    private  List<Album> albumsCombo =new ArrayList<>();
    private List<Album> album = new ArrayList<>();
    private FileManager image;

    @FXML
    private void selectImage(ActionEvent event) {

        image = new FileManager();
        image.chooseImage();

        if (image.hasFile()) {

            Image imageObj = new Image(image.getFile().toURI().toString());
            imageViewer.setImage(imageObj);
            tBox_tagImg.disableProperty().setValue(false);
            btn_upload.disableProperty().setValue(false);
            albumComboBox.disableProperty().setValue(false);

            photo = MetaExtractor.readImageMetadata(image.getFile());
            ThumbnailGenerator generator = new ThumbnailGenerator();
            generator.generateThumbnail(image.getFile());
            photo.setThumbnail(generator.getThumbnail());


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
    private void imageUpload(ActionEvent event) throws IOException
    {
            Album returnAlbum = null;
            ImageUploadService service = new ImageUploadService();
            if(albumComboBox.getValue() != null) {
                returnAlbum = service.chooseFromCombo(new Album(albumComboBox.getValue()),user);
                returnAlbum.setUser(user);
            }

            service.registerSmartAlbumCreator(new ModelAlbumCreator());
            service.registerSmartAlbumCreator(new CountryAlbumCreator());
            service.registerSmartAlbumCreator(new CityAlbumCreator());
            service.registerSmartAlbumCreator(new DateAlbumCreator());

            album.addAll(service.createAlbumsFromMetadata(photo,user));

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