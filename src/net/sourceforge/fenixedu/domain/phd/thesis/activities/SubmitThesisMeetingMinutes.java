package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;

public class SubmitThesisMeetingMinutes extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {

	if (!PhdThesisProcess.isMasterDegreeAdministrativeOfficeEmployee(userView)) {
	    throw new PreConditionNotValidException();
	}

	if (!process.hasState(PhdThesisProcessStateType.WAITING_FOR_THESIS_DISCUSSION_DATE_SCHEDULING)) {
	    throw new PreConditionNotValidException();
	}

	if (process.hasState(PhdThesisProcessStateType.THESIS_DISCUSSION_DATE_SCHECULED)) {
	    throw new PreConditionNotValidException();
	}

    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;

	for (final PhdProgramDocumentUploadBean each : bean.getDocuments()) {
	    if (each.hasAnyInformation()) {
		process.addDocument(each, userView.getPerson());
	    }
	}

	if (bean.isToNotify()) {
	    /*
	     * TODO: (check subject and body) AlertService.alertStudent(process,
	     * subject, body);
	     */
	}

	return process;
    }

}
