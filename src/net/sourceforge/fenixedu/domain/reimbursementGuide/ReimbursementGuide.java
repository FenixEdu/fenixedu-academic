/*
 * Created on 12/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.domain.reimbursementGuide;

import java.util.Calendar;

import net.sourceforge.fenixedu.util.State;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * This class contains all the information regarding a Reimbursement Guide. <br>
 * 
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a>
 */
public class ReimbursementGuide extends ReimbursementGuide_Base {

    /**
     * @return
     */
    public Calendar getCreationDate() {
        Calendar result = Calendar.getInstance();
        result.setTime(this.getCreation());
        return result;
    }

    /**
     * @param creationDate
     */
    public void setCreationDate(Calendar creationDate) {
        this.setCreation(creationDate.getTime());
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + ": ";
        result += " idInternal=" + this.getIdInternal();
        result += ", number=" + this.getNumber();
        result += ", creation Date=" + this.getCreationDate();
        result += "]";
        return result;
    }

    public boolean equals(Object obj) {
        boolean result = false;
        if (obj instanceof IReimbursementGuide) {
            IReimbursementGuide reimbursementGuide = (IReimbursementGuide) obj;

            if ((getNumber() == null && reimbursementGuide.getNumber() == null)
                    || (getNumber().equals(reimbursementGuide.getNumber()))) {
                result = true;
            }
        }

        return result;
    }

    public IReimbursementGuideSituation getActiveReimbursementGuideSituation() {
        return (IReimbursementGuideSituation) CollectionUtils.find(getReimbursementGuideSituations(),
                new Predicate() {
                    public boolean evaluate(Object obj) {
                        IReimbursementGuideSituation situation = (IReimbursementGuideSituation) obj;
                        return situation.getState().getState().intValue() == State.ACTIVE;
                    }
                });
    }

}
