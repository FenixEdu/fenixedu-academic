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

import java.util.Set;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.candidacy.Candidacy;
import org.fenixedu.academic.domain.candidacy.CandidacyOperationType;
import org.fenixedu.academic.domain.person.RoleType;

public class PrintRegistrationDeclarationOperation extends CandidacyOperation {

    public PrintRegistrationDeclarationOperation(Set<RoleType> roleTypes, Candidacy candidacy) {
        super(roleTypes, candidacy);
    }

    @Override
    protected void internalExecute() {
        // nothing to be done

    }

    @Override
    public CandidacyOperationType getType() {
        return CandidacyOperationType.PRINT_REGISTRATION_DECLARATION;
    }

    @Override
    public boolean isInput() {
        return false;
    }

    @Override
    public boolean isVisible() {
        return false;
    }

    @Override
    public boolean isAuthorized(Person person) {
        return super.isAuthorized(person) && person == getCandidacy().getPerson();
    }
}