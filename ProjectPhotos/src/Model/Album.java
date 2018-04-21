package Model;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table (name="Albums")
public class Album {
    @Id
    @Column (name="albumID")
    private UUID  albumID;

    @Column (name = "albumName")
    private String albumName;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="userid",referencedColumnName = "userid")
     User user;

    public UUID getAlbumID() {
        return albumID;
    }

    public void setAlbumID(UUID albumID) {
        this.albumID = albumID;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

}
