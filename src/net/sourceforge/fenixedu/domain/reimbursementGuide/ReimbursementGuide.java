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
        if (this.getCreation() != null) {
            Calendar result = Calendar.getInstance();
            result.setTime(this.getCreation());
            return result;
        }
        return null;
    }

    /**
     * @param creationDate
     */
    public void setCreationDate(Calendar creationDate) {
        if (creationDate != null) {
            this.setCreation(creationDate.getTime());    
        } else {
            this.setCreation(null);
        }
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
        if (obj instanceof IReimbursementGuide) {
            final IReimbursementGuide reimbursementGuide = (IReimbursementGuide) obj;
            return this.getIdInternal().equals(reimbursementGuide.getIdInternal());
        }
        return false;
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
