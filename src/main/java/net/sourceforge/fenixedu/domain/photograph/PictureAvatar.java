package net.sourceforge.fenixedu.domain.photograph;

import java.awt.Color;
import java.awt.image.BufferedImage;

import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.ByteArray;
import net.sourceforge.fenixedu.util.ContentType;

public class PictureAvatar extends PictureAvatar_Base {

    private PictureAvatar() {
        super();
    }

    private PictureAvatar(Photograph photograph, AspectRatio aspectRatio, PictureSize pictureSize, ByteArray pictureData,
            int width, int height, ContentType pictureFileFormat) {
        this();
        setPhotograph(photograph);
        setAspectRatio(aspectRatio);
        setPictureSize(pictureSize);
        setPictureData(pictureData);
        setWidth(width);
        setHeight(height);
        setPictureFileFormat(pictureFileFormat);
    }

    protected RootDomainObject getRootDomainObject() {
        return getPhotograph().getRootDomainObject();
    }

    public byte[] exportAsJPEG() {
        if (getPictureFileFormat() == ContentType.JPG) {
            return getPictureData().getBytes();
        }
        BufferedImage image = Picture.readImage(getPictureData());
        BufferedImage jpeg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        jpeg.createGraphics().drawImage(image, 0, 0, Color.BLACK, null);
        return Picture.writeImage(jpeg, ContentType.JPG).getBytes();
    }

    static public void createAvatars(Photograph photograph, ByteArray original) {
        BufferedImage image = Picture.readImage(original);
        for (AspectRatio aspectRatio : AspectRatio.values()) {
            BufferedImage adjustedImage = Picture.transform(image, aspectRatio);
            for (PictureSize pictureSize : PictureSize.values()) {
                BufferedImage avatar = Picture.fitTo(adjustedImage, pictureSize);
                new PictureAvatar(photograph, aspectRatio, pictureSize, Picture.writeImage(avatar, ContentType.PNG),
                        avatar.getWidth(), avatar.getHeight(), ContentType.PNG);
            }

        }
    }

}
