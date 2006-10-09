/*
 * Created on 30/Jan/2004
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.generateFiles;

import java.io.BufferedWriter;
import java.io.IOException;

import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants;

/**
 * @author Tânia Pousão
 *  
 */
public class GratuityFile {
    public static final int MAX_LINES_NUMBER = 8;

    public static final int MAX_INT_PAYMENT = 8;

    public static final int MAX_DEC_PAYMENT = 2;

    public static final int MAX_STUDENT_NUMBER = 5;

    public static final double INSURANCE = 2.50;

    public static final String CODE_MASTERDEGREE = "40";

    public static final String CODE_SPECIALIZATION = "30";

    public static final String WITHOUT_ADDRESS = "Sem morada";

    public static final String NOTHING_TO_PAY = "Nada a pagar";

    public static final String INSURANCE_TO_PAY = "Apenas seguro a pagar";

    public static final char SPACE = ' ';

    public static final char ZERO = '0';

    /**
     * Verify if the student hasn't address and nothing to pay letter
     * 
     * @param infoGratuitySituation
     * @return
     */
    protected static boolean valid(InfoGratuitySituation infoGratuitySituation,
            BufferedWriter writerErrors) throws IOException {
        //although the student hasn't a correct address the letter has to be
        // created
        //but the error is registed with the payment's reference
        if (isNullOrEmpty(infoGratuitySituation.getInfoStudentCurricularPlan().getInfoStudent()
                .getInfoPerson().getMorada())
                || isNullOrEmpty(infoGratuitySituation.getInfoStudentCurricularPlan().getInfoStudent()
                        .getInfoPerson().getLocalidade())
                || isNullOrEmpty(infoGratuitySituation.getInfoStudentCurricularPlan().getInfoStudent()
                        .getInfoPerson().getCodigoPostal())
                || isNullOrEmpty(infoGratuitySituation.getInfoStudentCurricularPlan().getInfoStudent()
                        .getInfoPerson().getLocalidadeCodigoPostal())) {
            writerErrors.write(infoGratuitySituation.getInfoStudentCurricularPlan().getInfoStudent()
                    .getNumber()
                    + String.valueOf(SPACE)
                    + WITHOUT_ADDRESS
                    + String.valueOf(SPACE)
                    + buildPaymentReference(infoGratuitySituation));
            writerErrors.newLine();
        }

        //the student hasn't nothing to pay, then the letter is not created
        //and the error is registed
        //but if the student has only the insurance, then the letter is not
        // created
        //and the error is registed
        if (infoGratuitySituation.getRemainingValue().doubleValue() <= 0
                && infoGratuitySituation.getInsurancePayed().equals(SessionConstants.PAYED_INSURANCE)) {
            writerErrors.write(infoGratuitySituation.getInfoStudentCurricularPlan().getInfoStudent()
                    .getNumber()
                    + " " + NOTHING_TO_PAY);
            writerErrors.newLine();
            return false;
        } else if (infoGratuitySituation.getRemainingValue().doubleValue() <= 0
                && !infoGratuitySituation.getInsurancePayed().equals(SessionConstants.PAYED_INSURANCE)) {
            writerErrors.write(infoGratuitySituation.getInfoStudentCurricularPlan().getInfoStudent()
                    .getNumber()
                    + " " + INSURANCE_TO_PAY);
            writerErrors.newLine();
            return true;
        }

        return true;
    }

    /**
     * Verify if the string is null pu empty
     * 
     * @param string
     * @return
     */
    protected static boolean isNullOrEmpty(String string) {
        return (string == null || string.length() <= 0);
    }

    /**
     * Build the payment's reference that the first two digits represent the
     * year, the next fifth digits are the student's number and the last two
     * digits are a payment's code
     * 
     * @param infoGratuitySituation
     * @return
     */
    protected static String buildPaymentReference(InfoGratuitySituation infoGratuitySituation) {
        StringBuilder reference = new StringBuilder();

        //year
        String year = infoGratuitySituation.getInfoGratuityValues().getInfoExecutionDegree()
                .getInfoExecutionYear().getYear();
        reference.append(year.substring(2, year.indexOf('/'))); //year was like
        // 2003/2004
        //student's number
        reference.append(addCharToStringUntilMax(ZERO, infoGratuitySituation
                .getInfoStudentCurricularPlan().getInfoStudent().getNumber().toString(),
                MAX_STUDENT_NUMBER));
        //code
        if (infoGratuitySituation.getInfoStudentCurricularPlan().getSpecialization() != null
                && (infoGratuitySituation.getInfoStudentCurricularPlan().getSpecialization()
                        .equals(Specialization.STUDENT_CURRICULAR_PLAN_MASTER_DEGREE))) {
            reference.append(CODE_MASTERDEGREE);
        } else if (infoGratuitySituation.getInfoStudentCurricularPlan().getSpecialization() != null
                && (infoGratuitySituation.getInfoStudentCurricularPlan().getSpecialization()
                        .equals(Specialization.STUDENT_CURRICULAR_PLAN_SPECIALIZATION))) {

            reference.append(CODE_SPECIALIZATION);
        }
        return reference.toString();
    }

    /**
     * add a char to the string until reach the max lenth
     * 
     * @param maximum
     *            digits for the number
     * @param number
     * @return string
     */
    protected static String addCharToStringUntilMax(char c, String string, int maxlength) {
        StringBuilder stringComplete = new StringBuilder();

        int stringLength = 0;
        if (string != null) {
            stringLength = string.length();
        }

        for (int i = 0; i < maxlength - stringLength; i++) {
            stringComplete.append(c);
        }
        stringComplete.append(string);

        return stringComplete.toString();
    }
}