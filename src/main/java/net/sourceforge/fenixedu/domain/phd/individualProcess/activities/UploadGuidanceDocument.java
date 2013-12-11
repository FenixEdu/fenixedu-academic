package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.guidance.PhdGuidanceDocument;

public class UploadGuidanceDocument extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess process, User userView) {
        if (process.isGuiderOrAssistentGuider(userView.getPerson())) {
            return;
        }

        if (process.isCoordinatorForPhdProgram(userView.getPerson())) {
            return;
        }

        throw new PreConditionNotValidException();
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, User userView, Object object) {
        PhdProgramDocumentUploadBean bean = (PhdProgramDocumentUploadBean) object;

        new PhdGuidanceDocument(process, bean.getType(), bean.getRemarks(), bean.getFileContent(), bean.getFilename(),
                userView.getPerson());

        return process;
    }

}