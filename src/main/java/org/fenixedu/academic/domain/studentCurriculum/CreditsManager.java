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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.IEnrolment;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.dto.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;

public class CreditsManager {

    static public Equivalence createEquivalence(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup,
            CurriculumGroup curriculumGroup, Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments,
            Double givenCredits, Grade givenGrade, ExecutionInterval executionInterval) {

        if (courseGroup != null) {
            return new Equivalence(studentCurricularPlan, courseGroup, enrolments, buildNoEnrolCurricularCourses(dismissals),
                    givenCredits, givenGrade, executionInterval);

        } else if (curriculumGroup != null) {
            return new Equivalence(studentCurricularPlan, curriculumGroup, enrolments, givenCredits, givenGrade,
                    executionInterval);

        } else {
            return new Equivalence(studentCurricularPlan, dismissals, enrolments, givenGrade, executionInterval);
        }

    }

    static public Substitution createSubstitution(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup,
            CurriculumGroup curriculumGroup, Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments,
            Double givenCredits, ExecutionInterval executionInterval) {

        if (courseGroup != null) {
            return new Substitution(studentCurricularPlan, courseGroup, enrolments, buildNoEnrolCurricularCourses(dismissals),
                    givenCredits, executionInterval);

        } else if (curriculumGroup != null) {
            return new Substitution(studentCurricularPlan, curriculumGroup, enrolments, givenCredits, executionInterval);

        } else {
            return new Substitution(studentCurricularPlan, dismissals, enrolments, executionInterval);
        }

    }

    static public InternalSubstitution createInternalSubstitution(StudentCurricularPlan studentCurricularPlan,
            CourseGroup courseGroup, CurriculumGroup curriculumGroup, Collection<SelectedCurricularCourse> dismissals,
            Collection<IEnrolment> enrolments, Double givenCredits, ExecutionInterval executionInterval) {

        if (courseGroup != null) {
            return new InternalSubstitution(studentCurricularPlan, courseGroup, enrolments,
                    buildNoEnrolCurricularCourses(dismissals), givenCredits, executionInterval);

        } else if (curriculumGroup != null) {
            return new InternalSubstitution(studentCurricularPlan, curriculumGroup, enrolments, givenCredits, executionInterval);

        } else {
            return new InternalSubstitution(studentCurricularPlan, dismissals, enrolments, executionInterval);
        }

    }

    static private Collection<CurricularCourse> buildNoEnrolCurricularCourses(Collection<SelectedCurricularCourse> dismissals) {

        if (dismissals == null || dismissals.isEmpty()) {
            return Collections.emptyList();
        }

        final Collection<CurricularCourse> noEnrolCurricularCourse = new ArrayList<CurricularCourse>(dismissals.size());
        for (final SelectedCurricularCourse selectedCurricularCourse : dismissals) {
            noEnrolCurricularCourse.add(selectedCurricularCourse.getCurricularCourse());
        }

        return noEnrolCurricularCourse;
    }

}
