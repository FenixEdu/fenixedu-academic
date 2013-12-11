/**
 * 
 */
package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessStateType;

public class ValidateJury extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {

        if (process.isJuryValidated()) {
            throw new PreConditionNotValidException();
        }

        if (process.getWhenJuryDesignated() == null) {
            throw new PreConditionNotValidException();
        }

        if (!process.hasPresidentJuryElement()) {
            /*
             * if this condition is removed then must update
             * ScheduleThesisMeeting: scheduling is performed by president jury
             * element
             */
            throw new PreConditionNotValidException();
        }

        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {
        final PhdThesisProcessBean bean = (PhdThesisProcessBean) object;
        process.setWhenJuryValidated(bean.getWhenJuryValidated());

        process.createState(PhdThesisProcessStateType.JURY_VALIDATED, userView.getPerson(), "");

        if (bean.isToNotify()) {
            /*
             * TODO: SEND ALERT after create!!!!!!!!!!!!
             */
        }

        return process;
    }
}