package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

public class HelicopterosLEARule implements IEnrollmentRule {
    
    private static final String HELI_CODE = "B52";
    
    private StudentCurricularPlan studentCurricularPlan;
    
    public HelicopterosLEARule(StudentCurricularPlan studentCurricularPlan) {
	this.studentCurricularPlan = studentCurricularPlan;
    }

    public List<CurricularCourse2Enroll> apply(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn)
	    throws EnrolmentRuleDomainException {
	CurricularCourse heli = studentCurricularPlan.getDegreeCurricularPlan().getCurricularCourseByCode(HELI_CODE);
	CurricularCourse2Enroll curricularCourse2Enroll = getCourseToEnroll(curricularCoursesToBeEnrolledIn, heli);
	if(curricularCourse2Enroll != null) {
	    curricularCourse2Enroll.setEnrollmentType(CurricularCourseEnrollmentType.TEMPORARY);
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
