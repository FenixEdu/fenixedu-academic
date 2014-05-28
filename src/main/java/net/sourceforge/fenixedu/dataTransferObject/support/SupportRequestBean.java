/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
