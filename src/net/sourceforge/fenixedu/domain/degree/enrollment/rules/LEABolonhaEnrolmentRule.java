package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentRuleDomainException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

public class LEABolonhaEnrolmentRule extends BolonhaEnrolmentRule {

    private static final String PROJECTO_CODE = "A72";

    private static final String DISSERTACAO_CODE = "$66";

    public LEABolonhaEnrolmentRule(StudentCurricularPlan studentCurricularPlan,
	    ExecutionPeriod executionPeriod) {
	super(studentCurricularPlan, executionPeriod);
    }

    public List<CurricularCourse2Enroll> apply(
	    List<CurricularCourse2Enroll> curricularCoursesToBeEnrolledIn)
	    throws EnrolmentRuleDomainException {

	final boolean projecto = isEnrolledInExecutionPeriod(PROJECTO_CODE);
	final boolean dissertacao = isEnrolledInExecutionPeriod(DISSERTACAO_CODE);

	if (projecto) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, DISSERTACAO_CODE);
	}
	if (dissertacao) {
	    removeCurricularCourse(curricularCoursesToBeEnrolledIn, PROJECTO_CODE);
	}

	return curricularCoursesToBeEnrolledIn;
    }

}
