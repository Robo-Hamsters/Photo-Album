package Model;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table (name="Albums")
public class Album {
    @Id
    @Column (name="albumID")
    private UUID  albumId;

    @Column (name = "albumName")
    private String albumName="";

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="idPhoto",referencedColumnName = "idPhoto")
     Photo photo;



    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="userid",referencedColumnName = "userid")
     User user;



    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

}
