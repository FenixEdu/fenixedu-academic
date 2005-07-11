package net.sourceforge.fenixedu.dataTransferObject.transactions;

import net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide.InfoReimbursementGuideEntry;
import net.sourceforge.fenixedu.domain.transactions.IReimbursementTransaction;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class InfoReimbursementTransaction extends InfoTransaction {

    private InfoReimbursementGuideEntry infoReimbursementGuideEntry;

    public void copyFromDomain(IReimbursementTransaction reimbursementTransaction) {

        super.copyFromDomain(reimbursementTransaction);

        this.infoReimbursementGuideEntry = InfoReimbursementGuideEntry
                .newInfoFromDomain(reimbursementTransaction.getReimbursementGuideEntry());
    }

    public static InfoReimbursementTransaction newInfoFromDomain(
            IReimbursementTransaction reimbursementTransaction) {

        if (reimbursementTransaction == null) {
            return null;
        }

        InfoReimbursementTransaction infoReimbursementTransaction = new InfoReimbursementTransaction();
        infoReimbursementTransaction.copyFromDomain(reimbursementTransaction);

        return infoReimbursementTransaction;
    }

    /**
     * @return Returns the infoReimbursementGuideEntry.
     */
    public InfoReimbursementGuideEntry getInfoReimbursementGuideEntry() {
        return infoReimbursementGuideEntry;
    }

    /**
     * @param infoReimbursementGuideEntry
     *            The infoReimbursementGuideEntry to set.
     */
    public void setInfoReimbursementGuideEntry(InfoReimbursementGuideEntry infoReimbursementGuideEntry) {
        this.infoReimbursementGuideEntry = infoReimbursementGuideEntry;
    }
}