/*
 * Created on 29/Fev/2004
 */
package net.sourceforge.fenixedu.domain.credits;

import net.sourceforge.fenixedu.domain.credits.event.CreditsEvent;

import org.fenixedu.bennu.core.domain.Bennu;

/**
 * @author jpvl
 */
public abstract class CreditLine extends CreditLine_Base implements
        net.sourceforge.fenixedu.domain.credits.event.ICreditsEventOriginator {

    public CreditLine() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    protected abstract CreditsEvent getCreditEventGenerated();

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

}
