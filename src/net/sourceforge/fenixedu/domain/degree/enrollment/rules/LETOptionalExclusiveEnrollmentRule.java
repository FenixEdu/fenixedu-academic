/**
 * 
 */
package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author Ricardo Rodrigues
 *
 */

public class LETOptionalExclusiveEnrollmentRule implements IEnrollmentRule {
    
    private static final String COMPLEMENTOS_INVESTIGACAO_OPERACIONAL_CODE = "AFG";
    private static final String AVALIACAO_PROJECTOS_DECISAO_PUBLICA_CODE = "ON";
    
    private final StudentCurricularPlan studentCurricularPlan;
    private final ExecutionSemester executionSemester;

    public LETOptionalExclusiveEnrollmentRule(StudentCurricularPlan studentCurricularPlan,
            ExecutionSemester executionSemester) {
        this.studentCurricularPlan = studentCurricularPlan;
        this.executionSemester = executionSemester;
    }

    public List apply(List curricularCoursesToBeEnrolledIn) {
    	
    	List<Enrolment> enrolments = studentCurricularPlan.getAllStudentEnrollmentsInExecutionPeriod(executionSemester);
        
        if(isEnrolledInExecutionPeriod(COMPLEMENTOS_INVESTIGACAO_OPERACIONAL_CODE, enrolments)){
            removeCurricularCourse(curricularCoursesToBeEnrolledIn,AVALIACAO_PROJECTOS_DECISAO_PUBLICA_CODE);
        }
        else
            if(isEnrolledInExecutionPeriod(AVALIACAO_PROJECTOS_DECISAO_PUBLICA_CODE, enrolments)){
                removeCurricularCourse(curricularCoursesToBeEnrolledIn,COMPLEMENTOS_INVESTIGACAO_OPERACIONAL_CODE);
            }
        return curricularCoursesToBeEnrolledIn;        
    }
    
    private boolean isEnrolledInExecutionPeriod(final String code, final List<Enrolment> enrolments) {
    	return CollectionUtils.exists(enrolments, new Predicate() {

			public boolean evaluate(Object arg0) {
				Enrolment enrolment = (Enrolment) arg0;
				return enrolment.getCurricularCourse().getCode().equals(code);
			}
    	});
    }

    private void removeCurricularCourse(List curricularCourses2Enroll, final String curricular_corse_code) {
        CollectionUtils.filter(curricularCourses2Enroll, new Predicate(){

            public boolean evaluate(Object arg0) {
                CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) arg0;
                return !curricularCourse2Enroll.getCurricularCourse().getCode().equals(curricular_corse_code);
            }});        
    }
}


