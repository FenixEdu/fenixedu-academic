package Util.gratuity;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import Util.FenixValuedEnum;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public final class SibsPaymentType extends FenixValuedEnum {

    public static final int SPECIALIZATION_GRATUTITY_TOTAL_TYPE = 30;

    public static final int SPECIALIZATION_GRATUTITY_FIRST_PHASE_TYPE = 31;

    public static final int SPECIALIZATION_GRATUTITY_SECOND_PHASE_TYPE = 32;

    public static final int MASTER_DEGREE_GRATUTITY_TOTAL_TYPE = 40;

    public static final int MASTER_DEGREE_GRATUTITY_FIRST_PHASE_TYPE = 41;

    public static final int MASTER_DEGREE_GRATUTITY_SECOND_PHASE_TYPE = 42;

    public static final int INSURANCE_TYPE = 20;

    public static final SibsPaymentType SPECIALIZATION_GRATUTITY_TOTAL = new SibsPaymentType(
            "sibsPaymentType.specialization.gratuity.total",
            SibsPaymentType.SPECIALIZATION_GRATUTITY_TOTAL_TYPE);

    public static final SibsPaymentType SPECIALIZATION_GRATUTITY_FIRST_PHASE = new SibsPaymentType(
            "sibsPaymentType.specialization.gratuity.first.phase",
            SibsPaymentType.SPECIALIZATION_GRATUTITY_FIRST_PHASE_TYPE);

    public static final SibsPaymentType SPECIALIZATION_GRATUTITY_SECOND_PHASE = new SibsPaymentType(
            "sibsPaymentType.specialization.gratuity.second.phase",
            SibsPaymentType.SPECIALIZATION_GRATUTITY_SECOND_PHASE_TYPE);

    public static final SibsPaymentType MASTER_DEGREE_GRATUTITY_TOTAL = new SibsPaymentType(
            "sibsPaymentType.masterDegree.gratuity.total",
            SibsPaymentType.MASTER_DEGREE_GRATUTITY_TOTAL_TYPE);

    public static final SibsPaymentType MASTER_DEGREE_GRATUTITY_FIRST_PHASE = new SibsPaymentType(
            "sibsPaymentType.masterDegree.gratuity.first.phase",
            SibsPaymentType.MASTER_DEGREE_GRATUTITY_FIRST_PHASE_TYPE);

    public static final SibsPaymentType MASTER_DEGREE_GRATUTITY_SECOND_PHASE = new SibsPaymentType(
            "sibsPaymentType.masterDegree.gratuity.second.phase",
            SibsPaymentType.MASTER_DEGREE_GRATUTITY_SECOND_PHASE_TYPE);

    public static final SibsPaymentType INSURANCE = new SibsPaymentType("sibsPaymentType.insurance",
            SibsPaymentType.INSURANCE_TYPE);

    /**
     * @param name
     * @param value
     */
    private SibsPaymentType(String name, int value) {
        super(name, value);
    }

    public static SibsPaymentType getEnum(String name) {
        return (SibsPaymentType) getEnum(SibsPaymentType.class, name);
    }

    public static SibsPaymentType getEnum(int value) {
        return (SibsPaymentType) getEnum(SibsPaymentType.class, value);
    }

    public static Map getEnumMap() {
        return getEnumMap(SibsPaymentType.class);
    }

    public static List getEnumList() {
        return getEnumList(SibsPaymentType.class);
    }

    public static Iterator iterator() {
        return iterator(SibsPaymentType.class);
    }

    public String toString() {
        String result = "Sibs Payment Type:\n";
        result += "\n  - Sibs Payment Type : " + this.getName();

        return result;
    }

}