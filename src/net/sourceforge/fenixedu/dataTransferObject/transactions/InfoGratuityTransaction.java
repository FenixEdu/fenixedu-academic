package net.sourceforge.fenixedu.dataTransferObject.transactions;

import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.domain.transactions.GratuityTransaction;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class InfoGratuityTransaction extends InfoPaymentTransaction {

    private InfoGratuitySituation infoGratuitySituation;

    protected void copyFromDomain(GratuityTransaction gratuityTransaction) {
	super.copyFromDomain(gratuityTransaction);
	this.infoGratuitySituation = InfoGratuitySituation.newInfoFromDomain(gratuityTransaction.getGratuitySituation());

    }

    public static InfoGratuityTransaction newInfoFromDomain(GratuityTransaction gratuityTransaction) {

	if (gratuityTransaction == null) {
	    return null;
	}

	InfoGratuityTransaction infoGratuityTransaction = new InfoGratuityTransaction();
	infoGratuityTransaction.copyFromDomain(gratuityTransaction);

	return infoGratuityTransaction;
    }

    /**
     * @return Returns the infoGratuitySituation.
     */
    public InfoGratuitySituation getInfoGratuitySituation() {
	return infoGratuitySituation;
    }

    /**
     * @param infoGratuitySituation
     *            The infoGratuitySituation to set.
     */
    public void setInfoGratuitySituation(InfoGratuitySituation infoGratuitySituation) {
	this.infoGratuitySituation = infoGratuitySituation;
    }
}