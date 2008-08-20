package net.sourceforge.fenixedu.domain.support;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contents.Content;

public class SupportDemand extends SupportDemand_Base {

    public SupportDemand(Content requestContext, SupportRequestPriority requestPriority, Person person, String responseEmail,
	    String subject, String body) {
	super();
	super.init(requestContext, requestPriority, person, responseEmail, subject, body);
    }

}
