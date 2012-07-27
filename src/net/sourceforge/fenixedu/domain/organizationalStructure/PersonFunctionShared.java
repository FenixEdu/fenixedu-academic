package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.math.BigDecimal;
import java.math.RoundingMode;

import net.sourceforge.fenixedu.domain.ExecutionInterval;

public class PersonFunctionShared extends PersonFunctionShared_Base {

    public PersonFunctionShared(Party parentParty, Party childParty, SharedFunction sharedfunction,
	    ExecutionInterval executionInterval, BigDecimal percentage) {
	setParentParty(parentParty);
	setChildParty(childParty);
	setAccountabilityType(sharedfunction);
	setOccupationInterval(executionInterval);
	setPercentage(percentage);
	if (getSharedFunction().getCredits() != null) {
	    setCredits(getSharedFunction().getCredits().multiply(percentage).setScale(2, RoundingMode.HALF_UP).doubleValue());
	} else {
	    setCredits(0.0);
	}
    }

    public SharedFunction getSharedFunction() {
	return (SharedFunction) getAccountabilityType();
    }

}
