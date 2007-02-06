package net.sourceforge.fenixedu.domain.curricularRules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.GenericPair;

public class MaximumNumberOfCreditsForEnrolmentPeriod extends MaximumNumberOfCreditsForEnrolmentPeriod_Base {
    
    static final public double MAXIMUM_NUMBER_OF_CREDITS = 40.0;
    
    private MaximumNumberOfCreditsForEnrolmentPeriod() {
        super();
        setCurricularRuleType(CurricularRuleType.MAXIMUM_NUMBER_OF_CREDITS_FOR_ENROLMENT_PERIOD);
    }

    @Override
    protected void removeOwnParameters() { }

    @Override
    public List<GenericPair<Object, Boolean>> getLabel() {        
	final List<GenericPair<Object, Boolean>> result = new ArrayList<GenericPair<Object, Boolean>>();

	result.add(new GenericPair<Object, Boolean>("label.maximumNumberOfCreditsForEnrolmentPeriod", true));
	result.add(new GenericPair<Object, Boolean>(": ", false));
	result.add(new GenericPair<Object, Boolean>(MAXIMUM_NUMBER_OF_CREDITS, false));
    
	return result;
    }
    
}
