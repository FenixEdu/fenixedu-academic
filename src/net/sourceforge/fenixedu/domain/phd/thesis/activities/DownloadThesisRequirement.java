/**
 * 
 */
package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;

public class DownloadThesisRequirement extends PhdThesisActivity {

	@Override
	protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {

		if (process.hasThesisRequirementDocument() && PhdThesisProcess.isParticipant(process, userView)) {
			return;
		}

		throw new PreConditionNotValidException();

	}

	@Override
	protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {
		// nothing to be done
		return null;
	}

}