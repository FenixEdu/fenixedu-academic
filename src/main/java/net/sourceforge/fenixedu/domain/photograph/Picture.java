/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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

    public void delete() {
        super.deleteDomainObject();
    }

    public byte[] getBytes() {
        return getPictureData().getBytes();
    }

    public void setupPictureMetadata(byte[] pictureData) {
        BufferedImage buffer = Picture.readImage(pictureData);
        setWidth(buffer.getWidth());
        setHeight(buffer.getHeight());
    }

    static public BufferedImage readImage(byte[] imageData) {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        try {
            return ImageIO.read(bais);
        } catch (IOException ioe) {
            throw new DomainException("error.photograph.imageio.failedReadingImageFromByteArray", ioe);
        }
    }

    static public byte[] writeImageAsBytes(BufferedImage image, ContentType fileFormat) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, fileFormat.getFileExtention(), out);
            return out.toByteArray();
        } catch (IOException ioe) {
            throw new DomainException("error.photograph.imageio.failedWritingImageToByteArray", ioe);
        }
    }

    static public ByteArray writeImage(BufferedImage image, ContentType fileFormat) {
        return new ByteArray(writeImageAsBytes(image, fileFormat));
    }

    /**
     * Executes an {@link AspectRatio} transformation by fitting the image to the new aspect ratio.
     * Note that it uses an RGBA color space making the resulting image not suitable to be rendered
     * as a JPEG. Before rendering as JPEG apply a filler on the background.
     * 
     * @param source
     * @param xRatio
     * @param yRatio
     * @return {@link BufferedImage}
     */
    static public BufferedImage transformFit(BufferedImage source, int xRatio, int yRatio) {
        int destW, destH;
        BufferedImage scaled, padded, finale;
        if ((1.0 * source.getWidth() / source.getHeight()) < (1.0 * xRatio / yRatio)) {
            destH = source.getHeight();
            destW = (int) Math.round((destH * xRatio * 1.0) / (yRatio * 1.0));
            scaled = Scalr.resize(source, Method.QUALITY, Mode.FIT_TO_HEIGHT, destW, destH);

            int padding = (int) Math.round((destW - source.getWidth()) / 2.0);
            padded = (padding != 0) ? Scalr.pad(scaled, padding, new Color(255, 255, 255, 0)) : scaled;
            finale = Scalr.crop(padded, 0, padding, destW, destH);
        } else {
            destW = source.getWidth();
            destH = (int) Math.round((destW * yRatio * 1.0) / (xRatio * 1.0));
            scaled = Scalr.resize(source, Method.QUALITY, Mode.FIT_TO_WIDTH, destW, destH);

            int padding = (int) Math.round((destH - source.getHeight()) / 2.0);
            padded = (padding != 0) ? Scalr.pad(scaled, padding, new Color(255, 255, 255, 0)) : scaled; //Scalr.pad() instead of ignoring padding = 0, it throws an Exception
            finale = Scalr.crop(padded, padding, 0, destW, destH);
        }
        return finale;
    }

    /**
     * Executes an {@link AspectRatio} transformation by zooming the image into the new aspect ratio.
     * Note that it uses an RGBA color space making the resulting image not suitable to be rendered
     * as a JPEG. Before rendering as JPEG apply a filler on the background.
     * 
     * @param source
     * @param xRatio
     * @param yRatio
     * @return {@link BufferedImage}
     */
    static public BufferedImage transformZoom(BufferedImage source, int xRatio, int yRatio) {
        int destW, destH;
        BufferedImage scaled, finale;
        if ((1.0 * source.getWidth() / source.getHeight()) > (1.0 * xRatio / yRatio)) {
            destH = source.getHeight();
            destW = (int) Math.round((destH * xRatio * 1.0) / (yRatio * 1.0));
            //scaled = Scalr.resize(source, Method.QUALITY, Mode.FIT_TO_WIDTH, destW, destH);

            int padding = (int) Math.round((source.getWidth() - destW) / 2.0);
            finale = Scalr.crop(source, padding, 0, destW, destH);
        } else {
            destW = source.getWidth();
            destH = (int) Math.round((destW * yRatio * 1.0) / (xRatio * 1.0));
            //scaled = Scalr.resize(source, Method.QUALITY, Mode.FIT_TO_HEIGHT, destW, destH);

            int padding = (int) Math.round((source.getHeight() - destH) / 2.0);
            finale = Scalr.crop(source, 0, padding, destW, destH);
        }
        return finale;
    }

    static public BufferedImage transform(BufferedImage source, AspectRatio aspectRatio, PictureMode pictureMode) {
        switch (pictureMode) {
        case FIT:
            return Picture.transformFit(source, aspectRatio.getXRatio(), aspectRatio.getYRatio());
        case ZOOM:
            return Picture.transformZoom(source, aspectRatio.getXRatio(), aspectRatio.getYRatio());
        default:
            return Picture.transformFit(source, aspectRatio.getXRatio(), aspectRatio.getYRatio());
        }
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
