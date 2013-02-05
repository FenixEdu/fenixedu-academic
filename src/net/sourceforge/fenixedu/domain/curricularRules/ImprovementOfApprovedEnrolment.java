package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ImprovementOfApprovedEnrolment extends CurricularRuleNotPersistent {

    private Enrolment toApply;

    public ImprovementOfApprovedEnrolment(final Enrolment enrolment) {
        if (enrolment == null) {
            throw new DomainException("curricular.rule.invalid.parameters");
        } else {
            this.toApply = enrolment;
        }
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        return Collections.singletonList(new GenericPair<Object, Boolean>("label.improvementOfApprovedEnrolment", true));
    }

    public Enrolment getEnrolment() {
        return toApply;
    }

    @Override
    public DegreeModule getDegreeModuleToApplyRule() {
        return getEnrolment().getDegreeModule();
    }

    @Override
    public CourseGroup getContextCourseGroup() {
        return null;
    }

    @Override
    public CompositeRule getParentCompositeRule() {
        return null;
    }

    @Override
    public CurricularRuleType getCurricularRuleType() {
        return CurricularRuleType.IMPROVEMENT_OF_APPROVED_ENROLMENT;
    }

    @Override
    public ExecutionSemester getBegin() {
        return ExecutionSemester.readActualExecutionSemester();
    }

    @Override
    public ExecutionSemester getEnd() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ImprovementOfApprovedEnrolment) {
            ImprovementOfApprovedEnrolment improvementOfApprovedEnrolment = (ImprovementOfApprovedEnrolment) obj;

            return toApply == improvementOfApprovedEnrolment.getEnrolment();
        }

        return false;
    }

    @Override
    public VerifyRuleExecutor createVerifyRuleExecutor() {
        return VerifyRuleExecutor.NULL_VERIFY_EXECUTOR;
    }

}
