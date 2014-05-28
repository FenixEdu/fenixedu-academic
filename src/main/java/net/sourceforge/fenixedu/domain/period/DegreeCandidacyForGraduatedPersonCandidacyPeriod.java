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
import net.sourceforge.fenixedu.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class DegreeCandidacyForGraduatedPersonCandidacyPeriod extends DegreeCandidacyForGraduatedPersonCandidacyPeriod_Base {

    private DegreeCandidacyForGraduatedPersonCandidacyPeriod() {
        super();
    }

    public DegreeCandidacyForGraduatedPersonCandidacyPeriod(final DegreeCandidacyForGraduatedPersonProcess candidacyProcess,
            final ExecutionYear executionYear, final DateTime start, final DateTime end) {
        this();
        init(candidacyProcess, executionYear, start, end);
    }

    private void init(final DegreeCandidacyForGraduatedPersonProcess candidacyProcess, final ExecutionInterval executionInterval,
            final DateTime start, final DateTime end) {
        checkParameters(candidacyProcess);
        checkIfCanCreate(executionInterval);
        super.init(executionInterval, start, end);
        addCandidacyProcesses(candidacyProcess);
    }

    private void checkParameters(final DegreeCandidacyForGraduatedPersonProcess candidacyProcess) {
        if (candidacyProcess == null) {
            throw new DomainException("error.DegreeCandidacyForGraduatedPersonCandidacyPeriod.invalid.candidacy.process");
        }
    }

    private void checkIfCanCreate(final ExecutionInterval executionInterval) {
        if (executionInterval.hasDegreeCandidacyForGraduatedPersonCandidacyPeriod()) {
            throw new DomainException(
                    "error.DegreeCandidacyForGraduatedPersonCandidacyPeriod.executionInterval.already.contains.candidacyPeriod.type",
                    executionInterval.getName());
        }
    }

    public DegreeCandidacyForGraduatedPersonProcess getDegreeCandidacyForGraduatedPersonProcess() {
        return (DegreeCandidacyForGraduatedPersonProcess) (hasAnyCandidacyProcesses() ? getCandidacyProcesses().iterator().next() : null);
    }

    @Override
    public ExecutionYear getExecutionInterval() {
        return (ExecutionYear) super.getExecutionInterval();
    }
}
