package net.sourceforge.fenixedu.domain.phd;

import jvstm.cps.ConsistencyPredicate;

public class ThesisSubjectOrder extends ThesisSubjectOrder_Base {

    public ThesisSubjectOrder() {
	super();
    }

    @ConsistencyPredicate
    public boolean checkHasThesisSubject() {
	return hasThesisSubject();
    }

}
