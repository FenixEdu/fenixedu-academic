package net.sourceforge.fenixedu.domain.phd.migration;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.CreateNewProcess;
import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.applicationTier.utils.MockUserView;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Role;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramCollaborationType;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.AddAssistantGuidingInformation;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.AddGuidingInformation;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.AddStudyPlan;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.CancelPhdProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.EditQualificationExams;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.ExemptPublicPresentationSeminarComission;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.RequestPublicThesisPresentation;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessBean.QualificationExamsResult;
import net.sourceforge.fenixedu.domain.phd.PhdParticipantBean;
import net.sourceforge.fenixedu.domain.phd.PhdProgram;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.PhdStudyPlanBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess.RatifyCandidacy;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess.RegistrationFormalization;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess.RequestCandidacyReview;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess.RequestRatifyCandidacy;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessStateBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.RatifyCandidacyBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.RegistrationFormalizationBean;
import net.sourceforge.fenixedu.domain.phd.migration.activities.SkipThesisJuryActivities;
import net.sourceforge.fenixedu.domain.phd.migration.common.ConversionUtilities;
import net.sourceforge.fenixedu.domain.phd.migration.common.FinalGradeTranslator;
import net.sourceforge.fenixedu.domain.phd.migration.common.PhdProgramTranslator;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.FinalEstimatedStateNotReachedException;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.IncompleteFieldsException;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.PersonNotFoundException;
import net.sourceforge.fenixedu.domain.phd.seminar.PublicPresentationSeminarProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisFinalGrade;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.RatifyFinalThesis;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.SetFinalGrade;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.SkipScheduleThesisDiscussion;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.SubmitThesis;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeetingSchedulingProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities.ScheduleFirstThesisMeetingRequest;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.activities.SkipScheduleFirstThesisMeeting;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import pt.ist.fenixWebFramework.security.UserView;

public class PhdMigrationIndividualProcessData extends PhdMigrationIndividualProcessData_Base {

    private transient Integer processNumber;
    private transient PhdProgram phdProgram;
    private transient String title;
    private transient String guiderNumber;
    private transient String assistantGuiderNumber;
    private transient LocalDate startProcessDate;
    private transient LocalDate startDevelopmentDate;
    private transient LocalDate requirementDate;
    private transient LocalDate meetingDate;
    private transient LocalDate firstDiscussionDate;
    private transient LocalDate secondDiscussionDate;
    private transient LocalDate edictDate;

    private transient PhdThesisFinalGrade classification;
    private transient LocalDate ratificationDate;
    private transient LocalDate annulmentDate;
    private transient LocalDate limitToFinishDate;

    private PhdMigrationIndividualProcessData() {
	super();
    }

    protected PhdMigrationIndividualProcessData(String data) {
	setData(data);
	setMigrationStatus(PhdMigrationProcessStateType.NOT_MIGRATED);
    }

    public void parseAndSetNumber() {
	parse();

	setNumber(processNumber);
    }

    public void parse() {
	try {
	    String[] fields = getData().split("\t");

	    try {
		processNumber = Integer.valueOf(fields[0].trim());
	    } catch (NumberFormatException e) {
		throw new IncompleteFieldsException("processNumber");
	    }

	    try {
		phdProgram = PhdProgramTranslator.translate(fields[1].trim());
	    } catch (NumberFormatException e) {
		throw new IncompleteFieldsException("phdProgram");
	    }
	    title = fields[2].trim();
	    guiderNumber = fields[3].trim();
	    assistantGuiderNumber = fields[4].trim();
	    startProcessDate = ConversionUtilities.parseDate(fields[5].trim());
	    startDevelopmentDate = ConversionUtilities.parseDate(fields[6].trim());
	    requirementDate = ConversionUtilities.parseDate(fields[7].trim());
	    meetingDate = ConversionUtilities.parseDate(fields[8].trim());
	    firstDiscussionDate = ConversionUtilities.parseDate(fields[9].trim());
	    secondDiscussionDate = ConversionUtilities.parseDate(fields[10].trim());
	    edictDate = ConversionUtilities.parseDate(fields[11].trim());

	    classification = FinalGradeTranslator.translate(fields[13].trim());
	    ratificationDate = ConversionUtilities.parseDate(fields[14].trim());
	    annulmentDate = ConversionUtilities.parseDate(fields[15].trim());
	    limitToFinishDate = ConversionUtilities.parseDate(fields[16].trim());

	} catch (NoSuchElementException e) {
	    throw new IncompleteFieldsException("Not enough fields");
	}
    }

    public boolean hasMigrationParseLog() {
	return !StringUtils.isEmpty(getMigrationParseLog());
    }

    public String getMigrationException() {
	if (!hasMigrationParseLog()) {
	    return null;
	}

	String exceptionLine = getMigrationParseLog();
	int messageStartIdx = exceptionLine.indexOf(" ");
	if (messageStartIdx == -1) {
	    return exceptionLine;
	}
	return exceptionLine.substring(0, exceptionLine.indexOf(" ") - 1);
    }

    public String getMigrationExceptionMessage() {
	if (!hasMigrationParseLog()) {
	    return null;
	}

	String exceptionLine = getMigrationParseLog();
	int messageStartIdx = exceptionLine.indexOf(" ");
	if (messageStartIdx == -1) {
	    return null;
	}
	return exceptionLine.substring(exceptionLine.indexOf(" "));
    }

    public Person getGuidingPerson() {
	if (guiderNumber.contains("E")) {
	    throw new PersonNotFoundException();
	}

	return getPerson(guiderNumber);
    }

    public Person getAssistantGuidingPerson() {
	if (assistantGuiderNumber.contains("E")) {
	    throw new PersonNotFoundException();
	}

	return getPerson(assistantGuiderNumber);
    }

    public Person getPerson(String identification) {
	Teacher teacher = Teacher.readByNumber(Integer.valueOf(identification));

	if (teacher == null) {
	    throw new PersonNotFoundException();
	}

	return teacher.getPerson();
    }

    public boolean hasExistingIndividualProgramProcess() {
	return getPhdIndividualProgramProcess() != null;
    }

    public PhdIndividualProgramProcess getPhdIndividualProgramProcess() {
	final PhdMigrationIndividualPersonalData personalData = getPhdMigrationIndividualPersonalData();

	if (personalData == null) {
	    return null;
	}

	if (!personalData.isPersonRegisteredOnFenix()) {
	    return null;
	}

	final Person student = personalData.getPerson();

	if (student.hasAnyPhdIndividualProgramProcesses()) {
	    return student.getPhdIndividualProgramProcesses().get(0);
	}

	return null;
    }

    private LocalDate retrieveDateForExecutionYear() {
	if (startDevelopmentDate != null) {
	    return startDevelopmentDate;
	}
	if (ratificationDate != null) {
	    return ratificationDate;
	}
	if (startProcessDate != null) {
	    return startProcessDate;
	}
	return null;
    }

    // startProcessDate; - começo do processo
    // startDevelopmentDate; - desenvolvimento de trabalho (alternativa é data
    // de homolgação)
    // requirementDate; - marcação das discussões
    // meetingDate; - data da reunião
    // firstDiscussionDate; - data da 1ª discussão
    // secondDiscussionDate; - data da 2ª discussão (usar esta
    // preferencialmente)
    // edictDate; - data da conclusão do process
    // classification -
    // ratificationDate; - data da homolgação da candidatura
    // annulmentDate; - data da anulação do processo
    // limitToFinishDate; - data limite para conclusão
    public PhdMigrationProcessStateType estimatedFinalMigrationStatus() {
	if (isProcessCanceled()) {
	    return PhdMigrationProcessStateType.CANCELED;
	}

	if (edictDate != null || classification != null) {
	    return PhdMigrationProcessStateType.CONCLUDED;
	}

	if (firstDiscussionDate != null || secondDiscussionDate != null) {
	    return PhdMigrationProcessStateType.COMPLETED_THESIS_DISCUSSION;
	}

	if (requirementDate != null) {
	    return PhdMigrationProcessStateType.REQUESTED_THESIS_DISCUSSION;
	}

	if (startDevelopmentDate != null || ratificationDate != null) {
	    return PhdMigrationProcessStateType.WORK_DEVELOPMENT;
	}

	if (ratificationDate != null) {
	    return PhdMigrationProcessStateType.CANDIDACY_RATIFIED;
	}

	if (startProcessDate != null) {
	    return PhdMigrationProcessStateType.CANDIDACY_CREATED;
	}

	return PhdMigrationProcessStateType.NOT_MIGRATED;
    }

    public boolean possibleToCompleteNextState() {
	final PhdMigrationProcessStateType activeState = getMigrationStatus();
	
	if(activeState.equals(PhdMigrationProcessStateType.CANCELED) || activeState.equals(PhdMigrationProcessStateType.CONCLUDED)) {
	    return false;
	}
	
	if (classification != null) {
	    return true;
	}

	if (activeState.equals(PhdMigrationProcessStateType.COMPLETED_THESIS_DISCUSSION)) {
	    if (edictDate != null) {
		return true;
	    }
	}

	if (activeState.equals(PhdMigrationProcessStateType.REQUESTED_THESIS_DISCUSSION)) {
	    if (firstDiscussionDate != null || secondDiscussionDate != null) {
		return true;
	    }
	}
	
	if(activeState.equals(PhdMigrationProcessStateType.WORK_DEVELOPMENT)) {
	    if (requirementDate != null) {
		return true;
	    }
	}
	
	if (activeState.equals(PhdMigrationProcessStateType.CANDIDACY_RATIFIED)) {
	    if (startDevelopmentDate != null || ratificationDate != null) {
		return true;
	    }
	}

	if(activeState.equals(PhdMigrationProcessStateType.CANDIDACY_CREATED)) {
	    if (ratificationDate != null) {
		return true;
	    }
	}
	
	if(activeState.equals(PhdMigrationProcessStateType.NOT_MIGRATED)) {
	    if (startProcessDate != null) {
		return true;
	    }
	}
	
	return false;
    }

    private boolean isProcessCanceled() {
	return annulmentDate != null;
    }

    public boolean proceedWithMigration() {
	final Person manager = Employee.readByNumber(3068).getPerson();
	final IUserView userView = new MockUserView(manager.getUsername(), new ArrayList<Role>(), manager) {

	    @Override
	    public boolean hasRoleType(final RoleType roleType) {
		return getPerson().hasRole(roleType);
	    }
	};
	UserView.setUser(userView);

	PhdMigrationProcessStateType activeState;
	PhdIndividualProgramProcess individualProcess = null;

	boolean returnVal = false;

	while (possibleToCompleteNextState()) {
	    activeState = getMigrationStatus();

	    returnVal = true;

	    if (activeState.equals(PhdMigrationProcessStateType.NOT_MIGRATED)) {
		individualProcess = createCandidacyProcess(userView);
		sendCandidacyToCoordinator(userView, individualProcess);
		setMigrationStatus(PhdMigrationProcessStateType.CANDIDACY_CREATED);
		continue;
	    }

	    if (activeState.equals(PhdMigrationProcessStateType.CANDIDACY_CREATED)) {
		ratifyCandidacyProcess(userView, individualProcess);
		setMigrationStatus(PhdMigrationProcessStateType.CANDIDACY_RATIFIED);
		continue;
	    }

	    if (activeState.equals(PhdMigrationProcessStateType.CANDIDACY_RATIFIED)) {
		formalizeRegistration(userView, individualProcess);
		setMigrationStatus(PhdMigrationProcessStateType.WORK_DEVELOPMENT);
		continue;
	    }

	    if (activeState.equals(PhdMigrationProcessStateType.WORK_DEVELOPMENT)) {
		requirePublicThesisPresentation(userView, individualProcess);
		setMigrationStatus(PhdMigrationProcessStateType.REQUESTED_THESIS_DISCUSSION);
		continue;
	    }

	    if (activeState.equals(PhdMigrationProcessStateType.REQUESTED_THESIS_DISCUSSION)) {
		skipJuryActivities(userView, individualProcess);
		manageMeetingsAndFinalThesis(userView, individualProcess);
		setMigrationStatus(PhdMigrationProcessStateType.COMPLETED_THESIS_DISCUSSION);
		continue;
	    }

	    if (activeState.equals(PhdMigrationProcessStateType.COMPLETED_THESIS_DISCUSSION)) {
		ratifyFinalThesis(userView, individualProcess);
		setMigrationStatus(PhdMigrationProcessStateType.CONCLUDED);
		continue;
	    }

	    break;
	}

	if (isProcessCanceled()) {
	    cancelPhdProgram(userView, individualProcess);
	    setMigrationStatus(PhdMigrationProcessStateType.CANCELED);
	}

	activeState = getMigrationStatus();
	if (!activeState.equals(estimatedFinalMigrationStatus())) {
	    throw new FinalEstimatedStateNotReachedException("Estimated: " + estimatedFinalMigrationStatus() + "\treached: "
		    + activeState);
	}

	return returnVal;
    }

    private PhdIndividualProgramProcess createCandidacyProcess(final IUserView userView) {
	final PhdProgramCandidacyProcessBean candidacyBean = new PhdProgramCandidacyProcessBean();

	candidacyBean.setCandidacyDate(this.startProcessDate);
	candidacyBean.setState(PhdProgramCandidacyProcessState.STAND_BY_WITH_COMPLETE_INFORMATION);
	candidacyBean.setPersonBean(getPhdMigrationIndividualPersonalData().getPersonBean());
	candidacyBean.setMigratedProcess(true);
	candidacyBean.setProgram(this.phdProgram);
	candidacyBean.setThesisTitle(this.title);
	candidacyBean.setPhdStudentNumber(getPhdMigrationIndividualPersonalData().getNumber());
	candidacyBean.setCollaborationType(PhdIndividualProgramCollaborationType.NONE);
	candidacyBean.setExecutionYear(ExecutionYear.readByDateTime(retrieveDateForExecutionYear()));
	candidacyBean.setFocusArea((phdProgram.getPhdProgramFocusAreasCount() == 1) ? phdProgram.getPhdProgramFocusAreas().get(0)
		: null);

	final PhdIndividualProgramProcess individualProcess = (PhdIndividualProgramProcess) CreateNewProcess.run(
		PhdIndividualProgramProcess.class, candidacyBean);

	return individualProcess;
    }

    private void sendCandidacyToCoordinator(final IUserView userView, final PhdIndividualProgramProcess individualProcess) {
	final PhdProgramCandidacyProcess candidacyProcess = individualProcess.getCandidacyProcess();

	final PhdProgramCandidacyProcessStateBean reviewBean = new PhdProgramCandidacyProcessStateBean(
		candidacyProcess.getIndividualProgramProcess());
	reviewBean.setState(PhdProgramCandidacyProcessState.PENDING_FOR_COORDINATOR_OPINION);
	reviewBean.setGenerateAlert(false);
	ExecuteProcessActivity.run(candidacyProcess, RequestCandidacyReview.class.getSimpleName(), reviewBean);

	final PhdProgramCandidacyProcessStateBean requestRatifyBean = new PhdProgramCandidacyProcessStateBean(individualProcess);
	requestRatifyBean.setGenerateAlert(false);
	requestRatifyBean.setState(PhdProgramCandidacyProcessState.WAITING_FOR_SCIENTIFIC_COUNCIL_RATIFICATION);
	ExecuteProcessActivity.run(candidacyProcess, RequestRatifyCandidacy.class.getSimpleName(), requestRatifyBean);
    }

    private void ratifyCandidacyProcess(final IUserView userView, final PhdIndividualProgramProcess individualProcess) {
	final PhdProgramCandidacyProcess candidacyProcess = individualProcess.getCandidacyProcess();
	final RatifyCandidacyBean ratifyBean = new RatifyCandidacyBean(candidacyProcess);
	ratifyBean.setWhenRatified(ratificationDate);
	ExecuteProcessActivity.run(candidacyProcess, RatifyCandidacy.class.getSimpleName(), ratifyBean);
    }

    private void formalizeRegistration(final IUserView userView, final PhdIndividualProgramProcess individualProcess) {
	final PhdIndividualProgramProcessBean individualProcessBean = new PhdIndividualProgramProcessBean(individualProcess);
	individualProcessBean.setQualificationExamsPerformed(QualificationExamsResult.NO);
	individualProcessBean.setQualificationExamsRequired(QualificationExamsResult.NO);
	ExecuteProcessActivity.run(individualProcess, EditQualificationExams.class.getSimpleName(), individualProcessBean);

	final PhdStudyPlanBean planBean = new PhdStudyPlanBean(individualProcess);
	planBean.setExempted(true);
	ExecuteProcessActivity.run(individualProcess, AddStudyPlan.class.getSimpleName(), planBean);

	final PhdProgramCandidacyProcess candidacyProcess = individualProcess.getCandidacyProcess();
	final RegistrationFormalizationBean formalizationBean = new RegistrationFormalizationBean(candidacyProcess);
	formalizationBean.setWhenStartedStudies(getMostAccurateStartDevelopmentDate());
	formalizationBean.setSelectRegistration(false);
	ExecuteProcessActivity.run(candidacyProcess, RegistrationFormalization.class.getSimpleName(), formalizationBean);
    }

    private LocalDate getMostAccurateStartDevelopmentDate() {
	if (startDevelopmentDate != null) {
	    return startDevelopmentDate;
	} else {
	    return ratificationDate;
	}
    }

    private void requirePublicThesisPresentation(final IUserView userView, final PhdIndividualProgramProcess individualProcess) {
	ExecuteProcessActivity.run(individualProcess, ExemptPublicPresentationSeminarComission.class.getSimpleName(),
		new PublicPresentationSeminarProcessBean());

	if (!StringUtils.isEmpty(guiderNumber)) {
	    final PhdMigrationGuiding migrationGuiding = getGuiding(guiderNumber);
	    if (migrationGuiding != null) {
		migrationGuiding.parse();
		final PhdParticipantBean guidingBean = migrationGuiding.getPhdParticipantBean(individualProcess);
		ExecuteProcessActivity.run(individualProcess, AddGuidingInformation.class.getSimpleName(), guidingBean);
	    }
	}

	if (!StringUtils.isEmpty(assistantGuiderNumber)) {
	    final PhdMigrationGuiding migrationAssistantGuiding = getGuiding(assistantGuiderNumber);
	    if (migrationAssistantGuiding != null) {
		migrationAssistantGuiding.parse();
		final PhdParticipantBean assistantGuidingBean = migrationAssistantGuiding
			.getPhdParticipantBean(individualProcess);
		ExecuteProcessActivity.run(individualProcess, AddAssistantGuidingInformation.class.getSimpleName(),
			assistantGuidingBean);
	    }
	}

	final PhdThesisProcessBean thesisBean = new PhdThesisProcessBean(individualProcess);
	thesisBean.setWhenThesisDiscussionRequired(requirementDate);
	thesisBean.setGenerateAlert(false);
	thesisBean.setToNotify(false);
	ExecuteProcessActivity.run(individualProcess, RequestPublicThesisPresentation.class.getSimpleName(), thesisBean);
    }

    private PhdMigrationGuiding getGuiding(String guidingNumber) {
	String alternativeGuidingNumber = "0".concat(guidingNumber);
	for (PhdMigrationGuiding migrationGuiding : getPhdMigrationProcess().getPhdMigrationGuiding()) {
	    if (guidingNumber.equals(migrationGuiding.getTeacherNumber())
		    || alternativeGuidingNumber.equals(migrationGuiding.getTeacherNumber())) {
		return migrationGuiding;
	    }
	}

	return null;
	// throw new
	// PhdMigrationGuidingNotFoundException("Did not find guiding with code: "
	// + guidingNumber);
    }

    private void skipJuryActivities(final IUserView userView,
	    final PhdIndividualProgramProcess individualProcess) {
	final PhdThesisProcess thesisProcess = individualProcess.getThesisProcess();
	final PhdThesisProcessBean thesisBean = new PhdThesisProcessBean();
	thesisBean.setThesisProcess(thesisProcess);
	thesisBean.setToNotify(false);
	thesisBean.setGenerateAlert(false);
	ExecuteProcessActivity.run(individualProcess.getThesisProcess(), SkipThesisJuryActivities.class.getSimpleName(),
		thesisBean);
    }

    private void manageMeetingsAndFinalThesis(final IUserView userView, final PhdIndividualProgramProcess individualProcess) {
	final PhdThesisProcess thesisProcess = individualProcess.getThesisProcess();
	final PhdThesisProcessBean thesisBean = new PhdThesisProcessBean();
	thesisBean.setThesisProcess(thesisProcess);
	thesisBean.setToNotify(false);
	thesisBean.setGenerateAlert(false);
	final PhdMeetingSchedulingProcess meetingProcess = thesisProcess.getMeetingProcess();
	ExecuteProcessActivity.run(meetingProcess, ScheduleFirstThesisMeetingRequest.class, thesisBean);

	thesisBean.setScheduledDate(getMeetingDate());
	thesisBean.setScheduledPlace("");
	ExecuteProcessActivity.run(meetingProcess, SkipScheduleFirstThesisMeeting.class, thesisBean);
	
	thesisBean.setScheduledDate(getMostAccurateDiscussionDateTime());
	thesisBean.setScheduledPlace("");
	ExecuteProcessActivity.run(thesisProcess, SkipScheduleThesisDiscussion.class, thesisBean);

	ExecuteProcessActivity.run(thesisProcess, SubmitThesis.class, thesisBean);
    }

    private DateTime getMostAccurateDiscussionDateTime() {
	if (secondDiscussionDate != null) {
	    return secondDiscussionDate.toDateTimeAtCurrentTime();
	} else if (firstDiscussionDate != null) {
	    return firstDiscussionDate.toDateTimeAtCurrentTime();
	}

	return null;
    }

    private DateTime getMeetingDate() {
	if (meetingDate != null) {
	    return meetingDate.toDateTimeAtCurrentTime();
	} else {
	    return null;
	}
    }

    private void ratifyFinalThesis(final IUserView userView, final PhdIndividualProgramProcess individualProcess) {
	final PhdThesisProcess thesisProcess = individualProcess.getThesisProcess();
	final PhdThesisProcessBean thesisBean = new PhdThesisProcessBean();
	thesisBean.setThesisProcess(thesisProcess);
	thesisBean.setToNotify(false);
	thesisBean.setGenerateAlert(false);
	thesisBean.setWhenFinalThesisRatified(edictDate);
	ExecuteProcessActivity.run(thesisProcess, RatifyFinalThesis.class, thesisBean);

	thesisBean.setConclusionDate(edictDate);
	thesisBean.setFinalGrade(classification);
	ExecuteProcessActivity.run(thesisProcess, SetFinalGrade.class, thesisBean);
    }

    private void cancelPhdProgram(final IUserView userView, final PhdIndividualProgramProcess individualProcess) {
	final PhdIndividualProgramProcessBean processBean = new PhdIndividualProgramProcessBean(individualProcess);
	ExecuteProcessActivity.run(individualProcess, CancelPhdProgramProcess.class.getSimpleName(), processBean);
    }

}
