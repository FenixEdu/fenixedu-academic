package DataBeans.transactions;

import DataBeans.InfoGuideEntry;
import DataBeans.util.Cloner;
import Dominio.transactions.IGratuityTransaction;
import Dominio.transactions.IInsuranceTransaction;
import Dominio.transactions.IPaymentTransaction;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public abstract class InfoPaymentTransaction extends InfoTransaction {

    private InfoGuideEntry infoGuideEntry;

    public InfoPaymentTransaction() {

    }

    public static InfoPaymentTransaction newInfoFromDomain(IPaymentTransaction paymentTransaction) {

        if (paymentTransaction == null) {
            return null;
        }

        InfoPaymentTransaction infoPaymentTransaction = null;

        if (paymentTransaction instanceof IGratuityTransaction) {

            infoPaymentTransaction = InfoGratuityTransaction
                    .newInfoFromDomain((IGratuityTransaction) paymentTransaction);

        } else if (paymentTransaction instanceof IInsuranceTransaction) {

            infoPaymentTransaction = InfoInsuranceTransaction
                    .newInfoFromDomain((IInsuranceTransaction) paymentTransaction);

        }

        return infoPaymentTransaction;
    }

    protected void copyFromDomain(IPaymentTransaction paymentTransaction) {

        super.copyFromDomain(paymentTransaction);

        InfoGuideEntry infoGuideEntry = null;
        if (paymentTransaction.getGuideEntry() != null) {
            infoGuideEntry = Cloner.copyIGuideEntry2InfoGuideEntry(paymentTransaction.getGuideEntry());
        }

        this.infoGuideEntry = infoGuideEntry;
    }

    /**
     * @return Returns the infoGuideEntry.
     */
    public InfoGuideEntry getInfoGuideEntry() {
        return infoGuideEntry;
    }

    /**
     * @param infoGuideEntry
     *            The infoGuideEntry to set.
     */
    public void setInfoGuideEntry(InfoGuideEntry infoGuideEntry) {
        this.infoGuideEntry = infoGuideEntry;
    }
}