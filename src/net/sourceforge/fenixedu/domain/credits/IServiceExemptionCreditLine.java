/*
 * Created on 7/Mar/2004
 */
package net.sourceforge.fenixedu.domain.credits;

import net.sourceforge.fenixedu.util.credits.ServiceExemptionType;

/**
 * @author jpvl
 */
public interface IServiceExemptionCreditLine extends IDatePeriodBasedCreditLine {
    /**
     * @return Returns the type.
     */
    public abstract ServiceExemptionType getType();

    /**
     * @param type
     *            The type to set.
     */
    public abstract void setType(ServiceExemptionType type);
}