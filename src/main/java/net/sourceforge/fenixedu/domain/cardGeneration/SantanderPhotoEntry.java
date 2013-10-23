package net.sourceforge.fenixedu.domain.cardGeneration;

import java.awt.Color;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.photograph.AspectRatio;
import net.sourceforge.fenixedu.domain.photograph.PictureMode;
import net.sourceforge.fenixedu.domain.photograph.PictureSize;

import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class SantanderPhotoEntry extends SantanderPhotoEntry_Base {

    public SantanderPhotoEntry(final Person person, final Photograph photograph) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
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
        return photograph.exportAsJPEG(photograph.getCustomAvatar(AspectRatio.ª9_by_10, PictureSize.LARGE.getWidth(),
                PictureSize.LARGE.getHeight(), PictureMode.FIT), Color.BLACK);
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
