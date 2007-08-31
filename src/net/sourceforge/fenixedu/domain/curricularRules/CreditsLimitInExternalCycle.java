package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalCurriculumGroup;

public class CreditsLimitInExternalCycle extends CurricularRuleNotPersistent {

    private static final double MAX_CREDITS = 80;

    private ExternalCurriculumGroup externalCurriculumGroup;

    public CreditsLimitInExternalCycle(final ExternalCurriculumGroup toApply) {
	if (toApply == null) {
	    throw new DomainException("curricular.rule.invalid.parameters");
	} else {
	    this.externalCurriculumGroup = toApply;
	}
    }

    public List<GenericPair<Object, Boolean>> getLabel() {
	return Collections.singletonList(new GenericPair<Object, Boolean>("label.creditsLimitInExternalCycle", true));
    }

    public DegreeModule getDegreeModuleToApplyRule() {
	return externalCurriculumGroup.getDegreeModule();
    }

    public CourseGroup getContextCourseGroup() {
	return null;
    }

    public CompositeRule getParentCompositeRule() {
	return null;
    }

    public boolean creditsExceedMaximum(final Double numberOfCredits) {
	return numberOfCredits.compareTo(MAX_CREDITS) > 0;
    }

    public CurricularRuleType getCurricularRuleType() {
	return CurricularRuleType.CREDITS_LIMIT_IN_EXTERNAL_CYCLE;
    }

    public ExecutionPeriod getBegin() {
	return ExecutionPeriod.readActualExecutionPeriod();
    }

    public ExecutionPeriod getEnd() {
	return null;
    }

    public ExternalCurriculumGroup getExternalCurriculumGroup() {
	return externalCurriculumGroup;
    }

    public Double getMaxCredits() {
	return MAX_CREDITS;
    }

    public VerifyRuleExecutor createVerifyRuleExecutor() {
	return VerifyRuleExecutor.NULL_VERIFY_EXECUTOR;
    }

}
