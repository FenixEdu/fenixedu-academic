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
package org.fenixedu.academic.service.services.administrativeOffice.candidacy;

import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.candidacy.Candidacy;
import org.fenixedu.academic.domain.candidacy.DFACandidacy;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.candidacy.PHDProgramCandidacy;
import org.joda.time.YearMonthDay;

public class CandidacyFactory {

    public static Candidacy newCandidacy(DegreeType degreeType, Person person, ExecutionDegree executionDegree,
            YearMonthDay startDate) throws DomainException {

        switch (degreeType) {
        case BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA:
            // TODO: remove this after PHD Program candidacy is completed and
            // data migrated
            return new PHDProgramCandidacy(person, executionDegree);
        case BOLONHA_ADVANCED_FORMATION_DIPLOMA:
            return new DFACandidacy(person, executionDegree, startDate);

        default:
            throw new DomainException("error.candidacyFactory.invalid.degree.type");
        }
    }
}
