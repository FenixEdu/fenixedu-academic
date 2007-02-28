package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

public class CineticaQuimicaLQRule implements IEnrollmentRule {
    
    private static final String CINETICA_CODE = "2P";
    private static final String QUIMICA_CODE = "BBK";
    
    private StudentCurricularPlan studentCurricularPlan;
    private ExecutionPeriod executionPeriod;
    
    public CineticaQuimicaLQRule(StudentCurricularPlan studentCurricularPlan, ExecutionPeriod executionPeriod) {
	this.studentCurricularPlan = studentCurricularPlan;
	this.executionPeriod = executionPeriod;
    }

    public List<CurricularCourse2Enroll> apply(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn)
	    throws EnrolmentRuleDomainException {
	
	final CurricularCourse quimicaCourse = studentCurricularPlan.getDegreeCurricularPlan().getCurricularCourseByCode(CINETICA_CODE);
	if(hasEverBeenEnrolled(studentCurricularPlan, quimicaCourse, executionPeriod)) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, QUIMICA_CODE);
	} else {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, CINETICA_CODE);
	}
	
	return curricularCoursesToBeEnrolledIn;
    }

    private boolean hasEverBeenEnrolled(StudentCurricularPlan studentCurricularPlan, CurricularCourse curricularCourse, ExecutionPeriod executionPeriod) {
	for (Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
	    if(enrolment.getCurricularCourse().equals(curricularCourse) && enrolment.getExecutionPeriod().isBefore(executionPeriod)) {
		return true;
	    }
	}
	return false;
    }
    
    protected void removeCurricularCourse(List curricularCourses2Enroll, final String curricular_corse_code) {
	CollectionUtils.filter(curricularCourses2Enroll, new Predicate() {

	    public boolean evaluate(Object arg0) {
		CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) arg0;
		return !curricularCourse2Enroll.getCurricularCourse().getCode().equals(
			curricular_corse_code);
	    }
	});
    }

}
