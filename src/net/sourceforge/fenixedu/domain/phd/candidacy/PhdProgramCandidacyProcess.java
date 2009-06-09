package net.sourceforge.fenixedu.domain.phd.candidacy;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;

import org.joda.time.LocalDate;

public class PhdProgramCandidacyProcess extends PhdProgramCandidacyProcess_Base {

    static abstract private class PhdActivity extends Activity<PhdProgramCandidacyProcess> {

	@Override
	final public void checkPreConditions(final PhdProgramCandidacyProcess process, final IUserView userView) {
	    processPreConditions(process, userView);
	    activityPreConditions(process, userView);
	}

	protected void processPreConditions(final PhdProgramCandidacyProcess process, final IUserView userView) {
	    // if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
	    // throw new PreConditionNotValidException();
	    // }
	}

	abstract protected void activityPreConditions(final PhdProgramCandidacyProcess process, final IUserView userView);
    }

    @StartActivity
    public static class CreateCandidacy extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	}

	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    final PhdProgramCandidacyProcess result = new PhdProgramCandidacyProcess((PhdProgramCandidacyProcessBean) object);
	    result.setState(PhdProgramCandidacyProcessState.STAND_BY_WITH_MISSING_INFORMATION);
	    return result;
	}
    }

    public static class UploadDocuments extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess arg0, IUserView arg1) {
	}

	@SuppressWarnings("unchecked")
	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    final List<PhdCandidacyDocumentUploadBean> documents = (List<PhdCandidacyDocumentUploadBean>) object;

	    for (final PhdCandidacyDocumentUploadBean each : documents) {
		if (each.hasAnyInformation()) {
		    new PhdProgramCandidacyProcessDocument(process, each.getType(), each.getRemarks(), each.getFileContent(),
			    each.getFilename(), userView.getPerson());
		}
	    }

	    return process;
	}
    }

    public static class DeleteDocument extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess arg0, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@SuppressWarnings("unchecked")
	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    ((PhdProgramCandidacyProcessDocument) object).delete();

	    return process;
	}
    }

    public static class EditCandidacyDate extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess arg0, IUserView userView) {
	    if (!isMasterDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@SuppressWarnings("unchecked")
	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    return process.edit((LocalDate) object);
	}
    }

    static public class AddCandidacyReferees extends PhdActivity {

	@Override
	protected void activityPreConditions(PhdProgramCandidacyProcess process, IUserView userView) {
	}

	@Override
	protected PhdProgramCandidacyProcess executeActivity(PhdProgramCandidacyProcess process, IUserView userView, Object object) {
	    for (final PhdCandidacyRefereeBean bean : (List<PhdCandidacyRefereeBean>) object) {
		process.addCandidacyReferees(new PhdCandidacyReferee(process, bean));
	    }
	    return process;
	}
    }

    static private boolean isMasterDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isMasterDegree();
    }

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new UploadDocuments());
	activities.add(new DeleteDocument());
	activities.add(new EditCandidacyDate());
	activities.add(new AddCandidacyReferees());
    }

    private PhdProgramCandidacyProcess(final PhdProgramCandidacyProcessBean bean) {
	super();

	// TODO: receive person as argument?
	checkCandidacyDate(bean.getExecutionYear(), bean.getCandidacyDate());
	setCandidacyDate(bean.getCandidacyDate());

	final Person person = bean.getOrCreatePersonFromBean();

	// if (!person.hasStudent()) {
	// TODO: generate when creating registration?
	// new Student(person);
	// }
	// person.setIstUsername();

	setCandidacyHashCode(bean.getCandidacyHashCode());
	setCandidacy(new PHDProgramCandidacy(person));

	if (bean.hasDegree()) {
	    getCandidacy().setExecutionDegree(bean.getExecutionDegree());
	}

	if (!bean.hasCollaborationType() || bean.getCollaborationType().generateCandidacyDebt()) {
	    new PhdProgramCandidacyEvent(AdministrativeOffice
		    .readByAdministrativeOfficeType(AdministrativeOfficeType.MASTER_DEGREE), bean.getOrCreatePersonFromBean(),
		    this);
	}
    }

    private void checkCandidacyDate(ExecutionYear executionYear, LocalDate candidacyDate) {
	check(candidacyDate, "error.phd.candidacy.PhdProgramCandidacyProcess.invalid.candidacy.date");
	if (!executionYear.containsDate(candidacyDate)) {
	    throw new DomainException(
		    "error.phd.candidacy.PhdProgramCandidacyProcess.executionYear.doesnot.contains.candidacy.date", candidacyDate
			    .toString("dd/MM/yyyy"), executionYear.getQualifiedName(), executionYear.getBeginDateYearMonthDay()
			    .toString("dd/MM/yyyy"), executionYear.getEndDateYearMonthDay().toString("dd/MM/yyyy"));
	}
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
	return false;
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    @Override
    public String getDisplayName() {
	return ResourceBundle.getBundle("resources/PhdResources").getString(getClass().getSimpleName());
    }

    private PhdProgramCandidacyProcess edit(final LocalDate candidacyDate) {
	checkCandidacyDate(getExecutionYear(), candidacyDate);
	setCandidacyDate(candidacyDate);
	return this;
    }

    private ExecutionYear getExecutionYear() {
	return getIndividualProgramProcess().getExecutionYear();
    }

    public boolean hasAnyPayments() {
	return hasEvent() && getEvent().hasAnyPayments();
    }

    public void cancelDebt(final Employee employee) {
	getEvent().cancel(employee);
    }

    public String getProcessNumber() {
	return getIndividualProgramProcess().getProcessNumber();
    }

    public Person getPerson() {
	return getIndividualProgramProcess().getPerson();
    }
}
