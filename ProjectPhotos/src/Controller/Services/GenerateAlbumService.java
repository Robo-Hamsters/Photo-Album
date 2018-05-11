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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
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


                        String webmapHtml = "<!DOCTYPE html>\n" +
                                "<html>\n" +
                                "<head>\n" +
                                "<meta name=\"viewport\" content=\"initial-scale=1.0, user-scalable=no\" />\n" +
                                "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>\n" +
                                "<style>html,body{height:100%;margin:0;padding:0;}#map_canvas{height:100%;}</style>\n" +
                                "<script type=\"text/javascript\" src=\"https://maps.googleapis.com/maps/api/js?sensor=false\"></script>\n" +
                                "<script type=\"text/javascript\">\n" +
                                "function initialise() {\n" +
                                "    var options = { zoom:13, mapTypeId:google.maps.MapTypeId.SATELLITE, center:new google.maps.LatLng("+photo.getLatitude()+","+photo.getLongitude()+")};\n" +
                                "    var map = new google.maps.Map(document.getElementById('map_canvas'), options);\n" +
                                "    var marker;\n" +
                                "\tmarker = new google.maps.Marker({\n" +
                                "\t\tposition:new google.maps.LatLng("+photo.getLatitude()+","+photo.getLongitude()+"), map:map, title:\"\"});\n" +
                                "\t\tgoogle.maps.event.addListener(marker, 'click', function() { document.location = \"\"; });\n" +
                                "}\n" +
                                "</script>\n" +
                                "</head>\n" +
                                "<body onload=\"initialise()\">\n" +
                                "<div id=\"map_canvas\"></div>\n" +
                                "</body>\n" +
                                "</html>";
                        Stage mapStage = new Stage();
                        mapStage.setWidth(400);
                        mapStage.setHeight(400);
                        mapStage.setTitle("Map");
                        BorderPane mapBorderPane = new BorderPane();

                        WebView browser = new WebView();
                        WebEngine webEngine = browser.getEngine();
                        webEngine.loadContent(webmapHtml);

                        mapBorderPane.setCenter(browser);

                        Scene mapScene = new Scene(mapBorderPane, Color.BLACK);
                        mapStage.setScene(mapScene);
                        mapStage.show();
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
            if(!album.isAutoGenerate())
                albumStr.add(album.getAlbumName());
        }
        albumNames.setItems(FXCollections.observableArrayList(albumStr));

    }
}
