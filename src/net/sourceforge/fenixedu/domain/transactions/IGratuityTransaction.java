package net.sourceforge.fenixedu.domain.transactions;

import net.sourceforge.fenixedu.domain.IGratuitySituation;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public interface IGratuityTransaction extends IPaymentTransaction {

    /**
     * @return Returns the gratuitySituation.
     */
    public abstract IGratuitySituation getGratuitySituation();

    /**
     * @param gratuitySituation
     *            The gratuitySituation to set.
     */
    public abstract void setGratuitySituation(IGratuitySituation gratuitySituation);

}