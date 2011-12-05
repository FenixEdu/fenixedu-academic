package net.sourceforge.fenixedu.domain;

import org.joda.time.DateTime;

public class FakeShiftEnrollment extends FakeShiftEnrollment_Base {

    public FakeShiftEnrollment() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public FakeShiftEnrollment(FakeShift fakeShift, Person person, String stuff) {
	this();
	setFakeShift(fakeShift);
	setPerson(person);
	setStuff(stuff);
	setCreationDate(new DateTime());
    }

    public void delete() {
	removePerson();
	removeFakeShift();

	removeRootDomainObject();
	deleteDomainObject();
    }
}
