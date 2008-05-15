package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalCurriculumGroup;

public class CreditsLimitInExternalCycle extends CurricularRuleNotPersistent {

    private static final double MIN_CREDITS_IN_PREVIOUS_CYCLE = 150;

    private static final double MAX_CREDITS_IN_EXTERNAL_CYCLE = 80;

    private ExternalCurriculumGroup externalCurriculumGroup;

    private CycleCurriculumGroup previousCycleCurriculumGroup;

    public CreditsLimitInExternalCycle(final CycleCurriculumGroup previousCycleCurriculumGroup,
	    final ExternalCurriculumGroup toApply) {
	if (toApply == null || previousCycleCurriculumGroup == null) {
	    throw new DomainException("curricular.rule.invalid.parameters");
	}

	this.previousCycleCurriculumGroup = previousCycleCurriculumGroup;
	this.externalCurriculumGroup = toApply;

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

    public boolean creditsExceedMaximumInExternalCycle(final Double numberOfCredits) {
	return numberOfCredits.compareTo(MAX_CREDITS_IN_EXTERNAL_CYCLE) > 0;
    }

    public boolean creditsInPreviousCycleSufficient(final Double previousCycleCredits) {
	return previousCycleCredits.compareTo(MIN_CREDITS_IN_PREVIOUS_CYCLE) >= 0;

    }

    public CurricularRuleType getCurricularRuleType() {
	return CurricularRuleType.CREDITS_LIMIT_IN_EXTERNAL_CYCLE;
    }

    public ExecutionSemester getBegin() {
	return ExecutionSemester.readActualExecutionSemester();
    }

    public ExecutionSemester getEnd() {
	return null;
    }

    public ExternalCurriculumGroup getExternalCurriculumGroup() {
	return externalCurriculumGroup;
    }

    public CycleCurriculumGroup getPreviousCycleCurriculumGroup() {
	return previousCycleCurriculumGroup;
    }

    public Double getMaxCreditsInExternalCycle() {
	return MAX_CREDITS_IN_EXTERNAL_CYCLE;
    }

    public Double getMinCreditsInPreviousCycle() {
	return MIN_CREDITS_IN_PREVIOUS_CYCLE;
    }

    public VerifyRuleExecutor createVerifyRuleExecutor() {
	return VerifyRuleExecutor.NULL_VERIFY_EXECUTOR;
    }

}
