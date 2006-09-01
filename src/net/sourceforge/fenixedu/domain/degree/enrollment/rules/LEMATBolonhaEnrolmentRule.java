package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

public class LEMATBolonhaEnrolmentRule extends BolonhaEnrolmentRule {


    private static final String DISSERTACAO_CODE = "$60";
    
    private static final String MECANICA_COMPUTACIONAL = "A6P";

    private static final String[] OPTIONAL_GROUP = { "ASG", "ASH", "ASI", "B31"};

    public LEMATBolonhaEnrolmentRule(StudentCurricularPlan studentCurricularPlan,
	    ExecutionPeriod executionPeriod) {
	super(studentCurricularPlan, executionPeriod);
    }

    public List<CurricularCourse2Enroll> apply(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn)
	    throws EnrolmentRuleDomainException {

	final boolean dissertacao = isEnrolledInExecutionPeriod(DISSERTACAO_CODE);

	if (dissertacao) {
	    if(studentCurricularPlan.isCurricularCourseApproved(studentCurricularPlan.getDegreeCurricularPlan().getCurricularCourseByCode(MECANICA_COMPUTACIONAL))) {
		removeCurricularCourses(curricularCoursesToBeEnrolledIn, Arrays.asList(OPTIONAL_GROUP));
	    }
	}

	return curricularCoursesToBeEnrolledIn;
    }
}
