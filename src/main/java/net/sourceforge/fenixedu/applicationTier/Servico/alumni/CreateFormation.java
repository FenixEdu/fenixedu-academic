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
package net.sourceforge.fenixedu.applicationTier.Servico.alumni;

import java.util.ArrayList;

import net.sourceforge.fenixedu.dataTransferObject.alumni.formation.AlumniFormation;
import net.sourceforge.fenixedu.domain.Alumni;
import net.sourceforge.fenixedu.domain.Formation;
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