/*
 * Created on 28/Jan/2004
 *  
 */
package net.sourceforge.fenixedu.presentationTier.Action.manager.generateFiles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants;

/**
 * @author Tânia Pousão
 *  
 */
public class GratuityFileSIBS extends GratuityFile {
    public static final int WHITE_SPACES_HEADER = 3;

    public static final int WHITE_SPACES_LINE = 2;

    public static final int WHITE_SPACES_FOOTER = 41;

    public static File buildFile(List infoGratuitySituations) throws Exception {
        if (infoGratuitySituations == null) {
            return null;
        }

        String fileName = null;
        File file = null;
        BufferedWriter writer = null;

        String fileNameErrors = null;
        File fileErrors = null;
        BufferedWriter writerErrors = null;

        try {
            //build the file's name with the first element
            fileName = nameFile((InfoGratuitySituation) infoGratuitySituations.get(0));
            file = new File(System.getProperty("java.io.tmpdir") + File.separator + fileName);
            writer = new BufferedWriter(new FileWriter(file));

            //errors if student hasn´t address or nothing to pay
            fileNameErrors = "erros_" + nameFile((InfoGratuitySituation) infoGratuitySituations.get(0));
            fileErrors = new File(System.getProperty("java.io.tmpdir") + File.separator + fileNameErrors);
            writerErrors = new BufferedWriter(new FileWriter(fileErrors));

            writeHeader(writer);

            Iterator iterator = infoGratuitySituations.listIterator();
            int linesNumber = 0;
            while (iterator.hasNext()) {
                InfoGratuitySituation infoGratuitySituation = (InfoGratuitySituation) iterator.next();

                if (valid(infoGratuitySituation, writerErrors)) {
                    writeLine(writer, infoGratuitySituation);
                    linesNumber++;
                }
            }

            writeFooter(writer, linesNumber);

            writer.close();
            writerErrors.close();

            return file;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new Exception();
        }
    }

    private static String nameFile(InfoGratuitySituation infoGratuitySituation) {
        StringBuffer fileName = new StringBuffer();

        String year = infoGratuitySituation.getInfoGratuityValues().getInfoExecutionDegree()
                .getInfoExecutionYear().getYear().replace('/', '-');
        fileName.append("SIBSPropinas");
        fileName.append(year);
        fileName.append(".txt");

        return fileName.toString();
    }

    /**
     * Creates a header that describes all column in file like <name
     * column>;...; <name column>
     * 
     * @param writer
     */
    private static void writeHeader(BufferedWriter writer) throws IOException {
        StringBuffer header = new StringBuffer();
        header.append(0); //register type, usually 0
        header.append("AEPS"); //file type
        header.append(90000820); //number with 8 digits
        header.append(50000000); //number with 8 digits
        header.append(formatDate(Calendar.getInstance().getTime())); //today's
        // date
        header.append(1); //sequence number, for omission it is 1
        //last file's date that it was sended
        //it is necessary fill later
        header.append("00000000");
        header.append(1); //last file's sequence number that it was sended,
        // usually it is 1
        header.append(20821); //entity's code
        header.append(978); //currency(coin) code, in this case it is euro's
        // code
        header.append(addCharToStringUntilMax(SPACE, "", WHITE_SPACES_HEADER)); // three
        // white
        // spaces

        writer.write(header.toString());
        writer.newLine();
    }

    /**
     * Creates a line with student's data separate by tab
     * 
     * @param writer
     * @param infoGratuitySituation
     */
    private static void writeLine(BufferedWriter writer, InfoGratuitySituation infoGratuitySituation)
            throws IOException {
        StringBuffer line = new StringBuffer();

        line.append(1); //register type, usually 1
        line.append(80); //processing type, usually 80 but can be 82
        line.append(buildPaymentReference(infoGratuitySituation)); //payment's
        // reference
        //payment's end date
        line.append(formatDate(infoGratuitySituation.getInfoGratuityValues().getEndPayment()));

        double payValue = findPayValue(infoGratuitySituation);

        //payment's max value
        line.append(buildPaymentValue(payValue, MAX_INT_PAYMENT, MAX_DEC_PAYMENT));
        //payment's begin date
        line.append(formatDate(infoGratuitySituation.getInfoGratuityValues().getStartPayment()));
        //payment's min value
        line.append(buildPaymentValue(payValue, MAX_INT_PAYMENT, MAX_DEC_PAYMENT));
        line.append(addCharToStringUntilMax(SPACE, "", WHITE_SPACES_LINE)); // two
        // white
        // spaces

        //write the line
        writer.write(line.toString());
        writer.newLine();
    }

    /**
     * @param writer
     */
    private static void writeFooter(BufferedWriter writer, int linesNumber) throws IOException {
        StringBuffer footer = new StringBuffer();

        footer.append(9); //register type, usually 9
        footer.append(addCharToStringUntilMax(ZERO, String.valueOf(linesNumber), MAX_LINES_NUMBER));
        //number of lines except header and footer, attention number with 8
        // digits
        footer.append(addCharToStringUntilMax(SPACE, "", WHITE_SPACES_FOOTER));
        // forty-one white spaces

        writer.write(footer.toString());
        writer.newLine();
    }

    /**
     * Find the value that the student will be
     * 
     * @param infoGratuitySituation
     * @return
     */
    private static double findPayValue(InfoGratuitySituation infoGratuitySituation) {
        double valueToPay = infoGratuitySituation.getRemainingValue().doubleValue();
        if (valueToPay < 0) {
            valueToPay = 0;
        }
        if (infoGratuitySituation.getInsurancePayed().equals(SessionConstants.NOT_PAYED_INSURANCE)) {
            valueToPay = valueToPay + INSURANCE;
        }

        return valueToPay;
    }

    /**
     * Build the value that it has 8 digits in int part, and it has 2 digits in
     * decimal part
     * 
     * @param infoGratuitySituation
     * @return
     */
    private static String buildPaymentValue(double value, int intDigits, int decDigits) {
        StringBuffer stringBuffer = new StringBuffer();

        String valueString = String.valueOf(value);
        String intPart = valueString.substring(0, valueString.indexOf('.'));
        String decPart = valueString.substring(valueString.indexOf('.') + 1);

        for (int i = 0; i < intDigits - intPart.length(); i++) {
            stringBuffer.append(ZERO);
        }
        stringBuffer.append(intPart);

        if (decPart.length() > decDigits) {
            decPart = decPart.substring(0, decDigits);
        }
        stringBuffer.append(decPart);
        for (int i = 0; i < decDigits - decPart.length(); i++) {
            stringBuffer.append(ZERO);
        }

        return stringBuffer.toString();
    }

    /**
     * Formats a date like yyyymmdd
     * 
     * @param date
     * @return string
     */
    private static String formatDate(Date date) {
        //if date is null then date is equal to today's date
        if (date == null) {
            date = new Date();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());

        StringBuffer dateString = new StringBuffer();
        dateString.append(calendar.get(Calendar.YEAR));

        int month = calendar.get(Calendar.MONTH) + 1;
        String monthString = "";
        if (month < 10) {
            monthString = "0";
        }
        monthString = monthString + String.valueOf(month);
        dateString.append(monthString);

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String dayString = "";
        if (day < 10) {
            dayString = "0";
        }
        dayString = dayString + String.valueOf(day);
        dateString.append(dayString);

        return dateString.toString();
    }
}