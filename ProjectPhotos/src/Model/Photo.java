package Model;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;

import javax.persistence.*;
import java.io.*;
import java.util.Date;
import java.util.UUID;

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
    private String model="";
    @Column (name="Image")
    private byte[] image;

    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="userid",referencedColumnName = "userid")
    User user;


    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="albumID",referencedColumnName = "albumID")
    Album album;

    public Album getAlbum() { return album; }

    public void setAlbum(Album album) { this.album = album; }
    public void setIdPhoto(UUID idPhoto) { this.idPhoto = idPhoto; }
    public void setCountry(String country) { this.country = country; }
    public void setCity(String city) { this.city = city; }
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

    public void setImage(byte[] image) {
        this.image = image;
    }
    public UUID getIdPhoto() {
        return idPhoto;
    }

    public String getNamePhoto() {
        return namePhoto;
    }

    public void setNamePhoto(String namePhoto) {
        this.namePhoto = namePhoto;
    }
    public boolean hasModel()
    {
        if(model == "")
            return false;
        else
            return true;
    }
    public boolean hasCountry()
    {
        if(country == "")
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

    public void readImageMetadata(File file)
    {
        size = (double)file.length() / 1048576;
        namePhoto=file.getName();
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);

            ExifIFD0Directory exifDirectory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            ExifSubIFDDirectory exifSubIFDDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);

            if (exifDirectory != null) {
                dateTime = exifDirectory.getDate(0x132);
                model = exifDirectory.getString(0x110);
            }
            if(dateTime==null && exifSubIFDDirectory!=null)
            {
                    dateTime = exifSubIFDDirectory.getDate(0x9003);
            }
            if (gpsDirectory!=null) {
                if(gpsDirectory.containsTag(0x1)) {
                    latitude = gpsDirectory.getGeoLocation().getLatitude();
                    longitude = gpsDirectory.getGeoLocation().getLongitude();
                }
            }
        } catch (ImageProcessingException ex) {
        } catch (IOException e) {
        }
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
    public Photo()
    {

    }
    public Photo(String namePhoto) {
        this.namePhoto = namePhoto;
    }
    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }
}
