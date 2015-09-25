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
import org.fenixedu.academic.domain.candidacyProcess.graduatedPerson.DegreeCandidacyForGraduatedPersonProcess;
import org.fenixedu.academic.domain.exceptions.DomainException;
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
        super.init(executionInterval, start, end);
        addCandidacyProcesses(candidacyProcess);
    }

    private void checkParameters(final DegreeCandidacyForGraduatedPersonProcess candidacyProcess) {
        if (candidacyProcess == null) {
            throw new DomainException("error.DegreeCandidacyForGraduatedPersonCandidacyPeriod.invalid.candidacy.process");
        }
    }

    public DegreeCandidacyForGraduatedPersonProcess getDegreeCandidacyForGraduatedPersonProcess() {
        return (DegreeCandidacyForGraduatedPersonProcess) (!getCandidacyProcessesSet().isEmpty() ? getCandidacyProcessesSet()
                .iterator().next() : null);
    }

    @Override
    public ExecutionYear getExecutionInterval() {
        return (ExecutionYear) super.getExecutionInterval();
    }
}
