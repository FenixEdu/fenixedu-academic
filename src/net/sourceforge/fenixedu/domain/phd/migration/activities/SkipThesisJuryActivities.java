package net.sourceforge.fenixedu.domain.phd.migration.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.caseHandling.Process;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;
import net.sourceforge.fenixedu.domain.phd.thesis.activities.PhdThesisActivity;
import net.sourceforge.fenixedu.domain.phd.thesis.meeting.PhdMeetingSchedulingProcess;

import org.joda.time.LocalDate;

public class SkipThesisJuryActivities extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {
	if (process.getActiveState() != PhdThesisProcessStateType.NEW) {
	    throw new PreConditionNotValidException();
	}

	if (!process.isAllowedToManageProcess(userView)) {
	    throw new PreConditionNotValidException();
	}
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {

	final PhdThesisProcessBean thesisBean = (PhdThesisProcessBean) object;

	process.setWhenJuryDesignated(process.getIndividualProgramProcess().getCandidacyDate());
	LocalDate candidacyDate = process.getIndividualProgramProcess().getCandidacyDate();
	process.setWhenJuryValidated(candidacyDate);

	process.createState(PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK, userView.getPerson(),
		thesisBean.getRemarks());

	if (!process.hasMeetingProcess()) {
	    Process.createNewProcess(userView, PhdMeetingSchedulingProcess.class, thesisBean);
	}

	return process;
    }

}
