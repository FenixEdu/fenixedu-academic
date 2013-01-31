package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.person.PersonBean;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;

public class EditPersonalInformation extends PhdIndividualProgramProcessActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess arg0, IUserView arg1) {
		// no precondition to check
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView, Object object) {

		final Person person = process.getPerson();
		if (process.isAllowedToManageProcess(userView)) {
			person.edit((PersonBean) object);
		} else if (!person.hasAnyPersonRoles() && !person.hasUser() && !person.hasStudent()
				&& process.getCandidacyProcess().isPublicCandidacy()) {
			// assuming public candidacy
			person.editPersonWithExternalData((PersonBean) object, true);
		}
		return process;
	}
}
