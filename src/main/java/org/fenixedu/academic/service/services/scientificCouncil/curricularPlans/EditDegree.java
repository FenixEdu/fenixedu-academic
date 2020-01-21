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
import org.fenixedu.academic.domain.DegreeInfo;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.GradeScale;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;
import org.fenixedu.academic.service.services.exceptions.NonExistingServiceException;
import org.fenixedu.academic.util.LocaleUtils;
import org.fenixedu.commons.i18n.LocalizedString;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditDegree {

    @Atomic
    public static void run(String externalId, LocalizedString name, String acronym, LocalizedString associatedInstitutions,
                              DegreeType degreeType, Double ectsCredits, GradeScale gradeScale, String prevailingScientificArea,
                              ExecutionYear executionYear, String code, String ministryCode) throws FenixServiceException {

        check(RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE);
        if (externalId == null || name == null || name.isEmpty() || acronym == null || degreeType == null
                || ectsCredits == null) {
            throw new InvalidArgumentsServiceException();
        }

        final Degree degreeToEdit = FenixFramework.getDomainObject(externalId);

        if (degreeToEdit == null) {
            throw new NonExistingServiceException();
        } else if (!degreeToEdit.getSigla().equalsIgnoreCase(acronym) || !LocaleUtils.equalInAnyLanguage(
            degreeToEdit.getNameFor(executionYear), name)  || !degreeToEdit.getDegreeType().equals(degreeType)) {

            final List<Degree> degrees = Degree.readNotEmptyDegrees();

            // assert unique degree code and unique pair name/type
            for (Degree degree : degrees) {
                if (degree != degreeToEdit) {
                    if (degree.getSigla().equalsIgnoreCase(acronym)) {
                        throw new FenixServiceException("error.existing.degree.acronym");
                    }
                    if (LocaleUtils.equalInAnyLanguage(degree.getNameFor(executionYear), name) && degree.getDegreeType().equals(degreeType)) {
                        throw new FenixServiceException("error.existing.degree.name.and.type");
                    }
                }
            }
        }

        degreeToEdit.edit(name, acronym, associatedInstitutions, degreeType, ectsCredits, gradeScale, prevailingScientificArea,
            executionYear);
        degreeToEdit.setCode(code);
        degreeToEdit.setMinistryCode(ministryCode);
    }

    @Atomic
    public static void editAssociatedInstitutions(String externalId, LocalizedString associatedInstitutions, ExecutionYear executionYear) throws FenixServiceException {

        check(RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE);
        if (externalId == null || associatedInstitutions == null) {
            throw new InvalidArgumentsServiceException();
        }

        final Degree degreeToEdit = FenixFramework.getDomainObject(externalId);

        if (degreeToEdit == null) {
            throw new NonExistingServiceException();
        }

        DegreeInfo degreeInfoFor = degreeToEdit.getDegreeInfoFor(executionYear);
        if (degreeInfoFor != null) {
            degreeInfoFor.setAssociatedInstitutions(associatedInstitutions);
        }
    }

}
