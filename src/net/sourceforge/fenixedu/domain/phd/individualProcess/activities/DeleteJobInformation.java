package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.Job;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;

public class DeleteJobInformation extends PhdIndividualProgramProcessActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
		if (!process.isAllowedToManageProcess(userView)) {
			throw new PreConditionNotValidException();
		}
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView, Object object) {
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

		return job.getCreator() == person
				|| new AcademicAuthorizationGroup(AcademicOperationType.MANAGE_PHD_PROCESS_STATE).isMember(job.getCreator());
	}
}
