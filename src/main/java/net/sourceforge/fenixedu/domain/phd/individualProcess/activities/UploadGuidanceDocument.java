package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.guidance.PhdGuidanceDocument;

import org.fenixedu.bennu.core.domain.User;

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