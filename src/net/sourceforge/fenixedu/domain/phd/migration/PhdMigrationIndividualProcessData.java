package net.sourceforge.fenixedu.domain.phd.migration;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;

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
import net.sourceforge.fenixedu.domain.phd.SearchPhdIndividualProgramProcessBean;
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
import pt.utl.ist.fenix.tools.util.i18n.Language;

public class PhdMigrationIndividualProcessData extends PhdMigrationIndividualProcessData_Base {

    private transient PhdMigrationIndividualProcessDataBean processBean;

    private PhdMigrationIndividualProcessData() {
	super();
    }

    protected PhdMigrationIndividualProcessData(String data) {
	setData(data);
	setMigrationStatus(PhdMigrationProcessStateType.NOT_MIGRATED);
    }

    public class PhdMigrationIndividualProcessDataBean {
	private transient PhdMigrationIndividualProcessData processData;

	private transient String data;

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

	public PhdMigrationIndividualProcessDataBean(PhdMigrationIndividualProcessData processData) {
	    setProcessData(processData);
	    setData(processData.getData());
	    parse();
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

	public PhdMigrationIndividualProcessData getProcessData() {
	    return processData;
	}

	public void setProcessData(PhdMigrationIndividualProcessData processData) {
	    this.processData = processData;
	}

	public String getData() {
	    return data;
	}

	public void setData(String data) {
	    this.data = data;
	}

	public Integer getProcessNumber() {
	    return processNumber;
	}

	public void setProcessNumber(Integer processNumber) {
	    this.processNumber = processNumber;
	}

	public PhdProgram getPhdProgram() {
	    return phdProgram;
	}

	public void setPhdProgram(PhdProgram phdProgram) {
	    this.phdProgram = phdProgram;
	}

	public String getTitle() {
	    return title;
	}

	public void setTitle(String title) {
	    this.title = title;
	}

	public String getGuiderNumber() {
	    return guiderNumber;
	}

	public void setGuiderNumber(String guiderNumber) {
	    this.guiderNumber = guiderNumber;
	}

	public String getAssistantGuiderNumber() {
	    return assistantGuiderNumber;
	}

	public void setAssistantGuiderNumber(String assistantGuiderNumber) {
	    this.assistantGuiderNumber = assistantGuiderNumber;
	}

	public LocalDate getStartProcessDate() {
	    return startProcessDate;
	}

	public void setStartProcessDate(LocalDate startProcessDate) {
	    this.startProcessDate = startProcessDate;
	}

	public LocalDate getStartDevelopmentDate() {
	    return startDevelopmentDate;
	}

	public void setStartDevelopmentDate(LocalDate startDevelopmentDate) {
	    this.startDevelopmentDate = startDevelopmentDate;
	}

	public LocalDate getRequirementDate() {
	    return requirementDate;
	}

	public void setRequirementDate(LocalDate requirementDate) {
	    this.requirementDate = requirementDate;
	}

	public LocalDate getMeetingDate() {
	    return meetingDate;
	}

	public void setMeetingDate(LocalDate meetingDate) {
	    this.meetingDate = meetingDate;
	}

	public LocalDate getFirstDiscussionDate() {
	    return firstDiscussionDate;
	}

	public void setFirstDiscussionDate(LocalDate firstDiscussionDate) {
	    this.firstDiscussionDate = firstDiscussionDate;
	}

	public LocalDate getSecondDiscussionDate() {
	    return secondDiscussionDate;
	}

	public void setSecondDiscussionDate(LocalDate secondDiscussionDate) {
	    this.secondDiscussionDate = secondDiscussionDate;
	}

	public LocalDate getEdictDate() {
	    return edictDate;
	}

	public void setEdictDate(LocalDate edictDate) {
	    this.edictDate = edictDate;
	}

	public PhdThesisFinalGrade getClassification() {
	    return classification;
	}

	public void setClassification(PhdThesisFinalGrade classification) {
	    this.classification = classification;
	}

	public LocalDate getRatificationDate() {
	    return ratificationDate;
	}

	public void setRatificationDate(LocalDate ratificationDate) {
	    this.ratificationDate = ratificationDate;
	}

	public LocalDate getAnnulmentDate() {
	    return annulmentDate;
	}

	public void setAnnulmentDate(LocalDate annulmentDate) {
	    this.annulmentDate = annulmentDate;
	}

	public LocalDate getLimitToFinishDate() {
	    return limitToFinishDate;
	}

	public void setLimitToFinishDate(LocalDate limitToFinishDate) {
	    this.limitToFinishDate = limitToFinishDate;
	}

	public boolean hasPhdProgram() {
	    return phdProgram != null;
	}

    }

    public boolean hasProcessBean() {
	return processBean != null;
    }

    public PhdMigrationIndividualProcessDataBean getProcessBean() {
	if (hasProcessBean()) {
	    return processBean;
	}

	processBean = new PhdMigrationIndividualProcessDataBean(this);
	return processBean;
    }

    public void parse() {
	getProcessBean();
    }

    public void parseAndSetNumber() {
	final PhdMigrationIndividualProcessDataBean personalBean = getProcessBean();
	setNumber(processBean.getProcessNumber());
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

    public String getMigrationExceptionMessageFromBundle() {
	final String exceptionString = getMigrationException();

	if (exceptionString == null) {
	    return null;
	}

	final String messageString = getMigrationExceptionMessage();
	String errorTranslated = ResourceBundle.getBundle("resources.PhdResources", Language.getLocale()).getString(
		"label.phd.migration.exception." + exceptionString);
	
	if(errorTranslated == null) {
	    errorTranslated = ResourceBundle.getBundle("resources.PhdResources", Language.getLocale()).getString(
			"label.phd.migration.exception.generic");
	}

	if (messageString != null) {
	    errorTranslated += " - " + getMigrationExceptionMessage();
	}
	
	return errorTranslated;
    }

    public Person getGuidingPerson() {
	if (getProcessBean().getGuiderNumber().contains("E")) {
	    throw new PersonNotFoundException();
	}

	return getPerson(getProcessBean().getGuiderNumber());
    }

    public Person getAssistantGuidingPerson() {
	if (getProcessBean().getAssistantGuiderNumber().contains("E")) {
	    throw new PersonNotFoundException();
	}

	return getPerson(getProcessBean().getAssistantGuiderNumber());
    }

    public Person getPerson(String identification) {
	Teacher teacher = Teacher.readByNumber(Integer.valueOf(identification));

	if (teacher == null) {
	    throw new PersonNotFoundException();
	}

	return teacher.getPerson();
    }

    public boolean isMigratedToIndividualProgramProcess() {
	return getMigratedIndividualProgramProcess() != null;
    }

    public PhdIndividualProgramProcess getMigratedIndividualProgramProcess() {

	final SearchPhdIndividualProgramProcessBean searchBean = new SearchPhdIndividualProgramProcessBean();
	searchBean.setFilterPhdPrograms(false);
	searchBean.setFilterPhdProcesses(false);

	for (final PhdIndividualProgramProcess process : PhdIndividualProgramProcess.search(searchBean.getPredicates())) {
	    if (process.getPhdStudentNumber() != null && process.getPhdStudentNumber().equals(getNumber())) {
		return process;
	    }
	}

	return null;
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

    public ExecutionYear getExecutionYear() {
	final LocalDate date = retrieveDateForExecutionYear();
	if (date != null) {
	    return ExecutionYear.readByDateTime(date);
	} else {
	    return null;
	}
    }

    private LocalDate retrieveDateForExecutionYear() {
	if (getProcessBean().getStartDevelopmentDate() != null) {
	    return getProcessBean().getStartDevelopmentDate();
	}
	if (getProcessBean().getRatificationDate() != null) {
	    return getProcessBean().getRatificationDate();
	}
	if (getProcessBean().getStartProcessDate() != null) {
	    return getProcessBean().getStartProcessDate();
	}
	return null;
    }

    public PhdMigrationProcessStateType estimatedFinalMigrationStatus() {
	if (isProcessCanceled()) {
	    return PhdMigrationProcessStateType.CANCELED;
	}

	if (getProcessBean().getEdictDate() != null || getProcessBean().getClassification() != null) {
	    return PhdMigrationProcessStateType.CONCLUDED;
	}

	if (getProcessBean().getFirstDiscussionDate() != null || getProcessBean().getSecondDiscussionDate() != null) {
	    return PhdMigrationProcessStateType.COMPLETED_THESIS_DISCUSSION;
	}

	if (getProcessBean().getRequirementDate() != null) {
	    return PhdMigrationProcessStateType.REQUESTED_THESIS_DISCUSSION;
	}

	if (getProcessBean().getStartDevelopmentDate() != null || getProcessBean().getRatificationDate() != null) {
	    return PhdMigrationProcessStateType.WORK_DEVELOPMENT;
	}

	if (getProcessBean().getRatificationDate() != null) {
	    return PhdMigrationProcessStateType.CANDIDACY_RATIFIED;
	}

	if (getProcessBean().getStartProcessDate() != null) {
	    return PhdMigrationProcessStateType.CANDIDACY_CREATED;
	}

	return PhdMigrationProcessStateType.NOT_MIGRATED;
    }

    public boolean possibleToCompleteNextState() {
	final PhdMigrationProcessStateType activeState = getMigrationStatus();
	
	if(activeState.equals(PhdMigrationProcessStateType.CANCELED) || activeState.equals(PhdMigrationProcessStateType.CONCLUDED)) {
	    return false;
	}
	
	if (getProcessBean().getClassification() != null) {
	    return true;
	}

	if (activeState.equals(PhdMigrationProcessStateType.COMPLETED_THESIS_DISCUSSION)) {
	    if (getProcessBean().getEdictDate() != null) {
		return true;
	    }
	}

	if (activeState.equals(PhdMigrationProcessStateType.REQUESTED_THESIS_DISCUSSION)) {
	    if (getProcessBean().getFirstDiscussionDate() != null || getProcessBean().getSecondDiscussionDate() != null) {
		return true;
	    }
	}
	
	if(activeState.equals(PhdMigrationProcessStateType.WORK_DEVELOPMENT)) {
	    if (getProcessBean().getRequirementDate() != null) {
		return true;
	    }
	}
	
	if (activeState.equals(PhdMigrationProcessStateType.CANDIDACY_RATIFIED)) {
	    if (getProcessBean().getStartDevelopmentDate() != null || getProcessBean().getRatificationDate() != null) {
		return true;
	    }
	}

	if(activeState.equals(PhdMigrationProcessStateType.CANDIDACY_CREATED)) {
	    if (getProcessBean().getRatificationDate() != null) {
		return true;
	    }
	}
	
	if(activeState.equals(PhdMigrationProcessStateType.NOT_MIGRATED)) {
	    if (getProcessBean().getStartProcessDate() != null) {
		return true;
	    }
	}
	
	return false;
    }

    private boolean isProcessCanceled() {
	return getProcessBean().getAnnulmentDate() != null;
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

	setMigrationDate(new DateTime());

	return returnVal;
    }

    private PhdIndividualProgramProcess createCandidacyProcess(final IUserView userView) {
	final PhdProgramCandidacyProcessBean candidacyBean = new PhdProgramCandidacyProcessBean();

	candidacyBean.setCandidacyDate(getProcessBean().getStartProcessDate());
	candidacyBean.setState(PhdProgramCandidacyProcessState.STAND_BY_WITH_COMPLETE_INFORMATION);
	candidacyBean.setPersonBean(getPhdMigrationIndividualPersonalData().getPersonBean());
	candidacyBean.setMigratedProcess(true);
	candidacyBean.setProgram(getProcessBean().getPhdProgram());
	candidacyBean.setThesisTitle(getProcessBean().getTitle());
	candidacyBean.setPhdStudentNumber(getPhdMigrationIndividualPersonalData().getNumber());
	candidacyBean.setCollaborationType(PhdIndividualProgramCollaborationType.NONE);
	candidacyBean.setExecutionYear(getExecutionYear());
	candidacyBean.setFocusArea((getProcessBean().getPhdProgram().getPhdProgramFocusAreasCount() == 1) ? getProcessBean()
		.getPhdProgram().getPhdProgramFocusAreas().get(0)
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
	ratifyBean.setWhenRatified(getProcessBean().getRatificationDate());
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
	if (getProcessBean().getStartDevelopmentDate() != null) {
	    return getProcessBean().getStartDevelopmentDate();
	} else {
	    return getProcessBean().getRatificationDate();
	}
    }

    private void requirePublicThesisPresentation(final IUserView userView, final PhdIndividualProgramProcess individualProcess) {
	ExecuteProcessActivity.run(individualProcess, ExemptPublicPresentationSeminarComission.class.getSimpleName(),
		new PublicPresentationSeminarProcessBean());

	if (!StringUtils.isEmpty(getProcessBean().getGuiderNumber())) {
	    final PhdMigrationGuiding migrationGuiding = getGuiding(getProcessBean().getGuiderNumber());
	    if (migrationGuiding != null) {
		migrationGuiding.parse();
		final PhdParticipantBean guidingBean = migrationGuiding.getPhdParticipantBean(individualProcess);
		ExecuteProcessActivity.run(individualProcess, AddGuidingInformation.class.getSimpleName(), guidingBean);
	    }
	}

	if (!StringUtils.isEmpty(getProcessBean().getAssistantGuiderNumber())) {
	    final PhdMigrationGuiding migrationAssistantGuiding = getGuiding(getProcessBean().getAssistantGuiderNumber());
	    if (migrationAssistantGuiding != null) {
		migrationAssistantGuiding.parse();
		final PhdParticipantBean assistantGuidingBean = migrationAssistantGuiding
			.getPhdParticipantBean(individualProcess);
		ExecuteProcessActivity.run(individualProcess, AddAssistantGuidingInformation.class.getSimpleName(),
			assistantGuidingBean);
	    }
	}

	final PhdThesisProcessBean thesisBean = new PhdThesisProcessBean(individualProcess);
	thesisBean.setWhenThesisDiscussionRequired(getProcessBean().getRequirementDate());
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
	if (getProcessBean().getSecondDiscussionDate() != null) {
	    return getProcessBean().getSecondDiscussionDate().toDateTimeAtCurrentTime();
	} else if (getProcessBean().getFirstDiscussionDate() != null) {
	    return getProcessBean().getFirstDiscussionDate().toDateTimeAtCurrentTime();
	}

	return null;
    }

    private DateTime getMeetingDate() {
	if (getProcessBean().getMeetingDate() != null) {
	    return getProcessBean().getMeetingDate().toDateTimeAtCurrentTime();
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
	thesisBean.setWhenFinalThesisRatified(getProcessBean().getEdictDate());
	ExecuteProcessActivity.run(thesisProcess, RatifyFinalThesis.class, thesisBean);

	thesisBean.setConclusionDate(getProcessBean().getEdictDate());
	thesisBean.setFinalGrade(getProcessBean().getClassification());
	ExecuteProcessActivity.run(thesisProcess, SetFinalGrade.class, thesisBean);
    }

    private void cancelPhdProgram(final IUserView userView, final PhdIndividualProgramProcess individualProcess) {
	final PhdIndividualProgramProcessBean processBean = new PhdIndividualProgramProcessBean(individualProcess);
	ExecuteProcessActivity.run(individualProcess, CancelPhdProgramProcess.class.getSimpleName(), processBean);
    }

}
