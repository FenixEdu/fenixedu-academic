package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;

public class MaximumNumberOfEnrolmentsInSpecialSeasonEvaluation extends CurricularRuleNotPersistent {
    
    public List<GenericPair<Object, Boolean>> getLabel() {
        return Collections.singletonList(new GenericPair<Object, Boolean>("label.maximumNumberOfEnrolmentsInSpecialSeasonEvaluation", true));
    }

    public DegreeModule getDegreeModuleToApplyRule() {
	return null;
    }

    public CourseGroup getContextCourseGroup() {
	return null;
    }

    public CompositeRule getParentCompositeRule() {
	return null;
    }

    public CurricularRuleType getCurricularRuleType() {
	return CurricularRuleType.MAXIMUM_NUMBER_OF_ENROLMENTS_IN_SPECIAL_SEASON_EVALUATION;
    }

    public ExecutionPeriod getBegin() {
	return ExecutionPeriod.readActualExecutionPeriod();
    }

    public ExecutionPeriod getEnd() {
	return null;
    }

    @Override
    public boolean equals(Object obj) {
	return obj instanceof MaximumNumberOfEnrolmentsInSpecialSeasonEvaluation;
    }

}
