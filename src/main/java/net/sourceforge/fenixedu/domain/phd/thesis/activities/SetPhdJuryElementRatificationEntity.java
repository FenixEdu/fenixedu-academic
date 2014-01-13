package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdJuryElementsRatificationEntity;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;

import org.fenixedu.bennu.core.domain.User;

public class SetPhdJuryElementRatificationEntity extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {
        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {
        PhdThesisProcessBean bean = (PhdThesisProcessBean) object;
        process.setPhdJuryElementsRatificationEntity(bean.getPhdJuryElementsRatificationEntity());

        if (PhdJuryElementsRatificationEntity.CUSTOM.equals(bean.getPhdJuryElementsRatificationEntity())) {
            process.setRatificationEntityCustomMessage(bean.getRatificationEntityCustomMessage());
        }

        process.setPresidentTitle(bean.getPresidentTitle());

        return process;

    }

}
