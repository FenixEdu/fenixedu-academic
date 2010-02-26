package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.TutorateBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.TutorshipSummary;
import net.sourceforge.fenixedu.domain.TutorshipSummaryRelation;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.presentationTier.Action.commons.tutorship.StudentsPerformanceGridDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/studentTutorship", module = "pedagogicalCouncil")
@Forwards( { @Forward(name = "searchStudentTutorship", path = "/pedagogicalCouncil/tutorship/showStudentPerformanceGrid.jsp"),
	@Forward(name = "showStudentPerformanceGrid", path = "/pedagogicalCouncil/tutorship/showStudentPerformanceGrid.jsp"),
	@Forward(name = "showStudentCurriculum", path = "/pedagogicalCouncil/tutorship/showStudentCurriculum.jsp"),
	@Forward(name = "chooseRegistration", path = "/pedagogicalCouncil/tutorship/chooseRegistration.jsp") })
public class StudentTutorshipDA extends StudentsPerformanceGridDispatchAction {

    @SuppressWarnings("unused")
    public ActionForward prepareStudentSearch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.setAttribute("tutorateBean", new TutorateBean());
	return mapping.findForward("searchStudentTutorship");
    }

    @SuppressWarnings("unused")
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
	    List<Teacher> tutors = new ArrayList<Teacher>();
	    for (Tutorship t : tutorships) {
		tutors.add(t.getTeacher());
	    }
	    request.setAttribute("tutors", tutors);
	    request.setAttribute("student", student.getPerson());

	    List<TutorshipSummary> pastSummaries = new ArrayList<TutorshipSummary>();
	    for (Tutorship t : student.getTutorships()) {
		for (TutorshipSummaryRelation tsr : t.getTutorshipSummaryRelations()) {
		    if (!tsr.getTutorshipSummary().isActive()) {
			pastSummaries.add(tsr.getTutorshipSummary());
		    }
		}
	    }

	    request.setAttribute("pastSummaries", pastSummaries);

	} else {
	    studentErrorMessage(request, bean.getPersonNumber());
	}

	return mapping.findForward("showStudentPerformanceGrid");
    }

    @SuppressWarnings("unused")
    public ActionForward prepareStudentCurriculum(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.setAttribute("tutorateBean", new TutorateBean());
	return mapping.findForward("showStudentCurriculum");
    }

    @SuppressWarnings("unused")
    public ActionForward showStudentRegistration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final TutorateBean tutorateBean = new TutorateBean();
	tutorateBean.setPersonNumber(getIntegerFromRequest(request, "studentNumber"));
	request.setAttribute("tutorateBean", tutorateBean);
	return showOrChoose(mapping, request);
    }

    @SuppressWarnings("unused")
    public ActionForward showStudentCurriculum(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	return showOrChoose(mapping, request);
    }

    private ActionForward showOrChoose(final ActionMapping mapping, final HttpServletRequest request) {
	Registration registration = null;

	final Integer registrationOID = getIntegerFromRequest(request, "registrationOID");
	TutorateBean bean = (TutorateBean) getObjectFromViewState("tutorateBean");
	if (bean == null) {
	    bean = (TutorateBean) request.getAttribute("tutorateBean");
	}

	if (registrationOID != null) {
	    registration = rootDomainObject.readRegistrationByOID(registrationOID);
	} else {
	    final Student student = Student.readStudentByNumber(bean.getPersonNumber());
	    if (student.getRegistrations().size() == 1) {
		registration = student.getRegistrations().get(0);
	    } else {
		request.setAttribute("student", student);
		return mapping.findForward("chooseRegistration");
	    }
	}

	if (registration == null) {
	    studentErrorMessage(request, bean.getPersonNumber());
	} else {
	    request.setAttribute("registration", registration);
	}

	return mapping.findForward("showStudentCurriculum");
    }

    private void studentErrorMessage(HttpServletRequest request, Integer studentNumber) {
	addActionMessage("error", request, "student.does.not.exist", String.valueOf(studentNumber));
    }

}
