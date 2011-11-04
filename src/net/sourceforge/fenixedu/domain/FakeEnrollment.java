package net.sourceforge.fenixedu.domain;

public class FakeEnrollment extends FakeEnrollment_Base {

    public FakeEnrollment() {
	super();
    }

    public FakeEnrollment(String stuff) {
	super();
	setStuff(stuff);
    }

    public void delete() {
	removePerson();
	deleteDomainObject();
    }

}
