package Controller;

import Controller.Services.NewAlbumService;
import Model.Album;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class NewAlbumController {

    @FXML
    private TextField textAlbumName;

    private User user;


    private Stage stage;

    @FXML
    void createAlbum(ActionEvent event)
    {
        NewAlbumService service = new NewAlbumService();
        Node source = (Node) event.getSource();
        stage = (Stage) source.getScene().getWindow();
        service.setController(this);

        Album album = new Album();
        album.setAlbumName(textAlbumName.getText());
        album.setUser(user);
        album.setAutoGenerate(false);

        service.createNewAlbum(album);



    }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }
    public Stage getStage() { return stage; }

    public void setStage(Stage stage) { this.stage = stage; }


}
