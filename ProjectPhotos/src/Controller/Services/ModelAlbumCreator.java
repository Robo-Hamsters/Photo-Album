package Controller.Services;

import Model.Photo;

public class ModelAlbumCreator implements SmartAlbumCreator {
    @Override
    public String getAlbumName(Photo photo) {
        return photo.getModel() == null ? "" : photo.getModel();
    }
}
