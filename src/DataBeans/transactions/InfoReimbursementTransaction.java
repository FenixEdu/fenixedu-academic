package DataBeans.transactions;

import DataBeans.guide.reimbursementGuide.InfoReimbursementGuideEntry;
import DataBeans.util.Cloner;
import Dominio.transactions.IReimbursementTransaction;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class InfoReimbursementTransaction extends InfoTransaction {

    private InfoReimbursementGuideEntry infoReimbursementGuideEntry;

    public void copyFromDomain(IReimbursementTransaction reimbursementTransaction) {

        super.copyFromDomain(reimbursementTransaction);

        this.infoReimbursementGuideEntry = Cloner
                .copyIReimbursementGuideEntry2InfoReimbursementGuideEntry(reimbursementTransaction
                        .getReimbursementGuideEntry());

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