package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdParticipantBean;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdGuiderAcceptanceLetter;

public class UploadGuidanceAcceptanceLetter extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess process, IUserView userView) {
	if (!PhdIndividualProgramProcess.isMasterDegreeAdministrativeOfficeEmployee(userView)) {
	    throw new PreConditionNotValidException();
	}
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, IUserView userView, Object object) {
	PhdParticipantBean guidingBean = (PhdParticipantBean) object;
	PhdParticipant guiding = guidingBean.getParticipant();
	PhdProgramDocumentUploadBean acceptanceLetter = guidingBean.getGuidingAcceptanceLetter();

	new PhdGuiderAcceptanceLetter(guiding, acceptanceLetter.getType(), "", acceptanceLetter.getFileContent(),
		acceptanceLetter.getFilename(), userView.getPerson());

	return process;
    }

}
