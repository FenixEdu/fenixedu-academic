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
package net.sourceforge.fenixedu.domain.candidacyProcess.standalone;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;

public class StandaloneIndividualCandidacyResultBean implements Serializable {

    private StandaloneIndividualCandidacyProcess candidacyProcess;
    private IndividualCandidacyState state;

    public StandaloneIndividualCandidacyResultBean(final StandaloneIndividualCandidacyProcess process) {
        setCandidacyProcess(process);
        if (process.isCandidacyAccepted() || process.isCandidacyRejected()) {
            setState(process.getCandidacyState());
        }
    }

    public StandaloneIndividualCandidacyProcess getCandidacyProcess() {
        return this.candidacyProcess;
    }

    public void setCandidacyProcess(StandaloneIndividualCandidacyProcess candidacyProcess) {
        this.candidacyProcess = candidacyProcess;
    }

    public IndividualCandidacyState getState() {
        return state;
    }

    public void setState(IndividualCandidacyState state) {
        this.state = state;
    }
}
