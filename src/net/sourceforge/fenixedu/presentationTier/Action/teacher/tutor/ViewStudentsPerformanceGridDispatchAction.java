package net.sourceforge.fenixedu.presentationTier.Action.teacher.tutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsPerformanceInfoBean;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.presentationTier.Action.commons.StudentsPerformanceGridDispatchAction;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;

public class ViewStudentsPerformanceGridDispatchAction extends StudentsPerformanceGridDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	final Person person = getLoggedPerson(request);

	StudentsPerformanceInfoBean bean = (StudentsPerformanceInfoBean) getRenderedObject(null);

	if (!person.getTeacher().getActiveTutorships().isEmpty()) {
	    if (bean == null) {
		bean = new StudentsPerformanceInfoBean();
		bean.setPerson(person);
		bean.setDegree(getFilteredDegrees(person).get(0));
		bean.setStudentsEntryYear(getEntryYearsForDegreeFilteredStudents(bean).get(0));
		bean.setCurrentMonitoringYear(getPossibleMonitoringYears(bean).get(0));
	    }

	    request.setAttribute("performanceGridFiltersBean", bean);
	}

	request.setAttribute("tutor", person);

	RenderUtils.invalidateViewState();

	return prepareStudentsPerformanceGrid(mapping, actionForm, request, response);
    }

    public ActionForward prepareStudentsPerformanceGrid(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final Person person = getLoggedPerson(request);

	if (!person.getTeacher().getActiveTutorships().isEmpty()) {
	    StudentsPerformanceInfoBean bean = (StudentsPerformanceInfoBean) request.getAttribute("performanceGridFiltersBean");

	    List<ExecutionYear> entryYears = getEntryYearsForDegreeFilteredStudents(bean);
	    bean.setStudentsEntryYearFromList(entryYears);

	    List<ExecutionYear> monitoringYears = getPossibleMonitoringYears(bean);
	    bean.setCurrentMonitoringYearFromList(monitoringYears);

	    final List<Tutorship> tutors = person.getTeacher().getActiveTutorshipsByStudentsEntryYearAndDegree(
		    bean.getStudentsEntryYear(), bean.getDegree());
	    Collections.sort(tutors, Tutorship.TUTORSHIP_COMPARATOR_BY_STUDENT_NUMBER);

	    PerformanceGridTableDTO performanceGridTable = createPerformanceGridTable(request, tutors, bean
		    .getStudentsEntryYear(), bean.getCurrentMonitoringYear());
	    getStatisticsAndPutInTheRequest(request, performanceGridTable);

	    request.setAttribute("performanceGridFiltersBean", bean);
	    request.setAttribute("performanceGridTable", performanceGridTable);
	    request.setAttribute("monitoringYear", bean.getCurrentMonitoringYear());
	    request.setAttribute("totalStudents", tutors.size());
	}

	request.setAttribute("tutor", person);

	return mapping.findForward("viewStudentsPerformanceGrid");
    }

    public ActionForward prepareAllStudentsStatistics(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	final Person person = getLoggedPerson(request);
	final Degree degree = rootDomainObject.readDegreeByOID(Integer.parseInt(request.getParameter("degreeOID")));
	final ExecutionYear studentsEntryYear = rootDomainObject.readExecutionYearByOID(Integer.parseInt(request
		.getParameter("entryYearOID")));
	final ExecutionYear currentMonitoringYear = rootDomainObject.readExecutionYearByOID(Integer.parseInt(request
		.getParameter("monitoringYearOID")));

	StudentsPerformanceInfoBean bean = new StudentsPerformanceInfoBean();
	bean.setPerson(person);
	bean.setDegree(degree);
	bean.setStudentsEntryYear(studentsEntryYear);
	bean.setCurrentMonitoringYear(currentMonitoringYear);

	request.setAttribute("performanceGridFiltersBean", bean);

	List<DegreeCurricularPlan> degreeCurricularPlans = new ArrayList(bean.getDegree().getDegreeCurricularPlans());
	Collections.sort(degreeCurricularPlans,
		DegreeCurricularPlan.DEGREE_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_EXECUTION_DEGREE_AND_DEGREE_CODE);

	List<StudentCurricularPlan> students = degreeCurricularPlans.get(0).getStudentsCurricularPlanGivenEntryYear(
		bean.getStudentsEntryYear());

	putAllStudentsStatisticsInTheRequest(request, students, bean.getCurrentMonitoringYear());

	request.setAttribute("entryYear", bean.getStudentsEntryYear());
	request.setAttribute("totalEntryStudents", students.size());

	return prepareStudentsPerformanceGrid(mapping, actionForm, request, response);
    }


    public Object getRenderedObject(String id) {
	if (id == null || id.equals("")) {
	    return (RenderUtils.getViewState() == null ? null : RenderUtils.getViewState().getMetaObject().getObject());
	} else {
	    return (RenderUtils.getViewState(id) == null ? null : RenderUtils.getViewState().getMetaObject().getObject());
	}
    }

    public List<ExecutionYear> getEntryYearsForDegreeFilteredStudents(StudentsPerformanceInfoBean bean) {
	List<ExecutionYear> executionYears = new ArrayList<ExecutionYear>();
	List<Tutorship> tutorships = bean.getPerson().getTeacher().getActiveTutorships();
	for (Tutorship tutorship : tutorships) {
	    StudentCurricularPlan studentCurricularPlan = tutorship.getStudentCurricularPlan();
	    ExecutionYear studentEntryYear = ExecutionYear.getExecutionYearByDate(studentCurricularPlan.getRegistration()
		    .getStartDate());
	    if (!executionYears.contains(studentEntryYear)
		    && bean.getDegree().equals(studentCurricularPlan.getRegistration().getDegree())) {
		executionYears.add(studentEntryYear);
	    }
	}
	Collections.sort(executionYears, new ReverseComparator());
	return executionYears;
    }

    public List<ExecutionYear> getPossibleMonitoringYears(StudentsPerformanceInfoBean bean) {
	List<ExecutionYear> executionYears = new ArrayList<ExecutionYear>();
	for (ExecutionYear year : RootDomainObject.getInstance().getExecutionYears()) {
	    if (year.isAfterOrEquals(bean.getStudentsEntryYear()))
		executionYears.add(year);
	}
	Collections.sort(executionYears, new ReverseComparator());
	return executionYears;
    }

    public List<Degree> getFilteredDegrees(Person person) {
	List<Degree> degrees = new ArrayList<Degree>();
	List<Tutorship> tutorships = person.getTeacher().getActiveTutorships();
	for (Tutorship tutorship : tutorships) {
	    StudentCurricularPlan studentCurricularPlan = tutorship.getStudentCurricularPlan();
	    if (!degrees.contains(studentCurricularPlan.getRegistration().getDegree())) {
		degrees.add(studentCurricularPlan.getRegistration().getDegree());
	    }
	}
	return degrees;
    }
}
