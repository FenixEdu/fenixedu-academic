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
import net.sourceforge.fenixedu.domain.candidacyProcess.degreeTransfer.DegreeTransferCandidacyProcess;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class DegreeTransferCandidacyPeriod extends DegreeTransferCandidacyPeriod_Base {

    private DegreeTransferCandidacyPeriod() {
        super();
    }

    public DegreeTransferCandidacyPeriod(final DegreeTransferCandidacyProcess process, final ExecutionYear executionYear,
            final DateTime start, final DateTime end) {
        this();
        checkParameters(process);
        checkIfCanCreate(executionYear);
        super.init(executionYear, start, end);
        addCandidacyProcesses(process);
    }

    private void checkParameters(final DegreeTransferCandidacyProcess candidacyProcess) {
        if (candidacyProcess == null) {
            throw new DomainException("error.DegreeTransferCandidacyPeriod.invalid.candidacy.process");
        }
    }

    private void checkIfCanCreate(final ExecutionInterval executionInterval) {
        if (executionInterval.hasDegreeTransferCandidacyPeriod()) {
            throw new DomainException(
                    "error.DegreeTransferCandidacyPeriod.executionInterval.already.contains.candidacyPeriod.type",
                    executionInterval.getName());
        }
    }

    @Override
    public ExecutionYear getExecutionInterval() {
        return (ExecutionYear) super.getExecutionInterval();
    }

    public DegreeTransferCandidacyProcess getDegreeTransferCandidacyProcess() {
        return (DegreeTransferCandidacyProcess) (hasAnyCandidacyProcesses() ? getCandidacyProcesses().iterator().next() : null);
    }
}
