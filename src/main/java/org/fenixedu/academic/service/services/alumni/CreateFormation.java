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
package org.fenixedu.academic.service.services.alumni;

import java.util.ArrayList;

import org.fenixedu.academic.domain.Alumni;
import org.fenixedu.academic.domain.Formation;
import org.fenixedu.academic.dto.alumni.formation.AlumniFormation;

import pt.ist.fenixframework.Atomic;

public class CreateFormation extends FormationManagement {

    protected void run(final Alumni alumni, final AlumniFormation formation) {
        createAlumniFormation(alumni, formation);
    }

    protected void run(final Alumni alumni, final ArrayList<AlumniFormation> formationList) {
        for (AlumniFormation formation : formationList) {
            createAlumniFormation(alumni, formation);
        }
    }

    private void createAlumniFormation(final Alumni alumni, final AlumniFormation formation) {

        new Formation(alumni.getStudent().getPerson(), formation.getFormationType(), formation.getFormationDegree(),
                formation.getEducationArea(), formation.getFormationBeginYear(), formation.getFormationEndYear(),
                formation.getFormationCredits(), formation.getFormationHours(), getFormationInstitution(formation),
                formation.getParentInstitution(), formation.getInstitutionType(), formation.getCountryUnit());
    }

    // Service Invokers migrated from Berserk

    private static final CreateFormation serviceInstance = new CreateFormation();

    @Atomic
    public static void runCreateFormation(Alumni alumni, AlumniFormation formation) {
        serviceInstance.run(alumni, formation);
    }

}