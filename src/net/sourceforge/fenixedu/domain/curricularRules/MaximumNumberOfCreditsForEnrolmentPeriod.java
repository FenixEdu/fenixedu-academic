package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class MaximumNumberOfCreditsForEnrolmentPeriod extends MaximumNumberOfCreditsForEnrolmentPeriod_Base {
    
    static final public double MAXIMUM_NUMBER_OF_CREDITS = 40.0;
    
    private MaximumNumberOfCreditsForEnrolmentPeriod() {
        super();
    }
    
    public MaximumNumberOfCreditsForEnrolmentPeriod(final DegreeModule degreeModuleToApplyRule,
	    final ExecutionPeriod begin, final ExecutionPeriod end) {
	
        super();
        checkDegreeModule(degreeModuleToApplyRule);
        init(degreeModuleToApplyRule, null, begin, end, CurricularRuleType.MAXIMUM_NUMBER_OF_CREDITS_FOR_ENROLMENT_PERIOD);
    }
    
    private void checkDegreeModule(final DegreeModule degreeModule) {
	if (!degreeModule.isRoot()) {
	    throw new DomainException("error.curricularRules.MaximumNumberOfCreditsForEnrolmentPeriod.should.be.applied.to.root.degreeModule");
	}
    }
    
    public MaximumNumberOfCreditsForEnrolmentPeriod(final DegreeModule degreeModuleToApplyRule, final ExecutionPeriod begin) {
	this(degreeModuleToApplyRule, begin, null);
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
    
}
