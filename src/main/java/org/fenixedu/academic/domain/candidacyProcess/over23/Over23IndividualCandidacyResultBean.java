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
package net.sourceforge.fenixedu.domain.candidacyProcess.over23;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyState;

public class Over23IndividualCandidacyResultBean implements Serializable {

    private Over23IndividualCandidacyProcess candidacyProcess;

    private IndividualCandidacyState state;

    private Degree acceptedDegree;

    public Over23IndividualCandidacyResultBean(final Over23IndividualCandidacyProcess candidacyProcess) {
        setCandidacyProcess(candidacyProcess);
        if (candidacyProcess.isCandidacyAccepted()) {
            setState(candidacyProcess.getCandidacyState());
            setAcceptedDegree(candidacyProcess.getAcceptedDegree());
        } else if (candidacyProcess.isCandidacyRejected()) {
            setState(candidacyProcess.getCandidacyState());
        }
    }

    public Over23IndividualCandidacyProcess getCandidacyProcess() {
        return this.candidacyProcess;
    }

    public void setCandidacyProcess(final Over23IndividualCandidacyProcess candidacyProcess) {
        this.candidacyProcess = candidacyProcess;
    }

    public IndividualCandidacyState getState() {
        return state;
    }

    public void setState(IndividualCandidacyState state) {
        this.state = state;
    }

    public Degree getAcceptedDegree() {
        return this.acceptedDegree;
    }

    public void setAcceptedDegree(final Degree acceptedDegree) {
        this.acceptedDegree = acceptedDegree;
    }

    public boolean isValid() {
        return getState() != null;
    }
}