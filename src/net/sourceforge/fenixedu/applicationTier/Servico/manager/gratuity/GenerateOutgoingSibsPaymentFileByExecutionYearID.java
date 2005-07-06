package net.sourceforge.fenixedu.applicationTier.Servico.manager.gratuity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.gratuity.masterDegree.FileNotCreatedServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.gratuity.masterDegree.InsufficientSibsPaymentPhaseCodesServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.gratuity.masterDegree.InsuranceNotDefinedServiceException;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IExecutionDegree;
import net.sourceforge.fenixedu.domain.IExecutionYear;
import net.sourceforge.fenixedu.domain.IGratuitySituation;
import net.sourceforge.fenixedu.domain.IInsuranceValue;
import net.sourceforge.fenixedu.domain.IPaymentPhase;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.gratuity.SibsPaymentType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentGratuitySituation;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.transactions.IPersistentInsuranceTransaction;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.util.gratuity.fileParsers.sibs.SibsOutgoingPaymentFileConstants;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

import pt.ist.utl.fenix.utils.SibsPaymentCodeFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * 
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class GenerateOutgoingSibsPaymentFileByExecutionYearID implements IService {

    public GenerateOutgoingSibsPaymentFileByExecutionYearID() {

    }

    /**
     * 
     * @param executionYear
     * @throws FenixServiceException
     */
    public void run(Integer executionYearID) throws FenixServiceException {

        StringBuffer outgoingSibsPaymentFile = new StringBuffer();

        try {
            ISuportePersistente sp = PersistenceSupportFactory.getDefaultPersistenceSupport();

            IExecutionYear executionYear = (IExecutionYear) sp.getIPersistentExecutionYear().readByOID(
                    ExecutionYear.class, executionYearID);

            IPersistentGratuitySituation gratuitySituationDAO = sp.getIPersistentGratuitySituation();

            IPersistentInsuranceTransaction insuranceTransactionDAO = sp
                    .getIPersistentInsuranceTransaction();

            IInsuranceValue insuranceValue = sp.getIPersistentInsuranceValue().readByExecutionYear(
                    executionYear.getIdInternal());

            if (insuranceValue == null) {
                throw new InsuranceNotDefinedServiceException("error.insurance.notDefinedForThisYear");
            }

            String shortYear = executionYear.getYear().split("/")[0].trim().substring(2);

            // read master degree and specialization execution degrees
            List executionDegreeList = sp.getIPersistentExecutionDegree()
                    .readByExecutionYearAndDegreeType(executionYear.getYear(), DegreeType.MASTER_DEGREE);

            int totalLines = 0;

            HashMap studentsWithInsuranceChecked = new HashMap();

            // add file header
            addHeader(outgoingSibsPaymentFile);

            // add lines gratuity
            for (Iterator iter = executionDegreeList.iterator(); iter.hasNext();) {

                IExecutionDegree executionDegree = (IExecutionDegree) iter.next();

                IDegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();

                List studentCurricularPlanList = degreeCurricularPlan.getStudentCurricularPlans();

                //add insurance lines
                for (Iterator iterator = studentCurricularPlanList.iterator(); iterator.hasNext();) {

                    IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) iterator
                            .next();

                    IStudent student = studentCurricularPlan.getStudent();

                    if (studentsWithInsuranceChecked.containsKey(student.getIdInternal())) {
                        continue;
                    }

                    studentsWithInsuranceChecked.put(student.getIdInternal(), null);

                    List insuranceTransactionList = insuranceTransactionDAO
                            .readAllNonReimbursedByExecutionYearAndStudent(executionYear, student);

                    if (insuranceTransactionList.size() == 0) {

                        // the student hasn't payed the insurance for this year
                        // yet
                        addLine(outgoingSibsPaymentFile,
                                SibsOutgoingPaymentFileConstants.LINE_REGISTER_TYPE,
                                SibsOutgoingPaymentFileConstants.LINE_PROCESSING_CODE, shortYear,
                                student.getNumber(), SibsPaymentCodeFactory.getCode(SibsPaymentType.INSURANCE) + "", null,
                                insuranceValue.getEndDate(), insuranceValue.getAnnualValue(),
                                insuranceValue.getAnnualValue());

                        totalLines++;
                    }

                }

                List gratuitySituationList = gratuitySituationDAO
                        .readGratuitySituationsByDegreeCurricularPlan(degreeCurricularPlan.getIdInternal());

                for (Iterator iterator = gratuitySituationList.iterator(); iterator.hasNext();) {

                    IGratuitySituation gratuitySituation = (IGratuitySituation) iterator.next();

                    //                    if
                    // (gratuitySituation.getStudentCurricularPlan().getSpecialization().equals(
                    //                            Specialization.INTEGRADO_TYPE)) {
                    //                        //nothing to be done
                    //                        continue;
                    //                    }

                    totalLines += addGratuityLines(outgoingSibsPaymentFile, gratuitySituation, shortYear);

                }

            }

            // add file footer
            addFooter(outgoingSibsPaymentFile, totalLines);

            writeOutgoingSibsPaymentFile(executionYear, outgoingSibsPaymentFile);

        } catch (ExcepcaoPersistencia e) {
            FenixServiceException newEx = new FenixServiceException("Persistence layer error");
            newEx.fillInStackTrace();
            throw newEx;
        }
    }

    /**
     * @param outgoingSibsPaymentFile
     * @throws FileNotCreatedServiceException
     */
    private void writeOutgoingSibsPaymentFile(IExecutionYear executionYear,
            StringBuffer outgoingSibsPaymentFile) throws FileNotCreatedServiceException {
        String year = executionYear.getYear().replace('/', '-');
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(System
                    .getProperty("java.io.tmpdir")
                    + File.separator + "SIBSPropinas" + year + ".txt", false));
            bufferedWriter.write(outgoingSibsPaymentFile.toString());
            bufferedWriter.close();
        } catch (IOException e) {
            throw new FileNotCreatedServiceException("error.creating.sibs.outgoing.file", e);
        }

    }

    /**
     * @param outgoingSibsPaymentFile
     */
    private void addHeader(StringBuffer outgoingSibsPaymentFile) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        outgoingSibsPaymentFile.append(SibsOutgoingPaymentFileConstants.HEADER_REGISTER_TYPE);
        outgoingSibsPaymentFile.append(SibsOutgoingPaymentFileConstants.FILE_TYPE);
        outgoingSibsPaymentFile.append(SibsOutgoingPaymentFileConstants.ID_SOURCE_INSTITUTION);
        outgoingSibsPaymentFile.append(SibsOutgoingPaymentFileConstants.ID_DESTINATION_INSTITUTION);
        outgoingSibsPaymentFile.append(simpleDateFormat.format(Calendar.getInstance().getTime()));
        outgoingSibsPaymentFile.append(SibsOutgoingPaymentFileConstants.OMISSION_SEQUENCE_NUMBER);
        //last file's date that it was sended
        //it is necessary fill later
        outgoingSibsPaymentFile.append("00000000");//The responsible person
        // will add the last send
        // data
        outgoingSibsPaymentFile.append(SibsOutgoingPaymentFileConstants.OMISSION_SEQUENCE_NUMBER);
        outgoingSibsPaymentFile.append(SibsOutgoingPaymentFileConstants.ENTITY);
        outgoingSibsPaymentFile.append(SibsOutgoingPaymentFileConstants.CURRENCY_CODE);
        outgoingSibsPaymentFile.append(addCharToStringUntilMax(
                SibsOutgoingPaymentFileConstants.SPACE_CHAR, "",
                SibsOutgoingPaymentFileConstants.WHITE_SPACES_IN_HEADER));

        outgoingSibsPaymentFile.append("\n");
    }

    /**
     * 
     * @param totalLines
     */
    private void addFooter(StringBuffer outgoingSibsPaymentFile, int totalLines) {

        outgoingSibsPaymentFile.append(SibsOutgoingPaymentFileConstants.FOOTER_REGISTER_TYPE);
        outgoingSibsPaymentFile.append(addCharToStringUntilMax(
                SibsOutgoingPaymentFileConstants.ZERO_CHAR, "" + totalLines,
                SibsOutgoingPaymentFileConstants.NUMBER_OF_LINES_DESCRIPTOR_LENGTH));

        outgoingSibsPaymentFile.append(addCharToStringUntilMax(
                SibsOutgoingPaymentFileConstants.SPACE_CHAR, "",
                SibsOutgoingPaymentFileConstants.WHITE_SPACES_IN_FOOTER));

        outgoingSibsPaymentFile.append("\n");

    }

    /**
     * 
     * @param outgoingSibsPaymentFile
     * @param gratuitySituation
     * @param shortYear
     * @throws InsufficientSibsPaymentPhaseCodesServiceException
     */
    private int addGratuityLines(StringBuffer outgoingSibsPaymentFile,
            IGratuitySituation gratuitySituation, String shortYear) throws InsufficientSibsPaymentPhaseCodesServiceException {

        int totalLinesAdded = 0;

        if ((gratuitySituation.getRemainingValue() == null)
                || (gratuitySituation.getRemainingValue().doubleValue() <= 0)) {
            //nothing to be done
            return totalLinesAdded;
        }

        Double scholarShipPartValue = null;
        if (gratuitySituation.getStudentCurricularPlan().getSpecialization().equals(
                Specialization.SPECIALIZATION)) {

            scholarShipPartValue = gratuitySituation.getRemainingValue();

        } else {
            scholarShipPartValue = new Double(gratuitySituation.getRemainingValue().doubleValue()
                    - (gratuitySituation.getGratuityValues().getFinalProofValue() == null ? 0
                            : gratuitySituation.getGratuityValues().getFinalProofValue().doubleValue()));
        }

        if (scholarShipPartValue.doubleValue() <= 0) {
            //nothing to be done;
            return totalLinesAdded;
        }

        IStudentCurricularPlan studentCurricularPlan = gratuitySituation.getStudentCurricularPlan();

        // add total payment line
        String sibsPaymentCode = determineTotalPaymentCode(studentCurricularPlan);
        Date startDate = gratuitySituation.getGratuityValues().getStartPayment();
        Date endDate = gratuitySituation.getGratuityValues().getEndPayment();

        if (endDate.before(Calendar.getInstance().getTime()) == true) {
            // end date already passed
            return totalLinesAdded;
        }

        addLine(outgoingSibsPaymentFile, SibsOutgoingPaymentFileConstants.LINE_REGISTER_TYPE,
                SibsOutgoingPaymentFileConstants.LINE_PROCESSING_CODE, shortYear, studentCurricularPlan
                        .getStudent().getNumber(), sibsPaymentCode, startDate, endDate,
                scholarShipPartValue, scholarShipPartValue);

        totalLinesAdded++;

        // add phase payment lines
        List paymentPhaseList = gratuitySituation.getGratuityValues().getPaymentPhaseList();

        double totalValueInPhases = 0;
        for (Iterator iter = paymentPhaseList.iterator(); iter.hasNext();) {
            IPaymentPhase paymentPhase = (IPaymentPhase) iter.next();
            totalValueInPhases += paymentPhase.getValue().doubleValue();
        }

        if ((scholarShipPartValue.doubleValue() - totalValueInPhases) > 0) {
            //there are no sufficient phases to pay the remaining value
            //so send the total value only
            return totalLinesAdded;
        }

        BeanComparator paymentPhaseDateComparator = new BeanComparator("endDate");
        ComparatorChain chainComparator = new ComparatorChain();
        chainComparator.addComparator(paymentPhaseDateComparator, true);
        Collections.sort(paymentPhaseList, chainComparator);

        int paymentPhaseNumber = 1;
        double totalValueToDivideInPhases = scholarShipPartValue.doubleValue();

        for (Iterator iter = paymentPhaseList.iterator(); iter.hasNext();) {
            IPaymentPhase paymentPhase = (IPaymentPhase) iter.next();

            if (paymentPhase.getEndDate().before(Calendar.getInstance().getTime())) {
                // end date for that phase already passed
                continue;
            }

            if ((paymentPhaseNumber == 1)
                    && (paymentPhase.getValue().doubleValue() >= totalValueToDivideInPhases)) {
                // phases are not required, because the total value is less then
                // the first phase
                return totalLinesAdded;
            }

            totalValueToDivideInPhases -= paymentPhase.getValue().doubleValue();

            sibsPaymentCode = determinePaymentPhaseCode(paymentPhaseNumber, studentCurricularPlan,
                    gratuitySituation);

            addLine(outgoingSibsPaymentFile, SibsOutgoingPaymentFileConstants.LINE_REGISTER_TYPE,
                    SibsOutgoingPaymentFileConstants.LINE_PROCESSING_CODE, shortYear,
                    studentCurricularPlan.getStudent().getNumber(), sibsPaymentCode, paymentPhase
                            .getStartDate(), paymentPhase.getEndDate(), paymentPhase.getValue(),
                    paymentPhase.getValue());

            totalLinesAdded++;

            paymentPhaseNumber++;

        }

        return totalLinesAdded;

    }

    /**
     * 
     * @param outgoingSibsPaymentFile
     * @param registerType
     * @param processingCode
     * @param executionYear
     * @param studentNumber
     * @param sibsPaymentType
     * @param startDate
     * @param endDate
     * @param minValue
     * @param maxValue
     */

    public void addLine(StringBuffer outgoingSibsPaymentFile, String registerType,
            String processingCode, String shortYear, Integer studentNumber, String sibsPaymentCode,
            Date startDate, Date endDate, Double minValue, Double maxValue) {

        if (startDate == null) {
            startDate = Calendar.getInstance().getTime();
        }
        if (endDate == null) {
            endDate = Calendar.getInstance().getTime();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");

        outgoingSibsPaymentFile.append(registerType);
        outgoingSibsPaymentFile.append(processingCode);

        //build reference
        outgoingSibsPaymentFile.append(shortYear);
        outgoingSibsPaymentFile.append(addCharToStringUntilMax(
                SibsOutgoingPaymentFileConstants.ZERO_CHAR, studentNumber.toString(),
                SibsOutgoingPaymentFileConstants.MAX_STUDENT_NUMBER_LENGTH));
        outgoingSibsPaymentFile.append(sibsPaymentCode);

        // add end payment date
        outgoingSibsPaymentFile.append(simpleDateFormat.format(endDate));

        // add max payment value accepted
        String paymentValue = buildPaymentValue(maxValue.doubleValue(),
                SibsOutgoingPaymentFileConstants.INTEGER_PART_LENGTH,
                SibsOutgoingPaymentFileConstants.DECIMAL_PART_LENGTH);
        outgoingSibsPaymentFile.append(paymentValue);

        // add start payment date
        outgoingSibsPaymentFile.append(simpleDateFormat.format(startDate));

        //	add min payment value accepted
        outgoingSibsPaymentFile.append(paymentValue);

        outgoingSibsPaymentFile.append(addCharToStringUntilMax(
                SibsOutgoingPaymentFileConstants.SPACE_CHAR, "",
                SibsOutgoingPaymentFileConstants.WHITE_SPACES_IN_LINE));

        outgoingSibsPaymentFile.append("\n");

    }

    /**
     * add a char to the string until reach the max lenth
     * 
     * @param maximum
     *            digits for the number
     * @param number
     * @return string
     */
    private String addCharToStringUntilMax(char c, String string, int maxlength) {
        StringBuffer stringComplete = new StringBuffer();

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

    /**
     * Build the value that it has 8 digits in int part, and it has 2 digits in
     * decimal part
     * 
     * @return
     */
    private String buildPaymentValue(double value, int intDigits, int decDigits) {
        StringBuffer stringBuffer = new StringBuffer();

        String valueString = String.valueOf(value);
        String intPart = valueString.substring(0, valueString.indexOf('.'));
        String decPart = valueString.substring(valueString.indexOf('.') + 1);

        for (int i = 0; i < intDigits - intPart.length(); i++) {
            stringBuffer.append(SibsOutgoingPaymentFileConstants.ZERO_CHAR);
        }
        stringBuffer.append(intPart);

        if (decPart.length() > decDigits) {
            decPart = decPart.substring(0, decDigits);
        }
        stringBuffer.append(decPart);
        for (int i = 0; i < decDigits - decPart.length(); i++) {
            stringBuffer.append(SibsOutgoingPaymentFileConstants.ZERO_CHAR);
        }

        return stringBuffer.toString();
    }

    private String determineTotalPaymentCode(IStudentCurricularPlan studentCurricularPlan) {

        int sibsPaymentCode = 0;
        Specialization specialization = studentCurricularPlan.getSpecialization();

        if (specialization.equals(Specialization.MASTER_DEGREE)) {

            sibsPaymentCode = SibsPaymentCodeFactory.getCode(SibsPaymentType.MASTER_DEGREE_GRATUTITY_TOTAL);

        } else {

            sibsPaymentCode = SibsPaymentCodeFactory.getCode(SibsPaymentType.SPECIALIZATION_GRATUTITY_TOTAL);
        }

        //IMPORTANT NOTE: In future integrated master degree codes should be
        // inserted here

        return sibsPaymentCode + "";
    }

    /**
     * @param paymentPhaseNumber
     * @param studentCurricularPlan
     * @return
     */
    private String determinePaymentPhaseCode(int paymentPhaseNumber,
            IStudentCurricularPlan studentCurricularPlan, IGratuitySituation gratuitySituation)
            throws InsufficientSibsPaymentPhaseCodesServiceException {

        int sibsPaymentCode = 0;

        if (paymentPhaseNumber == 1) {
            if (studentCurricularPlan.getSpecialization().equals(Specialization.SPECIALIZATION)) {

                sibsPaymentCode = SibsPaymentCodeFactory.getCode(SibsPaymentType.SPECIALIZATION_GRATUTITY_FIRST_PHASE);

            } else {

                sibsPaymentCode = SibsPaymentCodeFactory.getCode(SibsPaymentType.MASTER_DEGREE_GRATUTITY_FIRST_PHASE);
            }
            // IMPORTANT NOTE: In future integrated master degree codes should
            // be inserted here

        } else if (paymentPhaseNumber == 2) {

            if (studentCurricularPlan.getSpecialization().equals(Specialization.SPECIALIZATION)) {

                sibsPaymentCode = SibsPaymentCodeFactory.getCode(SibsPaymentType.SPECIALIZATION_GRATUTITY_SECOND_PHASE);

            } else {

                sibsPaymentCode = SibsPaymentCodeFactory.getCode(SibsPaymentType.MASTER_DEGREE_GRATUTITY_SECOND_PHASE);
            }

            // IMPORTANT NOTE: In future integrated master degree codes should
            // be inserted here

        } else {
            throw new InsufficientSibsPaymentPhaseCodesServiceException(gratuitySituation
                    .getGratuityValues().getExecutionDegree().getDegreeCurricularPlan().getName()
                    + " - "
                    + gratuitySituation.getGratuityValues().getExecutionDegree().getExecutionYear()
                            .getYear());
        }

        return sibsPaymentCode + "";

    }
} 