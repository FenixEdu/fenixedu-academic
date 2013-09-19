package net.sourceforge.fenixedu.domain.cardGeneration;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

import org.joda.time.DateTime;

public class SantanderEntry extends SantanderEntry_Base {

    static final public Comparator<SantanderEntry> COMPARATOR_BY_MOST_RECENTLY_CREATED = new Comparator<SantanderEntry>() {

        @Override
        public int compare(SantanderEntry o1, SantanderEntry o2) {
            return o1.getCreated().isAfter(o2.getCreated()) ? 1 : 0;
        }

    };

    public SantanderEntry() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setCreated(new DateTime());
    }

    public SantanderEntry(SantanderBatch batch, Person person, String line) {
        this();
        setSantanderBatch(batch);
        setPerson(person);
        setSantanderPhotoEntry(person.getSantanderPhotoEntry());
        setLine(line);
    }

    public void delete() {
        setPerson(null);
        setSantanderBatch(null);
        setSantanderPhotoEntry(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCreated() {
        return getCreated() != null;
    }

    @Deprecated
    public boolean hasSantanderBatch() {
        return getSantanderBatch() != null;
    }

    @Deprecated
    public boolean hasLine() {
        return getLine() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
