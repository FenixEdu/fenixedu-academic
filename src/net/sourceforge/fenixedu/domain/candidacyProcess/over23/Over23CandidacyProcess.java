package net.sourceforge.fenixedu.domain.candidacyProcess.over23;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.Activity;
import net.sourceforge.fenixedu.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.AcademicPeriod;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessState;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;
import net.sourceforge.fenixedu.domain.period.Over23CandidacyPeriod;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

import dml.runtime.RelationAdapter;

public class Over23CandidacyProcess extends Over23CandidacyProcess_Base {

    static {
	CandidacyPeriodCandidacyProcess.addListener(new RelationAdapter<CandidacyProcess, CandidacyPeriod>() {
	    @Override
	    public void beforeAdd(CandidacyProcess candidacyProcess, CandidacyPeriod candidacyPeriod) {
		super.beforeAdd(candidacyProcess, candidacyPeriod);

		if (candidacyProcess != null && candidacyPeriod != null && candidacyPeriod instanceof Over23CandidacyPeriod) {
		    if (candidacyPeriod.hasAnyCandidacyProcesses()) {
			throw new DomainException("error.Over23CandidacyProcess.candidacy.period.already.has.process");
		    }
		}
	    }
	});
    }

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new SetJury());
	activities.add(new EditCandidacyPeriod());
	activities.add(new SendInformationToJury());
	activities.add(new PrintCandidacies());
	activities.add(new InsertResultsFromJury());
	activities.add(new PublishCandidacyResults());
	activities.add(new CreateRegistrations());
    }

    protected Over23CandidacyProcess() {
	super();
    }

    protected Over23CandidacyProcess(final AcademicPeriod academicPeriod, final DateTime start, final DateTime end) {
	this();
	checkParameters(academicPeriod, start, end);
	setState(CandidacyProcessState.STAND_BY);
	new Over23CandidacyPeriod(this, academicPeriod, start, end);
    }

    private void checkParameters(final AcademicPeriod academicPeriod, final DateTime start, final DateTime end) {
	if (academicPeriod == null) {
	    throw new DomainException("error.Over23CandidacyProcess.invalid.academicPeriod");
	}

	if (start == null || end == null || start.isAfter(end)) {
	    throw new DomainException("error.Over23CandidacyProcess.invalid.interval");
	}
    }

    private void edit(final DateTime start, final DateTime end) {
	checkParameters(getCandidacyPeriod().getAcademicPeriod(), start, end);
	getCandidacyPeriod().edit(start, end);
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
	return isDegreeAdministrativeOfficeEmployee(userView) || userView.hasRoleType(RoleType.SCIENTIFIC_COUNCIL);
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    @Override
    public String getDisplayName() {
	return ResourceBundle.getBundle("resources/CaseHandlingResources").getString("label." + getClass().getName());
    }

    public AcademicPeriod getCandidacyAcademicPeriod() {
	return getCandidacyPeriod().getAcademicPeriod();
    }

    public DateTime getCandidacyStart() {
	return getCandidacyPeriod().getStart();
    }

    public DateTime getCandidacyEnd() {
	return getCandidacyPeriod().getEnd();
    }

    public boolean hasOpenCandidacyPeriod() {
	return hasCandidacyPeriod() && getCandidacyPeriod().isOpen();
    }

    public boolean hasOpenCandidacyPeriod(final DateTime date) {
	return hasCandidacyPeriod() && getCandidacyPeriod().isOpen(date);
    }

    boolean isInStandBy() {
	return getState() == CandidacyProcessState.STAND_BY;
    }

    boolean isSentToJury() {
	return getState() == CandidacyProcessState.SENT_TO_JURY;
    }

    boolean isPublished() {
	return getState() == CandidacyProcessState.PUBLISHED;
    }

    public List<Over23IndividualCandidacyProcess> getOver23IndividualCandidaciesThatCanBeSendToJury() {
	final List<Over23IndividualCandidacyProcess> result = new ArrayList<Over23IndividualCandidacyProcess>();
	for (final Over23IndividualCandidacyProcess child : getChildProcesses()) {
	    if (child.canBeSendToJury()) {
		result.add(child);
	    }
	}
	return result;
    }

    static private boolean isDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isDegree();
    }

    @StartActivity
    static public class CreateCandidacyPeriod extends Activity<Over23CandidacyProcess> {

	@Override
	public void checkPreConditions(Over23CandidacyProcess process, IUserView userView) {
	    if (!userView.hasRoleType(RoleType.SCIENTIFIC_COUNCIL)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected Over23CandidacyProcess executeActivity(Over23CandidacyProcess process, IUserView userView, Object object) {
	    final Over23CandidacyProcessBean bean = (Over23CandidacyProcessBean) object;
	    return new Over23CandidacyProcess(bean.getAcademicPeriod(), bean.getStart(), bean.getEnd());
	}
    }

    static public class SetJury extends Activity<Over23CandidacyProcess> {

	@Override
	public void checkPreConditions(Over23CandidacyProcess process, IUserView userView) {
	    throw new PreConditionNotValidException();
	}

	@Override
	protected Over23CandidacyProcess executeActivity(Over23CandidacyProcess process, IUserView userView, Object object) {
	    return process;
	}
    }

    static public class EditCandidacyPeriod extends Activity<Over23CandidacyProcess> {

	@Override
	public void checkPreConditions(Over23CandidacyProcess process, IUserView userView) {
	    if (!userView.hasRoleType(RoleType.SCIENTIFIC_COUNCIL)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected Over23CandidacyProcess executeActivity(Over23CandidacyProcess process, IUserView userView, Object object) {
	    final Over23CandidacyProcessBean bean = (Over23CandidacyProcessBean) object;
	    process.edit(bean.getStart(), bean.getEnd());
	    return process;
	}
    }

    static public class SendInformationToJury extends Activity<Over23CandidacyProcess> {

	@Override
	public void checkPreConditions(Over23CandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (!process.hasCandidacyPeriod() || process.hasOpenCandidacyPeriod()) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.isInStandBy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected Over23CandidacyProcess executeActivity(Over23CandidacyProcess process, IUserView userView, Object object) {
	    process.setState(CandidacyProcessState.SENT_TO_JURY);
	    return process;
	}
    }

    static public class PrintCandidacies extends Activity<Over23CandidacyProcess> {

	@Override
	public void checkPreConditions(Over23CandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	    if (process.isInStandBy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected Over23CandidacyProcess executeActivity(Over23CandidacyProcess process, IUserView userView, Object object) {
	    return process; // for now, nothing to be done
	}
    }

    static public class InsertResultsFromJury extends Activity<Over23CandidacyProcess> {

	@Override
	public void checkPreConditions(Over23CandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (process.isInStandBy()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected Over23CandidacyProcess executeActivity(Over23CandidacyProcess process, IUserView userView, Object object) {
	    final List<Over23IndividualCandidacyResultBean> beans = (List<Over23IndividualCandidacyResultBean>) object;
	    for (final Over23IndividualCandidacyResultBean bean : beans) {
		bean.getCandidacyProcess().executeActivity(userView, "IntroduceCandidacyResult", bean);
	    }
	    return process;
	}
    }

    static public class PublishCandidacyResults extends Activity<Over23CandidacyProcess> {

	@Override
	public void checkPreConditions(Over23CandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }

	    if (!process.isSentToJury()) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected Over23CandidacyProcess executeActivity(Over23CandidacyProcess process, IUserView userView, Object object) {
	    process.setState(CandidacyProcessState.PUBLISHED);
	    return process;
	}
    }

    static public class CreateRegistrations extends Activity<Over23CandidacyProcess> {

	@Override
	public void checkPreConditions(Over23CandidacyProcess process, IUserView userView) {

	    //TODO: remove this and remove comment
	    throw new PreConditionNotValidException();

	    //	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
	    //		throw new PreConditionNotValidException();
	    //	    }
	    //	    
	    //	    if (!process.isPublished()) {
	    //		throw new PreConditionNotValidException();
	    //	    }
	}

	@Override
	protected Over23CandidacyProcess executeActivity(Over23CandidacyProcess process, IUserView userView, Object object) {
	    for (final Over23IndividualCandidacyProcess candidacyProcess : process.getChildProcesses()) {
		if (candidacyProcess.isCandidacyAccepted() && !candidacyProcess.hasRegistrationForCandidacy()) {
		    createRegistration(candidacyProcess);
		}
	    }
	    return process;
	}

	private void createRegistration(final Over23IndividualCandidacyProcess candidacyProcess) {
	    new Registration(candidacyProcess.getCandidacyPerson(), getDegreeCurricularPlan(candidacyProcess));
	}

	private DegreeCurricularPlan getDegreeCurricularPlan(final Over23IndividualCandidacyProcess candidacyProcess) {
	    return candidacyProcess.getAcceptedDegree().getMostRecentDegreeCurricularPlan();
	}
    }
}
