package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PreviousYearsEnrolmentCurricularRule extends PreviousYearsEnrolmentCurricularRule_Base {
    
    private PreviousYearsEnrolmentCurricularRule() {
        super();
    }
    
    public PreviousYearsEnrolmentCurricularRule(final DegreeModule degreeModuleToApplyRule,
            final ExecutionPeriod begin, final ExecutionPeriod end) {
	this();
	checkDegreeModule(degreeModuleToApplyRule);
	init(degreeModuleToApplyRule, null, begin, end, CurricularRuleType.PREVIOUS_YEARS_ENROLMENT_CURRICULAR_RULE);
    }
    
    public PreviousYearsEnrolmentCurricularRule(final DegreeModule degreeModuleToApplyRule, final ExecutionPeriod begin) {
	this(degreeModuleToApplyRule, begin, null);
    }

    private void checkDegreeModule(final DegreeModule degreeModule) {
	if (!degreeModule.isRoot()) {
	    throw new DomainException("error.curricularRules.PreviousYearsEnrolmentCurricularRule.should.be.applied.to.root.degreeModule");
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
    
}
