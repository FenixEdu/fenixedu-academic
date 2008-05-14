package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public abstract class BolonhaEnrolmentRule implements IEnrollmentRule {

    protected final StudentCurricularPlan studentCurricularPlan;

    protected final ExecutionSemester executionSemester;
    
    protected BolonhaEnrolmentRule(StudentCurricularPlan studentCurricularPlan, ExecutionSemester executionSemester) {
	this.studentCurricularPlan = studentCurricularPlan;
	this.executionSemester = executionSemester;
    }
    
    protected int countEnrolments(String[] group) {
	int res = 0;
	for (String code : group) {
	    if(isEnrolledInExecutionPeriod(code)) {
		res++;
	    }
	}
	return res;
    }
    
    protected int countEnroledOrAprovedEnrolments(String[] group) {
	int res = 0;
	for (String code : group) {
	    if(isEnrolledInExecutionPeriodOrAproved(code)) {
		res++;
	    }
	}
	return res;
    }
    
    protected int countEnroledInPreviousExecutionPeriodOrAprovedEnrolments(String[] group) {
	int res = 0;
	for (String code : group) {
	    if(isEnrolledInPreviousExecutionPeriodOrAproved(code)) {
		res++;
	    }
	}
	return res;
    }



    protected boolean isGroupCompleted(CurricularCourseGroup optionalCurricularCourse, List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {
	
	int count = 0;
	for (CurricularCourse curricularCourse : optionalCurricularCourse.getCurricularCoursesSet()) {
	    if (isEnrolledInExecutionPeriodOrAproved(curricularCourse, executionSemester)) {
		count++;
	    }
	}
	
	if(count >= optionalCurricularCourse.getMaximumNumberOfOptionalCourses()) {
	    for (CurricularCourse curricularCourse : optionalCurricularCourse.getCurricularCourses()) {
		removeCurricularCourse(curricularCoursesToBeEnrolledIn, curricularCourse.getCode());
	    }
	    return true;
	} else {
	    return false;
	}
    }

    protected void removeCurricularCourses(
	    final List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn, final List<String> codes) {
	CollectionUtils.filter(curricularCoursesToBeEnrolledIn, new Predicate() {

	    public boolean evaluate(Object arg0) {
		CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) arg0;
		return !codes.contains(curricularCourse2Enroll.getCurricularCourse().getCode());
	    }
	});
    }

    protected boolean isEnrolledInExecutionPeriod(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
	
	return studentCurricularPlan.isCurricularCourseEnrolledInExecutionPeriod(curricularCourse,executionSemester);
    }
    
    private boolean isEnrolledInExecutionPeriodOrAproved(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
	
	return studentCurricularPlan.isCurricularCourseEnrolledInExecutionPeriod(curricularCourse,executionSemester)
		|| studentCurricularPlan.isCurricularCourseApproved(curricularCourse);

    }
    
    protected boolean isEnrolledInPreviousExecutionPeriod(final String code) {

	final CurricularCourse curricularCourse = studentCurricularPlan.getDegreeCurricularPlan().getCurricularCourseByCode(code);
	
	return isEnrolledInExecutionPeriod(curricularCourse, executionSemester.getPreviousExecutionPeriod());

    }
    
    protected boolean isEnrolledInExecutionPeriod(final String code) {

	final CurricularCourse curricularCourse = studentCurricularPlan.getDegreeCurricularPlan().getCurricularCourseByCode(code);
	
	return isEnrolledInExecutionPeriod(curricularCourse, executionSemester);

    }
    
    protected boolean isEnrolledInExecutionPeriodOrAproved(final String code) {

	final CurricularCourse curricularCourse = studentCurricularPlan.getDegreeCurricularPlan().getCurricularCourseByCode(code);
	
	return isEnrolledInExecutionPeriodOrAproved(curricularCourse, executionSemester);

    }
    
    protected boolean isEnrolledInPreviousExecutionPeriodOrAproved(final String code) {
	final CurricularCourse curricularCourse = studentCurricularPlan.getDegreeCurricularPlan().getCurricularCourseByCode(code);
	
	return isEnrolledInExecutionPeriodOrAproved(curricularCourse, executionSemester.getPreviousExecutionPeriod());
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
    
    protected boolean isAproved(final String code) {
	final CurricularCourse curricularCourse = studentCurricularPlan.getDegreeCurricularPlan().getCurricularCourseByCode(code);
	return studentCurricularPlan.isApproved(curricularCourse);
    }


}
