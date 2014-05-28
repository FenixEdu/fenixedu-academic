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
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import net.sourceforge.fenixedu.util.PeriodState;
import pt.ist.fenixframework.Atomic;

public class AlterExecutionPeriodState {

    @Atomic
    public static void run(final String year, final Integer semester, final PeriodState periodState) throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);

        final ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(year);
        final ExecutionSemester executionSemester = executionYear.getExecutionSemesterFor(semester);
        if (executionSemester == null) {
            throw new InvalidExecutionPeriod();
        }

        if (periodState.getStateCode().equals(PeriodState.CURRENT.getStateCode())) {
            // Deactivate the current
            final ExecutionSemester currentExecutionPeriod = ExecutionSemester.readActualExecutionSemester();
            if (currentExecutionPeriod != null) {
                final ExecutionYear currentExecutionYear = currentExecutionPeriod.getExecutionYear();
                currentExecutionPeriod.setState(PeriodState.OPEN);
                currentExecutionYear.setState(PeriodState.OPEN);
            }
            executionSemester.setState(periodState);
            executionSemester.getExecutionYear().setState(periodState);
        } else {
            executionSemester.setState(periodState);
            PeriodState currentPeriodState = periodState;
            for (final ExecutionSemester otherExecutionPeriod : executionYear.getExecutionPeriodsSet()) {
                if (currentPeriodState != null
                        && !otherExecutionPeriod.getState().getStateCode().equals(currentPeriodState.getStateCode())) {
                    currentPeriodState = null;
                }
            }
            if (currentPeriodState != null) {
                executionYear.setState(currentPeriodState);
            }
        }
    }

    public static class InvalidExecutionPeriod extends FenixServiceException {
        InvalidExecutionPeriod() {
            super();
        }
    }

}