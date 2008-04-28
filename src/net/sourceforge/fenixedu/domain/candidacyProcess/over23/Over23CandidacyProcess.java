package net.sourceforge.fenixedu.domain.candidacyProcess.over23;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.caseHandling.Activity;
import net.sourceforge.fenixedu.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.caseHandling.StartActivity;
import net.sourceforge.fenixedu.domain.AcademicPeriod;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcessState;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.period.CandidacyPeriod;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.joda.time.DateTime;

public class Over23CandidacyProcess extends Over23CandidacyProcess_Base {

    static private List<Activity> activities = new ArrayList<Activity>();
    static {
	activities.add(new SetJury());
	activities.add(new EditCandidacyPeriod());
	activities.add(new SendInformationToJury());
	activities.add(new PrintCandidacies());
	activities.add(new InsertResultsFromJury());
	activities.add(new PublishCandidacyResults());
	activities.add(new ModifyCandidacyState());
	activities.add(new CreateRegistrations());
    }

    protected Over23CandidacyProcess() {
	super();
    }

    protected Over23CandidacyProcess(final AcademicPeriod academicPeriod, final DateTime start, final DateTime end) {
	this();
	checkParameters(academicPeriod, start, end);
	checkRules(academicPeriod);
	setCandidacyPeriod(new CandidacyPeriod(academicPeriod, start, end));
	setState(CandidacyProcessState.STAND_BY);
    }

    private void checkParameters(final AcademicPeriod academicPeriod, final DateTime start, final DateTime end) {
	if (academicPeriod == null) {
	    throw new DomainException("error.Over23CandidacyProcess.invalid.academicPeriod");
	}

	if (start == null || end == null || start.isAfter(end)) {
	    throw new DomainException("error.Over23CandidacyProcess.invalid.interval");
	}
    }

    private void checkRules(final AcademicPeriod academicPeriod) {
	for (final CandidacyPeriod period : academicPeriod.getCandidacyPeriods()) {
	    if (period.containsCandidacyProcess(getClass())) {
		throw new DomainException("error.Over23CandidacyProcess.period.already.exists");
	    }
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

    private boolean isDegreeAdministrativeOfficeEmployee(IUserView userView) {
	return userView.hasRoleType(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)
		&& userView.getPerson().getEmployeeAdministrativeOffice().isDegree();
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
	    // TODO Auto-generated method stub
	    return null;
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
	    throw new PreConditionNotValidException();
	}

	@Override
	protected Over23CandidacyProcess executeActivity(Over23CandidacyProcess process, IUserView userView, Object object) {
	    // TODO Auto-generated method stub
	    return null;
	}
    }

    static public class PrintCandidacies extends Activity<Over23CandidacyProcess> {

	@Override
	public void checkPreConditions(Over23CandidacyProcess process, IUserView userView) {
	    throw new PreConditionNotValidException();
	}

	@Override
	protected Over23CandidacyProcess executeActivity(Over23CandidacyProcess process, IUserView userView, Object object) {
	    // TODO Auto-generated method stub
	    return null;
	}

    }

    static public class InsertResultsFromJury extends Activity<Over23CandidacyProcess> {

	@Override
	public void checkPreConditions(Over23CandidacyProcess process, IUserView userView) {
	    throw new PreConditionNotValidException();
	}

	@Override
	protected Over23CandidacyProcess executeActivity(Over23CandidacyProcess process, IUserView userView, Object object) {
	    // TODO Auto-generated method stub
	    return null;
	}
    }

    static public class PublishCandidacyResults extends Activity<Over23CandidacyProcess> {

	@Override
	public void checkPreConditions(Over23CandidacyProcess process, IUserView userView) {
	    throw new PreConditionNotValidException();
	}

	@Override
	protected Over23CandidacyProcess executeActivity(Over23CandidacyProcess process, IUserView userView, Object object) {
	    // TODO Auto-generated method stub
	    return null;
	}
    }

    static public class ModifyCandidacyState extends Activity<Over23CandidacyProcess> {

	@Override
	public void checkPreConditions(Over23CandidacyProcess process, IUserView userView) {
	    throw new PreConditionNotValidException();
	}

	@Override
	protected Over23CandidacyProcess executeActivity(Over23CandidacyProcess process, IUserView userView, Object object) {
	    // TODO Auto-generated method stub
	    return null;
	}

    }

    static public class CreateRegistrations extends Activity<Over23CandidacyProcess> {

	@Override
	public void checkPreConditions(Over23CandidacyProcess process, IUserView userView) {
	    throw new PreConditionNotValidException();
	}

	@Override
	protected Over23CandidacyProcess executeActivity(Over23CandidacyProcess process, IUserView userView, Object object) {
	    // TODO Auto-generated method stub
	    return null;
	}
    }

}
