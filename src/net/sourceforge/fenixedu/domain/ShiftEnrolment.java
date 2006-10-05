package net.sourceforge.fenixedu.domain;

import java.util.Comparator;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

public class ShiftEnrolment extends ShiftEnrolment_Base {

    public static final Comparator<ShiftEnrolment> COMPARATOR_BY_DATE = new ComparatorChain();
    static {
	((ComparatorChain) COMPARATOR_BY_DATE).addComparator(new BeanComparator("createdOn"));
	((ComparatorChain) COMPARATOR_BY_DATE).addComparator(new BeanComparator("idInternal"));
    }

    public  ShiftEnrolment() {
        super();
    }

}
