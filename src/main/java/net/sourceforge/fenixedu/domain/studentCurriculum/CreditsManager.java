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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;

public class CreditsManager {

    static public Equivalence createEquivalence(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup,
            CurriculumGroup curriculumGroup, Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments,
            Double givenCredits, Grade givenGrade, ExecutionSemester executionSemester) {

        if (courseGroup != null) {
            return new Equivalence(studentCurricularPlan, courseGroup, enrolments, buildNoEnrolCurricularCourses(dismissals),
                    givenCredits, givenGrade, executionSemester);

        } else if (curriculumGroup != null) {
            return new Equivalence(studentCurricularPlan, curriculumGroup, enrolments, givenCredits, givenGrade,
                    executionSemester);

        } else {
            return new Equivalence(studentCurricularPlan, dismissals, enrolments, givenGrade, executionSemester);
        }

    }

    static public Substitution createSubstitution(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup,
            CurriculumGroup curriculumGroup, Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments,
            Double givenCredits, ExecutionSemester executionSemester) {

        if (courseGroup != null) {
            return new Substitution(studentCurricularPlan, courseGroup, enrolments, buildNoEnrolCurricularCourses(dismissals),
                    givenCredits, executionSemester);

        } else if (curriculumGroup != null) {
            return new Substitution(studentCurricularPlan, curriculumGroup, enrolments, givenCredits, executionSemester);

        } else {
            return new Substitution(studentCurricularPlan, dismissals, enrolments, executionSemester);
        }

    }

    static public InternalSubstitution createInternalSubstitution(StudentCurricularPlan studentCurricularPlan,
            CourseGroup courseGroup, CurriculumGroup curriculumGroup, Collection<SelectedCurricularCourse> dismissals,
            Collection<IEnrolment> enrolments, Double givenCredits, ExecutionSemester executionSemester) {

        if (courseGroup != null) {
            return new InternalSubstitution(studentCurricularPlan, courseGroup, enrolments,
                    buildNoEnrolCurricularCourses(dismissals), givenCredits, executionSemester);

        } else if (curriculumGroup != null) {
            return new InternalSubstitution(studentCurricularPlan, curriculumGroup, enrolments, givenCredits, executionSemester);

        } else {
            return new InternalSubstitution(studentCurricularPlan, dismissals, enrolments, executionSemester);
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
