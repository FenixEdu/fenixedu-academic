/*
 * Created on 7/Mar/2004
 */
package net.sourceforge.fenixedu.dataTransferObject.credits;

import net.sourceforge.fenixedu.util.credits.ServiceExemptionType;

/**
 * @author jpvl
 */
public class InfoServiceExemptionCreditLine extends InfoDatePeriodBaseCreditLine {
    private ServiceExemptionType type;

    /**
     * @return Returns the type.
     */
    public ServiceExemptionType getType() {
        return type;
    }

    /**
     * @param type
     *            The type to set.
     */
    public void setType(ServiceExemptionType type) {
        this.type = type;
    }

}