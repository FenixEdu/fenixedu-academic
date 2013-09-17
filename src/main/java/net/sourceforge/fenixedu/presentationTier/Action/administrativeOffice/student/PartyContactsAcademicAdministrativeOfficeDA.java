package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.contacts.CreatePartyContact;
import net.sourceforge.fenixedu.applicationTier.Servico.contacts.EditPartyContact;
import net.sourceforge.fenixedu.dataTransferObject.contacts.PartyContactBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonInformationLog;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PartyContactValidationState;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.contacts.WebAddress;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.person.PartyContactsManagementDispatchAction;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

@Mapping(path = "/partyContacts", module = "academicAdministration", formBeanClass = FenixActionForm.class)
@Forwards({ @Forward(name = "createPartyContact", path = "/academicAdminOffice/createPartyContact.jsp"),
        @Forward(name = "editPartyContact", path = "/academicAdminOffice/editPartyContact.jsp"),
        @Forward(name = "inputValidationCode", path = "/academicAdminOffice/inputValidationCode.jsp"),
        @Forward(name = "editPersonalData", path = "/student.do?method=prepareEditPersonalData") })
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
            if (partyContact.hasPartyContactValidation()) {
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
        Collection<PersonInformationLog> logsList = person.getPersonInformationLogs();

        request.setAttribute("logsList", logsList);
        return mapping.findForward("viewStudentLogChanges");
    }

}
