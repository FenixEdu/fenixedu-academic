package Dominio.transactions;

import Dominio.IGuideEntry;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public abstract class PaymentTransaction extends Transaction {

    private Integer keyGuideEntry;

    private IGuideEntry guideEntry;

    /**
     * @return Returns the guideEntry.
     */
    public IGuideEntry getGuideEntry() {
        return guideEntry;
    }

    /**
     * @param guideEntry
     *            The guideEntry to set.
     */
    public void setGuideEntry(IGuideEntry guideEntry) {
        this.guideEntry = guideEntry;
    }

    /**
     * @return Returns the keyGuideEntry.
     */
    public Integer getKeyGuideEntry() {
        return keyGuideEntry;
    }

    /**
     * @param keyGuideEntry
     *            The keyGuideEntry to set.
     */
    public void setKeyGuideEntry(Integer keyGuideEntry) {
        this.keyGuideEntry = keyGuideEntry;
    }
}