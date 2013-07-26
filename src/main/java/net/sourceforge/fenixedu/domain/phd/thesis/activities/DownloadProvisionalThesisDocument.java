/**
 * 
 */
package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;

public class DownloadProvisionalThesisDocument extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {

        if (process.hasProvisionalThesisDocument() && PhdThesisProcess.isParticipant(process, userView)) {
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