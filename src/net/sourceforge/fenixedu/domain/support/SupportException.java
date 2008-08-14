package net.sourceforge.fenixedu.domain.support;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.log.requests.ErrorLog;

public class SupportException extends SupportException_Base {

    public SupportException(Content requestContext, SupportRequestPriority requestPriority, Person person, String responseEmail,
	    String subject, String body, ErrorLog errorLog) {
	super();
	checkParameters(errorLog);
	setErrorLog(errorLog);
	super.init(requestContext, requestPriority, person, responseEmail, subject, body);
    }

    private void checkParameters(ErrorLog errorLog) {
	// FIXME: in jsp exceptions, there is no errorLog creation
	// if (errorLog == null) {
	// throw new
	// DomainException("error.domain.support.SupportError.errorLog.null");
	// }
    }

}
