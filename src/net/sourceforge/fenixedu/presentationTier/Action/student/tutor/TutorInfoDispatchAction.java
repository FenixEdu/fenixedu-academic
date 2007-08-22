package net.sourceforge.fenixedu.presentationTier.Action.student.tutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.dataTransferObject.student.ExecutionPeriodStatisticsBean;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class TutorInfoDispatchAction extends FenixDispatchAction {
	
    public ActionForward prepare(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
        
    	final Person person = getLoggedPerson(request);
    	List<Tutorship> pastTutors = new ArrayList<Tutorship>();
    
    	List<Registration> registrations = person.getStudent().getRegistrations();

    	for(Registration registration : registrations) {
    		List<StudentCurricularPlan> studentCurricularPlans = registration.getStudentCurricularPlans();
    		for(StudentCurricularPlan studentCurricularPlan : studentCurricularPlans) {
    			for(Tutorship tutorship : studentCurricularPlan.getTutorships()) {
    				if(tutorship.isActive()) {
    					request.setAttribute("actualTutor", tutorship);
    					request.setAttribute("personID", tutorship.getTeacher().getPerson().getIdInternal());
    				} else {
    					pastTutors.add(tutorship);
    				}
    			} 			
    		}	   		
    	} 	
    	request.setAttribute("pastTutors", pastTutors); 	
    	return prepareStudentStatistics(mapping, actionForm, request, response);
    }
    
    public ActionForward prepareStudentStatistics(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request, HttpServletResponse response) {
    	
    	final Person person = getLoggedPerson(request);
    	final List<Registration> registrations = person.getStudent().getRegistrations();
    	
    	List<ExecutionPeriodStatisticsBean> studentStatistics = getStudentStatistics(registrations);
    	
    	request.setAttribute("studentStatistics", studentStatistics);   	
    	return mapping.findForward("ShowStudentTutorInfo"); 
    }
    
    /*
     * AUXIALIRY METHODS
     */
    
    private List<ExecutionPeriodStatisticsBean> getStudentStatistics(List<Registration> registrations) {
    	List<ExecutionPeriodStatisticsBean> studentStatistics = new ArrayList<ExecutionPeriodStatisticsBean>();
    	
    	Map<ExecutionPeriod, ExecutionPeriodStatisticsBean> enrolmentsByExecutionPeriod = new HashMap<ExecutionPeriod, ExecutionPeriodStatisticsBean>();
    	
    	for(Registration registration : registrations) {
    		for(StudentCurricularPlan studentCurricularPlan : registration.getStudentCurricularPlans()) {	
    			for(ExecutionPeriod executionPeriod : studentCurricularPlan.getEnrolmentsExecutionPeriods()) {
    				if (enrolmentsByExecutionPeriod.containsKey(executionPeriod)) {
    					ExecutionPeriodStatisticsBean executionPeriodStatisticsBean = enrolmentsByExecutionPeriod.get(executionPeriod);
    					executionPeriodStatisticsBean.addEnrolmentsWithinExecutionPeriod(studentCurricularPlan.getEnrolmentsByExecutionPeriod(executionPeriod));
    					enrolmentsByExecutionPeriod.put(executionPeriod, executionPeriodStatisticsBean);
    				}
    				else {
    					ExecutionPeriodStatisticsBean executionPeriodStatisticsBean = new ExecutionPeriodStatisticsBean(executionPeriod);
    					executionPeriodStatisticsBean.addEnrolmentsWithinExecutionPeriod(studentCurricularPlan.getEnrolmentsByExecutionPeriod(executionPeriod));
    					enrolmentsByExecutionPeriod.put(executionPeriod, executionPeriodStatisticsBean);
    				}
    			}
    		}
    	}
    	
    	studentStatistics.addAll(enrolmentsByExecutionPeriod.values());
    	
    	return studentStatistics;
    }
}
