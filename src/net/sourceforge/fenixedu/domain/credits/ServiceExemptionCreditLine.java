/*
 * Created on 7/Mar/2004
 */
package net.sourceforge.fenixedu.domain.credits;

import net.sourceforge.fenixedu.domain.credits.event.CreditsEvent;

/**
 * @author jpvl
 */
public class ServiceExemptionCreditLine extends ServiceExemptionCreditLine_Base {

    protected CreditsEvent getCreditEventGenerated() {
        return CreditsEvent.SERVICE_EXEMPTION;
    }

}