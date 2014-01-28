package net.sourceforge.fenixedu.dataTransferObject.support;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.support.SupportRequestPriority;
import net.sourceforge.fenixedu.domain.support.SupportRequestType;

import org.fenixedu.bennu.portal.domain.MenuFunctionality;

public class SupportRequestBean implements Serializable {

    private String responseEmail;
    private SupportRequestType requestType;
    private MenuFunctionality selectedFunctionality;
    private String subject;
    private String message;
    private SupportRequestPriority requestPriority;

    private SupportRequestBean() {
    }

    public static SupportRequestBean generateExceptionBean(final Person person) {
        final SupportRequestBean bean = new SupportRequestBean();
        bean.setRequestType(SupportRequestType.EXCEPTION);
        bean.setRequestPriority(SupportRequestPriority.EXCEPTION);
        bean.setResponseEmail(person != null ? person.getInstitutionalOrDefaultEmailAddressValue() : "");
        return bean;
    }

    public MenuFunctionality getSelectedFunctionality() {
        return selectedFunctionality;
    }

    public void setSelectedFunctionality(MenuFunctionality selectedFunctionality) {
        this.selectedFunctionality = selectedFunctionality;
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
