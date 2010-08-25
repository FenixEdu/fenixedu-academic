package net.sourceforge.fenixedu.domain.student.importation;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryDTO;
import net.sourceforge.fenixedu.dataTransferObject.accounting.EntryWithInstallmentDTO;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.EntryPhase;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.QueueJob;
import net.sourceforge.fenixedu.domain.QueueJobResult;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.Installment;
import net.sourceforge.fenixedu.domain.accounting.PaymentCodeType;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.AccountingEventPaymentCode;
import net.sourceforge.fenixedu.domain.accounting.paymentCodes.InstallmentPaymentCode;
import net.sourceforge.fenixedu.domain.accounting.paymentPlans.GratuityPaymentPlan;
import net.sourceforge.fenixedu.domain.accounting.report.GratuityReportQueueJob;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.DegreeCandidacy;
import net.sourceforge.fenixedu.domain.candidacy.IMDCandidacy;
import net.sourceforge.fenixedu.domain.candidacy.StandByCandidacySituation;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.UnitUtils;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.util.Money;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class DgesStudentImportationProcess extends DgesStudentImportationProcess_Base {

    private static final String ALAMEDA_UNIVERSITY = "A";
    private static final String TAGUS_UNIVERSITY = "T";

    private transient PrintWriter LOG_WRITER = null;

    protected DgesStudentImportationProcess() {
	super();
    }

    protected DgesStudentImportationProcess(final ExecutionYear executionYear, final Campus campus, final EntryPhase entryPhase,
	    DgesStudentImportationFile dgesStudentImportationFile) {
	check(executionYear, "error.DgesStudentImportationProcess.execution.year.is.null", new String[0]);
	check(campus, "error.DgesStudentImportationProcess.campus.is.null", new String[0]);
	check(entryPhase, "error.DgesStudentImportationProcess.entry.phase.is.null", new String[0]);
	check(dgesStudentImportationFile, "error.DgesStudentImportationProcess.importation.file.is.null");

	setExecutionYear(executionYear);
	setDgesStudentImportationForCampus(campus);
	setEntryPhase(entryPhase);
	setDgesStudentImportationFile(dgesStudentImportationFile);
    }

    @Override
    public QueueJobResult execute() throws Exception {
	ByteArrayOutputStream stream = new ByteArrayOutputStream();
	LOG_WRITER = new PrintWriter(new BufferedOutputStream(stream));

	importCandidates();

	final QueueJobResult queueJobResult = new QueueJobResult();
	queueJobResult.setContentType("text/csv");
	LOG_WRITER.flush();
	stream.flush();

	System.out.println(stream.toByteArray());

	queueJobResult.setContent(stream.toByteArray());

	System.out.println("Job " + getFilename() + " completed");
	stream.close();

	return queueJobResult;
    }

    @Override
    public String getFilename() {
	return "DgesStudentImportationProcess_result_" + getExecutionYear().getName().replaceAll("/", "-") + ".txt";
    }

    private void importCandidates() {

	final List<DegreeCandidateDTO> degreeCandidateDTOs = parseDgesFile(getDgesStudentImportationFile().getContents(),
		getUniversityAcronym(), getEntryPhase());

	final Employee employee = Employee.readByNumber(4581);

	LOG_WRITER.println(String.format("DGES Entries for %s : %s", getDgesStudentImportationForCampus().getName(),
		degreeCandidateDTOs.size()));

	createDegreeCandidacies(employee, degreeCandidateDTOs);
    }

    private void createDegreeCandidacies(final Employee employee, final List<DegreeCandidateDTO> degreeCandidateDTOs) {
	int processed = 0;
	int personsCreated = 0;

	for (final DegreeCandidateDTO degreeCandidateDTO : degreeCandidateDTOs) {

	    if (++processed % 150 == 0) {
		System.out.println("Processed :" + processed);
	    }

	    logCandidate(degreeCandidateDTO);

	    Person person = null;
	    try {
		person = degreeCandidateDTO.getMatchingPerson();
	    } catch (DegreeCandidateDTO.NotFoundPersonException e) {
		person = degreeCandidateDTO.createPerson();
		personsCreated++;
	    } catch (DegreeCandidateDTO.TooManyMatchedPersonsException e) {
		logTooManyMatchsForCandidate(degreeCandidateDTO);
		continue;
	    } catch (DegreeCandidateDTO.MatchingPersonException e) {
		throw new RuntimeException(e);
	    }

	    if (person.hasStudent() && person.getStudent().hasAnyRegistrations()) {
		logCandidateIsStudentWithRegistrationAlreadyExists(degreeCandidateDTO, person);
		continue;
	    }

	    if (person.hasTeacher()) {
		logCandidateIsTeacher(degreeCandidateDTO, person);
		continue;
	    }

	    if (person.hasRole(RoleType.EMPLOYEE) || person.hasEmployee()) {
		logCandidateIsEmployee(degreeCandidateDTO, person);
	    }

	    person.addPersonRoleByRoleType(RoleType.CANDIDATE);

	    if (!person.hasStudent()) {
		new Student(person);
		person.setIstUsername();
	    }

	    voidPreviousCandidacies(person, degreeCandidateDTO.getExecutionDegree(getExecutionYear(),
		    getDgesStudentImportationForCampus()));

	    final StudentCandidacy studentCandidacy = createCandidacy(employee, degreeCandidateDTO, person);
	    new StandByCandidacySituation(studentCandidacy, employee.getPerson());

	    createAvailableAccountingEventsPaymentCodes(person, studentCandidacy);
	}
    }

    private void createAvailableAccountingEventsPaymentCodes(final Person person, final StudentCandidacy studentCandidacy) {
	ExecutionDegree executionDegree = studentCandidacy.getExecutionDegree();

	GratuityPaymentPlan paymentPlan = getGratuityPaymentPlanForFirstTimeStudents(executionDegree);

	Money totalAmount = Money.ZERO;
	LabelFormatter descriptionForEntryType = getDescriptionForEntryType(executionDegree.getDegree(), EntryType.GRATUITY_FEE);
	for (Installment installment : paymentPlan.getInstallmentsSortedByEndDate()) {
	    EntryWithInstallmentDTO entryDTO = new EntryWithInstallmentDTO(EntryType.GRATUITY_FEE, null, installment.getAmount(),
		    descriptionForEntryType, installment);
	    studentCandidacy.addAvailablePaymentCodes(createInstallmentPaymentCode(entryDTO, person.getStudent()));
	    totalAmount = totalAmount.add(installment.getAmount());
	}

	EntryDTO fullPaymentEntryDTO = new EntryDTO(EntryType.GRATUITY_FEE, null, totalAmount, Money.ZERO, Money.ZERO,
		descriptionForEntryType, totalAmount);

	studentCandidacy.addAvailablePaymentCodes(createAccountingEventPaymentCode(fullPaymentEntryDTO, person.getStudent(),
		paymentPlan));
    }

    private GratuityPaymentPlan getGratuityPaymentPlanForFirstTimeStudents(final ExecutionDegree executionDegree) {
	List<GratuityPaymentPlan> paymentPlanList = executionDegree.getDegreeCurricularPlan().getServiceAgreementTemplate()
		.getGratuityPaymentPlansFor(getExecutionYear());

	for (GratuityPaymentPlan paymentPlan : paymentPlanList) {
	    if (paymentPlan.isForFirstTimeInstitutionStudents()) {
		return paymentPlan;
	    }
	}

	return null;
    }

    private void logCandidate(DegreeCandidateDTO degreeCandidateDTO) {
	LOG_WRITER.println("Processing: " + degreeCandidateDTO.toString());
    }

    private StudentCandidacy createCandidacy(final Employee employee, final DegreeCandidateDTO degreeCandidateDTO,
	    final Person person) {
	final ExecutionDegree executionDegree = degreeCandidateDTO.getExecutionDegree(getExecutionYear(),
		getDgesStudentImportationForCampus());
	StudentCandidacy candidacy = null;

	if (executionDegree.getDegree().getDegreeType() == DegreeType.BOLONHA_DEGREE) {
	    candidacy = new DegreeCandidacy(person, executionDegree, employee.getPerson(), degreeCandidateDTO.getEntryGrade(),
		    degreeCandidateDTO.getContigent(), degreeCandidateDTO.getIngression(), degreeCandidateDTO.getEntryPhase(),
		    degreeCandidateDTO.getPlacingOption());

	} else if (executionDegree.getDegree().getDegreeType() == DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE) {
	    candidacy = new IMDCandidacy(person, executionDegree, employee.getPerson(), degreeCandidateDTO.getEntryGrade(),
		    degreeCandidateDTO.getContigent(), degreeCandidateDTO.getIngression(), degreeCandidateDTO.getEntryPhase(),
		    degreeCandidateDTO.getPlacingOption());

	} else {
	    throw new RuntimeException("Unexpected degree type from DGES file");
	}

	candidacy.setHighSchoolType(degreeCandidateDTO.getHighSchoolType());
	candidacy.setFirstTimeCandidacy(true);
	createPrecedentDegreeInformation(candidacy, degreeCandidateDTO);

	return candidacy;
    }

    private void createPrecedentDegreeInformation(final StudentCandidacy studentCandidacy,
	    final DegreeCandidateDTO degreeCandidateDTO) {
	final PrecedentDegreeInformation precedentDegreeInformation = new PrecedentDegreeInformation(studentCandidacy);

	precedentDegreeInformation.setConclusionGrade(degreeCandidateDTO.getHighSchoolFinalGrade());
	precedentDegreeInformation.setDegreeDesignation(degreeCandidateDTO.getHighSchoolDegreeDesignation());
	precedentDegreeInformation.setInstitution(UnitUtils.readExternalInstitutionUnitByName(degreeCandidateDTO
		.getHighSchoolName()));
    }

    private void voidPreviousCandidacies(Person person, ExecutionDegree executionDegree) {
	for (Candidacy candidacy : person.getCandidacies()) {
	    if (candidacy instanceof StudentCandidacy) {
		StudentCandidacy studentCandidacy = (StudentCandidacy) candidacy;
		if (studentCandidacy.getExecutionDegree().getExecutionYear() == executionDegree.getExecutionYear()
			&& !studentCandidacy.isConcluded()) {
		    studentCandidacy.cancelCandidacy();
		}
	    }
	}
    }

    private void logCandidateIsEmployee(DegreeCandidateDTO degreeCandidateDTO, Person person) {
	LOG_WRITER.println(String.format("CANDIDATE WITH ID %s IS EMPLOYEE WITH NUMBER %s", degreeCandidateDTO
		.getDocumentIdNumber(), person.getEmployee().getEmployeeNumber()));

    }

    private void logCandidateIsTeacher(DegreeCandidateDTO degreeCandidateDTO, Person person) {
	LOG_WRITER.println(String.format("CANDIDATE WITH ID %s IS TEACHER WITH NUMBER %s", degreeCandidateDTO
		.getDocumentIdNumber(), person.getTeacher().getTeacherNumber()));
    }

    private void logCandidateIsStudentWithRegistrationAlreadyExists(DegreeCandidateDTO degreeCandidateDTO, Person person) {
	LOG_WRITER.println(String.format("CANDIDATE WITH ID %s IS THE STUDENT %s WITH REGISTRATIONS", degreeCandidateDTO
		.getDocumentIdNumber(), person.getStudent().getStudentNumber()));

    }

    private void logTooManyMatchsForCandidate(DegreeCandidateDTO degreeCandidateDTO) {
	LOG_WRITER.println(String.format("CANDIDATE WITH ID %s HAS MANY PERSONS", degreeCandidateDTO.getDocumentIdNumber()));
    }

    private String getUniversityAcronym() {
	return getDgesStudentImportationForCampus().isCampusAlameda() ? ALAMEDA_UNIVERSITY : TAGUS_UNIVERSITY;
    }

    public static boolean canRequestJob() {
	return QueueJob.getUndoneJobsForClass(GratuityReportQueueJob.class).isEmpty();
    }

    public static List<DgesStudentImportationProcess> readDoneJobs(final ExecutionYear executionYear) {
	List<DgesStudentImportationProcess> jobList = new ArrayList<DgesStudentImportationProcess>();

	CollectionUtils.select(executionYear.getDgesBaseProcess(), new Predicate() {

	    @Override
	    public boolean evaluate(Object arg0) {
		return (arg0 instanceof DgesStudentImportationProcess) && ((QueueJob) arg0).getDone();
	    }
	}, jobList);

	return jobList;
    }

    public static List<DgesStudentImportationProcess> readUndoneJobs(final ExecutionYear executionYear) {
	return new ArrayList(CollectionUtils.subtract(executionYear.getDgesBaseProcess(), readDoneJobs(executionYear)));
    }

    public static List<DgesStudentImportationProcess> readPendingJobs(final ExecutionYear executionYear) {
	List<DgesStudentImportationProcess> jobList = new ArrayList<DgesStudentImportationProcess>();

	CollectionUtils.select(executionYear.getDgesBaseProcess(), new Predicate() {

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
	return AccountingEventPaymentCode.create(PaymentCodeType.GRATUITY_FIRST_INSTALLMENT, new YearMonthDay(), paymentPlan
		.getFirstInstallment().getStartDate().plusMonths(1), null, entryDTO.getAmountToPay(), entryDTO.getAmountToPay(),
		student.getPerson());
    }

    private InstallmentPaymentCode createInstallmentPaymentCode(final EntryWithInstallmentDTO entry, final Student student) {
	final YearMonthDay installmentEndDate = entry.getInstallment().getEndDate();

	return InstallmentPaymentCode.create(PaymentCodeType.GRATUITY_FIRST_INSTALLMENT, new YearMonthDay(), entry
		.getInstallment().getEndDate(), null, entry.getInstallment(), entry.getAmountToPay(), entry.getAmountToPay(),
		student);
    }

}
