package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseGroup;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

public class LETBolonhaEnrolmentRule implements IEnrollmentRule {

    private static final String PROJECTOI_CODE = "LU";

    private static final String DISSERTACAO_CODE = "$60";

    private static final String[] COMMONS = { "44", "ON", "AFG", "B50", "ACZ", "AFL" };

    private static final String[] DEGREE = { "ACY", "AH0", "AFC", "AF6", "AF5", "OP", "4I", "0T", "81" };

    private static final String DEGREE_GROUP = "Opções 5ºano 1ºsem";

    private static final String MASTER_DEGREE_GROUP = "Opções 5ºano - Mestrado";

    private final StudentCurricularPlan studentCurricularPlan;

    private final ExecutionPeriod executionPeriod;

    public LETBolonhaEnrolmentRule(StudentCurricularPlan studentCurricularPlan,
	    ExecutionPeriod executionPeriod) {
	this.studentCurricularPlan = studentCurricularPlan;
	this.executionPeriod = executionPeriod;
    }

    public List<CurricularCourse2Enroll> apply(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn)
	    throws EnrolmentRuleDomainException {

	if (hasProjectoIOrDissertacaoToEnrol(curricularCoursesToBeEnrolledIn)) {
	    List<Enrolment> enrolments = studentCurricularPlan
		    .getAllStudentEnrollmentsInExecutionPeriod(executionPeriod);

	    final boolean projectoI = isEnrolledInExecutionPeriod(PROJECTOI_CODE, enrolments);
	    final boolean dissertacao = isEnrolledInExecutionPeriod(DISSERTACAO_CODE, enrolments);

	    if (projectoI) {
		removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	    }
	    if (dissertacao) {
		removeCurricularCourse(curricularCoursesToBeEnrolledIn, PROJECTOI_CODE);
		removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(DEGREE));
	    }

	    if (!projectoI && !dissertacao) {
		removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(DEGREE));
		removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(COMMONS));
	    }
	}
	
	isGroupCompleted(DEGREE_GROUP, curricularCoursesToBeEnrolledIn);
	
	isGroupCompleted(MASTER_DEGREE_GROUP, curricularCoursesToBeEnrolledIn);

	return curricularCoursesToBeEnrolledIn;
    }

    private boolean isGroupCompleted(String groupName, List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {
	CurricularCourseGroup optionalCurricularCourse = studentCurricularPlan.getDegreeCurricularPlan()
		.getBranchByName("").getOptionalCurricularCourseGroupByName(groupName);
	
	int count = 0;
	for (CurricularCourse curricularCourse : optionalCurricularCourse.getCurricularCoursesSet()) {
	    if (studentCurricularPlan.isCurricularCourseEnrolledInExecutionPeriod(curricularCourse,
		    executionPeriod)
		    || studentCurricularPlan.isCurricularCourseApproved(curricularCourse)) {
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

    private boolean hasProjectoIOrDissertacaoToEnrol(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {
	for (CurricularCourse2Enroll curricularCourse2Enroll : curricularCoursesToBeEnrolledIn) {
	    if (curricularCourse2Enroll.getCurricularCourse().getCode().equals(PROJECTOI_CODE)
		    || curricularCourse2Enroll.getCurricularCourse().getCode().equals(DISSERTACAO_CODE)) {
		return true;
	    }
	}
	return false;
    }

    private void removeCurricularCourses(
	    final List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn, final List<String> codes) {
	CollectionUtils.filter(curricularCoursesToBeEnrolledIn, new Predicate() {

	    public boolean evaluate(Object arg0) {
		CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) arg0;
		return !codes.contains(curricularCourse2Enroll.getCurricularCourse().getCode());
	    }
	});
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
	CollectionUtils.filter(curricularCourses2Enroll, new Predicate() {

	    public boolean evaluate(Object arg0) {
		CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) arg0;
		return !curricularCourse2Enroll.getCurricularCourse().getCode().equals(
			curricular_corse_code);
	    }
	});
    }

}
