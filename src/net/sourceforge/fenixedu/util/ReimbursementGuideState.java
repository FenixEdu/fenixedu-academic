/*
 * Created on 13/Nov/2003
 *
 */
package net.sourceforge.fenixedu.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:joao.mota@ist.utl.pt">João Mota </a> 13/Nov/2003
 *  
 */
public class ReimbursementGuideState extends FenixValuedEnum {

    public static final int ISSUED_TYPE = 1;

    public static final int APPROVED_TYPE = 2;

    public static final int PAYED_TYPE = 3;

    public static final int ANNULLED_TYPE = 4;

    public static final ReimbursementGuideState ISSUED = new ReimbursementGuideState("issued",
            ReimbursementGuideState.ISSUED_TYPE);

    public static final ReimbursementGuideState APPROVED = new ReimbursementGuideState("approved",
            ReimbursementGuideState.APPROVED_TYPE);

    public static final ReimbursementGuideState PAYED = new ReimbursementGuideState("payed",
            ReimbursementGuideState.PAYED_TYPE);

    public static final ReimbursementGuideState ANNULLED = new ReimbursementGuideState("annulled",
            ReimbursementGuideState.ANNULLED_TYPE);

    private ReimbursementGuideState(String name, int value) {
        super(name, value);
    }

    public static ReimbursementGuideState getEnum(String reimbursementGuideState) {
        return (ReimbursementGuideState) getEnum(ReimbursementGuideState.class, reimbursementGuideState);
    }

    public static ReimbursementGuideState getEnum(int reimbursementGuideState) {
        return (ReimbursementGuideState) getEnum(ReimbursementGuideState.class, reimbursementGuideState);
    }

    public static Map getEnumMap() {
        return getEnumMap(ReimbursementGuideState.class);
    }

    public static List getEnumList() {
        return getEnumList(ReimbursementGuideState.class);
    }

    public static Iterator iterator() {
        return iterator(ReimbursementGuideState.class);
    }

    public String toString() {
        String result = "Reimbursement Guide State:\n";
        result += "\n  - Reimbursement Guide State : " + this.getName();

        return result;
    }

}