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
        removePerson();
        removeRootDomainObject();
        deleteDomainObject();
    }

}
