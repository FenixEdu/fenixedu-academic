/*
 * Created on 18/Mar/2004
 *  
 */

package Dominio.reimbursementGuide;

import Dominio.DomainObject;
import Dominio.IGuideEntry;

/**
 * 
 * This class contains all the information regarding a Reimbursement Guide
 * Entry. <br>
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */

public class ReimbursementGuideEntry extends DomainObject implements IReimbursementGuideEntry {

    private Integer keyGuideEntry;

    private Integer keyReimbursementGuide;

    protected Double value;

    protected String justification;

    protected IGuideEntry guideEntry;

    protected IReimbursementGuide reimbursementGuide;

    public ReimbursementGuideEntry() {
    }

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
     * @return Returns the justification.
     */
    public String getJustification() {
        return justification;
    }

    /**
     * @param justification
     *            The justification to set.
     */
    public void setJustification(String justification) {
        this.justification = justification;
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

    /**
     * @return Returns the keyReimbursementGuide.
     */
    public Integer getKeyReimbursementGuide() {
        return keyReimbursementGuide;
    }

    /**
     * @param keyReimbursementGuide
     *            The keyReimbursementGuide to set.
     */
    public void setKeyReimbursementGuide(Integer keyReimbursementGuide) {
        this.keyReimbursementGuide = keyReimbursementGuide;
    }

    /**
     * @return Returns the reimbursementGuide.
     */
    public IReimbursementGuide getReimbursementGuide() {
        return reimbursementGuide;
    }

    /**
     * @param reimbursementGuide
     *            The reimbursementGuide to set.
     */
    public void setReimbursementGuide(IReimbursementGuide reimbursementGuide) {
        this.reimbursementGuide = reimbursementGuide;
    }

    /**
     * @return Returns the value.
     */
    public Double getValue() {
        return value;
    }

    /**
     * @param value
     *            The value to set.
     */
    public void setValue(Double value) {
        this.value = value;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IReimbursementGuideEntry) {
            IReimbursementGuideEntry reimbursementGuideEntry = (IReimbursementGuideEntry) obj;

            if ((this.getIdInternal() == null && reimbursementGuideEntry.getGuideEntry() == null)
                    || (this.getIdInternal().equals(reimbursementGuideEntry.getIdInternal()))) {
                result = true;
            }
        }

        return result;
    }

}