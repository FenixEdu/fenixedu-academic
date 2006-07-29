/*
 * Created on 12/Nov/2003
 *  
 */
package net.sourceforge.fenixedu.domain.reimbursementGuide;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * This class contains all the information regarding a Reimbursement Guide. <br/>
 * 
 * @author <a href="mailto:joao.mota@ist.utl.pt">Jo�o Mota </a>
 */
public class ReimbursementGuide extends ReimbursementGuide_Base {

    final static Comparator<ReimbursementGuide> NUMBER_COMPARATOR = new BeanComparator("number");

    public ReimbursementGuide() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

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

    public ReimbursementGuideSituation getActiveReimbursementGuideSituation() {
        return (ReimbursementGuideSituation) CollectionUtils.find(getReimbursementGuideSituations(),
                new Predicate() {
                    public boolean evaluate(Object obj) {
                        ReimbursementGuideSituation situation = (ReimbursementGuideSituation) obj;
                        return situation.getState().getState().equals(State.ACTIVE);
                    }
                });
    }

    public static Integer generateReimbursementGuideNumber() {
        List<ReimbursementGuide> reimbursementGuides = RootDomainObject.getInstance()
                .getReimbursementGuides();

        return (reimbursementGuides.isEmpty()) ? Integer.valueOf(1) : Collections.max(
                reimbursementGuides, NUMBER_COMPARATOR).getNumber() + 1;
    }
}
