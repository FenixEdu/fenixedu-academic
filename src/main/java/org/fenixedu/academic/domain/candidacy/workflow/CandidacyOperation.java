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
package org.fenixedu.academic.domain.candidacy.workflow;

import java.util.Comparator;
import java.util.Set;

import org.fenixedu.academic.domain.candidacy.Candidacy;
import org.fenixedu.academic.domain.candidacy.CandidacyOperationType;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.util.workflow.IStateWithOperations;
import org.fenixedu.academic.domain.util.workflow.Operation;

public abstract class CandidacyOperation extends Operation {

    public static Comparator<CandidacyOperation> COMPARATOR_BY_TYPE = new Comparator<CandidacyOperation>() {
        @Override
        public int compare(CandidacyOperation leftCandidacyOperation, CandidacyOperation rightCandidacyOperation) {
            return leftCandidacyOperation.getType().compareTo(rightCandidacyOperation.getType());

        }
    };

    private Candidacy candidacy;

    protected CandidacyOperation(Set<RoleType> roleTypes, Candidacy candidacy) {
        super(roleTypes);
        setCandidacy(candidacy);
    }

    public Candidacy getCandidacy() {
        return this.candidacy;
    }

    private void setCandidacy(Candidacy candidacy) {
        this.candidacy = candidacy;
    }

    @Override
    public IStateWithOperations getState() {
        return getCandidacy().getActiveCandidacySituation();
    }

    @Override
    public int compareTo(Operation operation) {
        return ((CandidacyOperation) operation).getType().compareTo(getType());
    }

    public abstract CandidacyOperationType getType();

}