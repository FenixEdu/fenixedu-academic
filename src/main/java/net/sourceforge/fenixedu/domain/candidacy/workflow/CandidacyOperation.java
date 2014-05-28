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
package net.sourceforge.fenixedu.domain.candidacy.workflow;

import java.util.Comparator;
import java.util.Set;

import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyOperationType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.util.workflow.IStateWithOperations;
import net.sourceforge.fenixedu.domain.util.workflow.Operation;

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