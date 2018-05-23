package Model.SmartAlbum;

import Model.Photo;
import Model.SmartAlbum.SmartAlbumCreator;

import java.util.Calendar;
import java.util.Locale;

public class DateAlbumCreator implements SmartAlbumCreator {
    @Override
    public String getAlbumName(Photo photo)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(photo.getDateTime());
        return cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) + " " +cal.get(Calendar.YEAR);
    }
}
