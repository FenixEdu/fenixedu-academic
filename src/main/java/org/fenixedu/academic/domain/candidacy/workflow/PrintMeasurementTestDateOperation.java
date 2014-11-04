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

import java.util.Collections;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyOperationType;
import net.sourceforge.fenixedu.domain.person.RoleType;

public class PrintMeasurementTestDateOperation extends CandidacyOperation {

    static private final long serialVersionUID = 1L;

    public PrintMeasurementTestDateOperation(Set<RoleType> roleTypes, Candidacy candidacy) {
        super(roleTypes, candidacy);
    }

    public PrintMeasurementTestDateOperation(final RoleType roleType, final Candidacy candidacy) {
        this(Collections.singleton(roleType), candidacy);
    }

    @Override
    protected void internalExecute() {
        // nothing to be done
    }

    @Override
    public CandidacyOperationType getType() {
        return CandidacyOperationType.PRINT_MEASUREMENT_TEST_DATE;
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
    public boolean isAuthorized(final Person person) {
        return super.isAuthorized(person) && person == getCandidacy().getPerson();
    }

}