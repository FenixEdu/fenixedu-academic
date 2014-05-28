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
package net.sourceforge.fenixedu.domain.studentCurriculum;

import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.BranchCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.degreeStructure.RootCourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class CurriculumGroupFactory {

    static public RootCurriculumGroup createRoot(final StudentCurricularPlan studentCurricularPlan,
            final RootCourseGroup rootCourseGroup, final CycleType cycleType) {

        return createRoot(studentCurricularPlan, rootCourseGroup, null, cycleType);
    }

    static public RootCurriculumGroup createRoot(StudentCurricularPlan studentCurricularPlan, RootCourseGroup rootCourseGroup,
            ExecutionSemester executionSemester, CycleType cycleType) {

        return (executionSemester != null) ?

        new RootCurriculumGroup(studentCurricularPlan, rootCourseGroup, executionSemester, cycleType) :

        new RootCurriculumGroup(studentCurricularPlan, rootCourseGroup, cycleType);

    }

    static public CurriculumGroup createGroup(final CurriculumGroup parentGroup, final CourseGroup courseGroup) {
        return createGroup(parentGroup, courseGroup, null);
    }

    static public CurriculumGroup createGroup(final CurriculumGroup parentGroup, final CourseGroup courseGroup,
            final ExecutionSemester executionSemester) {

        if (courseGroup.isCycleCourseGroup()) {
            final CycleCourseGroup cycleCourseGroup = (CycleCourseGroup) courseGroup;

            if (isExternalCycle(parentGroup, cycleCourseGroup)) {
                return (executionSemester != null) ?

                new ExternalCurriculumGroup((RootCurriculumGroup) parentGroup, cycleCourseGroup, executionSemester) :

                new ExternalCurriculumGroup((RootCurriculumGroup) parentGroup, cycleCourseGroup);

            }

            return (executionSemester != null) ?

            new CycleCurriculumGroup((RootCurriculumGroup) parentGroup, (CycleCourseGroup) courseGroup, executionSemester) :

            new CycleCurriculumGroup((RootCurriculumGroup) parentGroup, (CycleCourseGroup) courseGroup);

        } else if (courseGroup.isBranchCourseGroup()) {

            return (executionSemester != null) ?

            new BranchCurriculumGroup(parentGroup, (BranchCourseGroup) courseGroup, executionSemester) :

            new BranchCurriculumGroup(parentGroup, (BranchCourseGroup) courseGroup);

        } else if (courseGroup.isRoot()) {

            throw new DomainException("error.CurriculumGroupFactory.use.create.root.method");

        } else {

            return (executionSemester != null) ?

            new CurriculumGroup(parentGroup, courseGroup, executionSemester) :

            new CurriculumGroup(parentGroup, courseGroup);

        }
    }

    static private boolean isExternalCycle(final CurriculumGroup parentGroup, final CycleCourseGroup courseGroup) {
        return !parentGroup.getRootCurriculumGroup().getDegreeModule().hasDegreeModule(courseGroup);
    }

}
