package Controller;

import Controller.Services.*;
import Model.Album;
import Model.Photo;
import Model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AlbumController {
    @FXML
    private ListView<Album> albumListView;
    @FXML
    private Label labelUsername;
    @FXML
    private TilePane img_tilepane;
    @FXML
    private ScrollPane scrl_pane;
    private User user;
    private List<Photo> photos = new ArrayList<>();
    private List<Album> albums = new ArrayList<>();

    public void loadImageView(String albumName)
    {
        img_tilepane.getChildren().clear();
        img_tilepane.setVgap(15);
        img_tilepane.setHgap(15);
        img_tilepane.setPrefColumns(6);
        img_tilepane.setTileAlignment(Pos.TOP_LEFT);
        scrl_pane.setFitToWidth(true);
        scrl_pane.setFitToHeight(true);
        albumListView.getItems().clear();
        albumListView.getItems().add(new Album("All"));

        if(albums.size() > 0) {
            for (Album album : albums) {
                albumListView.getItems().add(album);
            }
        }
        if(photos.size() > 0) {
            if (albumName.equals("All")) {
                for (final Photo photo : photos) {
                    img_tilepane.getChildren().add(AlbumService.createTilePaneImageView(photo));
                }
            } else {
                for (final Photo photo : photos) {
                    if (photo.getAlbums().contains(albumName)) {
                        img_tilepane.getChildren().add(AlbumService.createTilePaneImageView(photo));
                    }
                }
            }
        }
    }

    @FXML
    public void displayByAlbum(MouseEvent event)
    {
        loadImageView(albumListView.getSelectionModel().getSelectedItem().getAlbumName());
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
        controller.setAlbum(albums);
        Stage stage = new Stage();
        stage.setTitle("Upload Image");
        stage.setScene(new Scene(imageUploadOpen));
        imageUploadOpen.getStylesheets().add(ImageUploadController.class.getResource("../UI/Styles/ImageUploadForm.css").toExternalForm());
        stage.setResizable(false);
        stage.showAndWait();
        retriveDBData();
        loadImageView("All");
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
        newAlbum.getStylesheets().add(NewAlbumController.class.getResource("../UI/Styles/NewAlbumForm.css").toExternalForm());
        controller.setUser(user);
        loader.setController(controller);
        stage.showAndWait();
        retriveDBData();
        loadImageView("All");
    }

    public void retriveDBData()
    {
        AlbumService service = new AlbumService(user);
        this.albums = service.getAlbums();
        this.photos = service.getPhotos();
    }
    public void setLabelTextUsername(String Username) {
        this.labelUsername.setText("Welcome "+Username+"!");
    }
    public void setUser(User user) {
        this.user = user;
    }
}
