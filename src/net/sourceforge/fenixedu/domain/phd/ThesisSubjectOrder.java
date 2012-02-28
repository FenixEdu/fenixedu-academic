package net.sourceforge.fenixedu.domain.phd;

import java.util.Comparator;

import jvstm.cps.ConsistencyPredicate;

public class ThesisSubjectOrder extends ThesisSubjectOrder_Base {

    public static Comparator<ThesisSubjectOrder> COMPARATOR_BY_ORDER = new Comparator<ThesisSubjectOrder>() {
	@Override
	public int compare(ThesisSubjectOrder order1, ThesisSubjectOrder order2) {
	    return order1.getSubjectOrder() - order2.getSubjectOrder();
	}
    };

    public ThesisSubjectOrder() {
	super();
    }

    public ThesisSubjectOrder(ThesisSubject subject, PhdIndividualProgramProcess phdProcess, int order) {
	this();
	setThesisSubject(subject);
	setPhdIndividualProgramProcess(phdProcess);
	setSubjectOrder(order);
    }

    @ConsistencyPredicate
    public boolean checkHasThesisSubject() {
	return hasThesisSubject();
    }

}
