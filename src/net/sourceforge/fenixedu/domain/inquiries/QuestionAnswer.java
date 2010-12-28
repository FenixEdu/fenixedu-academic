package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class QuestionAnswer extends QuestionAnswer_Base {

    public QuestionAnswer() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

}
