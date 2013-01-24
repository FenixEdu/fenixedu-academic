package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisJuryElementBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;

public class EditJuryElement extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {

	if (!process.isAllowedToManageProcess(userView)) {
	    throw new PreConditionNotValidException();
	}
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {

	final PhdThesisJuryElementBean bean = (PhdThesisJuryElementBean) object;
	final ThesisJuryElement juryElement = bean.getJuryElement();

	if (process.hasThesisJuryElements(juryElement)) {
	    juryElement.edit(bean);
	}

	return process;
    }
}
