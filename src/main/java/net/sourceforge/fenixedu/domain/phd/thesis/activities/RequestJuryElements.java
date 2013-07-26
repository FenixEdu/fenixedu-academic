/**
 * 
 */
package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.alert.AlertService;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;

public class RequestJuryElements extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {
        if (!process.getActiveState().equals(PhdThesisProcessStateType.NEW)) {
            throw new PreConditionNotValidException();
        }

        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {
        final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;
        process.createState(PhdThesisProcessStateType.WAITING_FOR_JURY_CONSTITUTION, userView.getPerson(), bean.getRemarks());

        if (bean.isToNotify()) {
            AlertService.alertCoordinators(process.getIndividualProgramProcess(),
                    "message.phd.alert.request.jury.elements.subject", "message.phd.alert.request.jury.elements.body");
        }

        return process;
    }
}