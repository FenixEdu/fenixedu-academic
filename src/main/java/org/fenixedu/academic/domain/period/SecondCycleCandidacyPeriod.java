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
package org.fenixedu.academic.domain.period;

import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.candidacyProcess.secondCycle.SecondCycleCandidacyProcess;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.joda.time.DateTime;

public class SecondCycleCandidacyPeriod extends SecondCycleCandidacyPeriod_Base {

    private SecondCycleCandidacyPeriod() {
        super();
    }

    public SecondCycleCandidacyPeriod(final SecondCycleCandidacyProcess candidacyProcess, final ExecutionYear executionInterval,
            final DateTime start, final DateTime end) {
        this();
        init(candidacyProcess, executionInterval, start, end);
    }

    private void init(final SecondCycleCandidacyProcess candidacyProcess, final ExecutionInterval executionInterval,
            final DateTime start, final DateTime end) {
        checkParameters(candidacyProcess);
        super.init(executionInterval, start, end);
        addCandidacyProcesses(candidacyProcess);
    }

    private void checkParameters(final SecondCycleCandidacyProcess candidacyProcess) {
        if (candidacyProcess == null) {
            throw new DomainException("error.SecondCycleCandidacyPeriod.invalid.candidacy.process");
        }
    }

    public SecondCycleCandidacyProcess getSecondCycleCandidacyProcess() {
        return (SecondCycleCandidacyProcess) (!getCandidacyProcessesSet().isEmpty() ? getCandidacyProcessesSet().iterator()
                .next() : null);
    }

    @Override
    public ExecutionYear getExecutionInterval() {
        return (ExecutionYear) super.getExecutionInterval();
    }

    @Override
    public void edit(final DateTime start, final DateTime end) {
        checkDates(start, end);
        checkIfCandEdit(start, end);
        super.setStart(start);
        super.setEnd(end);
    }

    private void checkIfCandEdit(DateTime start, DateTime end) {
        for (final SecondCycleCandidacyPeriod secondCycleCandidacyPeriod : getExecutionInterval()
                .getSecondCycleCandidacyPeriods()) {
            if (secondCycleCandidacyPeriod != this && secondCycleCandidacyPeriod.intercept(start, end)) {
                throw new DomainException("error.SecondCycleCandidacyPeriod.interception", getExecutionInterval().getName(),
                        start.toString("dd/MM/yyyy HH:mm"), end.toString("dd/MM/yyyy HH:mm"));
            }
        }
    }

    public SecondCycleCandidacyPeriod getNextPeriod() {
        // CandidacyPeriod.readPeriod(this.getClass(),
        // this.getExecutionInterval());
        return null;
    }
}
