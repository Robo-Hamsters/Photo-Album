package Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AlbumController {

    @FXML
    public void openImageUploadForm(ActionEvent event) throws IOException
    {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("../UI/ImageUploadForm.fxml"));
        Parent imageUploadOpen=loader.load();
        Stage stage = new Stage();
        stage.setTitle("Upload Image");
        stage.setScene(new Scene(imageUploadOpen));
        stage.show();

        ImageUploadController controller=loader.getController();


       /* Stage window=(Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(AlbumScene);
        window.show(); */
    }
}
