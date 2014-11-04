/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.ui.struts.action.administrativeOffice.student;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.PersonInformationLog;
import org.fenixedu.academic.domain.contacts.PartyContact;
import org.fenixedu.academic.domain.contacts.PartyContactValidationState;
import org.fenixedu.academic.domain.contacts.PhysicalAddress;
import org.fenixedu.academic.domain.contacts.WebAddress;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.dto.contacts.PartyContactBean;
import org.fenixedu.academic.service.services.contacts.CreatePartyContact;
import org.fenixedu.academic.service.services.contacts.EditPartyContact;
import org.fenixedu.academic.ui.struts.FenixActionForm;
import org.fenixedu.academic.ui.struts.action.person.PartyContactsManagementDispatchAction;
import org.fenixedu.bennu.struts.annotations.Forward;
import org.fenixedu.bennu.struts.annotations.Forwards;
import org.fenixedu.bennu.struts.annotations.Mapping;

import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/partyContacts", module = "academicAdministration", formBeanClass = FenixActionForm.class,
        functionality = SearchForStudentsDA.class)
@Forwards({ @Forward(name = "createPartyContact", path = "/academicAdminOffice/createPartyContact.jsp"),
        @Forward(name = "editPartyContact", path = "/academicAdminOffice/editPartyContact.jsp"),
        @Forward(name = "inputValidationCode", path = "/academicAdminOffice/inputValidationCode.jsp"),
        @Forward(name = "editPersonalData", path = "/academicAdministration/student.do?method=prepareEditPersonalData") })
public class PartyContactsAcademicAdministrativeOfficeDA extends PartyContactsManagementDispatchAction {

    private Student getStudent(final HttpServletRequest request) {
        final String studentID = request.getParameter("studentID");
        final Student student = FenixFramework.getDomainObject(studentID);
        request.setAttribute("student", student);
        return student;
    }

    @Override
    protected Person getParty(final HttpServletRequest request) {
        return getStudent(request).getPerson();
    }

    @Override
    public ActionForward postbackSetPublic(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("student", getStudent(request));
        return super.postbackSetPublic(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward postbackSetElements(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("student", getStudent(request));
        return super.postbackSetElements(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward invalid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("student", getStudent(request));
        return super.invalid(mapping, actionForm, request, response);
    }

    @Override
    public ActionForward backToShowInformation(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {
        return mapping.findForward("editPersonalData");
    }

    @Override
    public boolean editContact(PartyContactBean contact) {
        return EditPartyContact.run(contact, false);
    }

    @Override
    public PartyContact createContact(PartyContactBean contact) {
        return CreatePartyContact.run(contact, false);
    }

    @Override
    public ActionForward forwardToInputValidationCode(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response, PartyContact partyContact) {
        if (partyContact instanceof PhysicalAddress || partyContact instanceof WebAddress) {
            if (partyContact.getPartyContactValidation() != null) {
                partyContact.getPartyContactValidation().setState(PartyContactValidationState.VALID);
            }
            return backToShowInformation(mapping, actionForm, request, response);
        }
        getStudent(request);
        return mapping.findForward("inputValidationCode");
    }

    @Override
    protected void addWarningMessage(HttpServletRequest request, PartyContact partyContact) {
    }

    @Override
    protected void addWarningMessage(HttpServletRequest request, PartyContactBean contactBean) {
    }

    @Override
    public ActionForward viewStudentLog(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
            HttpServletResponse response) {

        Person person = getStudent(request).getPerson();
        Collection<PersonInformationLog> logsList = person.getPersonInformationLogsSet();

        request.setAttribute("logsList", logsList);
        return mapping.findForward("viewStudentLogChanges");
    }

}
