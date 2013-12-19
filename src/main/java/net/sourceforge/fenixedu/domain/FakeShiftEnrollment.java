package net.sourceforge.fenixedu.domain;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class FakeShiftEnrollment extends FakeShiftEnrollment_Base {

    public FakeShiftEnrollment() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public FakeShiftEnrollment(FakeShift fakeShift, Person person, String stuff) {
        this();
        setFakeShift(fakeShift);
        setPerson(person);
        setStuff(stuff);
        setCreationDate(new DateTime());
    }

    public void delete() {
        setPerson(null);
        setFakeShift(null);

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
    public boolean hasFakeShift() {
        return getFakeShift() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

}
