package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class LEBLBolonhaEnrolmentRule extends BolonhaEnrolmentRule {

    private static final String ESTAGIO_CODE = "B5F";

    private static final String DISSERTACAO_CODE = "B8S";

    public LEBLBolonhaEnrolmentRule(StudentCurricularPlan studentCurricularPlan,
	    ExecutionPeriod executionPeriod) {
	super(studentCurricularPlan, executionPeriod);
    }

    public List<CurricularCourse2Enroll> apply(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn)
	    throws EnrolmentRuleDomainException {

	if(isEnrolledInPreviousExecutionPeriodOrAproved(ESTAGIO_CODE) || isEnrolledInExecutionPeriod(ESTAGIO_CODE)) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, ESTAGIO_CODE);
	}

	if(isEnrolledInPreviousExecutionPeriodOrAproved(DISSERTACAO_CODE) || isEnrolledInExecutionPeriod(DISSERTACAO_CODE)) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, ESTAGIO_CODE);
	}

	return curricularCoursesToBeEnrolledIn;
    }
}
