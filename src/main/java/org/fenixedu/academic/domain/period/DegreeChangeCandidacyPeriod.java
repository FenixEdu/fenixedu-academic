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

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.candidacyProcess.degreeChange.DegreeChangeCandidacyProcess;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.joda.time.DateTime;

public class DegreeChangeCandidacyPeriod extends DegreeChangeCandidacyPeriod_Base {

    private DegreeChangeCandidacyPeriod() {
        super();
    }

    public DegreeChangeCandidacyPeriod(final DegreeChangeCandidacyProcess candidacyProcess, final ExecutionYear executionYear,
            final DateTime start, final DateTime end) {
        this();
        checkParameters(candidacyProcess);
        super.init(executionYear, start, end);
        addCandidacyProcesses(candidacyProcess);
    }

    private void checkParameters(final DegreeChangeCandidacyProcess candidacyProcess) {
        if (candidacyProcess == null) {
            throw new DomainException("error.DegreeChangeCandidacyPeriod.invalid.candidacy.process");
        }
    }

    public DegreeChangeCandidacyProcess getDegreeChangeCandidacyProcess() {
        return (DegreeChangeCandidacyProcess) (!getCandidacyProcessesSet().isEmpty() ? getCandidacyProcessesSet().iterator()
                .next() : null);
    }

    @Override
    public ExecutionYear getExecutionInterval() {
        return (ExecutionYear) super.getExecutionInterval();
    }
}
