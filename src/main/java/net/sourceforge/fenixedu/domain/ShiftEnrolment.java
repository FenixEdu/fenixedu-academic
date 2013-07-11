package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

import pt.ist.fenixframework.FenixFramework;

public class ShiftEnrolment extends ShiftEnrolment_Base {

    static public final Comparator<ShiftEnrolment> COMPARATOR_BY_DATE = new Comparator<ShiftEnrolment>() {
        @Override
        public int compare(ShiftEnrolment o1, ShiftEnrolment o2) {
            final int c = o1.getCreatedOn().compareTo(o2.getCreatedOn());
            return c == 0 ? AbstractDomainObject.COMPARATOR_BY_ID.compare(o1, o2) : c;
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
        setShift(null);
        setRegistration(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public boolean hasRegistration(final Registration registration) {
        return getRegistration() == registration;
    }

}
