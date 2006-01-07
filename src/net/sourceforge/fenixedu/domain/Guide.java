package net.sourceforge.fenixedu.domain;

import java.util.Iterator;

import net.sourceforge.fenixedu.util.NumberUtils;
import net.sourceforge.fenixedu.util.State;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class Guide extends Guide_Base {

    public String toString() {
        String result = "[GUIDE";
        result += ", codInt=" + getIdInternal();
        result += ", number=" + getNumber();
        result += ", year=" + getYear();
        result += ", contributor=" + getContributor();
        result += ", total=" + getTotal();
        result += ", remarks=" + getRemarks();
        result += ", guide Requester=" + getGuideRequester();
        result += ", execution Degree=" + getExecutionDegree();
        result += ", payment Type=" + getPaymentType();
        result += ", creation Date=" + getCreationDate();
        result += ", version=" + getVersion();
        result += ", payment Date=" + getPaymentDate();
        result += "]";
        return result;
    }

    public GuideSituation getActiveSituation() {
        if (this.getGuideSituations() != null) {
            Iterator iterator = this.getGuideSituations().iterator();
            while (iterator.hasNext()) {
                GuideSituation guideSituation = (GuideSituation) iterator.next();
                if (guideSituation.getState().equals(new State(State.ACTIVE))) {
                    return guideSituation;
                }
            }
        }
        return null;
    }
    
    public void updateTotalValue(){
        
        double total = 0;

        for (GuideEntry guideEntry : getGuideEntries()) {
            total += guideEntry.getPrice() * guideEntry.getQuantity();
        }
        
        setTotal(NumberUtils.formatNumber(total, 2));
        
    }

}
