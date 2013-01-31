package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdConfigurationIndividualProgramProcessBean;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;

public class ConfigurePhdIndividualProgramProcess extends PhdIndividualProgramProcessActivity {

	@Override
	protected void processPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
		// Turn off any preconditions
	}

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
		if (!process.isAllowedToManageProcess(userView)) {
			throw new PreConditionNotValidException();
		}
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView, Object object) {
		PhdConfigurationIndividualProgramProcessBean bean = (PhdConfigurationIndividualProgramProcessBean) object;

		process.getPhdConfigurationIndividualProgramProcess().configure(bean);
		return process;
	}

}