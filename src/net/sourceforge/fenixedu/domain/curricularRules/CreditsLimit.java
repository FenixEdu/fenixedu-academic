package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CreditsLimit extends CreditsLimit_Base {

    private CreditsLimit(final Double minimum, final Double maximum) {
	super();
	checkCredits(minimum, maximum);
	setMinimumCredits(minimum);
	setMaximumCredits(maximum);
	setCurricularRuleType(CurricularRuleType.CREDITS_LIMIT);
    }

    protected CreditsLimit(final DegreeModule degreeModuleToApplyRule,
	    final CourseGroup contextCourseGroup, final ExecutionPeriod begin,
	    final ExecutionPeriod end, final Double minimum, final Double maximum) {

	this(minimum, maximum);
	checkParameters(degreeModuleToApplyRule);
	init(degreeModuleToApplyRule, contextCourseGroup, begin, end);
    }

    private void checkParameters(final DegreeModule degreeModuleToApplyRule) {
	if (degreeModuleToApplyRule.isLeaf() && !degreeModuleToApplyRule.isOptional()) {
	    throw new DomainException(
		    "error.curricularRules.CreditsLimit.invalid.degreeModule.must.be.group.or.optional.curricularCourse");
	}
    }

    protected void edit(CourseGroup contextCourseGroup, Double minimumCredits, Double maximumCredits) {
	checkCredits(minimumCredits, maximumCredits);
	setContextCourseGroup(contextCourseGroup);
	setMinimumCredits(minimumCredits);
	setMaximumCredits(maximumCredits);
    }

    private void checkCredits(Double minimum, Double maximum) throws DomainException {
	if (minimum == null || maximum == null) {
	    throw new DomainException("curricular.rule.invalid.parameters");
	}
	if (minimum.doubleValue() > maximum.doubleValue()) {
	    throw new DomainException("error.minimum.greater.than.maximum");
	}
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
	List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();

	labelList.add(new GenericPair<Object, Boolean>("label.creditsForApproval", true));
	labelList.add(new GenericPair<Object, Boolean>(": ", false));
	if (getMinimumCredits().doubleValue() == getMaximumCredits().doubleValue()) {
	    labelList.add(new GenericPair<Object, Boolean>(getMinimumCredits(), false));
	} else {
	    labelList.add(new GenericPair<Object, Boolean>(getMinimumCredits(), false));
	    labelList.add(new GenericPair<Object, Boolean>(" ", false));
	    labelList.add(new GenericPair<Object, Boolean>("label.to", true));
	    labelList.add(new GenericPair<Object, Boolean>(" ", false));
	    labelList.add(new GenericPair<Object, Boolean>(getMaximumCredits(), false));
	}
	if (getContextCourseGroup() != null) {
	    labelList.add(new GenericPair<Object, Boolean>(", ", false));
	    labelList.add(new GenericPair<Object, Boolean>("label.inGroup", true));
	    labelList.add(new GenericPair<Object, Boolean>(" ", false));
	    labelList.add(new GenericPair<Object, Boolean>(getContextCourseGroup().getOneFullName(),
		    false));
	}

	return labelList;
    }

    @Override
    protected void removeOwnParameters() {
	// no domain parameters
    }

    public boolean allowCredits(final Double numberOfCredits) {
	return !(numberOfCredits.compareTo(getMinimumCredits()) < 0 || numberOfCredits.compareTo(getMaximumCredits()) > 0);
    }

    public boolean creditsExceedMaximum(final Double numberOfCredits) {
	return numberOfCredits.compareTo(getMaximumCredits()) > 0;
    }
}
