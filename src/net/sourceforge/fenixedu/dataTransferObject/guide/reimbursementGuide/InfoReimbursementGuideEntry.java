/*
 * Created on 22/Mar/2004
 *  
 */

package net.sourceforge.fenixedu.dataTransferObject.guide.reimbursementGuide;

import net.sourceforge.fenixedu.dataTransferObject.InfoGuideEntry;
import net.sourceforge.fenixedu.dataTransferObject.InfoObject;
import net.sourceforge.fenixedu.domain.reimbursementGuide.ReimbursementGuideEntry;

/**
 * 
 * This class contains all the information regarding a Reimbursement Guide
 * Entry. <br/>
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */

public class InfoReimbursementGuideEntry extends InfoObject {

    protected Double value;

    protected String justification;

    protected InfoGuideEntry infoGuideEntry;

    protected InfoReimbursementGuide infoReimbursementGuide;

    public InfoReimbursementGuideEntry() {
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

    /**
     * @return Returns the infoReimbursementGuide.
     */
    public InfoReimbursementGuide getInfoReimbursementGuide() {
        return infoReimbursementGuide;
    }

    /**
     * @param infoReimbursementGuide
     *            The infoReimbursementGuide to set.
     */
    public void setInfoReimbursementGuide(InfoReimbursementGuide infoReimbursementGuide) {
        this.infoReimbursementGuide = infoReimbursementGuide;
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

    public void copyFromDomain(ReimbursementGuideEntry reimbursementGuideEntry) {
        super.copyFromDomain(reimbursementGuideEntry);
        if (reimbursementGuideEntry != null) {
            setInfoGuideEntry(InfoGuideEntry.newInfoFromDomain(reimbursementGuideEntry.getGuideEntry()));
            // setInfoReimbursementGuide(null); to avoid circularity
            setJustification(reimbursementGuideEntry.getJustification());
            setValue(reimbursementGuideEntry.getValue());
        }
    }

    public static InfoReimbursementGuideEntry newInfoFromDomain(
            ReimbursementGuideEntry reimbursementGuideEntry) {
        InfoReimbursementGuideEntry infoReimbursementGuideEntry = null;
        if (reimbursementGuideEntry != null) {
            infoReimbursementGuideEntry = new InfoReimbursementGuideEntry();
            infoReimbursementGuideEntry.copyFromDomain(reimbursementGuideEntry);
        }

        return infoReimbursementGuideEntry;
    }

}