/*
 * Created on Apr 26, 2004
 *
 */
package net.sourceforge.fenixedu.util.gratuity.fileParsers.sibs;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.gratuity.masterDegree.InvalidSibsPaymentFileFormatServiceException;
import net.sourceforge.fenixedu.domain.gratuity.SibsPaymentStatus;
import net.sourceforge.fenixedu.domain.gratuity.SibsPaymentType;
import net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFile;
import net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFileEntry;
import pt.ist.utl.fenix.utils.SibsPaymentCodeFactory;


/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 *  
 */
public class SibsPaymentFileUtils {

    /**
     * This method parses and build sibs payment files, sorting the file entries
     * by student number
     * 
     * @param filename
     * @param fileEntries
     * @return @throws
     *         InvalidSibsPaymentFileFormatServiceException
     */

    public static SibsPaymentFile buildPaymentFile(String filename, List fileEntries)
            throws InvalidSibsPaymentFileFormatServiceException {

        SibsPaymentFile sibsFile = new SibsPaymentFile(filename);
        List sibsFileEntries = new ArrayList();
        String line = null;

        try {
            for (Iterator iter = fileEntries.iterator(); iter.hasNext();) {
                line = (String) iter.next();

                // parse type or record
                String typeOfRecordString = line.substring(
                        SibsPaymentFileConstants.FIELD_TYPE_OF_RECORD_BEGIN_INDEX,
                        SibsPaymentFileConstants.FIELD_TYPE_OF_RECORD_END_INDEX);

                if (Integer.parseInt(typeOfRecordString) == SibsPaymentFileConstants.DETAIL_RECORD_CODE) {

                    // parse transaction date
                    String transactionDateString = line.substring(
                            SibsPaymentFileConstants.FIELD_TRANSACTION_DATE_BEGIN_INDEX,
                            SibsPaymentFileConstants.FIELD_TRANSACTION_DATE_END_INDEX);

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddkkmm");
                    Date transactionDate = simpleDateFormat.parse(transactionDateString);

                    // parse payed value
                    String valueString = line.substring(
                            SibsPaymentFileConstants.FIELD_PAYED_VALUE_BEGIN_INDEX,
                            SibsPaymentFileConstants.FIELD_PAYED_VALUE_END_INDEX);

                    double parsedValue = (Double.parseDouble(valueString)) / 100; // 2
                    // decimal
                    // digits
                    Double value = new Double(parsedValue);

                    // parse reference
                    String sibsReference = line.substring(
                            SibsPaymentFileConstants.FIELD_REFERENCE_BEGIN_INDEX,
                            SibsPaymentFileConstants.FIELD_REFERENCE_END_INDEX);

                    // retrieve year from reference
                    String yearString = sibsReference.substring(
                            SibsPaymentFileConstants.FIELD_REFERENCE_YEAR_BEGIN_INDEX,
                            SibsPaymentFileConstants.FIELD_REFERENCE_YEAR_END_INDEX);

                    String currentYearString = (new SimpleDateFormat("yyyy")).format(new Date());

                    String fullYearString = currentYearString.substring(0, currentYearString.length()
                            - yearString.length());
                    fullYearString += yearString;

                    Integer year = Integer.valueOf(fullYearString);

                    //retrieve student number from reference
                    String studentNumberString = sibsReference.substring(
                            SibsPaymentFileConstants.FIELD_REFERENCE_STUDENT_NUMBER_BEGIN_INDEX,
                            SibsPaymentFileConstants.FIELD_REFERENCE_STUDENT_NUMBER_END_INDEX);
                    Integer studentNumber = Integer.valueOf(studentNumberString);

                    //retrieve payment type from reference
                    String paymentTypeString = sibsReference.substring(
                            SibsPaymentFileConstants.FIELD_REFERENCE_PAYMENT_CODE_BEGIN_INDEX,
                            SibsPaymentFileConstants.FIELD_REFERENCE_PAYMENT_CODE_END_INDEX);

                    SibsPaymentType paymentType = SibsPaymentCodeFactory.getPaymentType(new Integer(paymentTypeString));

                    sibsFileEntries.add(new SibsPaymentFileEntry(year, studentNumber, paymentType,
                            new Timestamp(transactionDate.getTime()), value, sibsFile,
                            SibsPaymentStatus.NOT_PROCESSED_PAYMENT));

                }
            }
        } catch (Exception e) {
            throw new InvalidSibsPaymentFileFormatServiceException(
                    "error.exception.masterDegree.invalidSibsFileFormat", e);

        }

        Collections.sort(sibsFileEntries, new Comparator() {

            public int compare(Object leftObject, Object rightObject) {
                int leftStudentNumber = ((SibsPaymentFileEntry) leftObject).getStudentNumber()
                        .intValue();
                int rightStudentNumber = ((SibsPaymentFileEntry) rightObject).getStudentNumber()
                        .intValue();
                if (leftStudentNumber > rightStudentNumber) {
                    return 1;
                } else if (leftStudentNumber == rightStudentNumber) {
                    return 0;
                } else {
                    return -1;
                }

            }

        });

        sibsFile.getSibsPaymentFileEntries().addAll(sibsFileEntries);

        return sibsFile;
    }

}
