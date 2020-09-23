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
package org.fenixedu.academic.domain.candidacy;

import org.fenixedu.academic.domain.EntryPhase;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.candidacy.workflow.FillPersonalDataOperation;
import org.fenixedu.academic.domain.candidacy.workflow.RegistrationOperation;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.domain.util.workflow.Operation;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DegreeCandidacy extends DegreeCandidacy_Base {

    public DegreeCandidacy(final Person person, final ExecutionDegree executionDegree) {
        super();
        init(person, executionDegree);
    }

    public DegreeCandidacy(final Person person, final ExecutionDegree executionDegree, final Person creator,
            final Double entryGrade, final String contigent, final IngressionType ingressionType, EntryPhase entryPhase) {
        this(person, executionDegree, creator, entryGrade, contigent, ingressionType, entryPhase, null);
    }

    public DegreeCandidacy(final Person person, final ExecutionDegree executionDegree, final Person creator,
            final Double entryGrade, final String contigent, final IngressionType ingressionType, EntryPhase entryPhase,
            Integer placingOption) {
        super();
        init(person, executionDegree, creator, entryGrade, contigent, ingressionType, entryPhase, placingOption);
    }

    @Override
    public String getDescription() {
        return BundleUtil.getString(Bundle.CANDIDATE, "label.degreeCandidacy") + " - "
                + getExecutionDegree().getDegreeCurricularPlan().getName() + " - "
                + getExecutionDegree().getExecutionYear().getYear();
    }

    @Override
    public Set<Operation> getOperations(CandidacySituation candidacySituation) {
        final Set<Operation> operations = new HashSet<Operation>();
        switch (candidacySituation.getCandidacySituationType()) {
        case STAND_BY:
            operations.add(new FillPersonalDataOperation(Collections.singleton(RoleType.CANDIDATE), this));
            break;
        case ADMITTED:
            operations.add(new RegistrationOperation(Collections.singleton(RoleType.CANDIDATE), this));
            break;
        case REGISTERED:
            break;
        }
        return operations;
    }

    @Override
    protected void moveToNextState(final CandidacyOperationType operationType, Person person) {
        switch (getActiveCandidacySituation().getCandidacySituationType()) {

        case STAND_BY:
            if (operationType == CandidacyOperationType.FILL_PERSONAL_DATA) {
                new AdmittedCandidacySituation(this, person);
            }
            break;

        case ADMITTED:
            if (operationType == CandidacyOperationType.REGISTRATION) {
                new RegisteredCandidacySituation(this, person);
            }
            break;

        }
    }

    @Override
    public boolean isConcluded() {
        return (getActiveCandidacySituation().getCandidacySituationType() == CandidacySituationType.REGISTERED || getActiveCandidacySituation()
                .getCandidacySituationType() == CandidacySituationType.CANCELLED);
    }

    @Override
    public String getDefaultState() {
        return null;
    }

    @Override
    public Map<String, Set<String>> getStateMapping() {
        return null;
    }

}