package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class LEICBolonhaEnrolmentRule extends BolonhaEnrolmentRule {

    private static final String[] DEGREE = {"B63"};

    private static final String[] MASTER_DEGREE = {"$38", "$129"};

    public LEICBolonhaEnrolmentRule(StudentCurricularPlan studentCurricularPlan,
	    ExecutionPeriod executionPeriod) {
	super(studentCurricularPlan, executionPeriod);
    }

    public List<CurricularCourse2Enroll> apply(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn)
	    throws EnrolmentRuleDomainException {

	if(studentCurricularPlan.getBranch() != null && studentCurricularPlan.getSecundaryBranch() != null) {
	    if (countEnrolments(DEGREE) == 1) {
		removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(MASTER_DEGREE));
	    }
	    if (countEnrolments(MASTER_DEGREE) >= 1) {
		removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(DEGREE));
	    }
	} else {
	    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(DEGREE));
	    removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(MASTER_DEGREE));
	}
	
	return curricularCoursesToBeEnrolledIn;
    }

}
