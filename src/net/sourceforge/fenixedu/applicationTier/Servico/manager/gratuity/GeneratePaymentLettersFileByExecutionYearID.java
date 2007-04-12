package net.sourceforge.fenixedu.applicationTier.Servico.manager.gratuity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.gratuity.masterDegree.FileNotCreatedServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.gratuity.masterDegree.InsufficientSibsPaymentPhaseCodesServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.gratuity.masterDegree.InsuranceNotDefinedServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.GratuityValues;
import net.sourceforge.fenixedu.domain.InsuranceValue;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.gratuity.SibsPaymentType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.gratuity.fileParsers.sibs.SibsOutgoingPaymentFileConstants;
import pt.ist.utl.fenix.utils.SibsPaymentCodeFactory;

public class GeneratePaymentLettersFileByExecutionYearID extends Service {

    // private class GratuityLetterPaymentPhase {
    //
    // private String endDate;
    //
    // private String fullSibsReference;
    //
    // private String value;
    //
    // public String getEndDate() {
    // return endDate;
    // }
    //
    // public void setEndDate(String endDate) {
    // this.endDate = endDate;
    // }
    //
    // public String getFullSibsReference() {
    // return fullSibsReference;
    // }
    //
    // public void setFullSibsReference(String fullSibsReference) {
    // this.fullSibsReference = fullSibsReference;
    // }
    //
    // public String getValue() {
    // return value;
    // }
    //
    // public void setValue(String value) {
    // this.value = value;
    // }
    // }

    private class GratuityLetterFileEntry {

        private int numberOfPaymentPhases;

        private StudentCurricularPlan studentCurricularPlan;

        private String totalGratuityValue;

        private String totalGratuityFullSibsReference;

        private String totalGratuityEndDate;

        private String insuranceEndDate;

        private String insuranceFullSibsReference;

        private String insuranceValue;

        // private List gratuityLetterPaymentPhases;

        public GratuityLetterFileEntry(StudentCurricularPlan studentCurricularPlan) {
            this.numberOfPaymentPhases = 0;
            // this.gratuityLetterPaymentPhases = new ArrayList();
            this.studentCurricularPlan = studentCurricularPlan;

        }

        public String getInsuranceEndDate() {
            return insuranceEndDate;
        }

        public void setInsuranceEndDate(String insuranceEndDate) {
            this.insuranceEndDate = insuranceEndDate;
        }

        public String getTotalGratuityEndDate() {
            return totalGratuityEndDate;
        }

        public void setTotalGratuityEndDate(String totalGratuityEndDate) {
            this.totalGratuityEndDate = totalGratuityEndDate;
        }

        public String getInsuranceFullSibsReference() {
            return insuranceFullSibsReference;
        }

        public void setInsuranceFullSibsReference(String insuranceFullSibsReference) {
            this.insuranceFullSibsReference = insuranceFullSibsReference;
        }

        public String getTotalGratuityFullSibsReference() {
            return totalGratuityFullSibsReference;
        }

        public void setTotalGratuityFullSibsReference(String totalGratuityFullSibsReference) {
            this.totalGratuityFullSibsReference = totalGratuityFullSibsReference;
        }

        // public List getGratuityLetterPaymentPhases() {
        // return gratuityLetterPaymentPhases;
        // }
        //
        // public void setGratuityLetterPaymentPhases(List
        // gratuityLetterPaymentPhases) {
        // this.gratuityLetterPaymentPhases = gratuityLetterPaymentPhases;
        // }

        public String getTotalGratuityValue() {
            return totalGratuityValue;
        }

        public void setTotalGratuityValue(String totalGratuityValue) {
            this.totalGratuityValue = totalGratuityValue;
        }

        public String getInsuranceValue() {
            return insuranceValue;
        }

        public void setInsuranceValue(String insuranceValue) {
            this.insuranceValue = insuranceValue;
        }

        public int getNumberOfPaymentPhases() {
            return numberOfPaymentPhases;
        }

        public void setNumberOfPaymentPhases(int numberOfPaymentPhases) {
            this.numberOfPaymentPhases = numberOfPaymentPhases;
        }

        public StudentCurricularPlan getStudentCurricularPlan() {
            return studentCurricularPlan;
        }

        public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
            this.studentCurricularPlan = studentCurricularPlan;
        }
    }

    private static final String PHASE_SIBS_BASE_COLUMN = "REFERENCIA_SIBS_PRESTACAO_";

    private static final String PHASE_END_DATE_BASE_COLUMN = "DATA_LIMITE_PAGAMENTO_PRESTACAO_";

    private static final String PHASE_VALUE_BASE_COLUMN = "VALOR_PRESTACAO_";

    private static final String INSURANCE_SIBS_REFERENCE_COLUMN = "REFERENCIA_SIBS_SEGURO";

    private static final String INSURANCA_END_DATE_COLUMN = "DATA_LIMITE_PAGAMENTO_SEGURO_ESCOLAR";

    private static final String INSURANCE_VALUE_COLUMN = "VALOR_SEGURO_ESCOLAR";

    private static final String TOTAL_GRATUITY_SIBS_REFERENCE_COLUMN = "REFERENCIA_SIBS_TOTAL_PROPINA";

    private static final String GRATUITY_TOTAL_END_DATE_COLUMN = "DATA_LIMITE_PAGAMENTO_TOTAL_PROPINA";

    private static final String GRATUITY_TOTAL_VALUE_COLUMN = "VALOR_TOTAL_PROPINA";

    private static final String MASTER_DEGREE_NAME_COLUMN = "MESTRADO";

    private static final String STUDENT_ADDRESS_COLUMN = "MORADA";

    private static final String STUDENT_NAME_COLUMN = "NOME";

    private static final String COLUMN_SEPARATOR = ";";

    private static final String DATA_SEPARATOR = "\t";

    private static final String STUDENT_NUMBER_COLUMN = "NUMERO";

    private static final String AREA_COLUMN = "LOCALIDADE";

    private static final String POSTAL_CODE_COLUMN = "COD_POSTAL";

    private static final String AREA_POSTAL_CODE_COLUMN = "LOCALIDADE_COD_POSTAL";

    private static final String DEGREE_COLUMN = "CURSO";

    public GeneratePaymentLettersFileByExecutionYearID() {

    }

    public byte[] run(Integer executionYearID, Date paymentEndDate) throws FenixServiceException,
            ExcepcaoPersistencia {
        ExecutionYear executionYear = rootDomainObject.readExecutionYearByOID(executionYearID);
        InsuranceValue insuranceValue = executionYear.getInsuranceValue();
        if (insuranceValue == null) {
            throw new InsuranceNotDefinedServiceException("error.insurance.notDefinedForThisYear");
        }

        // Date insurancePaymentEndDate = Calendar.getInstance().getTime();

        String shortYear = executionYear.getYear().split("/")[0].trim().substring(2);

        // read master degree and persistentSupportecialization execution
        // degrees
        List executionDegreeList = ExecutionDegree.getAllByExecutionYearAndDegreeType(executionYear
                .getYear(), DegreeType.MASTER_DEGREE);

        List gratuityLetterFileEntries = new ArrayList();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        NumberFormat numberFormat = NumberFormat.getInstance();
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        decimalFormat.applyPattern("######.##");

        Set<Integer> studentsWithInsuranceChecked = new HashSet<Integer>();

        for (Iterator iter = executionDegreeList.iterator(); iter.hasNext();) {

            ExecutionDegree executionDegree = (ExecutionDegree) iter.next();

            DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();

            List studentCurricularPlanList = degreeCurricularPlan.getStudentCurricularPlans();

            GratuityValues gratuityValues = executionDegree.getGratuityValues();

            for (Iterator iterator = studentCurricularPlanList.iterator(); iterator.hasNext();) {

                StudentCurricularPlan studentCurricularPlan = (StudentCurricularPlan) iterator.next();

                if (studentCurricularPlan.getSpecialization().equals(Specialization.STUDENT_CURRICULAR_PLAN_SPECIALIZATION)) {
                    if (!executionDegree.isFirstYear()) {
                        continue;
                    }
                }

                Registration registration = (studentCurricularPlan.getRegistration());

                GratuityLetterFileEntry gratuityLetterFileEntryInsurancePart = null;

                if (studentsWithInsuranceChecked.contains(registration.getIdInternal()) == false) {

                    studentsWithInsuranceChecked.add(registration.getIdInternal());

                    List insuranceTransactionList = registration
                            .readAllNonReimbursedInsuranceTransactionsByExecutionYear(executionYear);

                    if (insuranceTransactionList.size() == 0) {
                        // student hasn't payed insurance

                        gratuityLetterFileEntryInsurancePart = new GratuityLetterFileEntry(
                                studentCurricularPlan);

                        gratuityLetterFileEntryInsurancePart.setInsuranceEndDate(simpleDateFormat
                                .format(paymentEndDate));

                        gratuityLetterFileEntryInsurancePart.setInsuranceFullSibsReference(shortYear
                                + addCharToStringUntilMax(SibsOutgoingPaymentFileConstants.ZERO_CHAR,
                                        registration.getNumber().toString(),
                                        SibsOutgoingPaymentFileConstants.MAX_STUDENT_NUMBER_LENGTH)
                                + SibsPaymentCodeFactory.getCode(SibsPaymentType.INSURANCE));

                        gratuityLetterFileEntryInsurancePart.setInsuranceValue(decimalFormat
                                .format(insuranceValue.getAnnualValue()));

                    }
                }

                GratuityLetterFileEntry gratuityLetterFileEntry = null;

                // if ((studentCurricularPlan.getSpecialization().equals(
                // Specialization.INTEGRADO_TYPE) == false)
                // && (gratuityValues != null)) {

                if (gratuityValues != null) {

                    GratuitySituation gratuitySituation = studentCurricularPlan
                            .getGratuitySituationByGratuityValues(gratuityValues);

                    if (gratuitySituation != null) {

                        gratuityLetterFileEntry = createGratuityLetterFileEntryForGratuitySituation(
                                gratuitySituation, shortYear, paymentEndDate);
                    } else {
                        System.out.println("Registration " + registration.getNumber()
                                + " does not have a gratuity situation for year "
                                + executionDegree.getExecutionYear().getYear() + " Degree "
                                + executionDegree.getDegreeCurricularPlan().getName());
                    }

                }

                if (gratuityLetterFileEntry == null) {

                    // there wasn't gratuity part (only insurance)
                    gratuityLetterFileEntry = gratuityLetterFileEntryInsurancePart;

                } else {

                    if (gratuityLetterFileEntryInsurancePart != null) {
                        // insurance part exists
                        gratuityLetterFileEntry.setInsuranceEndDate(gratuityLetterFileEntryInsurancePart
                                .getInsuranceEndDate());
                        gratuityLetterFileEntry
                                .setInsuranceFullSibsReference(gratuityLetterFileEntryInsurancePart
                                        .getInsuranceFullSibsReference());
                        gratuityLetterFileEntry.setInsuranceValue(gratuityLetterFileEntryInsurancePart
                                .getInsuranceValue());
                    }
                }

                // this check is required because the insurance part can
                // also be null
                if (gratuityLetterFileEntry != null) {
                    gratuityLetterFileEntries.add(gratuityLetterFileEntry);
                }
            }
        }

        byte[] fileContent = writeLetterFiles(gratuityLetterFileEntries, executionYear);

        return fileContent;
    }

    /**
     * 
     * @param gratuityLetterFileEntry
     * @param gratuitySituation
     * @param shortYear
     * @return
     * @throws ExcepcaoPersistencia
     * @throws InsufficientSibsPaymentPhaseCodesServiceException
     */
    private GratuityLetterFileEntry createGratuityLetterFileEntryForGratuitySituation(
            GratuitySituation gratuitySituation, String shortYear, Date totalPaymentEndDate)
            throws InsufficientSibsPaymentPhaseCodesServiceException {

        // ignore integrated master degrees for now
        // if (gratuitySituation.getStudentCurricularPlan().getSpecialization()
        // .equals(Specialization.INTEGRADO_TYPE)) {
        //
        // return gratuityLetterFileEntry;
        // }

        if ((gratuitySituation.getRemainingValue() == null)
                || (gratuitySituation.getRemainingValue().doubleValue() <= 0)) {
            // nothing to be done
            return null;
        }

        Double scholarShipPartValue = null;
        if (gratuitySituation.getStudentCurricularPlan().getSpecialization().equals(
                Specialization.STUDENT_CURRICULAR_PLAN_SPECIALIZATION)) {

            scholarShipPartValue = gratuitySituation.getRemainingValue();

        } else {

            scholarShipPartValue = new Double(gratuitySituation.getRemainingValue().doubleValue()
                    - (gratuitySituation.getGratuityValues().getFinalProofValue() == null ? 0
                            : gratuitySituation.getGratuityValues().getFinalProofValue().doubleValue()));
        }

        if (scholarShipPartValue.doubleValue() <= 0) {
            // nothing to be done;
            return null;
        }

        StudentCurricularPlan studentCurricularPlan = gratuitySituation.getStudentCurricularPlan();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // add total payment line
        String sibsPaymentCode = determineTotalPaymentCode(studentCurricularPlan);
        // Date endDate = gratuitySituation.getGratuityValues().getEndPayment();
        //
        // if (endDate != null &&
        // endDate.before(Calendar.getInstance().getTime()) == true) {
        // // end date already passed
        // return null;
        // }

        // Date totalPaymentEndDate = Calendar.getInstance().getTime();

        GratuityLetterFileEntry gratuityLetterFileEntry = new GratuityLetterFileEntry(
                studentCurricularPlan);

        gratuityLetterFileEntry.setTotalGratuityFullSibsReference(shortYear
                + addCharToStringUntilMax(SibsOutgoingPaymentFileConstants.ZERO_CHAR,
                        studentCurricularPlan.getRegistration().getNumber().toString(),
                        SibsOutgoingPaymentFileConstants.MAX_STUDENT_NUMBER_LENGTH) + sibsPaymentCode);

        NumberFormat numberFormat = NumberFormat.getInstance();
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        decimalFormat.applyPattern("######.##");

        gratuityLetterFileEntry.setTotalGratuityValue(decimalFormat.format(scholarShipPartValue
                .doubleValue()));

        gratuityLetterFileEntry.setTotalGratuityEndDate(simpleDateFormat.format(totalPaymentEndDate));

        // int totalNumberOfPaymentPhases = 0;
        //
        // // add phase payment lines
        // List paymentPhaseList =
        // gratuitySituation.getGratuityValues().getPaymentPhaseList();
        //
        // double totalValueInPhases = 0;
        // for (Iterator iter = paymentPhaseList.iterator(); iter.hasNext();) {
        // PaymentPhase paymentPhase = (PaymentPhase) iter.next();
        // totalValueInPhases += paymentPhase.getValue().doubleValue();
        // }
        //
        // if ((scholarShipPartValue.doubleValue() - totalValueInPhases) > 0) {
        // // there are no sufficient phases to pay the remaining value
        // // so send the total value only
        // return gratuityLetterFileEntry;
        // }
        //
        // BeanComparator paymentPhaseDateComparator = new
        // BeanComparator("endDate");
        // ComparatorChain chainComparator = new ComparatorChain();
        // chainComparator.addComparator(paymentPhaseDateComparator, true);
        // paymentPhaseList = new ArrayList(paymentPhaseList);
        // Collections.sort(paymentPhaseList, chainComparator);
        //
        // int paymentPhaseNumber = 1;
        // double totalValueToDivideInPhases =
        // scholarShipPartValue.doubleValue();
        //
        // for (Iterator iter = paymentPhaseList.iterator(); iter.hasNext();) {
        // PaymentPhase paymentPhase = (PaymentPhase) iter.next();
        //
        // if
        // (paymentPhase.getEndDate().before(Calendar.getInstance().getTime()))
        // {
        // // end date for that phase already passed
        // continue;
        // }
        //
        // if ((paymentPhaseNumber == 1)
        // && (paymentPhase.getValue().doubleValue() >=
        // totalValueToDivideInPhases)) {
        // // phases are not required, because the total value is less then
        // // the first phase
        // return gratuityLetterFileEntry;
        // }
        //
        // totalValueToDivideInPhases -= paymentPhase.getValue().doubleValue();
        //
        // sibsPaymentCode = determinePaymentPhaseCode(paymentPhaseNumber,
        // studentCurricularPlan,
        // gratuitySituation);
        //
        // GratuityLetterPaymentPhase gratuityLetterPaymentPhase = new
        // GratuityLetterPaymentPhase();
        // gratuityLetterPaymentPhase.setEndDate(simpleDateFormat.format(paymentPhase.getEndDate()));
        //
        // gratuityLetterPaymentPhase.setValue(paymentPhase.getValue().toString());
        // gratuityLetterPaymentPhase.setFullSibsReference(shortYear
        // + addCharToStringUntilMax(SibsOutgoingPaymentFileConstants.ZERO_CHAR,
        // studentCurricularPlan.getRegistration().getNumber().toString(),
        // SibsOutgoingPaymentFileConstants.MAX_STUDENT_NUMBER_LENGTH)
        // + sibsPaymentCode);
        // gratuityLetterPaymentPhase.setEndDate(simpleDateFormat.format(paymentPhase.getEndDate()));
        //
        // gratuityLetterFileEntry.getGratuityLetterPaymentPhases().add(gratuityLetterPaymentPhase);
        //
        // totalNumberOfPaymentPhases++;
        //
        // paymentPhaseNumber++;
        //
        // }
        //
        // gratuityLetterFileEntry.setNumberOfPaymentPhases(totalNumberOfPaymentPhases);

        return gratuityLetterFileEntry;

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

    private String determineTotalPaymentCode(StudentCurricularPlan studentCurricularPlan) {

        int sibsPaymentCode = 0;
        Specialization specialization = studentCurricularPlan.getSpecialization();

        if (specialization.equals(Specialization.STUDENT_CURRICULAR_PLAN_MASTER_DEGREE)) {

            sibsPaymentCode = SibsPaymentCodeFactory
                    .getCode(SibsPaymentType.MASTER_DEGREE_GRATUTITY_TOTAL);

        } else {

            sibsPaymentCode = SibsPaymentCodeFactory
                    .getCode(SibsPaymentType.SPECIALIZATION_GRATUTITY_TOTAL);
        }

        // IMPORTANT NOTE: In future integrated master degree codes should be
        // inserted here

        return sibsPaymentCode + "";
    }

    // /**
    // * @param paymentPhaseNumber
    // * @param studentCurricularPlan
    // * @return
    // */
    // private String determinePaymentPhaseCode(int paymentPhaseNumber,
    // StudentCurricularPlan studentCurricularPlan, GratuitySituation
    // gratuitySituation)
    // throws InsufficientSibsPaymentPhaseCodesServiceException {
    //
    // int sibsPaymentCode = 0;
    //
    // if (paymentPhaseNumber == 1) {
    // if
    // (studentCurricularPlan.getSpecialization().equals(Specialization.SPECIALIZATION))
    // {
    //
    // sibsPaymentCode = SibsPaymentCodeFactory
    // .getCode(SibsPaymentType.SPECIALIZATION_GRATUTITY_FIRST_PHASE);
    //
    // } else {
    //
    // sibsPaymentCode = SibsPaymentCodeFactory
    // .getCode(SibsPaymentType.MASTER_DEGREE_GRATUTITY_FIRST_PHASE);
    // }
    // // IMPORTANT NOTE: In future integrated master degree codes should
    // // be inserted here
    //
    // } else if (paymentPhaseNumber == 2) {
    //
    // if
    // (studentCurricularPlan.getSpecialization().equals(Specialization.SPECIALIZATION))
    // {
    // sibsPaymentCode = SibsPaymentCodeFactory
    // .getCode(SibsPaymentType.SPECIALIZATION_GRATUTITY_SECOND_PHASE);
    //
    // } else {
    //
    // sibsPaymentCode = SibsPaymentCodeFactory
    // .getCode(SibsPaymentType.MASTER_DEGREE_GRATUTITY_SECOND_PHASE);
    // }
    //
    // // IMPORTANT NOTE: In future integrated master degree codes should
    // // be inserted here
    //
    // } else {
    // throw new
    // InsufficientSibsPaymentPhaseCodesServiceException(gratuitySituation
    // .getGratuityValues().getExecutionDegree().getDegreeCurricularPlan().getName()
    // + " - "
    // +
    // gratuitySituation.getGratuityValues().getExecutionDegree().getExecutionYear()
    // .getYear());
    // }
    //
    // return sibsPaymentCode + "";
    //
    // }

    /**
     * @param gratuityLetterFileEntries
     * @throws FileNotCreatedServiceException
     */
    // private void writeLetterFiles(List gratuityLetterFileEntries,
    // ExecutionYear executionYear)
    // throws FileNotCreatedServiceException {
    // HashMap letterFiles = new HashMap();
    // Integer numberOfPhases = null;
    //
    // for (Iterator iter = gratuityLetterFileEntries.iterator();
    // iter.hasNext();) {
    //
    // GratuityLetterFileEntry gratuityLetterFileEntry =
    // (GratuityLetterFileEntry) iter.next();
    //
    // numberOfPhases = new
    // Integer(gratuityLetterFileEntry.getGratuityLetterPaymentPhases().size());
    //
    // StringBuilder letterFile = (StringBuilder)
    // letterFiles.get(numberOfPhases);
    //
    // if (letterFile == null) {
    // letterFile =
    // createLetterFile(gratuityLetterFileEntry.getGratuityLetterPaymentPhases()
    // .size());
    // letterFiles.put(numberOfPhases, letterFile);
    // }
    //
    // // Registration number
    // letterFile.append(gratuityLetterFileEntry.getStudentCurricularPlan().getRegistration()
    // .getNumber()
    // + DATA_SEPARATOR);
    //
    // // Registration name
    // letterFile.append(gratuityLetterFileEntry.getStudentCurricularPlan().getRegistration()
    // .getPerson().getNome()
    // + DATA_SEPARATOR);
    //
    // // Registration address
    // letterFile.append(gratuityLetterFileEntry.getStudentCurricularPlan().getRegistration()
    // .getPerson().getMorada()
    // + DATA_SEPARATOR);
    //
    // // Registration localidade
    // letterFile.append(gratuityLetterFileEntry.getStudentCurricularPlan().getRegistration()
    // .getPerson().getLocalidade()
    // + DATA_SEPARATOR);
    //
    // // Registration cod. postal
    // letterFile.append(gratuityLetterFileEntry.getStudentCurricularPlan().getRegistration()
    // .getPerson().getCodigoPostal()
    // + DATA_SEPARATOR);
    //
    // // Registration localidade - cod. postal
    // letterFile.append(gratuityLetterFileEntry.getStudentCurricularPlan().getRegistration()
    // .getPerson().getLocalidadeCodigoPostal()
    // + DATA_SEPARATOR);
    //
    // // Master degree name
    // letterFile.append(gratuityLetterFileEntry.getStudentCurricularPlan()
    // .getDegreeCurricularPlan().getDegree().getNome()
    // + DATA_SEPARATOR);
    //
    // // Master degree curricular plan name
    // letterFile.append(gratuityLetterFileEntry.getStudentCurricularPlan()
    // .getDegreeCurricularPlan().getName()
    // + DATA_SEPARATOR);
    //
    // // gratuity full payment end date
    // letterFile.append(gratuityLetterFileEntry.getTotalGratuityEndDate() +
    // DATA_SEPARATOR);
    //
    // // gratuity full payment full sibs reference
    // letterFile.append(gratuityLetterFileEntry.getTotalGratuityFullSibsReference()
    // + DATA_SEPARATOR);
    //
    // // gratuity full payment value
    // letterFile.append(gratuityLetterFileEntry.getTotalGratuityValue() +
    // DATA_SEPARATOR);
    //
    // // insurance payment end date
    // letterFile.append(gratuityLetterFileEntry.getInsuranceEndDate() +
    // DATA_SEPARATOR);
    //
    // // insurance payment full sibs reference
    // letterFile.append(gratuityLetterFileEntry.getInsuranceFullSibsReference()
    // + DATA_SEPARATOR);
    //
    // // insurance payment value
    // letterFile.append(gratuityLetterFileEntry.getInsuranceValue() +
    // DATA_SEPARATOR);
    //
    // for (Iterator iterator =
    // gratuityLetterFileEntry.getGratuityLetterPaymentPhases().iterator();
    // iterator
    // .hasNext();) {
    //
    // GratuityLetterPaymentPhase gratuityLetterPaymentPhase =
    // (GratuityLetterPaymentPhase) iterator
    // .next();
    //
    // // payment phase end date
    // letterFile.append(gratuityLetterPaymentPhase.getEndDate() +
    // DATA_SEPARATOR);
    //
    // // payment phase full sibs reference
    // letterFile.append(gratuityLetterPaymentPhase.getFullSibsReference() +
    // DATA_SEPARATOR);
    //
    // // payment phase value
    // letterFile.append(gratuityLetterPaymentPhase.getValue() +
    // DATA_SEPARATOR);
    //
    // }
    //
    // letterFile.append("\n");
    // }
    //
    // String year = executionYear.getYear().replace('/', '-');
    // try {
    //
    // for (Iterator iter = letterFiles.keySet().iterator(); iter.hasNext();) {
    //
    // Integer phasesNumber = (Integer) iter.next();
    // StringBuilder letterFile = (StringBuilder) letterFiles.get(phasesNumber);
    //
    // String filename = System.getProperty("java.io.tmpdir") + File.separator +
    // "Cartas"
    // + year + "-" + phasesNumber + "fases" + ".txt";
    //
    // BufferedWriter bufferedWriter = new BufferedWriter(new
    // FileWriter(filename));
    //
    // bufferedWriter.write(letterFile.toString());
    // bufferedWriter.close();
    //
    // }
    //
    // } catch (IOException e) {
    // throw new FileNotCreatedServiceException("error.creating.letters.file",
    // e);
    // }
    //
    // }
    /**
     * @param gratuityLetterFileEntries
     * @throws FileNotCreatedServiceException
     */
    private byte[] writeLetterFiles(List gratuityLetterFileEntries, ExecutionYear executionYear)
            throws FileNotCreatedServiceException {
        StringBuilder letterFile = createLetterFile(0); // this needs cleanup.
        // for we are not using
        // payment phases so set
        // the value to 0

        for (Iterator iter = gratuityLetterFileEntries.iterator(); iter.hasNext();) {

            GratuityLetterFileEntry gratuityLetterFileEntry = (GratuityLetterFileEntry) iter.next();

            // Registration number
            letterFile.append(gratuityLetterFileEntry.getStudentCurricularPlan().getRegistration()
                    .getNumber()
                    + DATA_SEPARATOR);

            // Registration name
            letterFile.append(gratuityLetterFileEntry.getStudentCurricularPlan().getRegistration()
                    .getPerson().getName()
                    + DATA_SEPARATOR);

            // Registration address
            letterFile.append(gratuityLetterFileEntry.getStudentCurricularPlan().getRegistration()
                    .getPerson().getAddress()
                    + DATA_SEPARATOR);

            // Registration localidade
            letterFile.append(gratuityLetterFileEntry.getStudentCurricularPlan().getRegistration()
                    .getPerson().getArea()
                    + DATA_SEPARATOR);

            // Registration cod. postal
            letterFile.append(gratuityLetterFileEntry.getStudentCurricularPlan().getRegistration()
                    .getPerson().getAreaCode()
                    + DATA_SEPARATOR);

            // Registration localidade - cod. postal
            letterFile.append(gratuityLetterFileEntry.getStudentCurricularPlan().getRegistration()
                    .getPerson().getAreaOfAreaCode()
                    + DATA_SEPARATOR);

            // Master degree name
            letterFile.append(gratuityLetterFileEntry.getStudentCurricularPlan()
                    .getDegreeCurricularPlan().getDegree().getNome()
                    + DATA_SEPARATOR);

            // Master degree curricular plan name
            letterFile.append(gratuityLetterFileEntry.getStudentCurricularPlan()
                    .getDegreeCurricularPlan().getName()
                    + DATA_SEPARATOR);

            // gratuity full payment end date
            letterFile.append(gratuityLetterFileEntry.getTotalGratuityEndDate() + DATA_SEPARATOR);

            // gratuity full payment full sibs reference
            letterFile.append(gratuityLetterFileEntry.getTotalGratuityFullSibsReference()
                    + DATA_SEPARATOR);

            // gratuity full payment value
            letterFile.append(gratuityLetterFileEntry.getTotalGratuityValue() + DATA_SEPARATOR);

            // insurance payment end date
            letterFile.append(gratuityLetterFileEntry.getInsuranceEndDate() + DATA_SEPARATOR);

            // insurance payment full sibs reference
            letterFile.append(gratuityLetterFileEntry.getInsuranceFullSibsReference() + DATA_SEPARATOR);

            // insurance payment value
            letterFile.append(gratuityLetterFileEntry.getInsuranceValue() + DATA_SEPARATOR);

            /*
             * for (Iterator iterator =
             * gratuityLetterFileEntry.getGratuityLetterPaymentPhases().iterator();
             * iterator .hasNext();) {
             * 
             * GratuityLetterPaymentPhase gratuityLetterPaymentPhase =
             * (GratuityLetterPaymentPhase) iterator .next(); // payment phase
             * end date
             * letterFile.append(gratuityLetterPaymentPhase.getEndDate() +
             * DATA_SEPARATOR); // payment phase full sibs reference
             * letterFile.append(gratuityLetterPaymentPhase.getFullSibsReference() +
             * DATA_SEPARATOR); // payment phase value
             * letterFile.append(gratuityLetterPaymentPhase.getValue() +
             * DATA_SEPARATOR); }
             */

            letterFile.append("\n");
        }

        ByteArrayOutputStream file = new ByteArrayOutputStream();

        try {
            file.write(letterFile.toString().getBytes());
        } catch (IOException e) {
            throw new FileNotCreatedServiceException("error.creating.letters.file", e);
        }

        return file.toByteArray();

    }

    /**
     * 
     * @param numberOfPhases
     * @return
     */
    private StringBuilder createLetterFile(int numberOfPhases) {
        StringBuilder file = new StringBuilder();

        // add header
        file.append(STUDENT_NUMBER_COLUMN + COLUMN_SEPARATOR);
        file.append(STUDENT_NAME_COLUMN + COLUMN_SEPARATOR);
        file.append(STUDENT_ADDRESS_COLUMN + COLUMN_SEPARATOR);
        file.append(AREA_COLUMN + COLUMN_SEPARATOR);
        file.append(POSTAL_CODE_COLUMN + COLUMN_SEPARATOR);
        file.append(AREA_POSTAL_CODE_COLUMN + COLUMN_SEPARATOR);
        file.append(DEGREE_COLUMN + COLUMN_SEPARATOR);
        file.append(MASTER_DEGREE_NAME_COLUMN + COLUMN_SEPARATOR);
        file.append(GRATUITY_TOTAL_END_DATE_COLUMN + COLUMN_SEPARATOR);
        file.append(TOTAL_GRATUITY_SIBS_REFERENCE_COLUMN + COLUMN_SEPARATOR);
        file.append(GRATUITY_TOTAL_VALUE_COLUMN + COLUMN_SEPARATOR);
        file.append(INSURANCA_END_DATE_COLUMN + COLUMN_SEPARATOR);
        file.append(INSURANCE_SIBS_REFERENCE_COLUMN + COLUMN_SEPARATOR);
        file.append(INSURANCE_VALUE_COLUMN + COLUMN_SEPARATOR);

        for (int i = 0; i < numberOfPhases; i++) {
            file.append(PHASE_VALUE_BASE_COLUMN + i + COLUMN_SEPARATOR);
            file.append(PHASE_END_DATE_BASE_COLUMN + i + COLUMN_SEPARATOR);
            file.append(PHASE_SIBS_BASE_COLUMN + i + COLUMN_SEPARATOR);
        }

        file.deleteCharAt(file.length() - 1);
        file.append("\n");

        return file;

    }

}