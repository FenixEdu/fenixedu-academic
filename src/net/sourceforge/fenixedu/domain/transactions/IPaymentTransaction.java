package net.sourceforge.fenixedu.domain.transactions;

import net.sourceforge.fenixedu.domain.IGuideEntry;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public interface IPaymentTransaction extends ITransaction {

    /**
     * @return Returns the guideEntry.
     */
    public abstract IGuideEntry getGuideEntry();

    /**
     * @param guideEntry
     *            The guideEntry to set.
     */
    public abstract void setGuideEntry(IGuideEntry guideEntry);
}