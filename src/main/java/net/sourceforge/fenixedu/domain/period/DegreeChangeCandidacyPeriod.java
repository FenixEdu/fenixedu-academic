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
package net.sourceforge.fenixedu.domain.period;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeChange.DegreeChangeCandidacyProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class DegreeChangeCandidacyPeriod extends DegreeChangeCandidacyPeriod_Base {

    private DegreeChangeCandidacyPeriod() {
        super();
    }

    public DegreeChangeCandidacyPeriod(final DegreeChangeCandidacyProcess candidacyProcess, final ExecutionYear executionYear,
            final DateTime start, final DateTime end) {
        this();
        checkParameters(candidacyProcess);
        checkIfCanCreate(executionYear);
        super.init(executionYear, start, end);
        addCandidacyProcesses(candidacyProcess);
    }

    private void checkIfCanCreate(final ExecutionInterval executionInterval) {
        if (executionInterval.hasDegreeChangeCandidacyPeriod()) {
            throw new DomainException(
                    "error.DegreeChangeCandidacyPeriod.executionInterval.already.contains.candidacyPeriod.type",
                    executionInterval.getName());
        }
    }

    private void checkParameters(final DegreeChangeCandidacyProcess candidacyProcess) {
        if (candidacyProcess == null) {
            throw new DomainException("error.DegreeChangeCandidacyPeriod.invalid.candidacy.process");
        }
    }

    public DegreeChangeCandidacyProcess getDegreeChangeCandidacyProcess() {
        return (DegreeChangeCandidacyProcess) (hasAnyCandidacyProcesses() ? getCandidacyProcesses().iterator().next() : null);
    }

    @Override
    public ExecutionYear getExecutionInterval() {
        return (ExecutionYear) super.getExecutionInterval();
    }
}
