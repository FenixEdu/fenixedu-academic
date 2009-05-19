package net.sourceforge.fenixedu.domain.phd;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Qualification;
import net.sourceforge.fenixedu.domain.QualificationBean;
import net.sourceforge.fenixedu.domain.caseHandling.Activity;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean;

public class PhdIndividualProgramProcess extends PhdIndividualProgramProcess_Base {

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new EditPersonalInformation());
	activities.add(new AddQualification());
	activities.add(new DeleteQualification());
	activities.add(new EditJobsInformation());
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
	    final PhdIndividualProgramProcess createdProcess = new PhdIndividualProgramProcess(individualProgramBean
		    .getOrCreatePersonFromBean(), individualProgramBean.getExecutionYear(), individualProgramBean.getProgram());
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
	    return process.addQualification((QualificationBean) object);
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
		qualification.delete();
	    }
	    return process;
	}
    }

    static public class EditJobsInformation extends Activity<PhdIndividualProgramProcess> {

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

	    // TODO Auto-generated method stub
	    return null;
	}

    }

    static private boolean isMasterDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isMasterDegree();
    }

    private PhdIndividualProgramProcess(final Person person, final ExecutionYear executionYear, final PhdProgram program) {
	super();
	setPerson(person);
	setExecutionYear(executionYear);
	setPhdProgram(program);
	setState(PhdIndividualProgramProcessState.CANDIDACY);
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

    private PhdIndividualProgramProcess addQualification(final QualificationBean bean) {
	new Qualification(getPerson(), bean);
	return this;
    }

}
