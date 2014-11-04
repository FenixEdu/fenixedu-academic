/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.student.importation;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.fenixedu.academic.dto.accounting.EntryDTO;
import org.fenixedu.academic.dto.accounting.EntryWithInstallmentDTO;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Employee;
import org.fenixedu.academic.domain.EntryPhase;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.QueueJob;
import org.fenixedu.academic.domain.QueueJobResult;
import org.fenixedu.academic.domain.TutorshipIntention;
import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.EventType;
import org.fenixedu.academic.domain.accounting.Installment;
import org.fenixedu.academic.domain.accounting.PaymentCodeType;
import org.fenixedu.academic.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import org.fenixedu.academic.domain.accounting.paymentCodes.InstallmentPaymentCode;
import org.fenixedu.academic.domain.accounting.paymentPlans.GratuityPaymentPlan;
import org.fenixedu.academic.domain.accounting.postingRules.AdministrativeOfficeFeeAndInsurancePR;
import org.fenixedu.academic.domain.accounting.postingRules.AdministrativeOfficeFeePR;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.candidacy.Candidacy;
import org.fenixedu.academic.domain.candidacy.DegreeCandidacy;
import org.fenixedu.academic.domain.candidacy.IMDCandidacy;
import org.fenixedu.academic.domain.candidacy.StandByCandidacySituation;
import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.UnitUtils;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.student.PrecedentDegreeInformation;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.util.FenixConfigurationManager;
import org.fenixedu.academic.util.Money;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class DgesStudentImportationProcess extends DgesStudentImportationProcess_Base {

    private static final Logger logger = LoggerFactory.getLogger(DgesStudentImportationProcess.class);

    protected DgesStudentImportationProcess() {
        super();
    }

    protected DgesStudentImportationProcess(final ExecutionYear executionYear, final Space campus, final EntryPhase entryPhase,
            DgesStudentImportationFile dgesStudentImportationFile) {
        this();

        init(executionYear, campus, entryPhase, dgesStudentImportationFile);
    }

    protected void init(final ExecutionYear executionYear, final Space campus, final EntryPhase entryPhase,
            DgesStudentImportationFile dgesStudentImportationFile) {
        super.init(executionYear, entryPhase);
        String[] args = new String[0];

        if (campus == null) {
            throw new DomainException("error.DgesStudentImportationProcess.campus.is.null", args);
        }
        String[] args1 = {};
        if (dgesStudentImportationFile == null) {
            throw new DomainException("error.DgesStudentImportationProcess.importation.file.is.null", args1);
        }

        setDgesStudentImportationForCampus(campus);
        setDgesStudentImportationFile(dgesStudentImportationFile);
    }

    @Override
    public QueueJobResult execute() throws Exception {

        ByteArrayOutputStream stream = null;
        PrintWriter LOG_WRITER = null;
        try {
            stream = new ByteArrayOutputStream();
            LOG_WRITER = new PrintWriter(new BufferedOutputStream(stream));

            importCandidates(LOG_WRITER);
        } catch (Throwable a) {
            logger.error(a.getMessage(), a);
            throw new RuntimeException(a);
        }

        finally {
            if (LOG_WRITER != null) {
                LOG_WRITER.close();
            }
            stream.close();
        }

        final QueueJobResult queueJobResult = new QueueJobResult();
        queueJobResult.setContentType("text/plain");

        queueJobResult.setContent(stream.toByteArray());

        stream.close();
        return queueJobResult;
    }

    @Override
    public String getFilename() {
        return "DgesStudentImportationProcess_result_" + getExecutionYear().getName().replaceAll("/", "-") + ".txt";
    }

    public void importCandidates(final PrintWriter LOG_WRITER) {

        final List<DegreeCandidateDTO> degreeCandidateDTOs =
                parseDgesFile(getDgesStudentImportationFile().getContents(), getUniversityAcronym(), getEntryPhase());

        final Employee employee = Employee.readByNumber(4581);

        LOG_WRITER.println(String.format("DGES Entries for %s : %s", getDgesStudentImportationForCampus().getName(),
                degreeCandidateDTOs.size()));

        createDegreeCandidacies(LOG_WRITER, employee, degreeCandidateDTOs);
        distributeTutorshipIntentions(degreeCandidateDTOs);
    }

    private void distributeTutorshipIntentions(List<DegreeCandidateDTO> degreeCandidateDTOs) {
        if (getEntryPhase().equals(EntryPhase.FIRST_PHASE)) {
            HashMap<ExecutionDegree, Integer> studentsPerExecution = new HashMap<ExecutionDegree, Integer>();
            for (final DegreeCandidateDTO degreeCandidateDTO : degreeCandidateDTOs) {
                final ExecutionDegree executionDegree =
                        degreeCandidateDTO.getExecutionDegree(getExecutionYear(), getDgesStudentImportationForCampus());
                Integer numberOfStudents = studentsPerExecution.get(executionDegree);
                if (numberOfStudents != null) {
                    numberOfStudents++;
                } else {
                    numberOfStudents = 1;
                }
                studentsPerExecution.put(executionDegree, numberOfStudents);
            }

            for (ExecutionDegree executionDegree : studentsPerExecution.keySet()) {
                int numberOfStudents = studentsPerExecution.get(executionDegree);
                int numberOfTutors = TutorshipIntention.getTutorshipIntentions(executionDegree).size();
                if (numberOfTutors > 0) {
                    int exceedingStudents = numberOfStudents % numberOfTutors;
                    int studentPerTutor = numberOfStudents / numberOfTutors;
                    for (TutorshipIntention tutorshipIntention : TutorshipIntention.getTutorshipIntentions(executionDegree)) {
                        tutorshipIntention.setMaxStudentsToTutor(studentPerTutor);
                        if (exceedingStudents > 0) {
                            tutorshipIntention.setMaxStudentsToTutor(tutorshipIntention.getMaxStudentsToTutor() + 1);
                            exceedingStudents--;
                        }
                    }
                }
            }
        }
    }

    private void createDegreeCandidacies(final PrintWriter LOG_WRITER, final Employee employee,
            final List<DegreeCandidateDTO> degreeCandidateDTOs) {
        int processed = 0;
        int personsCreated = 0;

        String prefix = FenixConfigurationManager.getConfiguration().dgesUsernamePrefix();

        for (final DegreeCandidateDTO degreeCandidateDTO : degreeCandidateDTOs) {

            if (++processed % 150 == 0) {
                logger.info("Processed :" + processed);
            }

            int studentNumber = Student.generateStudentNumber();

            logCandidate(LOG_WRITER, degreeCandidateDTO);

            Person person = null;
            try {
                person = degreeCandidateDTO.getMatchingPerson();
                // Person may not yet have a user, so we will create it
                if (person.getUser() == null) {
                    person.setUser(new User(prefix + studentNumber));
                }
            } catch (DegreeCandidateDTO.NotFoundPersonException e) {
                person = degreeCandidateDTO.createPerson(prefix + studentNumber);
                logCreatedPerson(LOG_WRITER, person);
                personsCreated++;
            } catch (DegreeCandidateDTO.TooManyMatchedPersonsException e) {
                logTooManyMatchsForCandidate(LOG_WRITER, degreeCandidateDTO);
                continue;
            } catch (DegreeCandidateDTO.MatchingPersonException e) {
                throw new RuntimeException(e);
            }

            if (person.getStudent() != null && !person.getStudent().getRegistrationsSet().isEmpty()) {
                logCandidateIsStudentWithRegistrationAlreadyExists(LOG_WRITER, degreeCandidateDTO, person);
                continue;
            }

            if (person.getTeacher() != null || person.hasRole(RoleType.TEACHER)) {
                logCandidateIsTeacher(LOG_WRITER, degreeCandidateDTO, person);
                continue;
            }

            if (person.hasRole(RoleType.EMPLOYEE) || person.getEmployee() != null) {
                logCandidateIsEmployee(LOG_WRITER, degreeCandidateDTO, person);
            }

            RoleType.grant(RoleType.CANDIDATE, person.getUser());

            if (person.getStudent() == null) {
                // Ensure that the same student number is created
                new Student(person, studentNumber);
                logCreatedStudent(LOG_WRITER, person.getStudent());
            }

            voidPreviousCandidacies(person,
                    degreeCandidateDTO.getExecutionDegree(getExecutionYear(), getDgesStudentImportationForCampus()));

            final StudentCandidacy studentCandidacy = createCandidacy(employee, degreeCandidateDTO, person);
            new StandByCandidacySituation(studentCandidacy, employee.getPerson());

            createAvailableAccountingEventsPaymentCodes(person, studentCandidacy);
            createAdministrativeOfficeFeePaymentCode(person, studentCandidacy);
        }
    }

    private void createAdministrativeOfficeFeePaymentCode(Person person, StudentCandidacy studentCandidacy) {
        AdministrativeOffice office = getAdministrativeOffice(studentCandidacy.getDegreeCurricularPlan());
        AdministrativeOfficeFeeAndInsurancePR administrativeOfficePostingRule =
                findAdministrativeOfficeFeeAndInsurancePostingRule(office);

        studentCandidacy.addAvailablePaymentCodes(AccountingEventPaymentCode.create(
                PaymentCodeType.ADMINISTRATIVE_OFFICE_FEE_AND_INSURANCE, new YearMonthDay(), administrativeOfficePostingRule
                        .getAdministrativeOfficeFeePaymentLimitDate(getExecutionYear().getBeginDateYearMonthDay()
                                .toDateTimeAtMidnight(), getExecutionYear().getEndDateYearMonthDay().toDateTimeAtMidnight()),
                null, calculateAdministrativeOfficeFeeAndInsuranceAmount(office),
                calculateAdministrativeOfficeFeeAndInsuranceAmount(office), person));
    }

    private Money calculateAdministrativeOfficeFeeAndInsuranceAmount(final AdministrativeOffice office) {
        return calculateAdministrativeOfficeFeeAmount(office).add(calculateInsuranceAmount(office));
    }

    private Money calculateAdministrativeOfficeFeeAmount(AdministrativeOffice office) {
        return findAdministrativeOfficeFeePostingRule(office).getFixedAmount();
    }

    private AdministrativeOfficeFeeAndInsurancePR findAdministrativeOfficeFeeAndInsurancePostingRule(AdministrativeOffice office) {
        return (AdministrativeOfficeFeeAndInsurancePR) office.getServiceAgreementTemplate().findPostingRuleByEventType(
                EventType.ADMINISTRATIVE_OFFICE_FEE_INSURANCE);
    }

    private AdministrativeOfficeFeePR findAdministrativeOfficeFeePostingRule(AdministrativeOffice office) {
        return (AdministrativeOfficeFeePR) office.getServiceAgreementTemplate().findPostingRuleByEventType(
                EventType.ADMINISTRATIVE_OFFICE_FEE);
    }

    private Money calculateInsuranceAmount(AdministrativeOffice office) {
        AdministrativeOfficeFeeAndInsurancePR feeAndInsurancePostingRule =
                findAdministrativeOfficeFeeAndInsurancePostingRule(office);
        return feeAndInsurancePostingRule.getInsuranceAmount(
                getExecutionYear().getBeginDateYearMonthDay().toDateTimeAtMidnight(), getExecutionYear().getEndDateYearMonthDay()
                        .toDateTimeAtMidnight());
    }

    private AdministrativeOffice getAdministrativeOffice(final DegreeCurricularPlan degreeCurricularPlan) {
        return degreeCurricularPlan.getDegree().getAdministrativeOffice();
    }

    private void createAvailableAccountingEventsPaymentCodes(final Person person, final StudentCandidacy studentCandidacy) {
        ExecutionDegree executionDegree = studentCandidacy.getExecutionDegree();

        GratuityPaymentPlan paymentPlan = getGratuityPaymentPlanForFirstTimeStudents(executionDegree);

        Money totalAmount = Money.ZERO;
        LabelFormatter descriptionForEntryType = getDescriptionForEntryType(executionDegree.getDegree(), EntryType.GRATUITY_FEE);
        for (Installment installment : paymentPlan.getInstallmentsSortedByEndDate()) {
            EntryWithInstallmentDTO entryDTO =
                    new EntryWithInstallmentDTO(EntryType.GRATUITY_FEE, null, installment.getAmount(), descriptionForEntryType,
                            installment);
            studentCandidacy.addAvailablePaymentCodes(createInstallmentPaymentCode(entryDTO, person.getStudent()));
            totalAmount = totalAmount.add(installment.getAmount());
        }

        EntryDTO fullPaymentEntryDTO =
                new EntryDTO(EntryType.GRATUITY_FEE, null, totalAmount, Money.ZERO, totalAmount, descriptionForEntryType,
                        totalAmount);

        studentCandidacy.addAvailablePaymentCodes(createAccountingEventPaymentCode(fullPaymentEntryDTO, person.getStudent(),
                paymentPlan));
    }

    private GratuityPaymentPlan getGratuityPaymentPlanForFirstTimeStudents(final ExecutionDegree executionDegree) {
        List<GratuityPaymentPlan> paymentPlanList =
                executionDegree.getDegreeCurricularPlan().getServiceAgreementTemplate()
                        .getGratuityPaymentPlansFor(getExecutionYear());

        for (GratuityPaymentPlan paymentPlan : paymentPlanList) {
            if (paymentPlan.isForFirstTimeInstitutionStudents()) {
                return paymentPlan;
            }
        }

        return null;
    }

    private void logCreatedStudent(final PrintWriter LOG_WRITER, final Student student) {
        LOG_WRITER.println("Created student");
    }

    private void logCreatedPerson(final PrintWriter LOG_WRITER, final Person person) {
        LOG_WRITER.println("Created person");
    }

    private void logCandidate(final PrintWriter LOG_WRITER, DegreeCandidateDTO degreeCandidateDTO) {
        LOG_WRITER.println("-------------------------------------------------------------------");
        LOG_WRITER.println("Processing: " + degreeCandidateDTO.toString());
    }

    private StudentCandidacy createCandidacy(final Employee employee, final DegreeCandidateDTO degreeCandidateDTO,
            final Person person) {
        final ExecutionDegree executionDegree =
                degreeCandidateDTO.getExecutionDegree(getExecutionYear(), getDgesStudentImportationForCampus());
        StudentCandidacy candidacy = null;

        if (executionDegree.getDegree().getDegreeType() == DegreeType.BOLONHA_DEGREE) {
            candidacy =
                    new DegreeCandidacy(person, executionDegree, employee.getPerson(), degreeCandidateDTO.getEntryGrade(),
                            degreeCandidateDTO.getContigent(), degreeCandidateDTO.getIngression(),
                            degreeCandidateDTO.getEntryPhase(), degreeCandidateDTO.getPlacingOption());

        } else if (executionDegree.getDegree().getDegreeType() == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) {
            candidacy =
                    new IMDCandidacy(person, executionDegree, employee.getPerson(), degreeCandidateDTO.getEntryGrade(),
                            degreeCandidateDTO.getContigent(), degreeCandidateDTO.getIngression(),
                            degreeCandidateDTO.getEntryPhase(), degreeCandidateDTO.getPlacingOption());

        } else {
            throw new RuntimeException("Unexpected degree type from DGES file");
        }

        candidacy.setHighSchoolType(degreeCandidateDTO.getHighSchoolType());
        candidacy.setFirstTimeCandidacy(true);
        createPrecedentDegreeInformation(candidacy, degreeCandidateDTO);
        candidacy.setDgesStudentImportationProcess(this);

        return candidacy;
    }

    private void createPrecedentDegreeInformation(final StudentCandidacy studentCandidacy,
            final DegreeCandidateDTO degreeCandidateDTO) {
        final PrecedentDegreeInformation precedentDegreeInformation = studentCandidacy.getPrecedentDegreeInformation();
        precedentDegreeInformation.setStudentCandidacy(studentCandidacy);

        precedentDegreeInformation.setConclusionGrade(degreeCandidateDTO.getHighSchoolFinalGrade());
        precedentDegreeInformation.setDegreeDesignation(degreeCandidateDTO.getHighSchoolDegreeDesignation());
        precedentDegreeInformation.setInstitution(UnitUtils.readExternalInstitutionUnitByName(degreeCandidateDTO
                .getHighSchoolName()));
    }

    private void voidPreviousCandidacies(Person person, ExecutionDegree executionDegree) {
        for (Candidacy candidacy : person.getCandidaciesSet()) {
            if (candidacy instanceof StudentCandidacy) {
                StudentCandidacy studentCandidacy = (StudentCandidacy) candidacy;
                if (studentCandidacy.getExecutionDegree().getExecutionYear() == executionDegree.getExecutionYear()
                        && !studentCandidacy.isConcluded()) {
                    studentCandidacy.cancelCandidacy();
                }
            }
        }
    }

    private void logCandidateIsEmployee(final PrintWriter LOG_WRITER, DegreeCandidateDTO degreeCandidateDTO, Person person) {
        LOG_WRITER.println(String.format("CANDIDATE WITH ID %s IS EMPLOYEE WITH NUMBER %s",
                degreeCandidateDTO.getDocumentIdNumber(), person.getEmployee().getEmployeeNumber()));

    }

    private void logCandidateIsTeacher(final PrintWriter LOG_WRITER, DegreeCandidateDTO degreeCandidateDTO, Person person) {
        LOG_WRITER.println(String.format("CANDIDATE WITH ID %s IS TEACHER WITH USERNAME %s",
                degreeCandidateDTO.getDocumentIdNumber(), person.getUsername()));
    }

    private void logCandidateIsStudentWithRegistrationAlreadyExists(final PrintWriter LOG_WRITER,
            DegreeCandidateDTO degreeCandidateDTO, Person person) {
        LOG_WRITER.println(String.format("CANDIDATE WITH ID %s IS THE STUDENT %s WITH REGISTRATIONS",
                degreeCandidateDTO.getDocumentIdNumber(), person.getStudent().getStudentNumber()));

    }

    private void logTooManyMatchsForCandidate(final PrintWriter LOG_WRITER, DegreeCandidateDTO degreeCandidateDTO) {
        LOG_WRITER.println(String.format("CANDIDATE WITH ID %s HAS MANY PERSONS", degreeCandidateDTO.getDocumentIdNumber()));
    }

    String getUniversityAcronym() {
        return "ALAMEDA".equals(getDgesStudentImportationForCampus().getName()) ? ALAMEDA_UNIVERSITY : TAGUS_UNIVERSITY;
    }

    public static boolean canRequestJob() {
        return QueueJob.getUndoneJobsForClass(DgesStudentImportationProcess.class).isEmpty();
    }

    public static List<DgesStudentImportationProcess> readAllJobs(final ExecutionYear executionYear) {
        List<DgesStudentImportationProcess> jobList = new ArrayList<DgesStudentImportationProcess>();

        CollectionUtils.select(executionYear.getDgesBaseProcessSet(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return (arg0 instanceof DgesStudentImportationProcess);
            }
        }, jobList);

        return jobList;
    }

    public static List<DgesStudentImportationProcess> readDoneJobs(final ExecutionYear executionYear) {
        List<DgesStudentImportationProcess> jobList = new ArrayList<DgesStudentImportationProcess>();

        CollectionUtils.select(executionYear.getDgesBaseProcessSet(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return (arg0 instanceof DgesStudentImportationProcess) && ((QueueJob) arg0).getDone();
            }
        }, jobList);

        return jobList;
    }

    public static List<DgesStudentImportationProcess> readUndoneJobs(final ExecutionYear executionYear) {
        return new ArrayList(CollectionUtils.subtract(readAllJobs(executionYear), readDoneJobs(executionYear)));
    }

    public static List<DgesStudentImportationProcess> readPendingJobs(final ExecutionYear executionYear) {
        List<DgesStudentImportationProcess> jobList = new ArrayList<DgesStudentImportationProcess>();

        CollectionUtils.select(executionYear.getDgesBaseProcessSet(), new Predicate() {

            @Override
            public boolean evaluate(Object arg0) {
                return (arg0 instanceof DgesStudentImportationProcess) && ((QueueJob) arg0).getIsNotDoneAndNotCancelled();
            }
        }, jobList);

        return jobList;
    }

    public LabelFormatter getDescriptionForEntryType(final Degree degree, EntryType entryType) {
        final LabelFormatter labelFormatter = new LabelFormatter();
        labelFormatter.appendLabel(entryType.name(), "enum").appendLabel(" (").appendLabel(degree.getDegreeType().name(), "enum")
                .appendLabel(" - ").appendLabel(degree.getNameFor(getExecutionYear()).getContent()).appendLabel(" - ")
                .appendLabel(getExecutionYear().getYear()).appendLabel(")");

        return labelFormatter;
    }

    private AccountingEventPaymentCode createAccountingEventPaymentCode(final EntryDTO entryDTO, final Student student,
            final GratuityPaymentPlan paymentPlan) {
        final YearMonthDay installmentEndDate = new DateTime().plusDays(18).toYearMonthDay();

        return AccountingEventPaymentCode.create(PaymentCodeType.GRATUITY_FIRST_INSTALLMENT, new YearMonthDay(),
                installmentEndDate, null, entryDTO.getAmountToPay(), entryDTO.getAmountToPay(), student.getPerson());
    }

    private InstallmentPaymentCode createInstallmentPaymentCode(final EntryWithInstallmentDTO entry, final Student student) {
        final YearMonthDay installmentEndDate = new DateTime().plusDays(18).toYearMonthDay();

        if (entry.getInstallment().getOrder() == 1) {
            return InstallmentPaymentCode.create(PaymentCodeType.GRATUITY_FIRST_INSTALLMENT, new YearMonthDay(),
                    installmentEndDate, null, entry.getInstallment(), entry.getAmountToPay(), entry.getAmountToPay(), student);

        }

        return InstallmentPaymentCode.create(PaymentCodeType.GRATUITY_FIRST_INSTALLMENT, new YearMonthDay(), entry
                .getInstallment().getEndDate(), null, entry.getInstallment(), entry.getAmountToPay(), entry.getAmountToPay(),
                student);
    }

}
