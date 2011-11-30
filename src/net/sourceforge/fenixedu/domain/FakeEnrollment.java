package net.sourceforge.fenixedu.domain;

public class FakeEnrollment extends FakeEnrollment_Base {

    public FakeEnrollment() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public FakeEnrollment(String stuff) {
	this();
	setStuff(stuff);
    }

    public void delete() {
	removePerson();
	removeRootDomainObject();
	deleteDomainObject();
    }

}
