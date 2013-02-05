/**
 * 
 */
package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;
import pt.utl.ist.fenix.tools.util.Pair;

public class MoveJuryElementOrder extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, IUserView userView) {

        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, IUserView userView, Object object) {

        final Pair<ThesisJuryElement, Integer> elementInfo = (Pair<ThesisJuryElement, Integer>) object;
        final List<ThesisJuryElement> elements = new ArrayList<ThesisJuryElement>(process.getOrderedThesisJuryElements());

        int order = elementInfo.getValue().intValue();
        if (order >= 0 || order < elements.size()) {
            elements.remove(elementInfo.getKey());
            elements.add(order, elementInfo.getKey());

            for (int i = 0; i < elements.size(); i++) {
                elements.get(i).setElementOrder(i + 1);
            }
        }

        return process;
    }

}