package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdParticipantBean;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdGuiderAcceptanceLetter;

public class AddAssistantGuidingInformation extends PhdIndividualProgramProcessActivity {

	@Override
	protected void activityPreConditions(PhdIndividualProgramProcess arg0, IUserView arg1) {
		// no precondition to check
	}

	@Override
	protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView, Object object) {
		PhdParticipantBean bean = (PhdParticipantBean) object;
		PhdParticipant addAssistantGuiding = process.addAssistantGuiding((PhdParticipantBean) object);

		if (bean.getGuidingAcceptanceLetter() != null && bean.getGuidingAcceptanceLetter().getFileContent() != null) {
			PhdProgramDocumentUploadBean acceptanceLetter = bean.getGuidingAcceptanceLetter();
			new PhdGuiderAcceptanceLetter(addAssistantGuiding, acceptanceLetter.getType(), "", bean.getGuidingAcceptanceLetter()
					.getFileContent(), bean.getGuidingAcceptanceLetter().getFilename(), userView.getPerson());
		}

		return process;
	}
}