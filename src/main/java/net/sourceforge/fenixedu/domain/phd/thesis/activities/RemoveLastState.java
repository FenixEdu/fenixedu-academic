package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;

import org.fenixedu.bennu.core.domain.User;

public class RemoveLastState extends PhdThesisActivity {

    @Override
    protected void processPreConditions(PhdThesisProcess process, User userView) {
    }

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {
        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {
        process.removeLastState();

        return process;
    }

}
