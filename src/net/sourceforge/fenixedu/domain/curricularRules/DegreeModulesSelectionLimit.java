package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class DegreeModulesSelectionLimit extends DegreeModulesSelectionLimit_Base {

    private DegreeModulesSelectionLimit(final Integer minimum, final Integer maximum) {
	super();
	checkLimits(minimum, maximum);
	setMinimumLimit(minimum);
	setMaximumLimit(maximum);
	setCurricularRuleType(CurricularRuleType.DEGREE_MODULES_SELECTION_LIMIT);
    }

    protected DegreeModulesSelectionLimit(final CourseGroup degreeModuleToApplyRule,
	    final CourseGroup contextCourseGroup, final ExecutionPeriod begin,
	    final ExecutionPeriod end, final Integer minimum, final Integer maximum) {

	this(minimum, maximum);
	init(degreeModuleToApplyRule, contextCourseGroup, begin, end);
    }

    protected void edit(CourseGroup contextCourseGroup, Integer minimumLimit, Integer maximumLimit) {
	checkLimits(minimumLimit, maximumLimit);
	setContextCourseGroup(contextCourseGroup);
	setMinimumLimit(minimumLimit);
	setMaximumLimit(maximumLimit);
    }

    private void checkLimits(Integer minimum, Integer maximum) throws DomainException {
	if (minimum == null || maximum == null) {
	    throw new DomainException("curricular.rule.invalid.parameters");
	}
	if (minimum.intValue() > maximum.intValue()) {
	    throw new DomainException("error.minimum.greater.than.maximum");
	}
    }

    @Override
    public CourseGroup getDegreeModuleToApplyRule() {
	return (CourseGroup) super.getDegreeModuleToApplyRule();
    }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
	List<GenericPair<Object, Boolean>> labelList = new ArrayList<GenericPair<Object, Boolean>>();

	labelList.add(new GenericPair<Object, Boolean>("label.modulesSelection", true));
	labelList.add(new GenericPair<Object, Boolean>(": ", false));
	if (getMinimumLimit().intValue() == getMaximumLimit().intValue()) {
	    labelList.add(new GenericPair<Object, Boolean>("label.choose", true));
	    labelList.add(new GenericPair<Object, Boolean>(" ", false));
	    labelList.add(new GenericPair<Object, Boolean>(getMinimumLimit(), false));
	    labelList.add(new GenericPair<Object, Boolean>(" ", false));
	    if (getMinimumLimit().intValue() == 1) {
		labelList.add(new GenericPair<Object, Boolean>("label.module", true));
	    } else {
		labelList.add(new GenericPair<Object, Boolean>("label.modules", true));
	    }
	} else {
	    labelList.add(new GenericPair<Object, Boolean>("label.chooseFrom", true));
	    labelList.add(new GenericPair<Object, Boolean>(" ", false));
	    labelList.add(new GenericPair<Object, Boolean>(getMinimumLimit(), false));
	    labelList.add(new GenericPair<Object, Boolean>(" ", false));
	    labelList.add(new GenericPair<Object, Boolean>("label.to", true));
	    labelList.add(new GenericPair<Object, Boolean>(" ", false));
	    labelList.add(new GenericPair<Object, Boolean>(getMaximumLimit(), false));
	    labelList.add(new GenericPair<Object, Boolean>(" ", false));
	    labelList.add(new GenericPair<Object, Boolean>("label.modules", true));
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

    public boolean allowNumberOfDegreeModules(final Integer value) {
	return !(value.compareTo(getMinimumLimit()) < 0 || value.compareTo(getMaximumLimit()) > 0);
    }

    public boolean numberOfDegreeModulesExceedMaximum(final Integer numberOfDegreeModules) {
	return numberOfDegreeModules.compareTo(getMaximumLimit()) > 0;
    }
}
