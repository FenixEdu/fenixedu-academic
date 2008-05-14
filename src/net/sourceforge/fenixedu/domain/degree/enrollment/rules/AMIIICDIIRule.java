package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

public class AMIIICDIIRule implements IEnrollmentRule{
    
    private StudentCurricularPlan studentCurricularPlan;
    
    private ExecutionSemester executionSemester;
    
    private static final String AMII_CODE = "P5";
    
    private static final String CDII_CODE = "B71";
    
    public AMIIICDIIRule(StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester) {
	this.studentCurricularPlan = studentCurricularPlan;
	this.executionSemester = executionSemester;
    }

    public List<CurricularCourse2Enroll> apply(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) throws EnrolmentRuleDomainException {
	CurricularCourse cdII = studentCurricularPlan.getDegreeCurricularPlan().getCurricularCourseByCode(CDII_CODE);
	CurricularCourse2Enroll curricularCourse2Enroll = getCourseToEnroll(curricularCoursesToBeEnrolledIn, cdII); 
	if(curricularCourse2Enroll != null) {
	    CurricularCourse amII = studentCurricularPlan.getDegreeCurricularPlan().getCurricularCourseByCode(AMII_CODE);
	    if(studentCurricularPlan.isCurricularCourseApproved(amII) || studentCurricularPlan.isEnroledInExecutionPeriod(amII, executionSemester.getPreviousExecutionPeriod())) {
		curricularCourse2Enroll.setEnrollmentType(CurricularCourseEnrollmentType.TEMPORARY);
	    }
	}
	
	return curricularCoursesToBeEnrolledIn;
    }

    private CurricularCourse2Enroll getCourseToEnroll(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn, CurricularCourse curricularCourse) {
	for (CurricularCourse2Enroll curricularCourse2Enroll : curricularCoursesToBeEnrolledIn) {
	    if(curricularCourse2Enroll.getCurricularCourse().equals(curricularCourse)) {
		return curricularCourse2Enroll;
	    }
	}
	return null;
    }

}
