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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionInterval;
import net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess;

abstract public class CandidacyProcessCandidacyPeriod extends CandidacyProcessCandidacyPeriod_Base {

    protected CandidacyProcessCandidacyPeriod() {
        super();
    }

    public boolean hasCandidacyProcesses(final Class<? extends CandidacyProcess> clazz, final ExecutionInterval executionInterval) {
        return hasExecutionInterval(executionInterval) && containsCandidacyProcess(clazz);
    }

    public boolean hasExecutionInterval(final ExecutionInterval executionInterval) {
        return getExecutionInterval() == executionInterval;
    }

    public boolean containsCandidacyProcess(final Class<? extends CandidacyProcess> clazz) {
        for (final CandidacyProcess process : getCandidacyProcesses()) {
            if (process.getClass().equals(clazz)) {
                return true;
            }
        }
        return false;
    }

    public List<CandidacyProcess> getCandidacyProcesses(final Class<? extends CandidacyProcess> clazz) {
        final List<CandidacyProcess> result = new ArrayList<CandidacyProcess>();
        for (final CandidacyProcess process : getCandidacyProcesses()) {
            if (process.getClass().equals(clazz)) {
                result.add(process);
            }
        }
        return result;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyProcess> getCandidacyProcesses() {
        return getCandidacyProcessesSet();
    }

    @Deprecated
    public boolean hasAnyCandidacyProcesses() {
        return !getCandidacyProcessesSet().isEmpty();
    }

}
