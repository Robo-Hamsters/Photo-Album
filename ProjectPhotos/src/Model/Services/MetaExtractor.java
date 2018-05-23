package Model.Services;

import Model.Photo;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.file.FileSystemDirectory;

import java.io.File;
import java.io.IOException;

public class MetaExtractor {


    public static Photo readImageMetadata(File file) {
        Photo photo = new Photo();
        photo.setSize((double) file.length() / 1048576);
        photo.setNamePhoto(file.getName());

        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file);

            FileSystemDirectory fileSystemDirectory = metadata.getFirstDirectoryOfType(FileSystemDirectory.class);
            ExifIFD0Directory exifDirectory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
            ExifSubIFDDirectory exifSubIFDDirectory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
            GpsDirectory gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory.class);

            if (exifDirectory != null) {
                photo.setDateTime(exifDirectory.getDate(0x132));
                photo.setModel(exifDirectory.getString(0x110));
            }
            if (photo.getDateTime() == null && exifSubIFDDirectory != null) {
                photo.setDateTime(exifSubIFDDirectory.getDate(0x9003));
            }
            if (gpsDirectory != null) {
                if (gpsDirectory.containsTag(0x1)) {
                    photo.setLatitude(gpsDirectory.getGeoLocation().getLatitude());
                    photo.setLongitude(gpsDirectory.getGeoLocation().getLongitude());
                }
            }
            if(photo.getDateTime() == null)
            {
                photo.setDateTime(fileSystemDirectory.getDate(0x3));
            }
            if(photo.hasLocation()) {
                LocationParser gpsLocation = new LocationParser(photo.getLatitude(), photo.getLongitude());
                photo.setCity(gpsLocation.getCity());
                photo.setCountry(gpsLocation.getCountry());

            }
        } catch (ImageProcessingException ex) {
        } catch (IOException e) {
        }
        return photo;
    }
}
