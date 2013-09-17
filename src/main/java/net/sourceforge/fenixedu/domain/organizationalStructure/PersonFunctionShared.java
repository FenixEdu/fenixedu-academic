package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.math.BigDecimal;
import java.math.RoundingMode;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class PersonFunctionShared extends PersonFunctionShared_Base {
    private static final BigDecimal MAX_PERCENTAGE = new BigDecimal(100);

    public PersonFunctionShared(Party parentParty, Party childParty, SharedFunction sharedfunction,
            ExecutionInterval executionInterval, BigDecimal percentage) {
        setParentParty(parentParty);
        setChildParty(childParty);
        setAccountabilityType(sharedfunction);
        setOccupationInterval(executionInterval);
        setPercentage(percentage);
    }

    public SharedFunction getSharedFunction() {
        return (SharedFunction) getAccountabilityType();
    }

    @Override
    public boolean isPersonFunctionShared() {
        return true;
    }

    @Override
    public void setPercentage(BigDecimal percentage) {
        if (percentage == null || percentage.compareTo(BigDecimal.ZERO) < 0) {
            percentage = BigDecimal.ZERO;
        }
        if (percentage.compareTo(MAX_PERCENTAGE) > 0) {
            throw new DomainException("label.percentage.exceededMaxAllowed");
        }
        super.setPercentage(percentage);
        recalculateCredits();
    }

    public void recalculateCredits() {
        BigDecimal percentage = getPercentage();
        if (percentage != null && getSharedFunction().getCredits() != null) {
            setCredits(getSharedFunction().getCredits().multiply(percentage.divide(MAX_PERCENTAGE))
                    .setScale(2, RoundingMode.HALF_UP).doubleValue());
        } else {
            setCredits(0.0);
        }
    }

    @Deprecated
    public boolean hasPercentage() {
        return getPercentage() != null;
    }

}
