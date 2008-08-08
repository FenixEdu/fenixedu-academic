package net.sourceforge.fenixedu.dataTransferObject.support;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.log.requests.ErrorLog;
import net.sourceforge.fenixedu.domain.support.SupportRequestPriority;
import net.sourceforge.fenixedu.domain.support.SupportRequestType;

public class SupportRequestBean implements Serializable {

    private String responseEmail;
    private SupportRequestType requestType;
    private DomainReference<Content> requestContext;
    private String subject;
    private String message;
    private SupportRequestPriority requestPriority;
    private DomainReference<ErrorLog> errorLog;

    public SupportRequestBean() {
    }

    public static SupportRequestBean GenerateExceptionBean(Person person) {
	SupportRequestBean bean = new SupportRequestBean();
	bean.setRequestType(SupportRequestType.EXCEPTION);
	bean.setRequestPriority(SupportRequestPriority.EXCEPTION);
	bean.setResponseEmail(person.getInstitutionalOrDefaultEmailAddressValue());
	return bean;
    }

    public void setRequestContext(Content content) {
	this.requestContext = (content != null) ? new DomainReference<Content>(content) : null;
    }

    public Content getRequestContext() {
	return (this.requestContext != null) ? this.requestContext.getObject() : null;
    }

    public String getResponseEmail() {
	return responseEmail;
    }

    public void setResponseEmail(String responseEmail) {
	this.responseEmail = responseEmail;
    }

    public SupportRequestType getRequestType() {
	return requestType;
    }

    public void setRequestType(SupportRequestType requestType) {
	this.requestType = requestType;
    }

    public String getSubject() {
	return subject;
    }

    public void setSubject(String subject) {
	this.subject = subject;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public SupportRequestPriority getRequestPriority() {
	return requestPriority;
    }

    public void setRequestPriority(SupportRequestPriority requestPriority) {
	this.requestPriority = requestPriority;
    }

    public ErrorLog getErrorLog() {
	return (this.errorLog != null) ? this.errorLog.getObject() : null;
    }

    public void setErrorLog(ErrorLog errorLog) {
	this.errorLog = (errorLog != null) ? new DomainReference<ErrorLog>(errorLog) : null;
    }

}
