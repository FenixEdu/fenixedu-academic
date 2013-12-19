package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.student.Registration;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class ShiftEnrolment extends ShiftEnrolment_Base {

    static public final Comparator<ShiftEnrolment> COMPARATOR_BY_DATE = new Comparator<ShiftEnrolment>() {
        @Override
        public int compare(ShiftEnrolment o1, ShiftEnrolment o2) {
            final int c = o1.getCreatedOn().compareTo(o2.getCreatedOn());
            return c == 0 ? DomainObjectUtil.COMPARATOR_BY_ID.compare(o1, o2) : c;
        }
    };

    public ShiftEnrolment(final Shift shift, final Registration registration) {
        super();
        setRootDomainObject(Bennu.getInstance());
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

    @Deprecated
    public boolean hasRegistration() {
        return getRegistration() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasCreatedOn() {
        return getCreatedOn() != null;
    }

    @Deprecated
    public boolean hasShift() {
        return getShift() != null;
    }

}
