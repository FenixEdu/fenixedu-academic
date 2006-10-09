package net.sourceforge.fenixedu.applicationTier.Servico.manager.gratuity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.gratuity.masterDegree.DuplicateSibsPaymentFileProcessingServiceException;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GratuitySituation;
import net.sourceforge.fenixedu.domain.GratuityValues;
import net.sourceforge.fenixedu.domain.InsuranceValue;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonAccount;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.gratuity.SibsPaymentStatus;
import net.sourceforge.fenixedu.domain.gratuity.SibsPaymentType;
import net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFile;
import net.sourceforge.fenixedu.domain.gratuity.masterDegree.SibsPaymentFileEntry;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.transactions.GratuityTransaction;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;
import net.sourceforge.fenixedu.domain.transactions.PaymentType;
import net.sourceforge.fenixedu.domain.transactions.TransactionType;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.gratuity.fileParsers.sibs.SibsPaymentFileUtils;

public class ProcessSibsPaymentFile extends Service {

    /**
     * Process sibs payment files and creates corresponding payment transactions
     * 
     * @param filename
     * @param fileEntries
     * @throws FenixServiceException
     * @throws ExcepcaoPersistencia
     */
    public void run(String filename, List fileEntries, IUserView userView) throws FenixServiceException,
            ExcepcaoPersistencia {

        if (filename.trim().length() == 0) {
            throw new DuplicateSibsPaymentFileProcessingServiceException(
                    "error.exception.duplicateSibsPaymentFileProcessing");
        }

        final SibsPaymentFile storedPaymentFile = SibsPaymentFile.readByFilename(filename);
        if (storedPaymentFile != null) {
            throw new DuplicateSibsPaymentFileProcessingServiceException(
                    "error.exception.duplicateSibsPaymentFileProcessing");
        }

        SibsPaymentFile sibsPaymentFile = SibsPaymentFileUtils.buildPaymentFile(filename, fileEntries);

        buildTransactionsAndStoreFile(sibsPaymentFile, userView);
    }

    private void buildTransactionsAndStoreFile(SibsPaymentFile sibsPaymentFile, IUserView userView)
            throws ExcepcaoPersistencia {

        List<SibsPaymentFileEntry> sibsPaymentFileEntries = sibsPaymentFile.getSibsPaymentFileEntries();

        int totalPaymentEntries = sibsPaymentFileEntries.size();

        findDuplicatesAndMarkThem(sibsPaymentFileEntries, totalPaymentEntries);

        // lets build transactions for the entries in file

        for (int i = 0; i < totalPaymentEntries; i++) {

            SibsPaymentFileEntry sibsPaymentFileEntry = sibsPaymentFileEntries.get(i);

            if (sibsPaymentFileEntry.getPaymentStatus().equals(SibsPaymentStatus.PROCESSED_PAYMENT) == false) {
                continue;
            }

            // Exception cases should be inserted here
            // e.g. SMS credit payments (i.e. cases where duplicate entry
            // checking is not required)
            // assuming the form:
            // if (isSmsPayment())) {
            // do persistentSupportecific code if any
            // }
            // else { do persistentSupportecific code to insurance and
            // gratuities }

            // DegreeType should be changed in future to support Degree Registration
            // gratuity
            Registration registration = Registration.readStudentByNumberAndDegreeType(sibsPaymentFileEntry
                    .getStudentNumber(), DegreeType.MASTER_DEGREE);

            int year = sibsPaymentFileEntry.getYear().intValue();
            String executionYearName = year + "/" + (year + 1);
            ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(executionYearName);

            if (executionYear == null) {

                // Change status to be solved manually because we could not find
                // execution year
                sibsPaymentFileEntry.setPaymentStatus(SibsPaymentStatus.INVALID_EXECUTION_YEAR);
                continue;
            }

            Person responsiblePerson = Person.readPersonByUsername(userView.getUtilizador());

            PersonAccount personAccount = registration.getPerson().getAssociatedPersonAccount();
            if (personAccount == null) {
                personAccount = new PersonAccount(registration.getPerson());
            }

            if (sibsPaymentFileEntry.getPaymentType().equals(SibsPaymentType.INSURANCE)) {
                InsuranceValue insuranceValue = executionYear.getInsuranceValue();
                List insuranceTransactionList = registration
                        .readAllNonReimbursedInsuranceTransactionsByExecutionYear(executionYear);

                if (insuranceTransactionList.size() > 0) {
                    sibsPaymentFileEntry.setPaymentStatus(SibsPaymentStatus.DUPLICATE_INSURANCE_PAYMENT);

                } else {

                    if (sibsPaymentFileEntry.getPayedValue().equals(insuranceValue.getAnnualValue()) == false) {
                        // the value payed is not valid
                        sibsPaymentFileEntry.setPaymentStatus(SibsPaymentStatus.INVALID_INSURANCE_VALUE);

                    } else {
                        // create the insurance transaction payment for the
                        // execution year
                        new InsuranceTransaction(sibsPaymentFileEntry.getPayedValue(),
                                new Timestamp(new Date().getTime()), null, PaymentType.SIBS,
                                TransactionType.INSURANCE_PAYMENT, new Boolean(false),
                                responsiblePerson, personAccount, null, executionYear, registration);
                    }
                }
                continue;
            }

            Specialization specialization = determineSpecialization(sibsPaymentFileEntry);

            // DegreeType should be changed in future to meet Degree gratuity
            // requirements
            List<StudentCurricularPlan> studentCurricularPlanList = registration.getStudentCurricularPlans();

            List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>();
            List<StudentCurricularPlan> studentCurricularPlans = new ArrayList<StudentCurricularPlan>();
            for (StudentCurricularPlan studentCurricularPlan : studentCurricularPlanList) {

                if (!studentCurricularPlan.getSpecialization().equals(specialization)) {
                    continue;
                }

                ExecutionDegree candidateExecutionDegree = ExecutionDegree
                        .getByDegreeCurricularPlanAndExecutionYear(studentCurricularPlan
                                .getDegreeCurricularPlan(), executionYear.getYear());

                if (candidateExecutionDegree != null) {
                    executionDegrees.add(candidateExecutionDegree);
                    studentCurricularPlans.add(studentCurricularPlan);
                }
            }

            if ((executionDegrees.size() == 0) || (studentCurricularPlans.size() == 0)) {
                // Change status to be solved manually because we could not
                // decide the student curricular plan
                sibsPaymentFileEntry.setPaymentStatus(SibsPaymentStatus.INVALID_EXECUTION_DEGREE);
                continue;
            }

            if ((executionDegrees.size() > 1) || (studentCurricularPlans.size() > 1)) {
                // Change status to be solved manually because we could not
                // decide the student curricular plan
                sibsPaymentFileEntry
                        .setPaymentStatus(SibsPaymentStatus.UNABLE_TO_DETERMINE_STUDENT_CURRICULAR_PLAN);
                continue;
            }

            ExecutionDegree executionDegree = executionDegrees.get(0);
            StudentCurricularPlan studentCurricularPlan = studentCurricularPlans.get(0);

            GratuityValues gratuityValues = executionDegree.getGratuityValues();
            GratuitySituation gratuitySituation = studentCurricularPlan
                    .getGratuitySituationByGratuityValues(gratuityValues);
            if (gratuitySituation == null) {
                // Change status to be solved manually because the student does
                // not have a gratuity situation
                sibsPaymentFileEntry
                        .setPaymentStatus(SibsPaymentStatus.UNABLE_TO_DETERMINE_STUDENT_CURRICULAR_PLAN);
                continue;
            }

            TransactionType transactionType = bindSibsCodeTypeToTransactionCodeType(sibsPaymentFileEntry
                    .getPaymentType());

            new GratuityTransaction(sibsPaymentFileEntry.getPayedValue(), new Timestamp(
                    new Date().getTime()), null, PaymentType.SIBS, transactionType, new Boolean(false),
                    responsiblePerson, personAccount, null, gratuitySituation);

            // update remaining value of gratuity
            double oldRemainingValue = 0;
            if (gratuitySituation.getRemainingValue() != null) {
                oldRemainingValue = gratuitySituation.getRemainingValue().doubleValue();
            }
            double newRemainingValue = oldRemainingValue
                    - sibsPaymentFileEntry.getPayedValue().doubleValue();

            gratuitySituation.setRemainingValue(new Double(newRemainingValue));

        }

    }

    private void findDuplicatesAndMarkThem(List<SibsPaymentFileEntry> sibsPaymentFileEntries,
            int totalPaymentEntries) throws ExcepcaoPersistencia {
        for (int i = 0; i < totalPaymentEntries; i++) {

            SibsPaymentFileEntry sibsPaymentFileEntry = sibsPaymentFileEntries.get(i);

            if (sibsPaymentFileEntry.getPaymentStatus().equals(SibsPaymentStatus.NOT_PROCESSED_PAYMENT)) {

                sibsPaymentFileEntry.setPaymentStatus(SibsPaymentStatus.PROCESSED_PAYMENT);

                // Exception cases should be inserted here
                // e.g. SMS credit payments (i.e. cases where duplicate entry
                // checking is not required)
                // assuming the form:
                // if (isSmsPayment())) {
                // do persistentSupportecific code if any
                // }
                // else { do persistentSupportecific code to insurance and
                // gratuities }

                markDuplicateGratuityAndInsurancePayments(sibsPaymentFileEntry, sibsPaymentFileEntries,
                        totalPaymentEntries, i);
            }
        }
    }

    private TransactionType bindSibsCodeTypeToTransactionCodeType(SibsPaymentType sibsPaymentType) {
        // in future if codes change too much, the binding table should be
        // loaded from a config file
        TransactionType transactionType = null;

        if (sibsPaymentType.equals(SibsPaymentType.INSURANCE)) {

            transactionType = TransactionType.INSURANCE_PAYMENT;

        } else if ((sibsPaymentType.equals(SibsPaymentType.MASTER_DEGREE_GRATUTITY_FIRST_PHASE))
                || (sibsPaymentType.equals(SibsPaymentType.SPECIALIZATION_GRATUTITY_FIRST_PHASE))) {

            transactionType = TransactionType.GRATUITY_FIRST_PHASE_PAYMENT;

        } else if ((sibsPaymentType.equals(SibsPaymentType.MASTER_DEGREE_GRATUTITY_SECOND_PHASE))
                || (sibsPaymentType.equals(SibsPaymentType.SPECIALIZATION_GRATUTITY_SECOND_PHASE))) {

            transactionType = TransactionType.GRATUITY_SECOND_PHASE_PAYMENT;

        } else if ((sibsPaymentType.equals(SibsPaymentType.MASTER_DEGREE_GRATUTITY_TOTAL))
                || (sibsPaymentType.equals(SibsPaymentType.SPECIALIZATION_GRATUTITY_TOTAL))) {

            transactionType = TransactionType.GRATUITY_FULL_PAYMENT;
        }

        return transactionType;
    }

    private void markDuplicateGratuityAndInsurancePayments(SibsPaymentFileEntry sibsPaymentFileEntry,
            List sibsPaymentFileEntries, int totalPaymentEntries, int currentIndex)
            throws ExcepcaoPersistencia {

        // first check if the gratuity or insurance payment is repeated inside
        // the file
        for (int j = currentIndex + 1; j < totalPaymentEntries; j++) {
            SibsPaymentFileEntry duplicatePaymentCandidate = (SibsPaymentFileEntry) sibsPaymentFileEntries
                    .get(j);

            if (sibsPaymentFileEntry.getYear().equals(duplicatePaymentCandidate.getYear())
                    && sibsPaymentFileEntry.getStudentNumber().equals(
                            duplicatePaymentCandidate.getStudentNumber())
                    && sibsPaymentFileEntry.getPaymentType().equals(
                            duplicatePaymentCandidate.getPaymentType())) {

                if (sibsPaymentFileEntry.getPaymentType().equals(SibsPaymentType.INSURANCE)) {

                    sibsPaymentFileEntry.setPaymentStatus(SibsPaymentStatus.DUPLICATE_INSURANCE_PAYMENT);
                } else {
                    sibsPaymentFileEntry.setPaymentStatus(SibsPaymentStatus.DUPLICATE_GRATUITY_PAYMENT);
                }

            }
        }

        if (sibsPaymentFileEntry.getPaymentStatus()
                .equals(SibsPaymentStatus.DUPLICATE_INSURANCE_PAYMENT)
                || sibsPaymentFileEntry.getPaymentStatus().equals(
                        SibsPaymentStatus.DUPLICATE_GRATUITY_PAYMENT)) {
            // the entry is already marked
            return;
        }

        // next check if the gratuity or insurance payment is repeated in
        // database
        List sibsPaymentFileEntryList = SibsPaymentFileEntry
                .readByYearAndStudentNumberAndPaymentTypeExceptFileEntry(sibsPaymentFileEntry);
        if (sibsPaymentFileEntryList.size() > 0) {
            if (sibsPaymentFileEntry.getPaymentType().equals(SibsPaymentType.INSURANCE)) {
                sibsPaymentFileEntry.setPaymentStatus(SibsPaymentStatus.DUPLICATE_INSURANCE_PAYMENT);
            } else {
                sibsPaymentFileEntry.setPaymentStatus(SibsPaymentStatus.DUPLICATE_GRATUITY_PAYMENT);
            }
        }
    }

    private Specialization determineSpecialization(SibsPaymentFileEntry sibsPaymentFileEntry) {
        // if sibs payment codes change to much in future this logic should be
        // moved to a config file
        SibsPaymentType sibsPaymentType = sibsPaymentFileEntry.getPaymentType();
        if (sibsPaymentType.equals(SibsPaymentType.SPECIALIZATION_GRATUTITY_TOTAL)
                || sibsPaymentType.equals(SibsPaymentType.SPECIALIZATION_GRATUTITY_FIRST_PHASE)
                || sibsPaymentType.equals(SibsPaymentType.SPECIALIZATION_GRATUTITY_SECOND_PHASE)) {
            return Specialization.STUDENT_CURRICULAR_PLAN_SPECIALIZATION;
        }
        return Specialization.STUDENT_CURRICULAR_PLAN_MASTER_DEGREE;
        // degree code goes here
    }

}