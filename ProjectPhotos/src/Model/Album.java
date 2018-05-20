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
    private User user;

    public boolean isAutoGenerate() {
        return autoGenerate;
    }

    public void setAutoGenerate(boolean autoGenerate) {
        this.autoGenerate = autoGenerate;}
    public void setAlbumID(UUID albumID) {
        this.albumID = albumID;
    }
    public void setUser(User user) {
        this.user = user;
    }
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public UUID getAlbumID() {
        return albumID;
    }
    public User getUser() {
        return user;
    }
    public String getAlbumName() {
        return albumName;
    }

    public Album ()
    {
        albumName = "";
    }
    public Album(String albumName)
    {
        this.albumName = albumName;
        this.albumID = UUID.randomUUID();
    }
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

    @Override
    public String toString()
    {
        return this.albumName;
    }
}
