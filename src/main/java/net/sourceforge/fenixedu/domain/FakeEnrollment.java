package net.sourceforge.fenixedu.domain;

import org.joda.time.DateTime;

public class FakeEnrollment extends FakeEnrollment_Base {

    public FakeEnrollment() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    public FakeEnrollment(Person person, String stuff) {
        this();
        setPerson(person);
        setStuff(stuff);
        setCreationDate(new DateTime());
    }

    public void delete() {
        setPerson(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public boolean hasRootDomainObject() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCreationDate() {
        return getCreationDate() != null;
    }

    @Deprecated
    public boolean hasStuff() {
        return getStuff() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
