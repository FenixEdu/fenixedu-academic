package net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.Activity;
import net.sourceforge.fenixedu.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessBean;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessState;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;
import net.sourceforge.fenixedu.domain.period.DegreeCandidacyForGraduatedPersonCandidacyPeriod;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.joda.time.DateTime;

import dml.runtime.RelationAdapter;

public class DegreeCandidacyForGraduatedPersonProcess extends DegreeCandidacyForGraduatedPersonProcess_Base {

    static {
	CandidacyPeriodCandidacyProcess.addListener(new RelationAdapter<CandidacyProcess, CandidacyPeriod>() {
	    @Override
	    public void beforeAdd(CandidacyProcess candidacyProcess, CandidacyPeriod candidacyPeriod) {
		super.beforeAdd(candidacyProcess, candidacyPeriod);

		if (candidacyProcess != null && candidacyPeriod != null
			&& candidacyPeriod instanceof DegreeCandidacyForGraduatedPersonCandidacyPeriod) {
		    if (candidacyPeriod.hasAnyCandidacyProcesses()) {
			throw new DomainException(
				"error.DegreeCandidacyForGraduatedPersonProcess.candidacy.period.already.has.process");
		    }
		}
	    }
	});
    }

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new EditCandidacyPeriod());
    }

    private DegreeCandidacyForGraduatedPersonProcess() {
	super();
    }

    private DegreeCandidacyForGraduatedPersonProcess(final ExecutionInterval executionInterval, final DateTime start,
	    final DateTime end) {
	this();
	checkParameters(executionInterval, start, end);
	setState(CandidacyProcessState.STAND_BY);
	new DegreeCandidacyForGraduatedPersonCandidacyPeriod(this, executionInterval, start, end);
    }

    private void checkParameters(final ExecutionInterval executionInterval, final DateTime start, final DateTime end) {
	if (executionInterval == null) {
	    throw new DomainException("error.DegreeCandidacyForGraduatedPersonProcess.invalid.executionInterval");
	}

	if (start == null || end == null || start.isAfter(end)) {
	    throw new DomainException("error.DegreeCandidacyForGraduatedPersonProcess.invalid.interval");
	}
    }

    private void edit(final DateTime start, final DateTime end) {
	checkParameters(getCandidacyPeriod().getExecutionInterval(), start, end);
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

    // static methods

    static private boolean isDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isDegree();
    }

    @StartActivity
    static public class CreateCandidacyPeriod extends Activity<DegreeCandidacyForGraduatedPersonProcess> {

	@Override
	public void checkPreConditions(DegreeCandidacyForGraduatedPersonProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeCandidacyForGraduatedPersonProcess executeActivity(DegreeCandidacyForGraduatedPersonProcess process,
		IUserView userView, Object object) {
	    final CandidacyProcessBean bean = (CandidacyProcessBean) object;
	    return new DegreeCandidacyForGraduatedPersonProcess(bean.getExecutionInterval(), bean.getStart(), bean.getEnd());
	}
    }

    static private class EditCandidacyPeriod extends Activity<DegreeCandidacyForGraduatedPersonProcess> {

	@Override
	public void checkPreConditions(DegreeCandidacyForGraduatedPersonProcess process, IUserView userView) {
	    if (!isDegreeAdministrativeOfficeEmployee(userView)) {
		throw new PreConditionNotValidException();
	    }
	}

	@Override
	protected DegreeCandidacyForGraduatedPersonProcess executeActivity(DegreeCandidacyForGraduatedPersonProcess process,
		IUserView userView, Object object) {
	    final CandidacyProcessBean bean = (CandidacyProcessBean) object;
	    process.edit(bean.getStart(), bean.getEnd());
	    return process;
	}
    }
}
