package net.sourceforge.fenixedu.domain.phd.individualProcess.activities;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.alert.PhdCustomAlert;
import net.sourceforge.fenixedu.domain.phd.alert.PhdCustomAlertBean;

public class AddCustomAlert extends PhdIndividualProgramProcessActivity {

    @Override
    protected void activityPreConditions(PhdIndividualProgramProcess process, User userView) {
        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdIndividualProgramProcess executeActivity(PhdIndividualProgramProcess process, User userView, Object object) {

        new PhdCustomAlert((PhdCustomAlertBean) object);

        return process;
    }

}