package net.sourceforge.fenixedu.domain.phd;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Job;
import net.sourceforge.fenixedu.domain.JobBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.QualificationBean;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

public class PhdIndividualProgramProcess extends PhdIndividualProgramProcess_Base {

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new EditPersonalInformation());
	activities.add(new AddQualification());
	activities.add(new DeleteQualification());
	activities.add(new AddJobInformation());
	activities.add(new DeleteJobInformation());
	activities.add(new EditIndividualProcessInformation());
	activities.add(new AddGuidingInformation());
	activities.add(new DeleteGuiding());
	activities.add(new AddAssistantGuidingInformation());
	activities.add(new DeleteAssistantGuiding());
    }

    @StartActivity
    static public class CreateCandidacy extends Activity<PhdIndividualProgramProcess> {

	@Override
	public void checkPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    // no precondition to check
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess noProcess, IUserView userView,
		Object object) {

	    final PhdProgramCandidacyProcessBean individualProgramBean = (PhdProgramCandidacyProcessBean) object;
	    final PhdIndividualProgramProcess createdProcess = new PhdIndividualProgramProcess(individualProgramBean);
	    final PhdProgramCandidacyProcess candidacyProcess = Process.createNewProcess(userView,
		    PhdProgramCandidacyProcess.class, object);

	    candidacyProcess.setIndividualProgramProcess(createdProcess);

	    return createdProcess;

	}

    }

    static public class EditPersonalInformation extends Activity<PhdIndividualProgramProcess> {

	@Override
	public void checkPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    // no precondition to check
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    process.getPerson().edit((PersonBean) object);
	    return process;
	}
    }

    static public class AddQualification extends Activity<PhdIndividualProgramProcess> {

	@Override
	public void checkPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    // no precondition to check yet
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    return process.addQualification(userView.getPerson(), (QualificationBean) object);
	}

    }

    static public class DeleteQualification extends Activity<PhdIndividualProgramProcess> {

	@Override
	public void checkPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    // no precondition to check yet
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    final Qualification qualification = (Qualification) object;
	    if (process.getPerson().hasAssociatedQualifications(qualification)) {
		if (!canDelete(qualification, userView.getPerson())) {
		    throw new DomainException("error.PhdIndividualProgramProcess.DeleteQualification.not.authorized");
		}
		qualification.delete();
	    }
	    return process;
	}

	private boolean canDelete(final Qualification qualification, final Person person) {
	    if (!qualification.hasCreator()) {
		return false;
	    }
	    final Person creator = qualification.getCreator();
	    return creator == person || creator.hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE);
	}
    }

    static public class AddJobInformation extends Activity<PhdIndividualProgramProcess> {

	@Override
	public void checkPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    // no precondition to check yet
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    return process.addJobInformation(userView.getPerson(), (JobBean) object);
	}
    }

    static public class DeleteJobInformation extends Activity<PhdIndividualProgramProcess> {

	@Override
	public void checkPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    // no precondition to check yet
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    final Job job = (Job) object;
	    if (process.getPerson().hasJobs(job)) {
		if (!canDelete(job, userView.getPerson())) {
		    throw new DomainException("error.PhdIndividualProgramProcess.DeleteJobInformation.not.authorized");
		}
		job.delete();
	    }
	    return process;
	}

	private boolean canDelete(final Job job, final Person person) {
	    if (!job.hasCreator()) {
		return false;
	    }

	    return job.getCreator() == person || job.getCreator().hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE);
	}
    }

    static public class EditIndividualProcessInformation extends Activity<PhdIndividualProgramProcess> {

	@Override
	public void checkPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    // no precondition to check yet
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    return process.edit(userView, (PhdIndividualProgramProcessBean) object);
	}
    }

    static public class AddGuidingInformation extends Activity<PhdIndividualProgramProcess> {

	@Override
	public void checkPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    // no precondition to check yet
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    return process.addGuiding((PhdProgramGuidingBean) object);
	}
    }

    static public class DeleteGuiding extends Activity<PhdIndividualProgramProcess> {

	@Override
	public void checkPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    // no precondition to check yet
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    return process.deleteGuiding();
	}
    }

    static public class AddAssistantGuidingInformation extends Activity<PhdIndividualProgramProcess> {

	@Override
	public void checkPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    // no precondition to check yet
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    return process.addAssistantGuiding((PhdProgramGuidingBean) object);
	}
    }

    static public class DeleteAssistantGuiding extends Activity<PhdIndividualProgramProcess> {

	@Override
	public void checkPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	    // no precondition to check yet
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView,
		Object object) {
	    return process.deleteAssistantGuiding((PhdProgramGuiding) object);
	}
    }

    static private boolean isMasterDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isMasterDegree();
    }

    public PhdIndividualProgramProcess(PhdProgramCandidacyProcessBean bean) {
	super();
	final Person person = bean.getOrCreatePersonFromBean();
	checkParameters(person, bean.getExecutionYear(), bean.getProgram());
	setPerson(person);
	setExecutionYear(bean.getExecutionYear());
	setPhdProgram(bean.getProgram());
	setCollaborationType(PhdIndividualProgramCollaborationType.NONE);
	setState(PhdIndividualProgramProcessState.CANDIDACY);
	setPhdIndividualProcessNumber(PhdIndividualProgramProcessNumber.generateNextForYear(bean.getCandidacyDate().getYear()));
    }

    private void checkParameters(Person person, ExecutionYear executionYear, PhdProgram phdProgram) {
	check(person, "error.phd.PhdIndividualProgramProcess.person.cannot.be.null");
	check(executionYear, "error.phd.PhdIndividualProgramProcess.executionYear.cannot.be.null");
	check(phdProgram, "error.phd.PhdIndividualProgramProcess.phdProgram.cannot.be.null");
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
	return true;
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    @Override
    public String getDisplayName() {
	return ResourceBundle.getBundle("resources/PhdResources").getString(getClass().getSimpleName());
    }

    public String getCollaborationTypeName() {
	return getCollaborationType().getLocalizedName()
		+ (isOtherCollaborationType() ? " (" + getOtherCollaborationType() + ")" : "");
    }

    private boolean isOtherCollaborationType() {
	return getCollaborationType() == PhdIndividualProgramCollaborationType.OTHER;
    }

    private PhdIndividualProgramProcess addQualification(final Person person, final QualificationBean bean) {
	final Qualification qualification = new Qualification(getPerson(), bean);
	qualification.setCreator(person);
	return this;
    }

    private PhdIndividualProgramProcess addJobInformation(final Person person, final JobBean bean) {
	final Job job = new Job(getPerson(), bean);
	job.setCreator(person);
	return this;
    }

    private PhdIndividualProgramProcess addGuiding(final PhdProgramGuidingBean bean) {
	setGuiding(createPhdProgramGuiding(bean));
	return this;
    }

    public PhdIndividualProgramProcess deleteGuiding() {
	getGuiding().delete();
	return this;
    }

    private PhdIndividualProgramProcess addAssistantGuiding(final PhdProgramGuidingBean bean) {
	addAssistantguidings(createPhdProgramGuiding(bean));
	return this;
    }

    private PhdProgramGuiding createPhdProgramGuiding(final PhdProgramGuidingBean bean) {
	final PhdProgramGuiding guiding;
	if (bean.isInternal()) {
	    guiding = PhdProgramGuiding.create(bean.getPerson());
	} else {
	    guiding = PhdProgramGuiding.create(bean.getName(), bean.getQualification(), bean.getWorkLocation(), bean.getEmail());
	    guiding.edit(bean.getCategory(), bean.getAddress(), bean.getPhone());
	}
	return guiding;
    }

    public PhdIndividualProgramProcess deleteAssistantGuiding(PhdProgramGuiding assistant) {
	if (hasAssistantguidings(assistant)) {
	    assistant.delete();
	}
	return this;
    }

    public String getProcessNumber() {
	return getPhdIndividualProcessNumber().getFullProcessNumber();
    }

    private PhdIndividualProgramProcess edit(final IUserView userView, final PhdIndividualProgramProcessBean bean) {

	if (hasCandidacyProcess()) {
	    getCandidacyProcess().executeActivity(userView, PhdProgramCandidacyProcess.EditCandidacyDate.class.getSimpleName(),
		    bean.getCandidacyDate());
	}

	setThesisTitle(bean.getThesisTitle());
	setCollaborationType(bean.getCollaborationType());

	if (bean.isOtherCollaborationTypeSelected()) {
	    check(bean.getOtherCollaborationType(), "error.PhdIndividualProgramProcess.invalid.other.collaboration.type");
	    setOtherCollaborationType(bean.getOtherCollaborationType());
	}

	return this;
    }

    public LocalDate getCandidacyDate() {
	return getCandidacyProcess().getCandidacyDate();
    }

    static public Set<PhdIndividualProgramProcess> search(SearchPhdIndividualProgramProcessBean searchBean) {
	final Set<PhdIndividualProgramProcess> result = new HashSet<PhdIndividualProgramProcess>();
	final Set<Process> processesToSearch = new HashSet<Process>();

	if (!StringUtils.isEmpty(searchBean.getProcessNumber())) {

	    for (final PhdIndividualProgramProcessNumber phdIndividualProgramProcessNumber : RootDomainObject.getInstance()
		    .getPhdIndividualProcessNumbers()) {
		if (phdIndividualProgramProcessNumber.getFullProcessNumber().equals(searchBean.getProcessNumber())) {
		    processesToSearch.add(phdIndividualProgramProcessNumber.getProcess());
		}
	    }

	} else {
	    processesToSearch.addAll(RootDomainObject.getInstance().getProcesses());
	}

	for (final Process each : processesToSearch) {
	    if (each instanceof PhdIndividualProgramProcess) {
		final PhdIndividualProgramProcess phdIndividualProgramProcess = (PhdIndividualProgramProcess) each;
		if (matchesExecutionYear(searchBean, phdIndividualProgramProcess)
			&& matchesProcessState(searchBean, phdIndividualProgramProcess)) {
		    result.add(phdIndividualProgramProcess);
		}
	    }
	}

	return result;
    }

    private static boolean matchesProcessState(SearchPhdIndividualProgramProcessBean searchBean,
	    final PhdIndividualProgramProcess phdIndividualProgramProcess) {
	return searchBean.getProcessState() == null || searchBean.getProcessState() == phdIndividualProgramProcess.getState();
    }

    private static boolean matchesExecutionYear(SearchPhdIndividualProgramProcessBean searchBean,
	    final PhdIndividualProgramProcess phdIndividualProgramProcess) {
	return searchBean.getExecutionYear() == null
		|| searchBean.getExecutionYear() == phdIndividualProgramProcess.getExecutionYear();
    }
}
