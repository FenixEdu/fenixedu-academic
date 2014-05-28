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
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.Collections;

import net.sourceforge.fenixedu.applicationTier.Filtro.DegreeAdministrativeOfficeAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.ManagerAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourseEquivalence;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateCurricularCourseEquivalency {

    /*
     * ACCESSCONTROL
     * 
     * This method should check if the admin office should create the
     * equivalence or not
     */
    @Atomic
    public static void run(final String degreeCurricularPlanID, final String curricularCourseID,
            final String oldCurricularCourseID) {
        final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);
        final CurricularCourse curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(curricularCourseID);
        final CurricularCourse oldCurricularCourse = (CurricularCourse) FenixFramework.getDomainObject(oldCurricularCourseID);

        new CurricularCourseEquivalence(degreeCurricularPlan, curricularCourse, Collections.singleton(oldCurricularCourse));
    }

    // Service Invokers migrated from Berserk

    @Atomic
    public static void runCreateCurricularCourseEquivalency(String degreeCurricularPlanID, String curricularCourseID,
            String oldCurricularCourseID) throws NotAuthorizedException {
        try {
            DegreeAdministrativeOfficeAuthorizationFilter.instance.execute();
            run(degreeCurricularPlanID, curricularCourseID, oldCurricularCourseID);
        } catch (NotAuthorizedException ex1) {
            try {
                ManagerAuthorizationFilter.instance.execute();
                run(degreeCurricularPlanID, curricularCourseID, oldCurricularCourseID);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}