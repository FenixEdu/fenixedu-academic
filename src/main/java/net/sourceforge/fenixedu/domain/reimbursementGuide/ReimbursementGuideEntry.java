/*
 * Created on 18/Mar/2004
 *  
 */

package net.sourceforge.fenixedu.domain.reimbursementGuide;

import java.math.BigDecimal;

import pt.ist.bennu.core.domain.Bennu;

/**
 * This class contains all the information regarding a Reimbursement Guide
 * Entry. <br/>
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 */

public class ReimbursementGuideEntry extends ReimbursementGuideEntry_Base {

    public ReimbursementGuideEntry() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public void delete() {
        setGuideEntry(null);
        setReimbursementGuide(null);
        setReimbursementTransaction(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public Double getValue() {
        return getValueBigDecimal().doubleValue();
    }

    @Deprecated
    public void setValue(Double value) {
        setValueBigDecimal(BigDecimal.valueOf(value));
    }

    @Deprecated
    public boolean hasJustification() {
        return getJustification() != null;
    }

    @Deprecated
    public boolean hasReimbursementTransaction() {
        return getReimbursementTransaction() != null;
    }

    @Deprecated
    public boolean hasReimbursementGuide() {
        return getReimbursementGuide() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasValueBigDecimal() {
        return getValueBigDecimal() != null;
    }

    @Deprecated
    public boolean hasGuideEntry() {
        return getGuideEntry() != null;
    }

}
