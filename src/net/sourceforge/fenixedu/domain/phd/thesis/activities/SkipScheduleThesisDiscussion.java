package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;

public class SkipScheduleThesisDiscussion extends PhdThesisActivity {

	@Override
	protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {

		if (!process.getActiveState().equals(PhdThesisProcessStateType.WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING)) {
			throw new PreConditionNotValidException();
		}

		if (process.isAllowedToManageProcess(userView)) {
			return;
		}
	}

	@Override
	protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {

		final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;

		process.setDiscussionDate(bean.getScheduledDate());
		process.setDiscussionPlace(bean.getScheduledPlace());

		process.createState(PhdThesisProcessStateType.THESIS_DISCUSSION_DATE_SCHECULED, userView.getPerson(), bean.getRemarks());

		return process;
	}

}
