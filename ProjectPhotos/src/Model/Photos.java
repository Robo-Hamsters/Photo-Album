package Model;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.GpsDirectory;

import com.sun.jndi.toolkit.url.Uri;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public class Photos {

    private int idPhoto;

    public int getIdPhoto() {
        return idPhoto;
    }

    public void setIdPhoto(int idPhoto) {
        this.idPhoto = idPhoto;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    private Date dateTime;
    private String model;
    private double[] location = new double[2];
    private double size;
    private File file;


    public void chooseImage()
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");

        FileChooser.ExtensionFilter imageFilter=new FileChooser.ExtensionFilter("Image Files","*.png","*.jpeg","*.jpg");

        fileChooser.getExtensionFilters().add(imageFilter);
        fileChooser.setSelectedExtensionFilter(imageFilter);

        file = fileChooser.showOpenDialog(null);
    }

    public void readImageMetadata()
    {
        size = (double)file.length() / 1048576;

        try {
            Metadata metadata= ImageMetadataReader.readMetadata(file);

            ExifIFD0Directory exifDirectory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);

            if(exifDirectory != null) {
                dateTime = exifDirectory.getDate(0x132);
                model = exifDirectory.getString(0x110);

                if(gpsDirectory.containsTag(0x1)) {
                    location[0] = gpsDirectory.getGeoLocation().getLatitude();
                    location[1] = gpsDirectory.getGeoLocation().getLongitude();
                }
                //DecimalFormat decimalFormat =
                System.out.println("Size: " + String.format("%.3f",size) + " mb");
                System.out.println("Location: " + location[0] + " " + location[1]);
                System.out.println("Date & Time: " + dateTime);
                System.out.println("Model: " + model);
            }

        } catch (ImageProcessingException ex) {
        } catch (IOException e) {
        }

    }

    /*public void getMap(double[] location1)
    {

        String location="geo:0,0?=q="+location1;
        Uri geoUri =Uri.parse(location);
        Intent mapcall =new Intent(Intent.ACTION_VIEW,geoUri);

    }*/

}
