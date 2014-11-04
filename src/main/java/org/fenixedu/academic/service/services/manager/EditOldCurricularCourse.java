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

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class EditOldCurricularCourse {

    @Atomic
    public static void run(final String curricularCourseId, final String name, final String nameEn, final String code,
            final String acronym, final Integer minimumValueForAcumulatedEnrollments,
            final Integer maximumValueForAcumulatedEnrollments, final Double weigth, final Integer enrolmentWeigth,
            final Double credits, final Double ectsCredits, final Double theoreticalHours, final Double labHours,
            final Double praticalHours, final Double theoPratHours, final GradeScale gradeScale) throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);

        final CurricularCourse curricularCourse = (CurricularCourse) FenixFramework.getDomainObject(curricularCourseId);
        if (curricularCourse == null) {
            throw new FenixServiceException("error.createOldCurricularCourse.no.courseGroup");
        }

        curricularCourse.edit(name, nameEn, code, acronym, weigth, credits, ectsCredits, enrolmentWeigth,
                minimumValueForAcumulatedEnrollments, maximumValueForAcumulatedEnrollments, theoreticalHours, labHours,
                praticalHours, theoPratHours, gradeScale);
    }
}