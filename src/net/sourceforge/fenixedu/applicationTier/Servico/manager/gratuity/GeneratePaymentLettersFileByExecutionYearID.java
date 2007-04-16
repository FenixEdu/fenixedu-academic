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
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.gratuity.SibsPaymentType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.gratuity.fileParsers.sibs.SibsOutgoingPaymentFileConstants;
import pt.ist.utl.fenix.utils.SibsPaymentCodeFactory;

public class GeneratePaymentLettersFileByExecutionYearID extends Service {

    private class GratuityLetterFileEntry {

        private int numberOfPaymentPhases;

        private StudentCurricularPlan studentCurricularPlan;

        private String totalGratuityValue;

        private String totalGratuityFullSibsReference;

        private String totalGratuityEndDate;

        private String insuranceEndDate;

        private String insuranceFullSibsReference;

        private String insuranceValue;

        public GratuityLetterFileEntry(StudentCurricularPlan studentCurricularPlan) {
            this.numberOfPaymentPhases = 0;
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

        String shortYear = executionYear.getYear().split("/")[0].trim().substring(2);

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
                                .format(insuranceValue.getAnnualValueBigDecimal().doubleValue()));

                    }
                }

                GratuityLetterFileEntry gratuityLetterFileEntry = null;

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

    private GratuityLetterFileEntry createGratuityLetterFileEntryForGratuitySituation(
            GratuitySituation gratuitySituation, String shortYear, Date totalPaymentEndDate)
            throws InsufficientSibsPaymentPhaseCodesServiceException {

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

        return gratuityLetterFileEntry;

    }

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

    private byte[] writeLetterFiles(List gratuityLetterFileEntries, ExecutionYear executionYear)
            throws FileNotCreatedServiceException {
        StringBuilder letterFile = createLetterFile(0); // this needs cleanup.

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
            final Person person = gratuityLetterFileEntry.getStudentCurricularPlan().getRegistration().getPerson();
            if (person.hasDefaultPhysicalAddress()) {
        	final PhysicalAddress physicalAddress = person.getDefaultPhysicalAddress();
        	letterFile.append(physicalAddress.getAddress()).append(DATA_SEPARATOR);
        	letterFile.append(physicalAddress.getArea()).append(DATA_SEPARATOR);
        	letterFile.append(physicalAddress.getAreaCode()).append(DATA_SEPARATOR);
        	letterFile.append(physicalAddress.getAreaOfAreaCode()).append(DATA_SEPARATOR);
            } else {
        	letterFile.append(DATA_SEPARATOR);
        	letterFile.append(DATA_SEPARATOR);
        	letterFile.append(DATA_SEPARATOR);
        	letterFile.append(DATA_SEPARATOR);
            }
            
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