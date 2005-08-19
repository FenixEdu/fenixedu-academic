/*
 * Created on 29/Fev/2004
 */
package net.sourceforge.fenixedu.domain.credits;

import net.sourceforge.fenixedu.domain.credits.event.CreditsEvent;

/**
 * @author jpvl
 */
public abstract class CreditLine extends CreditLine_Base {

    protected abstract CreditsEvent getCreditEventGenerated();

}
