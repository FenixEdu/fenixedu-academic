package net.sourceforge.fenixedu.domain.photograph;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.ByteArray;
import net.sourceforge.fenixedu.util.ContentType;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;

public abstract class Picture extends Picture_Base {

    public Picture() {
        super();
    }

    public byte[] getBytes() {
        return getPictureData().getBytes();
    }

    public void setupPictureMetadata(ByteArray pictureData) {
        BufferedImage buffer = Picture.readImage(pictureData);
        setWidth(buffer.getWidth());
        setHeight(buffer.getHeight());
    }

    static public BufferedImage readImage(ByteArray imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData.getBytes());
        try {
            return ImageIO.read(bais);
        } catch (IOException ioe) {
            throw new DomainException("error.photograph.imageio.failedReadingImageFromByteArray", ioe);
        }
    }

    static public ByteArray writeImage(BufferedImage image, ContentType fileFormat) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, fileFormat.getFileExtention(), out);
            return new ByteArray(out.toByteArray());
        } catch (IOException ioe) {
            throw new DomainException("error.photograph.imageio.failedWritingImageToByteArray", ioe);
        }
    }

    /**
     * Executes an {@link AspectRatio} transformation. Note that it uses an RGBA color space
     * making the resulting image not suitable to be rendered as a JPEG. Before rendering as JPEG
     * apply a filler on the background.
     * 
     * @param source
     * @param xRatio
     * @param yRatio
     * @return {@link BufferedImage}
     */
    static public BufferedImage transform(BufferedImage source, int xRatio, int yRatio) {
        int destW, destH;
        BufferedImage scaled, padded, finale;
        if (source.getHeight() > source.getWidth()) {
            destH = source.getHeight();
            destW = (int) Math.round((destH * xRatio * 1.0) / (yRatio * 1.0));
            scaled = Scalr.resize(source, Method.QUALITY, Mode.FIT_TO_HEIGHT, destW, destH);

            int padding = (int) Math.round((destW - source.getWidth()) / 2.0);
            padded = Scalr.pad(scaled, padding, new Color(255, 255, 255, 0));
            finale = Scalr.crop(padded, 0, padding, destW, destH);
        } else {
            destW = source.getWidth();
            destH = (int) Math.round((destW * yRatio * 1.0) / (xRatio * 1.0));
            scaled = Scalr.resize(source, Method.QUALITY, Mode.FIT_TO_WIDTH, destW, destH);

            int padding = (int) Math.round((destH - source.getHeight()) / 2.0);
            padded = Scalr.pad(scaled, padding, new Color(255, 255, 255, 0));
            finale = Scalr.crop(padded, padding, 0, destW, destH);
        }
        return finale;
    }

    static public BufferedImage transform(BufferedImage source, AspectRatio aspectRatio) {
        return Picture.transform(source, aspectRatio.getXRatio(), aspectRatio.getYRatio());
    }

    static public BufferedImage fitTo(BufferedImage source, int width, int height) {
        Mode mode;
        if (source.getHeight() > source.getWidth()) {
            mode = Mode.FIT_TO_HEIGHT;
        } else {
            mode = Mode.FIT_TO_WIDTH;
        }
        return Scalr.resize(source, Method.QUALITY, mode, width, height);
    }

    static public BufferedImage fitTo(BufferedImage source, PictureSize pictureSize) {
        return Picture.fitTo(source, pictureSize.getWidth(), pictureSize.getHeight());
    }

}
