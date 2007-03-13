package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
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
    
    public List<GenericPair<Object, Boolean>> getLabel() {
        return Collections.singletonList(new GenericPair<Object, Boolean>("label.improvementOfApprovedEnrolment", true));
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
	return CurricularRuleType.IMPROVEMENT_OF_APPROVED_ENROLMENT;
    }

    public ExecutionPeriod getBegin() {
	return ExecutionPeriod.readActualExecutionPeriod();
    }

    public ExecutionPeriod getEnd() {
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
    
}
