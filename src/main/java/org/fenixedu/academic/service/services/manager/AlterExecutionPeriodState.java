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
package org.fenixedu.academic.service.services.manager;

import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.util.PeriodState;

import pt.ist.fenixframework.Atomic;

public class AlterExecutionPeriodState {

    @Atomic
    public static void run(final String year, final Integer semester, final PeriodState periodState)
            throws FenixServiceException {
        final ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(year);
        final ExecutionInterval executionInterval = executionYear.getExecutionSemesterFor(semester);
        if (executionInterval == null) {
            throw new InvalidExecutionPeriod();
        }

        if (periodState.getStateCode().equals(PeriodState.CURRENT.getStateCode())) {
            // Deactivate the current
            ExecutionInterval.findAllChilds().stream().filter(ei -> ei.isCurrent()).forEach(currentExecutionPeriod -> {
                final ExecutionYear currentExecutionYear = currentExecutionPeriod.getExecutionYear();
                currentExecutionPeriod.setState(PeriodState.OPEN);
                currentExecutionYear.setState(PeriodState.OPEN);
            });

            executionInterval.setState(periodState);
            executionInterval.getExecutionYear().setState(periodState);
        } else {
            executionInterval.setState(periodState);
            PeriodState currentPeriodState = periodState;
            for (final ExecutionInterval otherExecutionPeriod : executionYear.getExecutionPeriodsSet()) {
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