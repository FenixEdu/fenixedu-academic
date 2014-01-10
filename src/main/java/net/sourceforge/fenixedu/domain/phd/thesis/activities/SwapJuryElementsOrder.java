/**
 * 
 */
package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;

import org.fenixedu.bennu.core.domain.User;

import pt.utl.ist.fenix.tools.util.Pair;

public class SwapJuryElementsOrder extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {

        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {
        final Pair<ThesisJuryElement, ThesisJuryElement> elements = (Pair<ThesisJuryElement, ThesisJuryElement>) object;
        process.swapJuryElementsOrder(elements.getKey(), elements.getValue());
        return process;
    }

}