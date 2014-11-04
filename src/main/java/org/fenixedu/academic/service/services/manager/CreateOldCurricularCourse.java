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

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.GradeScale;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseType;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class CreateOldCurricularCourse {

    @Atomic
    public static void run(final String dcpId, final String cgId, final String name, final String nameEn, final String code,
            final String acronym, final Integer minimumValueForAcumulatedEnrollments,
            final Integer maximumValueForAcumulatedEnrollments, final Double weigth, final Integer enrolmentWeigth,
            final Double credits, final Double ectsCredits, final Integer year, final Integer semester,
            final String beginExecutionPeriodId, final String endExecutionPeriodId, final GradeScale gradeScale)
            throws FenixServiceException {

        final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(dcpId);
        if (degreeCurricularPlan == null) {
            throw new FenixServiceException("error.createOldCurricularCourse.no.degreeCurricularPlan");
        }

        final CourseGroup courseGroup = (CourseGroup) FenixFramework.getDomainObject(cgId);
        if (courseGroup == null) {
            throw new FenixServiceException("error.createOldCurricularCourse.no.courseGroup");
        }

        final CurricularCourse curricularCourse =
                degreeCurricularPlan.createCurricularCourse(name, code, acronym, Boolean.TRUE, CurricularStage.APPROVED);
        // hack to use dcp method
        curricularCourse.setDegreeCurricularPlan(null);

        curricularCourse.setNameEn(nameEn);
        curricularCourse.setWeigth(weigth);
        curricularCourse.setEnrollmentWeigth(enrolmentWeigth);
        curricularCourse.setMinimumValueForAcumulatedEnrollments(minimumValueForAcumulatedEnrollments);
        curricularCourse.setMaximumValueForAcumulatedEnrollments(maximumValueForAcumulatedEnrollments);
        curricularCourse.setCredits(credits);
        curricularCourse.setEctsCredits(ectsCredits);
        curricularCourse.setType(CurricularCourseType.NORMAL_COURSE);
        curricularCourse.setGradeScale(gradeScale);

        final CurricularPeriod curricularPeriod = getCurricularPeriod(degreeCurricularPlan, year, semester);
        final ExecutionSemester beginExecutionPeriod = FenixFramework.getDomainObject(beginExecutionPeriodId);
        final ExecutionSemester endExecutionPeriod = FenixFramework.getDomainObject(endExecutionPeriodId);

        courseGroup.addContext(curricularCourse, curricularPeriod, beginExecutionPeriod, endExecutionPeriod);
    }

    private static CurricularPeriod getCurricularPeriod(final DegreeCurricularPlan degreeCurricularPlan, final Integer year,
            final Integer semester) {
        CurricularPeriod curricularPeriod = degreeCurricularPlan.getCurricularPeriodFor(year, semester);
        if (curricularPeriod == null) {
            curricularPeriod = degreeCurricularPlan.createCurricularPeriodFor(year, semester);
        }
        return curricularPeriod;
    }
}