package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.student.Registration;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.DateTime;

public class ShiftEnrolment extends ShiftEnrolment_Base {

    public static final Comparator<ShiftEnrolment> COMPARATOR_BY_DATE = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_DATE).addComparator(new BeanComparator("createdOn"));
	((ComparatorChain) COMPARATOR_BY_DATE).addComparator(DomainObject.COMPARATOR_BY_ID);
    }

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
