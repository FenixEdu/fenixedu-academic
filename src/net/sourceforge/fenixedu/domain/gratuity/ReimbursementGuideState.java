/*
 * Created on 13/Nov/2003
 *
 */
package net.sourceforge.fenixedu.domain.gratuity;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> 13/Nov/2003
 *  
 */
public enum ReimbursementGuideState {

    ISSUED,

    APPROVED,

    PAYED,

    ANNULLED;

    public String getName() {
        return name();
    }

}