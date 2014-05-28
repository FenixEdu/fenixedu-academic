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
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class CreateDegree {

    @Atomic
    public static void run(String name, String nameEn, String acronym, DegreeType degreeType, Double ectsCredits,
            GradeScale gradeScale, String prevailingScientificArea, AdministrativeOffice administrativeOffice)
            throws FenixServiceException {
        check(RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE);

        if (name == null || nameEn == null || acronym == null || degreeType == null || ectsCredits == null) {
            throw new InvalidArgumentsServiceException();
        }

        final List<Degree> degrees = Degree.readNotEmptyDegrees();

        for (Degree degree : degrees) {
            if (degree.getSigla().equalsIgnoreCase(acronym)) {
                throw new FenixServiceException("error.existing.degree.acronym");
            }
            ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

            if ((degree.getNameFor(currentExecutionYear).getContent(MultiLanguageString.pt).equalsIgnoreCase(name) || degree
                    .getNameFor(currentExecutionYear).getContent(MultiLanguageString.en).equalsIgnoreCase(nameEn))
                    && degree.getDegreeType().equals(degreeType)) {
                throw new FenixServiceException("error.existing.degree.name.and.type");
            }
        }

        new Degree(name, nameEn, acronym, degreeType, ectsCredits, gradeScale, prevailingScientificArea, administrativeOffice);
    }

}