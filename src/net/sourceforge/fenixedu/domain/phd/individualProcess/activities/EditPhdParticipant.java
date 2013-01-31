package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipantBean;

public class EditPhdParticipant extends PhdIndividualProgramProcessActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
		if (!process.isAllowedToManageProcess(userView)) {
			throw new PreConditionNotValidException();
		}
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView, Object object) {
		PhdParticipantBean bean = (PhdParticipantBean) object;
		bean.getParticipant().edit(bean);

		return process;
	}

}