package Controller;

import Model.Photos;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;


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
    private Label lbl_location;


    @FXML
    private void selectImage(ActionEvent event) {
        Photos image = new Photos();
        image.chooseImage();
        if (image.getFile() != null) {
            Image imageObj = new Image(image.getFile().toURI().toString());
            imageViewer.setImage(imageObj);
            btn_newFolder.disableProperty().setValue(false);
            btn_selectFolder.disableProperty().setValue(false);
            tBox_tagImg.disableProperty().setValue(false);
            btn_upload.disableProperty().setValue(false);
            image.readImageMetadata();
        }
    }

    @FXML
    private void imageUpload(ActionEvent event) throws IOException {

        // TODO -> This function is being called by button Upload. Right some code about importing an image and its characteristics to the database.

    }
}