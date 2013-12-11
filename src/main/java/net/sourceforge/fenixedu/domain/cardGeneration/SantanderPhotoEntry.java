package net.sourceforge.fenixedu.domain.cardGeneration;

import java.awt.Color;
import java.awt.image.BufferedImage;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.photograph.Picture;
import net.sourceforge.fenixedu.domain.photograph.PictureOriginal;
import net.sourceforge.fenixedu.util.ContentType;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;
import org.joda.time.DateTime;

import pt.ist.bennu.core.domain.Bennu;
import pt.ist.fenixframework.Atomic;

public class SantanderPhotoEntry extends SantanderPhotoEntry_Base {

    public SantanderPhotoEntry(final Person person, final Photograph photograph) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setWhenGenerated(new DateTime());
        if (person.getSantanderPhotoEntry() != null) {
            setNext(person.getSantanderPhotoEntry());
        }
        setPerson(person);
        setPhotograph(photograph);
        setSequenceNumber(SantanderSequenceNumberGenerator.getNewPhotoSequenceNumber());
    }

    @Atomic
    public static SantanderPhotoEntry getOrCreatePhotoEntryForPerson(final Person person) {
        final Photograph personalPhoto = person.getPersonalPhoto();
        if (personalPhoto != null) {
            final SantanderPhotoEntry entry = person.getSantanderPhotoEntry();
            return entry == null || entry.getWhenGenerated().isBefore(personalPhoto.getStateChange()) ? new SantanderPhotoEntry(
                    person, personalPhoto) : entry;
        }
        return null;
    }

    public byte[] getPhotoAsByteArray() {
        final Photograph photograph = getPhotograph();
        final PictureOriginal original = photograph.getOriginal();
        final BufferedImage image =
                original.getPictureFileFormat() == ContentType.JPG ? Picture.readImage(original.getPictureData().getBytes()) : read(original);
        return transform(image);
    }

    private BufferedImage read(final PictureOriginal original) {
        BufferedImage image = Picture.readImage(original.getPictureData().getBytes());
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        result.createGraphics().drawImage(image, 0, 0, Color.WHITE, null);
        return result;
    }

    private byte[] transform(final BufferedImage image) {
        final BufferedImage adjustedImage = transformZoom(image, 9, 10);
        final BufferedImage avatar = Scalr.resize(adjustedImage, Method.QUALITY, Mode.FIT_EXACT, 180, 200);
        return Picture.writeImageAsBytes(avatar, ContentType.JPG);
    }

    private BufferedImage transformZoom(final BufferedImage source, int xRatio, int yRatio) {
        int destW, destH;
        BufferedImage finale;
        if ((1.0 * source.getWidth() / source.getHeight()) > (1.0 * xRatio / yRatio)) {
            destH = source.getHeight();
            destW = (int) Math.round((destH * xRatio * 1.0) / (yRatio * 1.0));

            int padding = (int) Math.round((source.getWidth() - destW) / 2.0);
            finale = Scalr.crop(source, padding, 0, destW, destH);
        } else {
            destW = source.getWidth();
            destH = (int) Math.round((destW * yRatio * 1.0) / (xRatio * 1.0));

            int padding = (int) Math.round((source.getHeight() - destH) / 2.0);
            finale = Scalr.crop(source, 0, padding, destW, destH);
        }
        return finale;
    }

    public String getPhotoIdentifier() {
        return makeZeroPaddedNumber(42, 5) + "E" + makeZeroPaddedNumber(getSequenceNumber(), 6);
    }

    private static String makeZeroPaddedNumber(int number, int size) {
        if (String.valueOf(number).length() > size) {
            throw new NumberFormatException("Number has more digits than allocated room.");
        }
        String format = "%0" + size + "d";
        return String.format(format, number);
    }

}
