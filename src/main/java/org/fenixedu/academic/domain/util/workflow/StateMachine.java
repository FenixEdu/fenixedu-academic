/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.util.workflow;

import org.fenixedu.academic.domain.exceptions.DomainException;

public class StateMachine {

    public static IState execute(IState state) {
        state.checkConditionsToForward();
        return state.nextState();
    }

    public static IState execute(final IState state, final StateBean bean) {
        final String nextState = bean.getNextState();
        if (state.getValidNextStates().contains(nextState)) {
            state.checkConditionsToForward(bean);
            return state.nextState(bean);
        } else {
            throw new DomainException("error.invalid.next.state");
        }
    }

}
