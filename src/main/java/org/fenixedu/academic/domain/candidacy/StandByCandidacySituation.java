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
package net.sourceforge.fenixedu.domain.candidacy;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class StandByCandidacySituation extends StandByCandidacySituation_Base {

    public StandByCandidacySituation(Candidacy candidacy) {
        this(candidacy, AccessControl.getPerson());
    }

    public StandByCandidacySituation(Candidacy candidacy, Person person) {
        super();
        init(candidacy, person);
    }

    @Override
    public boolean canChangePersonalData() {
        return true;
    }

    @Override
    public CandidacySituationType getCandidacySituationType() {
        return CandidacySituationType.STAND_BY;
    }

    @Override
    public boolean getCanCandidacyDataBeValidated() {
        return true;
    }

    @Override
    public boolean canExecuteOperationAutomatically() {
        return false;
    }
}