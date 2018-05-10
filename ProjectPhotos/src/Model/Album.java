package Model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table (name="Albums")
public class Album {
    @Id
    @Column (name="albumID")
    private UUID  albumID;


    @Column (name = "albumName")
    private String albumName;


    @Column (name="autoGenerate")
    private boolean autoGenerate;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="userid",referencedColumnName = "userid")
     User user;


    public boolean isAutoGenerate() {
        return autoGenerate;
    }

    public void setAutoGenerate(boolean autoGenerate) {
        this.autoGenerate = autoGenerate;}
    public UUID getAlbumID() {
        return albumID;
    }

    public Album (){albumName = "";}

    public Album(String albumName, boolean autoGenerate)
    {
        this.albumID = UUID.randomUUID();
        this.albumName = albumName;
        this.autoGenerate = autoGenerate;
    }
    public Album(String albumName, boolean autoGenerate,User user)
    {
        this.albumID = UUID.randomUUID();
        this.albumName = albumName;
        this.autoGenerate = autoGenerate;
        this.user = user;
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

    public Album(String albumName) { this.albumName = albumName; this.albumID = UUID.randomUUID();}


    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    @Override
    public String toString()
    {
        return this.albumName;
    }
}
