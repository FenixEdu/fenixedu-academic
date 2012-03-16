package net.sourceforge.fenixedu.domain.phd;

import java.util.Comparator;

import jvstm.cps.ConsistencyPredicate;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class ThesisSubjectOrder extends ThesisSubjectOrder_Base {

    public static Comparator<ThesisSubjectOrder> COMPARATOR_BY_ORDER = new Comparator<ThesisSubjectOrder>() {
	@Override
	public int compare(ThesisSubjectOrder order1, ThesisSubjectOrder order2) {
	    return order1.getSubjectOrder() - order2.getSubjectOrder();
	}
    };

    public ThesisSubjectOrder() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public ThesisSubjectOrder(ThesisSubject subject, PhdIndividualProgramProcess phdProcess, int order) {
	this();
	setThesisSubject(subject);
	setPhdIndividualProgramProcess(phdProcess);
	setSubjectOrder(order);
    }

    public void delete() {
	for (ThesisSubjectOrder followingSubjectOrder : getPhdIndividualProgramProcess().getThesisSubjectOrdersSorted()) {
	    if (followingSubjectOrder.getSubjectOrder() > getSubjectOrder()) {
		followingSubjectOrder.decreaseSubjectOrder();
	    }
	}
	
	removeThesisSubject();
	removePhdIndividualProgramProcess();

	removeRootDomainObject();
	deleteDomainObject();
    }

    public void decreaseSubjectOrder() {
	if (getSubjectOrder() > 1) {
	    setSubjectOrder(getSubjectOrder() - 1);
	}
    }

    @ConsistencyPredicate
    public boolean checkHasThesisSubject() {
	return hasThesisSubject();
    }

    @ConsistencyPredicate
    public boolean checkHasPhdIndividualProgramProcess() {
	return hasPhdIndividualProgramProcess();
    }

}
