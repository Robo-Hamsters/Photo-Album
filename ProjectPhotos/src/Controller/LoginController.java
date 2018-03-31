package Controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class LoginController {

    public void openAlbumForm(ActionEvent event) throws IOException
    {
        FXMLLoader loader=new FXMLLoader();
        loader.setLocation(getClass().getResource("../UI/AlbumForm.fxml"));
        Parent albumOpen=loader.load();
        Scene AlbumScene=new Scene(albumOpen);

        AlbumController controller=loader.getController();


        Stage window=(Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(AlbumScene);
        window.show();
    }

}



