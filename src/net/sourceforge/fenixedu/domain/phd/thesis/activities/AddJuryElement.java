package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisJuryElementBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;

public class AddJuryElement extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {
	if (process.isJuryValidated()) {
	    throw new PreConditionNotValidException();
	}

	if (!process.isAllowedToManageProcess(userView)) {
	    throw new PreConditionNotValidException();
	}
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
	process.checkJuryReporterNotGuider((PhdThesisJuryElementBean) object);

	ThesisJuryElement.create(process, (PhdThesisJuryElementBean) object);
	return process;
    }
}