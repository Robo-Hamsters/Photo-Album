package Model.Services;

import javassist.bytecode.ByteArray;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class ThumbnailGenerator {

    public byte[] getThumbnail() {
        return thumbnail;
    }

    byte[] thumbnail;


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

}
