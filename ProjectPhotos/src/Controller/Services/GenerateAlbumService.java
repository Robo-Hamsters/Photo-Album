package Controller.Services;

import Model.Album;
import Model.Photo;
import Model.User;
import Repo.AlbumRepo;
import Repo.DBConnector;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class GenerateAlbumService extends TransactionHandler {

    private ComboBox<String> albumNames;
    private User user;

    public void fillComboBox(ComboBox<String> albumNames, User user)
    {
        this.albumNames = albumNames;
        this.user = user;
        createTransaction();


    }

    public static ImageView createTilePaneImageView(Photo photo)
    {
        ImageView imageView = new ImageView(new Image(new ByteArrayInputStream(photo.getImage()),150, 105,true,true));
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                Stage stage = new Stage();

                if(mouseEvent.getButton().equals(MouseButton.PRIMARY))
                {
                    if(mouseEvent.getClickCount() == 2)
                    {
                        BorderPane borderPane = new BorderPane();
                        ImageView imageView = new ImageView();
                        Image img = new Image(new ByteArrayInputStream(photo.getImage()));
                        imageView.setImage(img);
                        imageView.setStyle("-fx-background-color: BLACK");
                        imageView.setFitHeight(stage.getHeight() - 10);
                        imageView.setPreserveRatio(true);
                        imageView.setSmooth(true);
                        imageView.setCache(true);
                        borderPane.setCenter(imageView);
                        borderPane.setStyle("-fx-background-color: BLACK");
                        Stage newStage = new Stage();
                        newStage.setWidth(stage.getWidth());
                        newStage.setHeight(stage.getHeight());
                        newStage.setTitle(photo.getNamePhoto());
                        imageView.fitHeightProperty().bind(newStage.heightProperty());
                        imageView.fitWidthProperty().bind(newStage.widthProperty());
                        Scene scene = new Scene(borderPane, Color.BLACK);
                        newStage.setScene(scene);
                        newStage.show();
                    }
                }


            }
        });
        return imageView;

    }


    @Override
    public void task(DBConnector con) {
        AlbumRepo albumRepo = new AlbumRepo();

        List<Album> listAlbum=albumRepo.findByUser(user, con);
        List<String> albumStr = new ArrayList<>();

        for(Album album : listAlbum)
        {
            albumStr.add(album.getAlbumName());
        }
        albumNames.setItems(FXCollections.observableArrayList(albumStr));

    }
}
