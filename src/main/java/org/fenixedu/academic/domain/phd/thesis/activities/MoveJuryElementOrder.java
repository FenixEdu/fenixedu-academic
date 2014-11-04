/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * 
 */
package net.sourceforge.fenixedu.domain.phd.thesis.activities;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.caseHandling.PreConditionNotValidException;
import net.sourceforge.fenixedu.domain.phd.thesis.PhdThesisProcess;
import net.sourceforge.fenixedu.domain.phd.thesis.ThesisJuryElement;

import org.fenixedu.bennu.core.domain.User;

import pt.utl.ist.fenix.tools.util.Pair;

public class MoveJuryElementOrder extends PhdThesisActivity {

    @Override
    protected void activityPreConditions(PhdThesisProcess process, User userView) {

        if (!process.isAllowedToManageProcess(userView)) {
            throw new PreConditionNotValidException();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected PhdThesisProcess executeActivity(PhdThesisProcess process, User userView, Object object) {

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