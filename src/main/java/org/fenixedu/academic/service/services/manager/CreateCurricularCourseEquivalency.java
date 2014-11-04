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
package org.fenixedu.academic.service.services.manager;

import java.util.Collections;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.CurricularCourseEquivalence;
import org.fenixedu.academic.domain.DegreeCurricularPlan;

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

}