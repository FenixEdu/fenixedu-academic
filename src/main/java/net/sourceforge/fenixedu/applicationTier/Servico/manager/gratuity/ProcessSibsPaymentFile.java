package net.sourceforge.fenixedu.applicationTier.Servico.manager.gratuity;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.List;

import org.fenixedu.bennu.core.domain.User;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;

public class ProcessSibsPaymentFile {

    /**
     * Process sibs payment files and creates corresponding payment transactions
     * 
     * @param filename
     * @param fileEntries
     * @throws FenixServiceException
     * @throws ExcepcaoPersistencia
     */
    @Atomic
    public static void run(String filename, List fileEntries, User userView) throws FenixServiceException {
        check(RolePredicates.MANAGER_PREDICATE);

        throw new UnsupportedOperationException("TO REMOVE");
        //
        // if (filename.trim().length() == 0) {
        // throw new DuplicateSibsPaymentFileProcessingServiceException(
        // "error.exception.duplicateSibsPaymentFileProcessing");
        // }
        //
        // final SibsPaymentFile storedPaymentFile =
        // SibsPaymentFile.readByFilename(filename);
        // if (storedPaymentFile != null) {
        // throw new DuplicateSibsPaymentFileProcessingServiceException(
        // "error.exception.duplicateSibsPaymentFileProcessing");
        // }
        //
        // SibsPaymentFile sibsPaymentFile =
        // SibsPaymentFileUtils.buildPaymentFile(filename, fileEntries);
        //
        // buildTransactionsAndStoreFile(sibsPaymentFile, userView);
    }
//
//    private static void buildTransactionsAndStoreFile(SibsPaymentFile sibsPaymentFile, User userView) {
//
//        Collection<SibsPaymentFileEntry> sibsPaymentFileEntries = sibsPaymentFile.getSibsPaymentFileEntries();
//
//        int totalPaymentEntries = sibsPaymentFileEntries.size();
//
//        findDuplicatesAndMarkThem(sibsPaymentFileEntries, totalPaymentEntries);
//
//        // lets build transactions for the entries in file
//
//        for (int i = 0; i < totalPaymentEntries; i++) {
//
//            SibsPaymentFileEntry sibsPaymentFileEntry = sibsPaymentFileEntries.get(i);
//
//            if (sibsPaymentFileEntry.getPaymentStatus().equals(SibsPaymentStatus.PROCESSED_PAYMENT) == false) {
//                continue;
//            }
//
//            // Exception cases should be inserted here
//            // e.g. SMS credit payments (i.e. cases where duplicate entry
//            // checking is not required)
//            // assuming the form:
//            // if (isSmsPayment())) {
//            // do persistentSupportecific code if any
//            // }
//            // else { do persistentSupportecific code to insurance and
//            // gratuities }
//
//            // DegreeType should be changed in future to support Degree
//            // Registration
//            // gratuity
//            Registration registration =
//                    Registration.readStudentByNumberAndDegreeType(sibsPaymentFileEntry.getStudentNumber(),
//                            DegreeType.MASTER_DEGREE);
//
//            int year = sibsPaymentFileEntry.getYear().intValue();
//            String executionYearName = year + "/" + (year + 1);
//            ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(executionYearName);
//
//            if (executionYear == null) {
//
//                // Change status to be solved manually because we could not find
//                // execution year
//                sibsPaymentFileEntry.setPaymentStatus(SibsPaymentStatus.INVALID_EXECUTION_YEAR);
//                continue;
//            }
//
//            Person responsiblePerson = Person.readPersonByUsername(userView.getUsername());
//
//            PersonAccount personAccount = registration.getPerson().getAssociatedPersonAccount();
//            if (personAccount == null) {
//                personAccount = new PersonAccount(registration.getPerson());
//            }
//
//            if (sibsPaymentFileEntry.getPaymentType().equals(SibsPaymentType.INSURANCE)) {
//                InsuranceValue insuranceValue = executionYear.getInsuranceValue();
//                List insuranceTransactionList =
//                        registration.readAllNonReimbursedInsuranceTransactionsByExecutionYear(executionYear);
//
//                if (insuranceTransactionList.size() > 0) {
//                    sibsPaymentFileEntry.setPaymentStatus(SibsPaymentStatus.DUPLICATE_INSURANCE_PAYMENT);
//
//                } else {
//
//                    if (sibsPaymentFileEntry.getPayedValue().equals(insuranceValue.getAnnualValue()) == false) {
//                        // the value payed is not valid
//                        sibsPaymentFileEntry.setPaymentStatus(SibsPaymentStatus.INVALID_INSURANCE_VALUE);
//
//                    } else {
//                        // create the insurance transaction payment for the
//                        // execution year
//                        new InsuranceTransaction(sibsPaymentFileEntry.getPayedValue(), new Timestamp(new Date().getTime()), null,
//                                PaymentType.SIBS, TransactionType.INSURANCE_PAYMENT, new Boolean(false), responsiblePerson,
//                                personAccount, null, executionYear, registration);
//                    }
//                }
//                continue;
//            }
//
//            Specialization specialization = determineSpecialization(sibsPaymentFileEntry);
//
//            // DegreeType should be changed in future to meet Degree gratuity
//            // requirements
//            Collection<StudentCurricularPlan> studentCurricularPlanList = registration.getStudentCurricularPlans();
//
//            List<ExecutionDegree> executionDegrees = new ArrayList<ExecutionDegree>();
//            List<StudentCurricularPlan> studentCurricularPlans = new ArrayList<StudentCurricularPlan>();
//            for (StudentCurricularPlan studentCurricularPlan : studentCurricularPlanList) {
//
//                if (!studentCurricularPlan.getSpecialization().equals(specialization)) {
//                    continue;
//                }
//
//                ExecutionDegree candidateExecutionDegree =
//                        ExecutionDegree.getByDegreeCurricularPlanAndExecutionYear(
//                                studentCurricularPlan.getDegreeCurricularPlan(), executionYear.getYear());
//
//                if (candidateExecutionDegree != null) {
//                    executionDegrees.add(candidateExecutionDegree);
//                    studentCurricularPlans.add(studentCurricularPlan);
//                }
//            }
//
//            if ((executionDegrees.size() == 0) || (studentCurricularPlans.size() == 0)) {
//                // Change status to be solved manually because we could not
//                // decide the student curricular plan
//                sibsPaymentFileEntry.setPaymentStatus(SibsPaymentStatus.INVALID_EXECUTION_DEGREE);
//                continue;
//            }
//
//            if ((executionDegrees.size() > 1) || (studentCurricularPlans.size() > 1)) {
//                // Change status to be solved manually because we could not
//                // decide the student curricular plan
//                sibsPaymentFileEntry.setPaymentStatus(SibsPaymentStatus.UNABLE_TO_DETERMINE_STUDENT_CURRICULAR_PLAN);
//                continue;
//            }
//
//            ExecutionDegree executionDegree = executionDegrees.iterator().next();
//            StudentCurricularPlan studentCurricularPlan = studentCurricularPlans.iterator().next();
//
//            GratuityValues gratuityValues = executionDegree.getGratuityValues();
//            GratuitySituation gratuitySituation = studentCurricularPlan.getGratuitySituationByGratuityValues(gratuityValues);
//            if (gratuitySituation == null) {
//                // Change status to be solved manually because the student does
//                // not have a gratuity situation
//                sibsPaymentFileEntry.setPaymentStatus(SibsPaymentStatus.UNABLE_TO_DETERMINE_STUDENT_CURRICULAR_PLAN);
//                continue;
//            }
//
//            TransactionType transactionType = sibsPaymentFileEntry.getPaymentType().getTransactionType();
//
//            new GratuityTransaction(sibsPaymentFileEntry.getPayedValue(), new Timestamp(new Date().getTime()), null,
//                    PaymentType.SIBS, transactionType, new Boolean(false), responsiblePerson, personAccount, null,
//                    gratuitySituation);
//
//            gratuitySituation.updateValues();
//
//        }
//
//    }

//    private static void findDuplicatesAndMarkThem(Collection<SibsPaymentFileEntry> sibsPaymentFileEntries, int totalPaymentEntries) {
//        for (int i = 0; i < totalPaymentEntries; i++) {
//
//            SibsPaymentFileEntry sibsPaymentFileEntry = sibsPaymentFileEntries.get(i);
//
//            if (sibsPaymentFileEntry.getPaymentStatus().equals(SibsPaymentStatus.NOT_PROCESSED_PAYMENT)) {
//
//                sibsPaymentFileEntry.setPaymentStatus(SibsPaymentStatus.PROCESSED_PAYMENT);
//
//                // Exception cases should be inserted here
//                // e.g. SMS credit payments (i.e. cases where duplicate entry
//                // checking is not required)
//                // assuming the form:
//                // if (isSmsPayment())) {
//                // do persistentSupportecific code if any
//                // }
//                // else { do persistentSupportecific code to insurance and
//                // gratuities }
//
//                markDuplicateGratuityAndInsurancePayments(sibsPaymentFileEntry, sibsPaymentFileEntries, totalPaymentEntries, i);
//            }
//        }
//    }
//
//    private static void markDuplicateGratuityAndInsurancePayments(SibsPaymentFileEntry sibsPaymentFileEntry,
//            List sibsPaymentFileEntries, int totalPaymentEntries, int currentIndex) {
//
//        // first check if the gratuity or insurance payment is repeated inside
//        // the file
//        for (int j = currentIndex + 1; j < totalPaymentEntries; j++) {
//            SibsPaymentFileEntry duplicatePaymentCandidate = (SibsPaymentFileEntry) sibsPaymentFileEntries.get(j);
//
//            if (sibsPaymentFileEntry.getYear().equals(duplicatePaymentCandidate.getYear())
//                    && sibsPaymentFileEntry.getStudentNumber().equals(duplicatePaymentCandidate.getStudentNumber())
//                    && sibsPaymentFileEntry.getPaymentType().equals(duplicatePaymentCandidate.getPaymentType())) {
//
//                if (sibsPaymentFileEntry.getPaymentType().equals(SibsPaymentType.INSURANCE)) {
//
//                    sibsPaymentFileEntry.setPaymentStatus(SibsPaymentStatus.DUPLICATE_INSURANCE_PAYMENT);
//                } else {
//                    sibsPaymentFileEntry.setPaymentStatus(SibsPaymentStatus.DUPLICATE_GRATUITY_PAYMENT);
//                }
//
//            }
//        }
//
//        if (sibsPaymentFileEntry.getPaymentStatus().equals(SibsPaymentStatus.DUPLICATE_INSURANCE_PAYMENT)
//                || sibsPaymentFileEntry.getPaymentStatus().equals(SibsPaymentStatus.DUPLICATE_GRATUITY_PAYMENT)) {
//            // the entry is already marked
//            return;
//        }
//
//        // next check if the gratuity or insurance payment is repeated in
//        // database
//        List sibsPaymentFileEntryList =
//                SibsPaymentFileEntry.readByYearAndStudentNumberAndPaymentTypeExceptFileEntry(sibsPaymentFileEntry);
//        if (sibsPaymentFileEntryList.size() > 0) {
//            if (sibsPaymentFileEntry.getPaymentType().equals(SibsPaymentType.INSURANCE)) {
//                sibsPaymentFileEntry.setPaymentStatus(SibsPaymentStatus.DUPLICATE_INSURANCE_PAYMENT);
//            } else {
//                sibsPaymentFileEntry.setPaymentStatus(SibsPaymentStatus.DUPLICATE_GRATUITY_PAYMENT);
//            }
//        }
//    }
//
//    private static Specialization determineSpecialization(SibsPaymentFileEntry sibsPaymentFileEntry) {
//        // if sibs payment codes change to much in future this logic should be
//        // moved to a config file
//        SibsPaymentType sibsPaymentType = sibsPaymentFileEntry.getPaymentType();
//        if (sibsPaymentType.equals(SibsPaymentType.SPECIALIZATION_GRATUTITY_TOTAL)
//                || sibsPaymentType.equals(SibsPaymentType.SPECIALIZATION_GRATUTITY_FIRST_PHASE)
//                || sibsPaymentType.equals(SibsPaymentType.SPECIALIZATION_GRATUTITY_SECOND_PHASE)) {
//            return Specialization.STUDENT_CURRICULAR_PLAN_SPECIALIZATION;
//        }
//        return Specialization.STUDENT_CURRICULAR_PLAN_MASTER_DEGREE;
//        // degree code goes here
//    }

}