package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationSelectExecutionYearBean;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person.PersonBeanFactoryEditor;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.MobilePhone;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.Phone;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.contacts.WebAddress;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class StudentDA extends FenixDispatchAction {

    private Student getStudent(final HttpServletRequest request) {
	final String studentID = request.getParameter("studentID");
	final Student student = rootDomainObject.readStudentByOID(Integer.valueOf(studentID));
	request.setAttribute("student", student);
	return student;
    }

    private Registration getRegistration(final HttpServletRequest request) {
	String registrationID = request.getParameter("registrationID");
	if (registrationID == null) {
	    registrationID = (String) request.getAttribute("registrationId");
	}
	final Registration registration = rootDomainObject.readRegistrationByOID(Integer
		.valueOf(registrationID));
	request.setAttribute("registration", registration);
	return registration;
    }

    public ActionForward prepareEditPersonalData(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	final Student student = getStudent(request);

	final Employee employee = student.getPerson().getEmployee();
	if (employee != null && employee.getCurrentWorkingContract() != null) {
	    addActionMessage(request, "message.student.personIsEmployee");
	    return mapping.findForward("viewStudentDetails");
	}

	request.setAttribute("personBean", new PersonBeanFactoryEditor(student.getPerson()));
	return mapping.findForward("editPersonalData");
    }

    public ActionForward editPersonalData(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {
	getStudent(request);
	executeFactoryMethod(request);
	RenderUtils.invalidateViewState();
	addActionMessage(request, "message.student.personDataEditedWithSuccess");
	return mapping.findForward("viewStudentDetails");
    }
    
    public ActionForward viewPersonalData(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {

	request.setAttribute("personBean", new PersonBeanFactoryEditor(getStudent(request).getPerson()));
	return mapping.findForward("viewPersonalData");
    }

    public ActionForward visualizeRegistration(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	getRegistration(request);
	return mapping.findForward("viewRegistrationDetails");
    }

    public ActionForward visualizeStudent(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	getStudent(request);
	return mapping.findForward("viewStudentDetails");
    }

    public ActionForward viewRegistrationCurriculum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	RegistrationSelectExecutionYearBean bean = (RegistrationSelectExecutionYearBean) getRenderedObject();
	if (bean == null) {
	    bean = new RegistrationSelectExecutionYearBean(getRegistration(request));
	}
	
	request.setAttribute("bean", bean);
	return mapping.findForward("view-registration-curriculum");
    }

    public ActionForward prepareCreatePhysicalAddress(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return prepareCreatePartyContact(mapping, actionForm, request, response, PhysicalAddress.class);
    }
    
    public ActionForward prepareCreatePhone(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return prepareCreatePartyContact(mapping, actionForm, request, response, Phone.class);
    }
    
    public ActionForward prepareCreateMobilePhone(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return prepareCreatePartyContact(mapping, actionForm, request, response, MobilePhone.class);
    }
    
    public ActionForward prepareCreateEmailAddress(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return prepareCreatePartyContact(mapping, actionForm, request, response, EmailAddress.class);
    }
    
    public ActionForward prepareCreateWebAddress(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return prepareCreatePartyContact(mapping, actionForm, request, response, WebAddress.class);
    }
    
    private ActionForward prepareCreatePartyContact(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response, Class<? extends PartyContact> clazz) {
	
	request.setAttribute("student", getStudent(request));
	request.setAttribute("partyContactName", clazz.getSimpleName());
	
	return mapping.findForward("createPartyContact");
    }
    
    public ActionForward createPartyContact(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return prepareEditPersonalData(mapping, actionForm, request, response);
    }
    
    public ActionForward prepareEditPartyContact(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	
	final Student student = getStudent(request);
	request.setAttribute("student", student);
	request.setAttribute("partyContact", getPartyContact(student, request));
	return mapping.findForward("editPartyContact");
    }

    private PartyContact getPartyContact(final Student student, final HttpServletRequest request) {
	final Integer contactId = getIntegerFromRequest(request, "contactId");
	for (final PartyContact contact : student.getPerson().getPartyContacts()) {
	    if (contact.getIdInternal().equals(contactId)) {
		return contact;
	    }
	}
	return null;
    }
    
    public ActionForward editPartyContact(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) {
	return prepareEditPersonalData(mapping, actionForm, request, response);
    }
    
    public ActionForward changeDefaultPartyContact(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	executeService("ChangeDefaultPartyContact", new Object[] {getPartyContact(getStudent(request), request)});
	return prepareEditPersonalData(mapping, actionForm, request, response);
    }

    
    public ActionForward deletePartyContact(ActionMapping mapping, ActionForm actionForm,
	    HttpServletRequest request, HttpServletResponse response) throws FenixFilterException,
	    FenixServiceException {

	try {
	    executeService("DeletePartyContact", new Object[] {getPartyContact(getStudent(request), request)});
	} catch (DomainException e) {
	    addActionMessage(request, e.getMessage(), e.getArgs());
	}
	return prepareEditPersonalData(mapping, actionForm, request, response);
    }
    
}
