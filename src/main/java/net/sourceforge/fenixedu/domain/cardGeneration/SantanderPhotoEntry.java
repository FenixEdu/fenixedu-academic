package net.sourceforge.fenixedu.domain.cardGeneration;

import java.awt.Color;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.photograph.AspectRatio;
import net.sourceforge.fenixedu.domain.photograph.PictureAvatar;
import net.sourceforge.fenixedu.domain.photograph.PictureMode;
import net.sourceforge.fenixedu.domain.photograph.PictureSize;

import org.joda.time.DateTime;

public class SantanderPhotoEntry extends SantanderPhotoEntry_Base {

    public SantanderPhotoEntry(final Person person, final Photograph photograph) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setWhenGenerated(new DateTime());
        if (person.hasSantanderPhotoEntry()) {
            setNext(person.getSantanderPhotoEntry());
        }
        setPerson(person);
        setPhotograph(photograph);
        setSequenceNumber(SantanderSequenceNumberGenerator.getNewPhotoSequenceNumber());
    }

    public static SantanderPhotoEntry getOrCreatePhotoEntryForPerson(final Person person) {
        final Photograph personalPhoto = person.getPersonalPhoto();
        if (personalPhoto != null) {
            final PictureAvatar avatar = getAvatar(personalPhoto);
            if (avatar != null) {
                final SantanderPhotoEntry entry = person.getSantanderPhotoEntry();
                return entry == null || entry.getWhenGenerated().isBefore(personalPhoto.getStateChange()) ? new SantanderPhotoEntry(
                        person, personalPhoto) : entry;
            }
        }
        return null;
    }

    private static PictureAvatar getAvatar(Photograph personalPhoto) {
        try {
            return personalPhoto.getAvatar(AspectRatio.ª9_by_10, PictureSize.LARGE, PictureMode.FIT);
        } catch (final DomainException ex) {
            // Ignore non existing photo, but don't generate santander photo sheet.
            return null;
        }
    }

    public byte[] getPhotoAsByteArray() {
        final Photograph photograph = getPhotograph();
        final PictureAvatar avatar = photograph.getAvatar(AspectRatio.ª9_by_10, PictureSize.LARGE, PictureMode.FIT);
        return avatar.exportAsJPEG(Color.BLACK);
    }

}
