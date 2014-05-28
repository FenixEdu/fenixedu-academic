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
/*
 * Created on 23/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Jo�o Mota
 * 
 *         23/Jul/2003 fenix-head ServidorAplicacao.Servico.scientificCouncil
 * 
 */
public class SetBasicCurricularCoursesService {

    @Atomic
    public static Boolean run(List<String> curricularCoursesIds, String degreeCurricularPlanId) throws FenixServiceException {
        check(RolePredicates.SCIENTIFIC_COUNCIL_PREDICATE);

        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanId);

        List<CurricularCourse> basicCurricularCourses = degreeCurricularPlan.getCurricularCoursesByBasicAttribute(Boolean.TRUE);

        Iterator itBCCourses = basicCurricularCourses.iterator();
        CurricularCourse basicCourse;

        while (itBCCourses.hasNext()) {

            basicCourse = (CurricularCourse) itBCCourses.next();
            basicCourse.setBasic(new Boolean(false));
        }

        Iterator<String> itId = curricularCoursesIds.iterator();

        while (itId.hasNext()) {

            CurricularCourse curricularCourseBasic = (CurricularCourse) FenixFramework.getDomainObject(itId.next());
            curricularCourseBasic.setBasic(new Boolean(true));

        }

        return true;
    }

}