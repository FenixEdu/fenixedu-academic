package pt.ist.utl.fenix.utils;

import net.sourceforge.fenixedu.domain.gratuity.SibsPaymentType;

public class SibsPaymentCodeFactory {

    public static int getCode(SibsPaymentType sibsPaymentType) {

        switch (sibsPaymentType) {

        case SPECIALIZATION_GRATUTITY_TOTAL:
            return 30;

        case SPECIALIZATION_GRATUTITY_FIRST_PHASE:
            return 31;

        case SPECIALIZATION_GRATUTITY_SECOND_PHASE:
            return 32;

        case MASTER_DEGREE_GRATUTITY_TOTAL:
            return 40;

        case MASTER_DEGREE_GRATUTITY_FIRST_PHASE:
            return 41;

        case MASTER_DEGREE_GRATUTITY_SECOND_PHASE:
            return 42;

        case INSURANCE:
            return 60;

        default:
            return 0;

        }

    }

    public static SibsPaymentType getPaymentType(int sibsPaymentCode) {

        switch (sibsPaymentCode) {

        case 30:
            return SibsPaymentType.SPECIALIZATION_GRATUTITY_TOTAL;

        case 31:
            return SibsPaymentType.SPECIALIZATION_GRATUTITY_FIRST_PHASE;

        case 32:
            return SibsPaymentType.SPECIALIZATION_GRATUTITY_SECOND_PHASE;

        case 40:
            return SibsPaymentType.MASTER_DEGREE_GRATUTITY_TOTAL;

        case 41:
            return SibsPaymentType.MASTER_DEGREE_GRATUTITY_FIRST_PHASE;

        case 42:
            return SibsPaymentType.MASTER_DEGREE_GRATUTITY_SECOND_PHASE;

        case 60:
            return SibsPaymentType.INSURANCE;

        default:
            return null;

        }

    }
}
