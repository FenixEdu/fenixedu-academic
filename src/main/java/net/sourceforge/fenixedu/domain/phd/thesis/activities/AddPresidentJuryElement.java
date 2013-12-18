/**
 * 
 */
package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import org.fenixedu.bennu.core.domain.User;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisJuryElementBean;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;

public class AddPresidentJuryElement extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {

        if (process.isJuryValidated()) {
            throw new PreConditionNotValidException();
        }

        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {

        process.checkJuryPresidentNotGuider((PhdThesisJuryElementBean) object);

        if (process.hasPresidentJuryElement()) {
            process.getPresidentJuryElement().delete();
        }

        ThesisJuryElement.createPresident(process, (PhdThesisJuryElementBean) object);
        return process;
    }
}