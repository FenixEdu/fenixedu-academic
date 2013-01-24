package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdJuryElementsRatificationEntity;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcessBean;

public class SetPhdJuryElementRatificationEntity extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {
	if (!process.isAllowedToManageProcess(userView)) {
	    throw new PreConditionNotValidException();
	}
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	PhdThesisProcessBean bean = (PhdThesisProcessBean) object;
	process.setPhdJuryElementsRatificationEntity(bean.getPhdJuryElementsRatificationEntity());

	if (PhdJuryElementsRatificationEntity.CUSTOM.equals(bean.getPhdJuryElementsRatificationEntity())) {
	    process.setRatificationEntityCustomMessage(bean.getRatificationEntityCustomMessage());
	}

	process.setPresidentTitle(bean.getPresidentTitle());

	return process;

    }

}
