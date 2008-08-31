package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

public class ShiftEnrolment extends ShiftEnrolment_Base {

    public static final Comparator<ShiftEnrolment> COMPARATOR_BY_DATE = new Comparator<ShiftEnrolment>() {

	@Override
	public int compare(ShiftEnrolment o1, ShiftEnrolment o2) {
	    final int c = o1.getCreatedOn().compareTo(o2.getCreatedOn());
	    return c == 0 ? DomainObject.COMPARATOR_BY_ID.compare(o1, o2) : c;
	}

    };

    public ShiftEnrolment(final Shift shift, final Registration registration) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setRegistration(registration);
	setShift(shift);
	setCreatedOn(new DateTime());
    }

    public void delete() {
	removeShift();
	removeRegistration();
	removeRootDomainObject();
	deleteDomainObject();
    }

}
