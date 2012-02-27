package net.sourceforge.fenixedu.domain.phd;

import jvstm.cps.ConsistencyPredicate;

public class ThesisSubjectOrder extends ThesisSubjectOrder_Base {

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
