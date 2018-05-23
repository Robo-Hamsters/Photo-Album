package Model.SmartAlbum;

import Model.Photo;
import Model.SmartAlbum.SmartAlbumCreator;

public class CountryAlbumCreator implements SmartAlbumCreator {
    @Override
    public String getAlbumName(Photo photo) {
        return photo.getCountry() == null ? "" : photo.getCountry();
    }
}
