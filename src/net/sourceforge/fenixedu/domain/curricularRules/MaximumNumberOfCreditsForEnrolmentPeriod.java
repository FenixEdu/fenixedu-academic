package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.curricularRules.executors.verifyExecutors.VerifyRuleExecutor;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class MaximumNumberOfCreditsForEnrolmentPeriod extends MaximumNumberOfCreditsForEnrolmentPeriod_Base {

    static final public double MAXIMUM_NUMBER_OF_CREDITS = 40.5;

    static final public double ACCUMULATED_FACTOR = 0.75;

    public MaximumNumberOfCreditsForEnrolmentPeriod(final DegreeModule degreeModuleToApplyRule, final ExecutionSemester begin,
	    final ExecutionSemester end) {

	super();
	checkDegreeModule(degreeModuleToApplyRule);
	init(degreeModuleToApplyRule, null, begin, end, CurricularRuleType.MAXIMUM_NUMBER_OF_CREDITS_FOR_ENROLMENT_PERIOD);
    }

    public MaximumNumberOfCreditsForEnrolmentPeriod(final DegreeModule degreeModuleToApplyRule, final ExecutionSemester begin) {
	this(degreeModuleToApplyRule, begin, null);
    }

    private void checkDegreeModule(final DegreeModule degreeModule) {
	if (!degreeModule.isRoot()) {
	    throw new DomainException(
		    "error.curricularRules.MaximumNumberOfCreditsForEnrolmentPeriod.should.be.applied.to.root.degreeModule");
	}
    }

    @Override
    protected void checkParameters(final DegreeModule degreeModuleToApplyRule, final ExecutionSemester begin) {
	if (degreeModuleToApplyRule == null || begin == null) {
	    throw new DomainException("curricular.rule.invalid.parameters");
	}
    }

    @Override
    public boolean isVisible() {
	return false;
    }

    @Override
    protected void removeOwnParameters() {
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
	final List<GenericPair<Object, Boolean>> result = new ArrayList<GenericPair<Object, Boolean>>(3);

	result.add(new GenericPair<Object, Boolean>("label.maximumNumberOfCreditsForEnrolmentPeriod", true));
	result.add(new GenericPair<Object, Boolean>(": ", false));
	result.add(new GenericPair<Object, Boolean>(MAXIMUM_NUMBER_OF_CREDITS, false));

	return result;
    }

    public static Double getAccumulatedEctsCredits(final CurricularCourse curricularCourse, final ExecutionSemester executionSemester) {
	return curricularCourse.getEctsCredits(executionSemester.getSemester(), executionSemester) * ACCUMULATED_FACTOR;
    }

    public VerifyRuleExecutor createVerifyRuleExecutor() {
	return VerifyRuleExecutor.NULL_VERIFY_EXECUTOR;
    }

}
