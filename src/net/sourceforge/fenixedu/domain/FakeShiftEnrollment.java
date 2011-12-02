package net.sourceforge.fenixedu.domain;

public class FakeShiftEnrollment extends FakeShiftEnrollment_Base {

    public FakeShiftEnrollment() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public FakeShiftEnrollment(FakeShift fakeShift, Person person) {
	this();
	setFakeShift(fakeShift);
	setPerson(person);
    }

    public void delete() {
	removePerson();
	removeFakeShift();

	removeRootDomainObject();
	deleteDomainObject();
    }
}
