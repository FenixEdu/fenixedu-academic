package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.person.PartyContactsManagementDispatchAction;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/partyContacts", module = "academicAdminOffice", formBeanClass = FenixActionForm.class)
@Forwards( { @Forward(name = "createPartyContact", path = "/academicAdminOffice/createPartyContact.jsp"),
	@Forward(name = "editPartyContact", path = "/academicAdminOffice/editPartyContact.jsp"),
	@Forward(name = "editPersonalData", path = "/student.do?method=prepareEditPersonalData") })
public class PartyContactsAcademicAdministrativeOfficeDA extends PartyContactsManagementDispatchAction {
    private Student getStudent(final HttpServletRequest request) {
	final Student student = rootDomainObject.readStudentByOID(Integer.valueOf(request.getParameter("studentID")));
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
}
