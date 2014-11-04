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
package org.fenixedu.academic.service.services.coordinator.degreeCurricularPlanManagement;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.Collection;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.dto.InfoDegreeCurricularPlan;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author <a href="mailto:amam@mega.ist.utl.pt">Amin Amirali</a>
 * @author <a href="mailto:frnp@mega.ist.utl.pt">Francisco Paulo</a>
 * 
 */
public class ReadDegreeCurricularPlanHistoryByDegreeCurricularPlanID {
    @Atomic
    public static InfoDegreeCurricularPlan run(String degreeCurricularPlanID) throws FenixServiceException {
        check(RolePredicates.COORDINATOR_PREDICATE);

        InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;

        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);

        if (degreeCurricularPlan != null) {
            Collection<CurricularCourse> allCurricularCourses = degreeCurricularPlan.getCurricularCoursesSet();

            if (allCurricularCourses != null && !allCurricularCourses.isEmpty()) {
                infoDegreeCurricularPlan = createInfoDegreeCurricularPlan(degreeCurricularPlan, allCurricularCourses);
            }
        }

        return infoDegreeCurricularPlan;
    }

    private static InfoDegreeCurricularPlan createInfoDegreeCurricularPlan(DegreeCurricularPlan degreeCurricularPlan,
            Collection allCurricularCourses) {
        return InfoDegreeCurricularPlan.newInfoFromDomain(degreeCurricularPlan);
    }

}