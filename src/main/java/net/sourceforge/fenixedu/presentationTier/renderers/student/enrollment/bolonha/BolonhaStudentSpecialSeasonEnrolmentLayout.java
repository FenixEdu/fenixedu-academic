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
package net.sourceforge.fenixedu.presentationTier.renderers.student.enrollment.bolonha;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import pt.ist.fenixWebFramework.renderers.components.HtmlBlockContainer;

public class BolonhaStudentSpecialSeasonEnrolmentLayout extends BolonhaStudentEnrolmentLayout {

    @Override
    protected void generateCycleCourseGroupsToEnrol(final HtmlBlockContainer container,
            final ExecutionSemester executionSemester, final StudentCurricularPlan studentCurricularPlan, int depth) {

        if (studentCurricularPlan.hasConcludedAnyInternalCycle()
                && studentCurricularPlan.getDegreeType().hasExactlyOneCycleType()) {
            return;
        }

        if (canPerformStudentEnrolments) {
            for (final CycleType cycleType : getAllCycleTypesToEnrolPreviousToFirstExistingCycle(studentCurricularPlan)) {
                generateCourseGroupToEnroll(container,
                        buildDegreeModuleToEnrolForCycle(studentCurricularPlan, cycleType, executionSemester), depth
                                + getRenderer().getWidthDecreasePerLevel());

            }
        }
    }

}
