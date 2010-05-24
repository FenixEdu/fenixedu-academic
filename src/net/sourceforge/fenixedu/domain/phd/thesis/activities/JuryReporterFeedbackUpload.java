package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdThesisReportFeedbackDocument;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;

public class JuryReporterFeedbackUpload extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {

	if (!process.isJuryValidated()) {
	    throw new PreConditionNotValidException();
	}

	if (!process.hasState(PhdThesisProcessStateType.WAITING_FOR_JURY_REPORTER_FEEDBACK)) {
	    throw new PreConditionNotValidException();
	}

	if (PhdProgramProcess.isMasterDegreeAdministrativeOfficeEmployee(userView)) {
	    return;
	}

	if (!process.isParticipant(userView.getPerson())) {
	    throw new PreConditionNotValidException();
	}

	final ThesisJuryElement element = process.getThesisJuryElement(userView.getPerson());
	if (element == null || !element.getReporter().booleanValue()) {
	    throw new PreConditionNotValidException();
	}
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;

	for (final PhdProgramDocumentUploadBean documentBean : bean.getDocuments()) {
	    if (documentBean.hasAnyInformation()) {
		new PhdThesisReportFeedbackDocument(bean.getJuryElement(), documentBean.getRemarks(), documentBean
			.getFileContent(), documentBean.getFilename(), null);
	    }
	}

	return process;
    }

}