package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.ArrayList;
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

public class LEANBolonhaEnrolmentRule implements IEnrollmentRule {

    private static final String PROJECTOI_CODE = "LU";

    private static final String DISSERTACAO_CODE = "$64";

    private static final String[] COMMONS_810 = { "MD", "L1", "72", "B1W", "AVI", "LT", "A3Q", "A7B" };

    private static final String[] COMMONS_820 = { "L1", "72", "B1V", "B1W", "AVI", "LT", "A7B" };

    private static final String[] DEGREE_810 = {"A2I", "L7", "A2J", "BO", "XG" };
    
    private static final String[] DEGREE_820 = {"A2I", "L7", "A2J", "B1X" };

    private static final String DEGREE_GROUP = "Opções 5ºano 1ºsem";

    private static final String MASTER_DEGREE_GROUP = "Opções 5ºano - Mestrado";

    private final StudentCurricularPlan studentCurricularPlan;

    private final ExecutionPeriod executionPeriod;

    public LEANBolonhaEnrolmentRule(StudentCurricularPlan studentCurricularPlan,
	    ExecutionPeriod executionPeriod) {
	this.studentCurricularPlan = studentCurricularPlan;
	this.executionPeriod = executionPeriod;
    }

    public List<CurricularCourse2Enroll> apply(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn)
	    throws EnrolmentRuleDomainException {

	if(studentCurricularPlan.getBranch() != null) {
	    if(studentCurricularPlan.getBranch().getCode().equals("810")) {
		return apply(curricularCoursesToBeEnrolledIn, COMMONS_810, DEGREE_810);
	    } 

	    if(studentCurricularPlan.getBranch().getCode().equals("820")) {
		return apply(curricularCoursesToBeEnrolledIn, COMMONS_820, DEGREE_820);
	    }
	}
	removeAll(curricularCoursesToBeEnrolledIn);
	return curricularCoursesToBeEnrolledIn;
    }
    
    private void removeAll(List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn) {
	List<String> coursesToRemove = new ArrayList<String>();
	coursesToRemove.add(PROJECTOI_CODE);
	coursesToRemove.add(DISSERTACAO_CODE);
	coursesToRemove.addAll(Arrays.asList(COMMONS_810));
	coursesToRemove.addAll(Arrays.asList(COMMONS_820));
	coursesToRemove.addAll(Arrays.asList(DEGREE_810));
	coursesToRemove.addAll(Arrays.asList(DEGREE_820));
	removeCurricularCourses(curricularCoursesToBeEnrolledIn, coursesToRemove);
    }

    private List<CurricularCourse2Enroll> apply(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn, String[] COMMONS, String[] DEGREE) {
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
	
	/*isGroupCompleted(DEGREE_GROUP, curricularCoursesToBeEnrolledIn);
	
	isGroupCompleted(MASTER_DEGREE_GROUP, curricularCoursesToBeEnrolledIn);*/

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
