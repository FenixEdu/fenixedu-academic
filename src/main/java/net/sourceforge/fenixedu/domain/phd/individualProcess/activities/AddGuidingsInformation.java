package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import java.util.List;

import org.fenixedu.bennu.core.domain.User;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdParticipant;
import net.sourceforge.fenixedu.domain.phd.PhdParticipantBean;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdGuiderAcceptanceLetter;

public class AddGuidingsInformation extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess arg0, User arg1) {
        // no precondition to check
    }

    @SuppressWarnings("unchecked")
    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, User userView, Object object) {
        for (final PhdParticipantBean bean : (List<PhdParticipantBean>) object) {
            PhdParticipant guiding = process.addGuiding(bean);
            if (bean.getGuidingAcceptanceLetter() != null && bean.getGuidingAcceptanceLetter().getFileContent() != null) {
                PhdProgramDocumentUploadBean acceptanceLetter = bean.getGuidingAcceptanceLetter();
                new PhdGuiderAcceptanceLetter(guiding, acceptanceLetter.getType(), "", bean.getGuidingAcceptanceLetter()
                        .getFileContent(), bean.getGuidingAcceptanceLetter().getFilename(), userView.getPerson());
            }
        }
        return process;
    }
}