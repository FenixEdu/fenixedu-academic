package net.sourceforge.fenixedu.presentationTier.Action.externalSupervision.consult;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.student.ViewStudentTimeTable;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

@Mapping(path = "/viewTimetable", module = "externalSupervision", formBean = "studentTimeTableForm")
@Forwards({ @Forward(name = "chooseRegistration", path = "/student/timeTable/chooseRegistration.jsp"),
		@Forward(name = "showTimeTable", path = "/externalSupervision/consult/showTimetable.jsp") })
public class ShowStudentTimeTable extends ViewStudentTimeTable {

	public ActionForward prepareForSupervisor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws FenixActionException, FenixFilterException, FenixServiceException {

		final String personId = request.getParameter("personId");
		Person person = AbstractDomainObject.fromExternalId(personId);
		final Student student = person.getStudent();

		request.setAttribute("student", student);

		List<Registration> registrations = person.getStudent().getActiveRegistrations();
		if (registrations.size() == 0) {
			return forwardToShowTimeTableForSupervisor(person.getStudent().getLastRegistration(), mapping, request);
		} else if (registrations.size() == 1) {
			return forwardToShowTimeTableForSupervisor(registrations.get(0), mapping, request);
		} else {
			request.setAttribute("registrations", registrations);
			return mapping.findForward("chooseRegistration");
		}
	}

}
