package net.sourceforge.fenixedu.presentationTier.Action.pedagogicalCouncil;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.pedagogicalCouncil.NumberBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsPerformanceInfoBean;
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
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/studentTutorship", module = "pedagogicalCouncil")
@Forwards({
	@Forward(name = "searchStudentTutorship", path = "/pedagogicalCouncil/tutorship/showStudentPerformanceGrid.jsp", tileProperties = @Tile(title = "private.pedagogiccouncil.tutoring.viewperformancegrids")),
	@Forward(name = "showStudentPerformanceGrid", path = "/pedagogicalCouncil/tutorship/showStudentPerformanceGrid.jsp"),
	@Forward(name = "showStudentCurriculum", path = "/pedagogicalCouncil/tutorship/showStudentCurriculum.jsp", tileProperties = @Tile(title = "private.pedagogiccouncil.tutoring.studentcurriculum")),
	@Forward(name = "chooseRegistration", path = "/pedagogicalCouncil/tutorship/chooseRegistration.jsp") })
public class StudentTutorshipDA extends StudentsPerformanceGridDispatchAction {

    public ActionForward prepareStudentSearch(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.setAttribute("tutorateBean", new NumberBean());
	return mapping.findForward("searchStudentTutorship");
    }

    public ActionForward showStudentPerformanceGrid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	NumberBean numberBean = (NumberBean) getRenderedObject("tutorateBean");

	Student student;
	StudentsPerformanceInfoBean performanceBean;
	if (numberBean != null) {
	    student = Student.readStudentByNumber(numberBean.getNumber());
	} else {
	    performanceBean = (StudentsPerformanceInfoBean) getRenderedObject("performanceGridFiltersBean");
	    student = performanceBean.getStudent();

	    numberBean = new NumberBean();
	    numberBean.setNumber(student.getNumber());
	    request.setAttribute("numberBean", numberBean);
	}
	if (student != null) {
	    List<Tutorship> tutorships = student.getActiveTutorships();
	    performanceBean = getOrCreateStudentsPerformanceBean(request, student);
	    if (tutorships.size() > 0) {

		PerformanceGridTableDTO performanceGridTable = createPerformanceGridTable(request, tutorships,
			performanceBean.getStudentsEntryYear(), performanceBean.getCurrentMonitoringYear());

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
	    studentErrorMessage(request, numberBean.getNumber());
	}

	return mapping.findForward("showStudentPerformanceGrid");
    }

    protected StudentsPerformanceInfoBean getOrCreateStudentsPerformanceBean(HttpServletRequest request, Student student) {
	StudentsPerformanceInfoBean bean = (StudentsPerformanceInfoBean) getRenderedObject("performanceGridFiltersBean");
	if (bean != null) {
	    request.setAttribute("performanceGridFiltersBean", bean);
	    return bean;
	}

	bean = new StudentsPerformanceInfoBean();
	bean.setStudent(student);
	request.setAttribute("performanceGridFiltersBean", bean);
	return bean;
    }

    public ActionForward prepareStudentCurriculum(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	request.setAttribute("tutorateBean", new NumberBean());
	return mapping.findForward("showStudentCurriculum");
    }

    public ActionForward showStudentRegistration(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final NumberBean numberBean = new NumberBean();
	numberBean.setNumber(getIntegerFromRequest(request, "studentNumber"));
	request.setAttribute("tutorateBean", numberBean);
	return showOrChoose(mapping, request);
    }

    public ActionForward showStudentCurriculum(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	return showOrChoose(mapping, request);
    }

    private ActionForward showOrChoose(final ActionMapping mapping, final HttpServletRequest request) {
	Registration registration = null;

	final Integer registrationOID = getIntegerFromRequest(request, "registrationOID");
	NumberBean bean = (NumberBean) getObjectFromViewState("tutorateBean");
	if (bean == null) {
	    bean = (NumberBean) request.getAttribute("tutorateBean");
	}

	if (registrationOID != null) {
	    registration = rootDomainObject.readRegistrationByOID(registrationOID);
	} else {
	    final Student student = Student.readStudentByNumber(bean.getNumber());
	    if (student.getRegistrations().size() == 1) {
		registration = student.getRegistrations().get(0);
	    } else {
		request.setAttribute("student", student);
		return mapping.findForward("chooseRegistration");
	    }
	}

	if (registration == null) {
	    studentErrorMessage(request, bean.getNumber());
	} else {
	    request.setAttribute("registration", registration);
	}

	return mapping.findForward("showStudentCurriculum");
    }

    private void studentErrorMessage(HttpServletRequest request, Integer studentNumber) {
	addActionMessage("error", request, "student.does.not.exist", String.valueOf(studentNumber));
    }

}
