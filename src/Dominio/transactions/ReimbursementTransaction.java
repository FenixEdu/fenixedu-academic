package Dominio.transactions;

import Dominio.reimbursementGuide.ReimbursementGuideEntry;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public abstract class ReimbursementTransaction extends Transaction {

    private Integer keyReimbursementGuideEntry;

    private ReimbursementGuideEntry reimbursementGuideEntry;

    /**
     * @return Returns the keyReimbursementGuideEntry.
     */
    public Integer getKeyReimbursementGuideEntry() {
        return keyReimbursementGuideEntry;
    }

    /**
     * @param keyReimbursementGuideEntry
     *            The keyReimbursementGuideEntry to set.
     */
    public void setKeyReimbursementGuideEntry(Integer keyReimbursementGuideEntry) {
        this.keyReimbursementGuideEntry = keyReimbursementGuideEntry;
    }

    /**
     * @return Returns the reimbursementGuideEntry.
     */
    public ReimbursementGuideEntry getReimbursementGuideEntry() {
        return reimbursementGuideEntry;
    }

    /**
     * @param reimbursementGuideEntry
     *            The reimbursementGuideEntry to set.
     */
    public void setReimbursementGuideEntry(ReimbursementGuideEntry reimbursementGuideEntry) {
        this.reimbursementGuideEntry = reimbursementGuideEntry;
    }
}