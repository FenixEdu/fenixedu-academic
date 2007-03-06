package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PreviousYearsEnrolmentCurricularRule extends PreviousYearsEnrolmentCurricularRule_Base {
    
    private PreviousYearsEnrolmentCurricularRule() {
        super();
    }
    
    public PreviousYearsEnrolmentCurricularRule(final CourseGroup toApplyRule,
            final ExecutionPeriod begin, final ExecutionPeriod end) {
	this();
	checkDegreeModule(toApplyRule);
	init(toApplyRule, null, begin, end, CurricularRuleType.PREVIOUS_YEARS_ENROLMENT);
    }
    
    public PreviousYearsEnrolmentCurricularRule(final CourseGroup toApplyRule, final ExecutionPeriod begin) {
	this(toApplyRule, begin, null);
    }

    private void checkDegreeModule(final CourseGroup courseGroup) {
	if (!courseGroup.isRoot()) {
	    throw new DomainException("error.curricularRules.PreviousYearsEnrolmentCurricularRule.should.be.applied.to.root.CourseGroup");
	}
    }
    
    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {
        return Collections.singletonList(new GenericPair<Object, Boolean>("label.previousYearsEnrolment", true));
    }

    @Override
    protected void removeOwnParameters() {
	// No additional parameters
    }
    
    @Override
    public boolean isVisible() {
        return false;
    }
}
