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

import java.util.Collections;
import java.util.Set;

import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.candidacy.Candidacy;
import org.fenixedu.academic.domain.candidacy.CandidacyOperationType;
import org.fenixedu.academic.domain.candidacy.StudentCandidacy;
import org.fenixedu.academic.domain.person.RoleType;

public class PrintAllDocumentsOperation extends CandidacyOperation {

    static private final long serialVersionUID = 1L;

    public PrintAllDocumentsOperation(Set<RoleType> roleTypes, Candidacy candidacy) {
        super(roleTypes, candidacy);
    }

    public PrintAllDocumentsOperation(final RoleType roleType, final Candidacy candidacy) {
        this(Collections.singleton(roleType), candidacy);
    }

    @Override
    protected void internalExecute() {
        // TODO Auto-generated method stub
    }

    @Override
    public CandidacyOperationType getType() {
        return CandidacyOperationType.PRINT_ALL_DOCUMENTS;
    }

    @Override
    public boolean isInput() {
        return false;
    }

    @Override
    public StudentCandidacy getCandidacy() {
        return (StudentCandidacy) super.getCandidacy();
    }

    @Override
    public boolean isVisible() {
        return getCandidacy().getExecutionYear().equals(ExecutionYear.readCurrentExecutionYear());
    }

    @Override
    public boolean isAuthorized(Person person) {
        return super.isAuthorized(person) && person == getCandidacy().getPerson();
    }
}