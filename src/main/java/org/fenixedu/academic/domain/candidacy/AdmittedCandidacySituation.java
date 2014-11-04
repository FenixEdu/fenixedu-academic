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

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.predicate.AccessControl;

public class AdmittedCandidacySituation extends AdmittedCandidacySituation_Base {

    public AdmittedCandidacySituation(Candidacy candidacy) {
        this(candidacy, AccessControl.getPerson());
    }

    public AdmittedCandidacySituation(Candidacy candidacy, Person person) {
        super();
        init(candidacy, person);
    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
        return CandidacySituationType.ADMITTED;
    }

    @Override
    public boolean getCanRegister() {
        return true;
    }

    @Override
    public boolean canExecuteOperationAutomatically() {
        return (getCandidacy() instanceof DegreeCandidacy || getCandidacy() instanceof IMDCandidacy);
    }

}
