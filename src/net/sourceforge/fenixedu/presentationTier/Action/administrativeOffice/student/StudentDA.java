package net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person.PersonBeanFactoryEditor;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.formbeans.FenixActionForm;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/student", module = "academicAdminOffice", formBeanClass = FenixActionForm.class)
@Forwards( { @Forward(name = "viewStudentDetails", path = "/academicAdminOffice/student/viewStudentDetails.jsp"),
	@Forward(name = "editPersonalData", path = "/academicAdminOffice/editPersonalData.jsp"),
	@Forward(name = "viewPersonalData", path = "/academicAdminOffice/viewPersonalData.jsp") })
public class StudentDA extends StudentRegistrationDA {

    private Student getAndSetStudent(final HttpServletRequest request) {
	final String studentID = request.getParameter("studentID");
	final Student student = rootDomainObject.readStudentByOID(Integer.valueOf(studentID));
	request.setAttribute("student", student);
	return student;
    }

    public ActionForward prepareEditPersonalData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	final Student student = getAndSetStudent(request);

	final Employee employee = student.getPerson().getEmployee();
	if (employee != null && employee.getCurrentWorkingContract() != null) {
	    addActionMessage(request, "message.student.personIsEmployee");
	    return mapping.findForward("viewStudentDetails");
	}

	request.setAttribute("personBean", new PersonBeanFactoryEditor(student.getPerson()));
	return mapping.findForward("editPersonalData");
    }

    public ActionForward editPersonalData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {
	getAndSetStudent(request);
	try {
	    executeFactoryMethod();
	    RenderUtils.invalidateViewState();
	} catch (DomainException ex) {
	    addActionMessage(request, ex.getKey(), ex.getArgs());

	    request.setAttribute("personBean", getRenderedObject());
	    return mapping.findForward("editPersonalData");
	}

	addActionMessage(request, "message.student.personDataEditedWithSuccess");

	return mapping.findForward("viewStudentDetails");
    }

    public ActionForward viewPersonalData(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	request.setAttribute("personBean", new PersonBeanFactoryEditor(getAndSetStudent(request).getPerson()));
	return mapping.findForward("viewPersonalData");
    }

    public ActionForward visualizeStudent(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {
	getAndSetStudent(request);
	return mapping.findForward("viewStudentDetails");
    }
}