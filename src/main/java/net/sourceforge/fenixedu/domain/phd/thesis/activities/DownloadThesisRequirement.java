/**
 * 
 */
package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;

import org.fenixedu.bennu.core.domain.User;

public class DownloadThesisRequirement extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {

        if (process.hasThesisRequirementDocument() && PhdThesisProcess.isParticipant(process, userView)) {
            return;
        }

        throw new PreConditionNotValidException();

    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {
        // nothing to be done
        return null;
    }

}