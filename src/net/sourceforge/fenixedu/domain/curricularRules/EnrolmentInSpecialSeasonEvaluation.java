package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class EnrolmentInSpecialSeasonEvaluation extends CurricularRuleNotPersistent {
    
    private Enrolment toApply;
    
    public EnrolmentInSpecialSeasonEvaluation(final Enrolment enrolment) {
	if (enrolment == null) {
	    throw new DomainException("curricular.rule.invalid.parameters");
	} else {
	    this.toApply = enrolment;
	}
    }
    
    public List<GenericPair<Object, Boolean>> getLabel() {
        return Collections.singletonList(new GenericPair<Object, Boolean>("label.enrolmentInSpecialSeasonEvaluation", true));
    }

    public Enrolment getEnrolment() {
	return toApply;
    }

    public DegreeModule getDegreeModuleToApplyRule() {
	return getEnrolment().getDegreeModule();
    }

    public CourseGroup getContextCourseGroup() {
	return null;
    }

    public CompositeRule getParentCompositeRule() {
	return null;
    }

    public CurricularRuleType getCurricularRuleType() {
	return CurricularRuleType.ENROLMENT_IN_SPECIAL_SEASON_EVALUATION;
    }

    public ExecutionPeriod getBegin() {
	return ExecutionPeriod.readActualExecutionPeriod();
    }

    public ExecutionPeriod getEnd() {
	return null;
    }

}
