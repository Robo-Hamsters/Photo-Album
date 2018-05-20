package Controller.Services;

import Model.Photo;

public class CountryAlbumCreator implements SmartAlbumCreator {
    @Override
    public String getAlbumName(Photo photo) {
        return photo.getCountry() == null ? "" : photo.getCountry();
    }
}
