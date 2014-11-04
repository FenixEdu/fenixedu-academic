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

import java.util.List;

import org.fenixedu.academic.domain.Formation;
import org.fenixedu.academic.dto.alumni.formation.AlumniFormation;

import pt.ist.fenixframework.Atomic;

public class EditFormation extends FormationManagement {

    protected void run(final AlumniFormation formation) {
        editAlumniFormation(formation);
    }

    protected void run(final List<AlumniFormation> formationList) {

        for (AlumniFormation formation : formationList) {
            editAlumniFormation(formation);
        }
    }

    private void editAlumniFormation(final AlumniFormation formation) {

        Formation dbFormation = formation.getAssociatedFormation();
        dbFormation.edit(formation, getFormationInstitution(formation));
    }

    // Service Invokers migrated from Berserk

    private static final EditFormation serviceInstance = new EditFormation();

    @Atomic
    public static void runEditFormation(AlumniFormation formation) {
        serviceInstance.run(formation);
    }

}