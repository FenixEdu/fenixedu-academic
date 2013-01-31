package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessState;

public class AddState extends PhdThesisActivity {

	@Override
	protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {
		if (!process.isAllowedToManageProcess(userView)) {
			throw new PreConditionNotValidException();
		}
	}

	@Override
	protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
		PhdThesisProcessBean bean = (PhdThesisProcessBean) object;

		PhdThesisProcessState.createWithGivenStateDate(process, bean.getProcessState(), userView.getPerson(), bean.getRemarks(),
				bean.getStateDate().toDateTimeAtStartOfDay());

		return process;
	}

}
