package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;

public class ScheduleThesisMeeting extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {

	if (!process.hasState(PhdThesisProcessStateType.WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING)) {
	    throw new PreConditionNotValidException();
	}

	if (PhdThesisProcess.isMasterDegreeAdministrativeOfficeEmployee(userView)) {
	    return;
	}

	if (!process.isPresidentJuryElement(userView.getPerson())) {
	    throw new PreConditionNotValidException();
	}
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {

	final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;

	if (bean.isToNotify()) {
	    /*
	     * 
	     * 
	     * TODO notify
	     */
	}

	/*
	 * DateTime, place
	 * 
	 * - give access to external users (similar to request jury reviews)
	 */

	process.createState(PhdThesisProcessStateType.THESIS_DISCUSSION_DATE_SCHECULED, userView.getPerson(), bean.getRemarks());

	return process;
    }

}
