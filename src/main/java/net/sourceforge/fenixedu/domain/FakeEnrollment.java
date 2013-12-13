package net.sourceforge.fenixedu.domain;

import org.joda.time.DateTime;

import pt.ist.bennu.core.domain.Bennu;

public class FakeEnrollment extends FakeEnrollment_Base {

    public FakeEnrollment() {
        super();
        setRootDomainObject(Bennu.getInstance());
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
    public boolean hasBennu() {
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
