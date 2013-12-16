package net.sourceforge.fenixedu.domain.cardGeneration;

import pt.ist.bennu.core.domain.Bennu;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Person;

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
        setRootDomainObject(Bennu.getInstance());
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

    public static SantanderEntry readByUsernameAndCategory(String username, String category) {
        for (SantanderEntry entry : Bennu.getInstance().getSantanderEntriesSet()) {
            if (entry
                    .getLine()
                    .subSequence(1 + 10 + 15 + 15 + 40 + 50 + 50 + 8 + 30 + 10 + 10 + 9 + 16 + 10,
                            1 + 10 + 15 + 15 + 40 + 50 + 50 + 8 + 30 + 10 + 10 + 9 + 16 + 10 + 10).equals(username)
                    && entry.getLine()
                            .subSequence(
                                    1 + 10 + 15 + 15 + 40 + 50 + 50 + 8 + 30 + 10 + 10 + 9 + 16 + 10 + 10 + 1 + 2 + 8 + 11 + 1
                                            + 4 + 4 + 10 + 5,
                                    1 + 10 + 15 + 15 + 40 + 50 + 50 + 8 + 30 + 10 + 10 + 9 + 16 + 10 + 10 + 1 + 2 + 8 + 11 + 1
                                            + 4 + 4 + 10 + 5 + 1).equals(category)) {
                return entry;
            }
        }
        return null;
    }

    @Deprecated
    public boolean hasBennu() {
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
