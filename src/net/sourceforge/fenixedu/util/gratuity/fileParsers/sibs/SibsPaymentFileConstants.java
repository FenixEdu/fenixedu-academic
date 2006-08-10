/*
 * Created on Apr 26, 2004
 *
 */
package net.sourceforge.fenixedu.util.gratuity.fileParsers.sibs;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class SibsPaymentFileConstants {

    // Type of record field
    public static final int FIELD_TYPE_OF_RECORD_BEGIN_INDEX = 0;

    public static final int FIELD_TYPE_OF_RECORD_END_INDEX = 1;

    // Date field and components
    public static final int FIELD_TRANSACTION_DATE_BEGIN_INDEX = 15;

    public static final int FIELD_TRANSACTION_DATE_END_INDEX = 27;

    // Payed value field
    public static final int FIELD_PAYED_VALUE_BEGIN_INDEX = 27;

    public static final int FIELD_PAYED_VALUE_END_INDEX = 37;

    // Reference field
    public static final int FIELD_REFERENCE_BEGIN_INDEX = 74;

    public static final int FIELD_REFERENCE_END_INDEX = 83;

    // Year inside reference field
    public static final int FIELD_REFERENCE_YEAR_BEGIN_INDEX = 0;

    public static final int FIELD_REFERENCE_YEAR_END_INDEX = 2;

    public static final int FIELD_REFERENCE_YEAR_MAX_SIZE = 2;

    // Registration number inside reference field
    public static final int FIELD_REFERENCE_STUDENT_NUMBER_BEGIN_INDEX = 2;

    public static final int FIELD_REFERENCE_STUDENT_NUMBER_END_INDEX = 7;

    // Gratuity type inside reference field
    public static final int FIELD_REFERENCE_PAYMENT_CODE_BEGIN_INDEX = 7;

    public static final int FIELD_REFERENCE_PAYMENT_CODE_END_INDEX = 9;

    // Codes for each entry in file
    public static final int HEADER_RECORD_CODE = 0;

    //NOTE: This is the code actually used by SIBS.
    //The number provided by SIBS user manual its not up-to-date
    public static final int DETAIL_RECORD_CODE = 2;

    public static final int FOOTER_RECORD_CODE = 9;

}