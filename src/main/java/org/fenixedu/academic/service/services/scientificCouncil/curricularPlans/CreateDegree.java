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
package org.fenixedu.academic.service.services.scientificCouncil.curricularPlans;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.List;

import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.GradeScale;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;
import org.fenixedu.academic.util.LocaleUtils;
import org.fenixedu.bennu.core.signals.DomainObjectEvent;
import org.fenixedu.bennu.core.signals.Signal;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixframework.Atomic;

public class CreateDegree {

    @Atomic
    public static void run(LocalizedString name, String acronym, LocalizedString associatedInstitutions, DegreeType degreeType, Double ectsCredits,
            GradeScale gradeScale, String prevailingScientificArea, AdministrativeOffice administrativeOffice)
            throws FenixServiceException {
        check(RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE);

        if (name == null || name.isEmpty() || acronym == null || degreeType == null || ectsCredits == null) {
            throw new InvalidArgumentsServiceException();
        }

        final List<Degree> degrees = Degree.readNotEmptyDegrees();

        for (Degree degree : degrees) {
            if (degree.getSigla().equalsIgnoreCase(acronym)) {
                throw new FenixServiceException("error.existing.degree.acronym");
            }
            ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

            if (LocaleUtils.equalInAnyLanguage(degree.getNameFor(currentExecutionYear), name) && degree.getDegreeType().equals(degreeType)) {
                throw new FenixServiceException("error.existing.degree.name.and.type");
            }
        }

        Degree degree =
                new Degree(name, acronym, associatedInstitutions, degreeType, ectsCredits, gradeScale, prevailingScientificArea, administrativeOffice);
        Signal.emit(Degree.CREATED_SIGNAL, new DomainObjectEvent<Degree>(degree));
    }

}
