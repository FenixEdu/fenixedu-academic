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

import java.util.Collection;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.util.workflow.Operation;
import org.fenixedu.academic.predicate.AccessControl;

public class StandByConfirmedDataCandidacySituation extends StandByConfirmedDataCandidacySituation_Base {

    public StandByConfirmedDataCandidacySituation(Candidacy candidacy) {
        this(candidacy, AccessControl.getPerson());
    }

    public StandByConfirmedDataCandidacySituation(Candidacy candidacy, Person person) {
        super();
        init(candidacy, person);
    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
        return CandidacySituationType.STAND_BY_CONFIRMED_DATA;
    }

    @Override
    public Collection<Operation> getOperationsForPerson(Person person) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean canExecuteOperationAutomatically() {
        return false;
    }
}