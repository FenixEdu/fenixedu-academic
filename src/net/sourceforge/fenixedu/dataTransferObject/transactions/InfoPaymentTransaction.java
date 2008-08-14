package net.sourceforge.fenixedu.dataTransferObject.transactions;

import net.sourceforge.fenixedu.dataTransferObject.InfoGuideEntry;
import net.sourceforge.fenixedu.domain.transactions.GratuityTransaction;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;
import net.sourceforge.fenixedu.domain.transactions.PaymentTransaction;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public abstract class InfoPaymentTransaction extends InfoTransaction {

    private InfoGuideEntry infoGuideEntry;

    public InfoPaymentTransaction() {

    }

    public static InfoPaymentTransaction newInfoFromDomain(PaymentTransaction paymentTransaction) {

	if (paymentTransaction == null) {
	    return null;
	}

	InfoPaymentTransaction infoPaymentTransaction = null;

	if (paymentTransaction instanceof GratuityTransaction) {

	    infoPaymentTransaction = InfoGratuityTransaction.newInfoFromDomain((GratuityTransaction) paymentTransaction);

	} else if (paymentTransaction instanceof InsuranceTransaction) {

	    infoPaymentTransaction = InfoInsuranceTransaction.newInfoFromDomain((InsuranceTransaction) paymentTransaction);

	}

	return infoPaymentTransaction;
    }

    protected void copyFromDomain(PaymentTransaction paymentTransaction) {

	super.copyFromDomain(paymentTransaction);

	InfoGuideEntry infoGuideEntry = null;
	if (paymentTransaction.getGuideEntry() != null) {
	    infoGuideEntry = InfoGuideEntry.newInfoFromDomain(paymentTransaction.getGuideEntry());
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