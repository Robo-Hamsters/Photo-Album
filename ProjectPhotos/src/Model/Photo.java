package Model;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;

import javax.imageio.ImageIO;
import javax.persistence.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;

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

    public void readImageMetadata(File file)
    {
        size = (double)file.length() / 1048576;
        namePhoto=file.getName();
        generateThumbnail(file);
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);

            ExifIFD0Directory exifDirectory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            ExifSubIFDDirectory exifSubIFDDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);

            if (exifDirectory != null) {
                setDateTime(exifDirectory.getDate(0x132));
                setModel(exifDirectory.getString(0x110));
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
        if(model == null)
            model = "";
        if(city == null)
            city = "";
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

    public void generateThumbnail(File file)
    {
        BufferedImage original;
        try {
            original = ImageIO.read(file);
            int thumbnailWidth = 150;
            int widthScale, heightScale;
            if(original.getWidth() > original.getHeight())
            {
                heightScale = (int)(1.1 * thumbnailWidth);
                widthScale = (int)((heightScale * 1.0) / original.getHeight() * original.getWidth());
            } else {
                widthScale = (int)(1.1 * thumbnailWidth);
                heightScale = (int)((widthScale * 1.0) / original.getWidth() * original.getHeight());
            }
            BufferedImage resizedImage = new BufferedImage(widthScale, heightScale, original.getType());
            Graphics2D g = resizedImage.createGraphics();

            g.setComposite(AlphaComposite.Src);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.drawImage(original, 0, 0, widthScale, heightScale, null);
            g.dispose();

            int x = (resizedImage.getWidth() - thumbnailWidth) / 2;
            int y = (resizedImage.getHeight() - thumbnailWidth) / 2;
            if(x < 0 || y < 0)
                throw new IllegalArgumentException("Width of thumbnail is bigger");
            BufferedImage thumbnailImage = resizedImage.getSubimage(x, y, thumbnailWidth, thumbnailWidth);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write( thumbnailImage, "jpg", baos );
            baos.flush();
            thumbnail = baos.toByteArray();
            baos.close();

            //ImageIO.write(thumbnailImage, "JPG", new File("./asdasd.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Photo(String namePhoto) {
        this.namePhoto = namePhoto;
    }
    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }
}
