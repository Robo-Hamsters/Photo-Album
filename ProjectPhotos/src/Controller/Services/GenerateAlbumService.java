package Controller.Services;

import Model.Album;
import Model.User;
import Repo.AlbumRepo;
import Repo.DBConnector;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;

import java.util.ArrayList;
import java.util.List;

public class GenerateAlbumService {

    public void fillComboBox(ComboBox<String> albumNames, User user)
    {
        DBConnector con = new DBConnector();
        con.databaseConnect();
        con.setSession(con.getFactory().getCurrentSession()) ;
        con.getSession().beginTransaction();

        AlbumRepo albumRepo = new AlbumRepo();

        List<Album> listAlbum=albumRepo.findByUser(user, con);
        List<String> albumStr = new ArrayList<>();

        for(Album album : listAlbum)
        {
            albumStr.add(album.getAlbumName());
        }
        albumNames.setItems(FXCollections.observableArrayList(albumStr));
        con.databaseDisconnect();
    }


}
