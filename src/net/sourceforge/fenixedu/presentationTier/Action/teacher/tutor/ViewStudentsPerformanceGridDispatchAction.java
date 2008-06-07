package net.sourceforge.fenixedu.presentationTier.Action.teacher.tutor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.StudentsPerformanceInfoBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.TutorStatisticsBean;
import net.sourceforge.fenixedu.dataTransferObject.teacher.tutor.PerformanceGridTableDTO.PerformanceGridLine;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.commons.collections.comparators.ReverseComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.renderers.utils.RenderUtils;


public class ViewStudentsPerformanceGridDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	
    	final Person person = getLoggedPerson(request);    	
    	
    	StudentsPerformanceInfoBean bean = (StudentsPerformanceInfoBean)getRenderedObject(null);
    	
    	if(!person.getTeacher().getActiveTutorships().isEmpty()){
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
    
    public ActionForward prepareStudentsPerformanceGrid(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	final Person person = getLoggedPerson(request);
    	
    	if(!person.getTeacher().getActiveTutorships().isEmpty()){
	    	StudentsPerformanceInfoBean bean = (StudentsPerformanceInfoBean)request.getAttribute("performanceGridFiltersBean");
	    	
	    	List<ExecutionYear> entryYears = getEntryYearsForDegreeFilteredStudents(bean);
	    	bean.setStudentsEntryYearFromList(entryYears);
	    	
	    	List<ExecutionYear> monitoringYears = getPossibleMonitoringYears(bean);
	    	bean.setCurrentMonitoringYearFromList(monitoringYears);
	    	
	    	final List<Tutorship> tutors = person.getTeacher().getActiveTutorshipsByStudentsEntryYearAndDegree(bean.getStudentsEntryYear(), bean.getDegree());	
	    	Collections.sort(tutors, Tutorship.TUTORSHIP_COMPARATOR_BY_STUDENT_NUMBER);  	
	    	
	    	PerformanceGridTableDTO performanceGridTable = null;
	    	Object[] args = new Object[] { tutors, bean.getStudentsEntryYear(), bean.getCurrentMonitoringYear() };
	    	try {
	    		performanceGridTable = (PerformanceGridTableDTO)executeService(request, "CreatePerformanceGridTable", args);
	    	} catch (FenixServiceException ex) {
	    		addActionMessage(request, ex.getMessage(), ex.getArgs());
	    	}
	    	
	    	getStatisticsAndPutInTheRequest(request, performanceGridTable);
	    	
	    	request.setAttribute("performanceGridFiltersBean", bean);
	    	request.setAttribute("performanceGridTable", performanceGridTable);
	    	request.setAttribute("monitoringYear", bean.getCurrentMonitoringYear());
	    	request.setAttribute("totalStudents", tutors.size());
    	}
    	
    	request.setAttribute("tutor", person);
    	
        return mapping.findForward("viewStudentsPerformanceGrid");
    }
    
    public ActionForward prepareAllStudentsStatistics(ActionMapping mapping, ActionForm actionForm,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
    	final Person person = getLoggedPerson(request);
    	final Degree degree = rootDomainObject.readDegreeByOID(Integer.parseInt(request.getParameter("degreeOID")));
    	final ExecutionYear studentsEntryYear = rootDomainObject.readExecutionYearByOID(Integer.parseInt(request.getParameter("entryYearOID")));
    	final ExecutionYear currentMonitoringYear = rootDomainObject.readExecutionYearByOID(Integer.parseInt(request.getParameter("monitoringYearOID")));
    	
    	StudentsPerformanceInfoBean bean = new StudentsPerformanceInfoBean();
    	bean.setPerson(person);
    	bean.setDegree(degree);
    	bean.setStudentsEntryYear(studentsEntryYear);
    	bean.setCurrentMonitoringYear(currentMonitoringYear);
    	
    	request.setAttribute("performanceGridFiltersBean", bean);
    	
    	List<DegreeCurricularPlan> degreeCurricularPlans = new ArrayList(bean.getDegree().getDegreeCurricularPlans());
    	Collections.sort(degreeCurricularPlans, DegreeCurricularPlan.DEGREE_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_EXECUTION_DEGREE_AND_DEGREE_CODE);
		
    	List<StudentCurricularPlan> students = degreeCurricularPlans.get(0).getStudentsCurricularPlanGivenEntryYear(bean.getStudentsEntryYear());
    	
		putAllStudentsStatisticsInTheRequest(request, students, bean.getCurrentMonitoringYear());
		
		request.setAttribute("entryYear", bean.getStudentsEntryYear());
		request.setAttribute("totalEntryStudents", students.size());
    	
		return prepareStudentsPerformanceGrid(mapping, actionForm, request, response);
    }
    
    /*
     * AUXILIARY METHODS
     * 
     */
    private void getStatisticsAndPutInTheRequest(HttpServletRequest request, PerformanceGridTableDTO performanceGridTable) {
    	final List<PerformanceGridLine> performanceGridLines = performanceGridTable.getPerformanceGridTableLines();
    	
    	Map<Integer,TutorStatisticsBean> statisticsByApprovedEnrolmentsNumber = new HashMap<Integer,TutorStatisticsBean>();
    	int maxApprovedEnrolments = 0;
    	
    	for(PerformanceGridLine studentGridLine : performanceGridLines) {
    		maxApprovedEnrolments = Math.max(maxApprovedEnrolments, studentGridLine.getApprovedEnrolmentsNumber());
    		
    		if(statisticsByApprovedEnrolmentsNumber.containsKey(studentGridLine.getApprovedEnrolmentsNumber())) {
				TutorStatisticsBean tutorStatisticsbean = statisticsByApprovedEnrolmentsNumber.get(studentGridLine.getApprovedEnrolmentsNumber());
				tutorStatisticsbean.setStudentsNumber( tutorStatisticsbean.getStudentsNumber() + 1);
			}
			else {
				TutorStatisticsBean tutorStatisticsBean = new TutorStatisticsBean(1, studentGridLine.getApprovedEnrolmentsNumber(), performanceGridLines.size());
				tutorStatisticsBean.setApprovedEnrolmentsNumber(studentGridLine.getApprovedEnrolmentsNumber());
				statisticsByApprovedEnrolmentsNumber.put(studentGridLine.getApprovedEnrolmentsNumber(), tutorStatisticsBean);
			}
    	}
    	
    	putStatisticsInTheRequest(request, maxApprovedEnrolments, performanceGridLines.size(), statisticsByApprovedEnrolmentsNumber, "tutorStatistics");
    }
    
    private void putAllStudentsStatisticsInTheRequest(HttpServletRequest request, List<StudentCurricularPlan> students, ExecutionYear currentMonitoringYear){
    	Map<Integer,TutorStatisticsBean> statisticsByApprovedEnrolmentsNumber = new HashMap<Integer,TutorStatisticsBean>();
    	
    	int maxApprovedEnrolments = 0;
    	
		for(StudentCurricularPlan scp : students) {
			List<Enrolment> enrolments = scp.getEnrolmentsByExecutionYear(currentMonitoringYear);
			int approvedEnrolments = 0;
			
			for(Enrolment enrolment : enrolments) {			
				if(!enrolment.getCurricularCourse().isAnual() && enrolment.getEnrollmentState().equals(EnrollmentState.APROVED)) {
					approvedEnrolments++;
				}
			}
			
			maxApprovedEnrolments = Math.max(maxApprovedEnrolments, approvedEnrolments);
			
			if(statisticsByApprovedEnrolmentsNumber.containsKey(approvedEnrolments)) {
				TutorStatisticsBean tutorStatisticsbean = statisticsByApprovedEnrolmentsNumber.get(approvedEnrolments);
				tutorStatisticsbean.setStudentsNumber( tutorStatisticsbean.getStudentsNumber() + 1);
			}
			else {
				TutorStatisticsBean tutorStatisticsBean = new TutorStatisticsBean(1, approvedEnrolments, students.size());
				tutorStatisticsBean.setApprovedEnrolmentsNumber(approvedEnrolments);
				statisticsByApprovedEnrolmentsNumber.put(approvedEnrolments, tutorStatisticsBean);
			}	
		}
		
		putStatisticsInTheRequest(request, maxApprovedEnrolments, students.size(), statisticsByApprovedEnrolmentsNumber, "allStudentsStatistics");
    }
    
    private void putStatisticsInTheRequest(HttpServletRequest request, Integer maxApprovedEnrolments, Integer studentsSize, Map<Integer, TutorStatisticsBean> statisticsByApprovedEnrolmentsNumber, 
    		String attributeId) {
    	
    	if(studentsSize != 0) {
	    	List<TutorStatisticsBean> statistics = new ArrayList<TutorStatisticsBean>();
	    	for(int i = 0 ; i <= maxApprovedEnrolments ; i++) {
	    		if(statisticsByApprovedEnrolmentsNumber.containsKey(i)) {
	    			statistics.add(statisticsByApprovedEnrolmentsNumber.get(i));
	    		}
	    		else {
	    			statistics.add(new TutorStatisticsBean(0, i, studentsSize));
	    		}
	    	}
	    	
	    	Collections.sort(statistics);
	    	
	    	request.setAttribute(attributeId, statistics);
    	}
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
    	for(Tutorship tutorship : tutorships) {
    		StudentCurricularPlan studentCurricularPlan = tutorship.getStudentCurricularPlan();
    		ExecutionYear studentEntryYear = ExecutionYear.getExecutionYearByDate(studentCurricularPlan.getRegistration().getStartDate());
    		if(!executionYears.contains(studentEntryYear) && bean.getDegree().equals(studentCurricularPlan.getRegistration().getDegree())) {
    			executionYears.add(studentEntryYear);
    		}		
    	}
    	Collections.sort(executionYears, new ReverseComparator());
    	
    	return executionYears; 
    }
    
    public List<ExecutionYear> getPossibleMonitoringYears(StudentsPerformanceInfoBean bean) {
    	List<ExecutionYear> executionYears = new ArrayList<ExecutionYear>();
    	for(ExecutionYear year : RootDomainObject.getInstance().getExecutionYears()) {
    		if(year.isAfterOrEquals(bean.getStudentsEntryYear()))
    			executionYears.add(year);
    	}
    	Collections.sort(executionYears, new ReverseComparator());
    	
    	return executionYears;
    }
    
    public List<Degree> getFilteredDegrees(Person person) {
		List<Degree> degrees = new ArrayList<Degree>();
    	List<Tutorship> tutorships = person.getTeacher().getActiveTutorships();
    	for(Tutorship tutorship : tutorships) {
    		StudentCurricularPlan studentCurricularPlan = tutorship.getStudentCurricularPlan();
			if(!degrees.contains(studentCurricularPlan.getRegistration().getDegree())) {
				degrees.add(studentCurricularPlan.getRegistration().getDegree());
			}
    	}
    	return degrees;
    }
}

