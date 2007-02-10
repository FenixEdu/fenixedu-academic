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

public class LEGMBolonhaEnrolmentRule extends BolonhaEnrolmentRule {


    private static final String DISSERTACAO_CODE = "B7S";
    
    private static final String DISSERTACAO_II_CODE = "B7T";

    private static final String[] COMMONS = { "AZY", "AZX", "AZZ"};

    private static final String[] DEGREE = { "AG3", "5Q", "B0B", "B07", "B04", "B03"};

    public LEGMBolonhaEnrolmentRule(StudentCurricularPlan studentCurricularPlan,
	    ExecutionPeriod executionPeriod) {
	super(studentCurricularPlan, executionPeriod);
    }

    public List<CurricularCourse2Enroll> apply(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn)
	    throws EnrolmentRuleDomainException {

	if (countEnroledOrAprovedEnrolments(DEGREE) >= 1) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	}
	if (isEnrolledInPreviousExecutionPeriodOrAproved(DISSERTACAO_CODE) || isEnrolledInExecutionPeriodOrAproved(DISSERTACAO_II_CODE)) {
	    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(DEGREE));
	}

	return curricularCoursesToBeEnrolledIn;
    }
}
