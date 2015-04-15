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

import org.fenixedu.academic.domain.DegreeOfficialPublication;
import org.fenixedu.academic.domain.DegreeSpecializationArea;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;
import org.fenixedu.academic.util.MultiLanguageString;

import pt.ist.fenixframework.Atomic;

public class CreateDegreeSpecializationArea {

    /**
     * Must ensure "REQUIRED" slots are filled
     * 
     * @param degree
     * @param date
     * @throws FenixServiceException
     */
    @Atomic
    public static void run(DegreeOfficialPublication degreeOfficialPublication, String area, String name)
            throws FenixServiceException {
        check(RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE);

        if (degreeOfficialPublication == null || area == null || name == null) {
            throw new InvalidArgumentsServiceException();
        }

        DegreeSpecializationArea specializationArea =
                new DegreeSpecializationArea(degreeOfficialPublication, new MultiLanguageString(area));
        specializationArea.setName(new MultiLanguageString(name));
        degreeOfficialPublication.addSpecializationArea(specializationArea);

    }

}