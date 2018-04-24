package Controller;

import Model.Photo;
import Model.User;
import Repo.DBConnector;
import Repo.PhotoRepo;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class AlbumController {


    @FXML
    private Label labelUsername;

    @FXML
    private TilePane img_tilepane;

    @FXML
    private ScrollPane scrl_pane;

    private User user;

    public void loadImageView() {
        img_tilepane.getChildren().clear();
        img_tilepane.setVgap(15);
        img_tilepane.setHgap(15);
        img_tilepane.setPrefColumns(6);
        img_tilepane.setTileAlignment(Pos.TOP_LEFT);
        scrl_pane.setFitToWidth(true);
        scrl_pane.setFitToHeight(true);
        DBConnector con = new DBConnector();
        con.databaseConnect();
        con.setSession(con.getFactory().getCurrentSession());
        con.getSession().beginTransaction();

        PhotoRepo photoRepo = new PhotoRepo();

        List<Photo> photos = photoRepo.findByUser(user, con);
        for (final Photo photo : photos) {
            Image img = new Image(new ByteArrayInputStream(photo.getImage()),150, 105,true,true);
            ImageView imageView = new ImageView(img);
            img_tilepane.getChildren().addAll(imageView);
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
        }
        con.databaseDisconnect();
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
        Stage stage = new Stage();
        stage.setTitle("Upload Image");
        stage.setScene(new Scene(imageUploadOpen));
        imageUploadOpen.getStylesheets().add(ImageUploadController.class.getResource("../UI/Styles/ImageUploadForm.css").toExternalForm());
        stage.setResizable(false);
        stage.showAndWait();
        loadImageView();
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
    }

    public Label getLabelUsername() {
        return labelUsername;
    }

    public void setLabelTextUsername(String Username) {
        this.labelUsername.setText("Welcome "+Username+"!");
    }
    public void setUser(User user) { this.user = user; }

}
