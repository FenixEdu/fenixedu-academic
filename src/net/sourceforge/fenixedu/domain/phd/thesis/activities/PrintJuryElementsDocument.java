/**
 * 
 */
package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;

public class PrintJuryElementsDocument extends PhdThesisActivity {

	@Override
	protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {
		if (!process.isJuryValidated()) {
			throw new PreConditionNotValidException();
		}
	}

	@Override
	protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
		// nothing to be done
		return process;
	}

}