package Dominio.transactions;

import Dominio.reimbursementGuide.IReimbursementGuideEntry;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public interface IReimbursementTransaction extends ITransaction {

    /**
     * @return Returns the reimbursementGuideEntry.
     */
    public abstract IReimbursementGuideEntry getReimbursementGuideEntry();

    /**
     * @param reimbursementGuideEntry
     *            The reimbursementGuideEntry to set.
     */
    public abstract void setReimbursementGuideEntry(IReimbursementGuideEntry reimbursementGuideEntry);
}