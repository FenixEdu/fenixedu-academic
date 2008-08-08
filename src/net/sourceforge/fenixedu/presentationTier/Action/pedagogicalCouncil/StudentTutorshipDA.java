package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.TutorateBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.commons.StudentsPerformanceGridDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/studentTutorship", module = "pedagogicalCouncil")
@Forwards( { @Forward(name = "searchStudentTutorship", path = "/pedagogicalCouncil/tutorship/showStudentPerformanceGrid.jsp"),
	@Forward(name = "showStudentPerformanceGrid", path = "/pedagogicalCouncil/tutorship/showStudentPerformanceGrid.jsp"),
	@Forward(name = "showStudentCurriculum", path = "/pedagogicalCouncil/tutorship/showStudentCurriculum.jsp") })
public class StudentTutorshipDA extends StudentsPerformanceGridDispatchAction {

    public ActionForward prepareStudentSearch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.setAttribute("tutorateBean", new TutorateBean());
	return mapping.findForward("searchStudentTutorship");
    }

    public ActionForward showStudentPerformanceGrid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	TutorateBean bean = (TutorateBean) getRenderedObject("tutorateBean");

	Student student = Student.readStudentByNumber(bean.getPersonNumber());
	if (student != null) {
	    List<Tutorship> tutorships = student.getActiveTutorships();
	    if (tutorships.size() > 0) {

		PerformanceGridTableDTO performanceGridTable = createPerformanceGridTable(request, tutorships, student
			.getFirstRegistrationExecutionYear(), ExecutionYear.readCurrentExecutionYear());

		getStatisticsAndPutInTheRequest(request, performanceGridTable);

		Integer years = 0;
		for (Tutorship t : tutorships) {
		    if (t.getStudentCurricularPlan().getDegreeDuration().compareTo(years) > 0) {
			years = t.getStudentCurricularPlan().getDegreeDuration();
		    }
		}
		request.setAttribute("degreeCurricularPeriods", years);
		request.setAttribute("performanceGridTable", performanceGridTable);
		request.setAttribute("monitoringYear", ExecutionYear.readCurrentExecutionYear());
	    }

	    request.setAttribute("student", student.getPerson());
	} else {
	    studentErrorMessage(request, bean.getPersonNumber());
	}

	return mapping.findForward("showStudentPerformanceGrid");
    }

    public ActionForward prepareStudentCurriculum(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.setAttribute("tutorateBean", new TutorateBean());
	return mapping.findForward("showStudentCurriculum");
    }

    public ActionForward showStudentRegistration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	TutorateBean tutorateBean = new TutorateBean();
	tutorateBean.setPersonNumber(getIntegerFromRequest(request, "studentNumber"));
	request.setAttribute("tutorateBean", tutorateBean);
	return studentCurriculum(mapping, actionForm, request, response, getIntegerFromRequest(request, "studentNumber"));
    }

    public ActionForward showStudentCurriculum(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	TutorateBean bean = (TutorateBean) getObjectFromViewState("tutorateBean");
	return studentCurriculum(mapping, actionForm, request, response, bean.getPersonNumber());
    }

    private ActionForward studentCurriculum(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response, Integer studentNumber) throws Exception {

	Student student = Student.readStudentByNumber(studentNumber);
	if (student != null) {
	    final List<Registration> registrations = student.getRegistrations();
	    if (!registrations.isEmpty()) {
		request.setAttribute("registration", registrations.iterator().next());
	    }
	} else {
	    studentErrorMessage(request, studentNumber);
	}

	return mapping.findForward("showStudentCurriculum");
    }

    private void studentErrorMessage(HttpServletRequest request, Integer studentNumber) {
	addActionMessage("error", request, "student.does.not.exist", String.valueOf(studentNumber));
    }

}
