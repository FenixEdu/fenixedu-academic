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
package net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal;

import java.util.Collection;
import java.util.HashSet;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;

public class InternalSubstitutionDismissalBean extends DismissalBean {

    static private final long serialVersionUID = 1L;

    @Override
    public Collection<? extends CurricularCourse> getAllCurricularCoursesToDismissal() {

        final Collection<CurricularCourse> result = new HashSet<CurricularCourse>();

        final StudentCurricularPlan scp = getStudentCurricularPlan();
        final Collection<CycleType> cyclesToEnrol = scp.getDegreeType().getSupportedCyclesToEnrol();

        if (cyclesToEnrol.isEmpty()) {

            for (final CurricularCourse curricularCourse : scp.getDegreeCurricularPlan().getCurricularCoursesSet()) {
                if (curricularCourse.isActive(getExecutionPeriod()) && !isApproved(scp, curricularCourse)) {
                    result.add(curricularCourse);
                }
            }

        } else {

            for (final CycleType cycleType : cyclesToEnrol) {
                final CourseGroup courseGroup = getCourseGroupWithCycleTypeToCollectCurricularCourses(scp, cycleType);
                if (courseGroup != null) {
                    for (final CurricularCourse curricularCourse : courseGroup.getAllCurricularCourses(getExecutionPeriod())) {
                        if (!isApproved(scp, curricularCourse)) {
                            result.add(curricularCourse);
                        }
                    }
                }
            }

        }

        return result;
    }

    /**
     * Do not use isApproved from StudentCurricularPlan, because that method
     * also check global equivalences, and in internal substitution we can not
     * check that.
     */
    private boolean isApproved(final StudentCurricularPlan studentCurricularPlan, final CurricularCourse curricularCourse) {
        for (final Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
            if (enrolment.getCurricularCourse().isEquivalent(curricularCourse) && enrolment.isApproved()) {
                return true;
            }
        }
        return false;
    }

    private CourseGroup getCourseGroupWithCycleTypeToCollectCurricularCourses(final StudentCurricularPlan studentCurricularPlan,
            final CycleType cycleType) {

        final CycleCurriculumGroup curriculumGroup = studentCurricularPlan.getCycle(cycleType);
        return curriculumGroup != null ? curriculumGroup.getDegreeModule() : studentCurricularPlan.getDegreeCurricularPlan()
                .getCycleCourseGroup(cycleType);

    }

}
