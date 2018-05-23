package Model.SmartAlbum;

import Model.Photo;

public class CityAlbumCreator implements SmartAlbumCreator {
    @Override
    public String getAlbumName(Photo photo) {
        return photo.getCity() == null ? "" : photo.getCity();
    }
}
