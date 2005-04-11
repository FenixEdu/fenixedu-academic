package net.sourceforge.fenixedu.domain.gratuity;


/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public enum SibsPaymentStatus {

    NOT_PROCESSED_PAYMENT,

    PROCESSED_PAYMENT,

    DUPLICATE_GRATUITY_PAYMENT,

    DUPLICATE_INSURANCE_PAYMENT,

    INVALID_EXECUTION_YEAR,

    INVALID_EXECUTION_DEGREE,

    INVALID_INSURANCE_VALUE,

    UNABLE_TO_DETERMINE_STUDENT_CURRICULAR_PLAN;

}