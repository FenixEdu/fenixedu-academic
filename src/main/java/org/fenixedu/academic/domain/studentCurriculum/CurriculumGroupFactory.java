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
package org.fenixedu.academic.domain.studentCurriculum;

import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CycleCourseGroup;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.RootCourseGroup;
import org.fenixedu.academic.domain.exceptions.DomainException;

public class CurriculumGroupFactory {

    static public RootCurriculumGroup createRoot(final StudentCurricularPlan studentCurricularPlan,
            final RootCourseGroup rootCourseGroup, final CycleType cycleType) {

        return createRoot(studentCurricularPlan, rootCourseGroup, null, cycleType);
    }

    static public RootCurriculumGroup createRoot(StudentCurricularPlan studentCurricularPlan, RootCourseGroup rootCourseGroup,
            ExecutionInterval executionInterval, CycleType cycleType) {

        return (executionInterval != null) ?

                new RootCurriculumGroup(studentCurricularPlan, rootCourseGroup, executionInterval, cycleType) :

                new RootCurriculumGroup(studentCurricularPlan, rootCourseGroup, cycleType);

    }

    static public CurriculumGroup createGroup(final CurriculumGroup parentGroup, final CourseGroup courseGroup) {
        return createGroup(parentGroup, courseGroup, null);
    }

    static public CurriculumGroup createGroup(final CurriculumGroup parentGroup, final CourseGroup courseGroup,
            final ExecutionInterval executionInterval) {

        if (courseGroup.isCycleCourseGroup()) {
            final CycleCourseGroup cycleCourseGroup = (CycleCourseGroup) courseGroup;

            if (isExternalCycle(parentGroup, cycleCourseGroup)) {
                return (executionInterval != null) ?

                        new ExternalCurriculumGroup((RootCurriculumGroup) parentGroup, cycleCourseGroup, executionInterval) :

                        new ExternalCurriculumGroup((RootCurriculumGroup) parentGroup, cycleCourseGroup);

            }

            return (executionInterval != null) ?

                    new CycleCurriculumGroup((RootCurriculumGroup) parentGroup, (CycleCourseGroup) courseGroup,
                            executionInterval) :

                    new CycleCurriculumGroup((RootCurriculumGroup) parentGroup, (CycleCourseGroup) courseGroup);

        } else if (courseGroup.isBranchCourseGroup()) {

            return (executionInterval != null) ?

                    new CurriculumGroup(parentGroup, courseGroup, executionInterval) :

                    new CurriculumGroup(parentGroup, courseGroup);

        } else if (courseGroup.isRoot()) {

            throw new DomainException("error.CurriculumGroupFactory.use.create.root.method");

        } else {

            return (executionInterval != null) ?

                    new CurriculumGroup(parentGroup, courseGroup, executionInterval) :

                    new CurriculumGroup(parentGroup, courseGroup);

        }
    }

    static private boolean isExternalCycle(final CurriculumGroup parentGroup, final CycleCourseGroup courseGroup) {
        return !parentGroup.getRootCurriculumGroup().getDegreeModule().hasDegreeModule(courseGroup);
    }

}
