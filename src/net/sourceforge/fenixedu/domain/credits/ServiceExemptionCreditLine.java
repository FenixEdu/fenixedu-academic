/*
 * Created on 7/Mar/2004
 */
package net.sourceforge.fenixedu.domain.credits;

import net.sourceforge.fenixedu.domain.credits.event.CreditsEvent;
import net.sourceforge.fenixedu.util.credits.ServiceExemptionType;

/**
 * @author jpvl
 */
public class ServiceExemptionCreditLine extends DatePeriodBaseCreditLine implements
        IServiceExemptionCreditLine {
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

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.credits.CreditLine#getCreditEventGenerated()
     */
    protected CreditsEvent getCreditEventGenerated() {
        return CreditsEvent.SERVICE_EXEMPTION;
    }

}