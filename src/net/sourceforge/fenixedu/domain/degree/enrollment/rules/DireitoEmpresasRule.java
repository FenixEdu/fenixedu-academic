package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

public class DireitoEmpresasRule implements IEnrollmentRule{
    
    private StudentCurricularPlan studentCurricularPlan;
    
    private ExecutionSemester executionSemester;
    
    private static final String DIREITO_CODE = "B96";
    
    private static final String DIREITO_EMPRESAS_CODE = "A4G";
    
    public DireitoEmpresasRule(StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester) {
	this.studentCurricularPlan = studentCurricularPlan;
	this.executionSemester = executionSemester;
    }

    public List<CurricularCourse2Enroll> apply(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) throws EnrolmentRuleDomainException {
	CurricularCourse direito = studentCurricularPlan.getDegreeCurricularPlan().getCurricularCourseByCode(DIREITO_CODE);
	CurricularCourse2Enroll courseToEnroll = getCourseToEnroll(curricularCoursesToBeEnrolledIn, direito);
	if(courseToEnroll != null) {
	    CurricularCourse direitoEmpresas = studentCurricularPlan.getDegreeCurricularPlan().getCurricularCourseByCode(DIREITO_EMPRESAS_CODE);
	    if(studentCurricularPlan.isEnroledInExecutionPeriod(direitoEmpresas, executionSemester.getPreviousExecutionPeriod())) {
		courseToEnroll.setEnrollmentType(CurricularCourseEnrollmentType.TEMPORARY);
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
