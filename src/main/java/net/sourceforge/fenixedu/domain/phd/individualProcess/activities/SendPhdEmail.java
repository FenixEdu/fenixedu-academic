package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import org.fenixedu.bennu.core.domain.User;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.email.PhdIndividualProgramProcessEmail;
import net.sourceforge.fenixedu.domain.phd.email.PhdIndividualProgramProcessEmailBean;

public class SendPhdEmail extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess process, User userView) {
        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, User userView, Object object) {

        final PhdIndividualProgramProcessEmailBean bean = (PhdIndividualProgramProcessEmailBean) object;
        bean.setProcess(process);
        PhdIndividualProgramProcessEmail.createEmail(bean);

        return process;
    }

}