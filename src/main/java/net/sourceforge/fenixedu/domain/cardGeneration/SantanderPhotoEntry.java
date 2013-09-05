package net.sourceforge.fenixedu.domain.cardGeneration;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Photograph;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

public class SantanderPhotoEntry extends SantanderPhotoEntry_Base {
    
    public SantanderPhotoEntry(final Person person) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setWhenGenerated(new DateTime());
        if (person.hasSantanderPhotoEntry()) {
            setNext(person.getSantanderPhotoEntry());
        }
        setPerson(person);
        setSequenceNumber(SantanderSequenceNumberGenerator.getNewPhotoSequenceNumber());
    }

    public static SantanderPhotoEntry getOrCreatePhotoEntryForPerson(final Person person) {
        final Photograph personalPhoto = person.getPersonalPhoto();
        if (personalPhoto != null) {
            final SantanderPhotoEntry entry = person.getSantanderPhotoEntry();
            return entry == null || entry.getWhenGenerated().isBefore(personalPhoto.getStateChange())
                    ? new SantanderPhotoEntry(person) : entry;
        }
        return null;
    }

}
