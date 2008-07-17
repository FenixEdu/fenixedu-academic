package net.sourceforge.fenixedu.domain.support;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public abstract class SupportRequest extends SupportRequest_Base {
    
    protected  SupportRequest() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        super.setRequestTime(new DateTime());
    }

    protected SupportRequest(SupportRequestPriority requestPriority) {
	this();
	checkParameters(requestPriority);
	setRequestPriority(requestPriority);
    }

    protected SupportRequest(Content requestContext, SupportRequestPriority requestPriority, Person person, String responseEmail, String subject, String body) {
	this(requestPriority);
	init(requestContext, requestPriority, person, responseEmail, subject, body);
    }
    
    protected void init(Content requestContext, SupportRequestPriority requestPriority, Person person, String responseEmail, String subject, String body) {
	checkParameters(requestPriority);
	setRequestContext(requestContext);
	setRequestPriority(requestPriority);
	setRequester(person);
	setResponseEmail(responseEmail);
	setSubject(subject);
	setMessage(body);
    }

    protected void checkParameters(SupportRequestPriority requestPriority) {
	
	if (requestPriority == null) {
	    throw new DomainException("error.domain.support.SupportRequest.priority.null");
	}
    }
    
    @Override
    public void setRequestTime(DateTime requestTime) {
	throw new DomainException("error.domain.support.SupportRequest.requestTime.cannot.be.changed");
    }

}
