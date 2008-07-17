package net.sourceforge.fenixedu.dataTransferObject.support;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.contents.Content;
import net.sourceforge.fenixedu.domain.support.SupportRequestPriority;
import net.sourceforge.fenixedu.domain.support.SupportRequestType;

public class SupportRequestBean implements Serializable {

    private String responseEmail;
    private SupportRequestType requestType;
    private DomainReference<Content> requestContext;
    private String subject;
    private String message;
    private SupportRequestPriority requestPriority;

    public SupportRequestBean() {
    }

    public SupportRequestBean(SupportRequestType requestType, SupportRequestPriority requestPriority) {
	setRequestType(requestType);
	setRequestPriority(requestPriority);
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

}
