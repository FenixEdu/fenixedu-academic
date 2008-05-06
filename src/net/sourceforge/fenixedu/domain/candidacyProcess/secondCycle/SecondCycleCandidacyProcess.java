package net.sourceforge.fenixedu.domain.candidacyProcess.secondCycle;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.Activity;
import net.sourceforge.fenixedu.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.AcademicPeriod;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessState;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;
import net.sourceforge.fenixedu.domain.period.SecondCycleCandidacyPeriod;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.joda.time.DateTime;

import dml.runtime.RelationAdapter;

public class SecondCycleCandidacyProcess extends SecondCycleCandidacyProcess_Base {

    static {
	CandidacyPeriodCandidacyProcess.addListener(new RelationAdapter<CandidacyProcess, CandidacyPeriod>() {
	    @Override
	    public void beforeAdd(CandidacyProcess candidacyProcess, CandidacyPeriod candidacyPeriod) {
		super.beforeAdd(candidacyProcess, candidacyPeriod);

		if (candidacyProcess != null && candidacyPeriod != null && candidacyPeriod instanceof SecondCycleCandidacyPeriod) {
		    if (candidacyPeriod.hasAnyCandidacyProcesses()) {
			throw new DomainException("error.SecondCycleCandidacyProcess.candidacy.period.already.has.process");
		    }
		}
	    }
	});
    }

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new EditCandidacyPeriod());
	//TODO: activities.add(new SendToCoordinator());
	//TODO: activities.add(new PrintCandidacies());
	//TODO: activities.add(new IntroduceCandidacyResults());
	//TODO: activities.add(new SendToScientificCouncil());
	//TODO: activities.add(new PublishCandidacyResults());
	//TODO: activities.add(new CreateRegistrations());
    }

    private SecondCycleCandidacyProcess() {
	super();
    }

    private SecondCycleCandidacyProcess(final AcademicPeriod academicPeriod, final DateTime start, final DateTime end) {
	this();
	checkParameters(academicPeriod, start, end);
	setState(CandidacyProcessState.STAND_BY);
	new SecondCycleCandidacyPeriod(this, academicPeriod, start, end);
    }

    private void checkParameters(final AcademicPeriod academicPeriod, final DateTime start, final DateTime end) {
	if (academicPeriod == null) {
	    throw new DomainException("error.SecondCycleCandidacyProcess.invalid.academicPeriod");
	}

	if (start == null || end == null || start.isAfter(end)) {
	    throw new DomainException("error.SecondCycleCandidacyProcess.invalid.interval");
	}
    }

    private void edit(final DateTime start, final DateTime end) {
	checkParameters(getCandidacyPeriod().getAcademicPeriod(), start, end);
	getCandidacyPeriod().edit(start, end);
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
	return isDegreeAdministrativeOfficeEmployee(userView) || userView.hasRoleType(RoleType.SCIENTIFIC_COUNCIL)
		|| userView.hasRoleType(RoleType.COORDINATOR);
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    @Override
    public String getDisplayName() {
	final ResourceBundle bundle = ResourceBundle.getBundle("resources/CaseHandlingResources");
	String message = bundle.getString("label." + getClass().getName());
	message += " - " + getCandidacyAcademicPeriod().getName();
	message += " (" + getCandidacyStart().toString("dd/MM/yyyy") + " : " + getCandidacyEnd().toString("dd/MM/yyyy") + ")";
	return message;
    }

    static private boolean isDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isDegree();
    }

    @StartActivity
    static public class CreateCandidacyPeriod extends Activity<SecondCycleCandidacyProcess> {

	@Override
	public void checkPreConditions(SecondCycleCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected SecondCycleCandidacyProcess executeActivity(SecondCycleCandidacyProcess process, IUserView userView,
		Object object) {
	    final SecondCycleCandidacyProcessBean bean = (SecondCycleCandidacyProcessBean) object;
	    return new SecondCycleCandidacyProcess(bean.getAcademicPeriod(), bean.getStart(), bean.getEnd());
	}
    }

    static private class EditCandidacyPeriod extends Activity<SecondCycleCandidacyProcess> {

	@Override
	public void checkPreConditions(SecondCycleCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected SecondCycleCandidacyProcess executeActivity(SecondCycleCandidacyProcess process, IUserView userView,
		Object object) {
	    final SecondCycleCandidacyProcessBean bean = (SecondCycleCandidacyProcessBean) object;
	    process.edit(bean.getStart(), bean.getEnd());
	    return process;
	}
    }

    static private class SendToCoordinator extends Activity<SecondCycleCandidacyProcess> {

	@Override
	public void checkPreConditions(SecondCycleCandidacyProcess process, IUserView userView) {
	    throw new PreConditionNotValidException();
	}

	@Override
	protected SecondCycleCandidacyProcess executeActivity(SecondCycleCandidacyProcess process, IUserView userView,
		Object object) {
	    // TODO Auto-generated method stub
	    return null;
	}
    }

    static private class PrintCandidacies extends Activity<SecondCycleCandidacyProcess> {

	@Override
	public void checkPreConditions(SecondCycleCandidacyProcess process, IUserView userView) {
	    throw new PreConditionNotValidException();

	}

	@Override
	protected SecondCycleCandidacyProcess executeActivity(SecondCycleCandidacyProcess process, IUserView userView,
		Object object) {
	    // TODO Auto-generated method stub
	    return null;
	}
    }

    static private class IntroduceCandidacyResults extends Activity<SecondCycleCandidacyProcess> {

	@Override
	public void checkPreConditions(SecondCycleCandidacyProcess process, IUserView userView) {
	    throw new PreConditionNotValidException();
	}

	@Override
	protected SecondCycleCandidacyProcess executeActivity(SecondCycleCandidacyProcess process, IUserView userView,
		Object object) {
	    // TODO Auto-generated method stub
	    return null;
	}
    }

    static private class SendToScientificCouncil extends Activity<SecondCycleCandidacyProcess> {

	@Override
	public void checkPreConditions(SecondCycleCandidacyProcess process, IUserView userView) {
	    throw new PreConditionNotValidException();

	}

	@Override
	protected SecondCycleCandidacyProcess executeActivity(SecondCycleCandidacyProcess process, IUserView userView,
		Object object) {
	    // TODO Auto-generated method stub
	    return null;
	}

    }

    static private class PublishCandidacyResults extends Activity<SecondCycleCandidacyProcess> {

	@Override
	public void checkPreConditions(SecondCycleCandidacyProcess process, IUserView userView) {
	    throw new PreConditionNotValidException();

	}

	@Override
	protected SecondCycleCandidacyProcess executeActivity(SecondCycleCandidacyProcess process, IUserView userView,
		Object object) {
	    // TODO Auto-generated method stub
	    return null;
	}
    }

    static private class CreateRegistrations extends Activity<SecondCycleCandidacyProcess> {

	@Override
	public void checkPreConditions(SecondCycleCandidacyProcess process, IUserView userView) {
	    throw new PreConditionNotValidException();

	}

	@Override
	protected SecondCycleCandidacyProcess executeActivity(SecondCycleCandidacyProcess process, IUserView userView,
		Object object) {
	    // TODO Auto-generated method stub
	    return null;
	}
    }
}
