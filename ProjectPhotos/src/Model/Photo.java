package Model;


import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "Photos")

public class Photo {

    @Id
    @Column(name="idPhoto")
    private UUID idPhoto;
    @Column (name="name")
    private String  namePhoto="";
    @Column (name="Longitude")
    private double longitude = 0.0;
    @Column (name="Latitude")
    private double latitude = 0.0;
    @Column (name="Size")
    private double size = 0.0;
    @Column (name="Country")
    private String country = "";
    @Column (name="City")
    private String city = "";
    @Column (name="Date")
    private Date dateTime;
    @Column (name="Model")
    private String model = "";
    @Column (name="Image")
    private byte[] image;
    @Column (name="Thumbnail")
    private byte[] thumbnail;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="userid",referencedColumnName = "userid")
    private User user;
    @Column (name="albums")
    @ElementCollection (targetClass = String.class,fetch=FetchType.EAGER)
    private List<String> albums = new ArrayList<>();

    public Photo(){};

    public void setIdPhoto(UUID idPhoto) {
        this.idPhoto = idPhoto;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
    public void setSize(double size) {
        this.size = size;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public void setImage(byte[] image) {
        this.image = image;
    }
    public void setAlbums(List<String> albums) { this.albums = albums == null ? new ArrayList<>() :  albums; }
    public void setNamePhoto(String namePhoto) {
        this.namePhoto = namePhoto;
    }
    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Date getDateTime() {
        return dateTime;
    }
    public String getModel() {
        return model;
    }
    public double getSize() {
        return size;
    }
    public double getLongitude() { return longitude; }
    public double getLatitude() { return latitude; }
    public String getCountry() { return country; }
    public String getCity() { return city;}
    public byte[] getImage() {
        return image;
    }
    public List<String> getAlbums() { return albums; }
    public UUID getIdPhoto() {
        return idPhoto;
    }
    public String getNamePhoto() {
        return namePhoto;
    }
    public byte[] getThumbnail() {
        return thumbnail;
    }

    public boolean hasModel()
    {
        if(model.isEmpty())
            return false;
        else
            return true;
    }
    public boolean hasCountry()
    {
        if(country.isEmpty())
            return false;
        else
            return true;
    }
    public boolean hasLocation()
    {
        if(longitude == 0 && latitude == 0)
            return false;
        else
            return true;
    }



    public Photo(UUID idPhoto, double longitude, double latitude, double size, String country, String city, Date dateTime, String model) {
        this.idPhoto = idPhoto;
        this.longitude = longitude;
        this.latitude = latitude;
        this.size = size;
        this.country = country;
        this.city = city;
        this.dateTime = dateTime;
        this.model = model;
    }

    public Photo(String namePhoto) {
        this.namePhoto = namePhoto;
    }
    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }
}
