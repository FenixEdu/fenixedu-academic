/*
 * Created on 13/Nov/2003
 *  
 */

package net.sourceforge.fenixedu.domain.reimbursementGuide;

import java.util.Calendar;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> 13/Nov/2003
 * 
 */
public class ReimbursementGuideSituation extends ReimbursementGuideSituation_Base {

    /**
     * @return
     */
    public Calendar getModificationDate() {
        Calendar result = Calendar.getInstance();
        result.setTime(this.getModification());
        return result;
    }

    /**
     * @param modificationDate
     */
    public void setModificationDate(Calendar modificationDate) {
        this.setModification(modificationDate.getTime());
    }

    /**
     * @return Returns the officialDate.
     */
    public Calendar getOfficialDate() {
        Calendar result = Calendar.getInstance();
        result.setTime(this.getOfficial());
        return result;
    }

    /**
     * @param officialDate
     *            The officialDate to set.
     */
    public void setOfficialDate(Calendar officialDate) {
        this.setModification(officialDate.getTime());
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IReimbursementGuideSituation) {
            IReimbursementGuideSituation reimbursementGuideSituation = (IReimbursementGuideSituation) obj;

            if ((this.getIdInternal() == null && reimbursementGuideSituation.getIdInternal() == null)
                    || (this.getIdInternal().equals(reimbursementGuideSituation.getIdInternal()))) {
                result = true;
            }
        }

        return result;
    }

}
