package net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.Activity;
import net.sourceforge.fenixedu.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessState;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;
import net.sourceforge.fenixedu.domain.period.DegreeTransferCandidacyPeriod;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.joda.time.DateTime;

import dml.runtime.RelationAdapter;

public class DegreeTransferCandidacyProcess extends DegreeTransferCandidacyProcess_Base {

    static {
	CandidacyPeriodCandidacyProcess.addListener(new RelationAdapter<CandidacyProcess, CandidacyPeriod>() {
	    @Override
	    public void beforeAdd(CandidacyProcess candidacyProcess, CandidacyPeriod candidacyPeriod) {
		super.beforeAdd(candidacyProcess, candidacyPeriod);
		if (candidacyProcess != null && candidacyPeriod != null
			&& candidacyPeriod instanceof DegreeTransferCandidacyPeriod) {
		    if (candidacyPeriod.hasAnyCandidacyProcesses()) {
			throw new DomainException("error.DegreeTransferCandidacyProcess.candidacy.period.already.has.process");
		    }
		}
	    }
	});
    }

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new EditCandidacyPeriod());
	// activities.add(new SendToCoordinator());
	// activities.add(new SendToScientificCouncil());
	// activities.add(new PrintCandidaciesFromInstitutionDegrees());
	// activities.add(new PrintCandidaciesFromExternalDegrees());
	// activities.add(new IntroduceCandidacyResults());
	// activities.add(new PublishCandidacyResults());
	// activities.add(new CreateRegistrations());
    }

    private DegreeTransferCandidacyProcess() {
	super();
    }

    private DegreeTransferCandidacyProcess(final ExecutionYear executionYear, final DateTime start, final DateTime end) {
	this();
	checkParameters(executionYear, start, end);
	setState(CandidacyProcessState.STAND_BY);
	new DegreeTransferCandidacyPeriod(this, executionYear, start, end);
    }

    private void checkParameters(final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
	if (executionInterval == null) {
	    throw new DomainException("error.DegreeTransferCandidacyProcess.invalid.executionInterval");
	}

	if (start == null || end == null || start.isAfter(end)) {
	    throw new DomainException("error.DegreeTransferCandidacyProcess.invalid.interval");
	}
    }

    private void edit(final DateTime start, final DateTime end) {
	checkParameters(getCandidacyPeriod().getExecutionInterval(), start, end);
	getCandidacyPeriod().edit(start, end);
    }

    @Override
    public boolean canExecuteActivity(IUserView userView) {
	return isDegreeAdministrativeOfficeEmployee(userView);
    }

    @Override
    public List<Activity> getActivities() {
	return activities;
    }

    // static information

    static private boolean isDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isDegree();
    }

    @StartActivity
    static public class CreateCandidacyPeriod extends Activity<DegreeTransferCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeTransferCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeTransferCandidacyProcess executeActivity(DegreeTransferCandidacyProcess dummy, IUserView userView,
		Object object) {
	    final CandidacyProcessBean bean = (CandidacyProcessBean) object;
	    return new DegreeTransferCandidacyProcess((ExecutionYear) bean.getExecutionInterval(), bean.getStart(), bean.getEnd());
	}
    }

    static private class EditCandidacyPeriod extends Activity<DegreeTransferCandidacyProcess> {
	@Override
	public void checkPreConditions(DegreeTransferCandidacyProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeTransferCandidacyProcess executeActivity(DegreeTransferCandidacyProcess process, IUserView userView,
		Object object) {
	    final CandidacyProcessBean bean = (CandidacyProcessBean) object;
	    process.edit(bean.getStart(), bean.getEnd());
	    return process;
	}
    }
}
