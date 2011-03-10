package net.sourceforge.fenixedu.domain.inquiries;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;

public class InquiryResultComment extends InquiryResultComment_Base {

    public InquiryResultComment(InquiryResult questionResult, Person person, ResultPersonCategory delegate, Integer resultOrder) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setInquiryResult(questionResult);
	setPerson(person);
	setPersonCategory(delegate);
	setResultOrder(resultOrder);
    }

}
