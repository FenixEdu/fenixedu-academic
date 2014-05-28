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

import java.util.Collection;
import java.util.HashSet;

import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class Equivalence extends Equivalence_Base {

    public Equivalence() {
        super();
        setGrade(Grade.createEmptyGrade());
    }

    public Equivalence(StudentCurricularPlan studentCurricularPlan, Collection<SelectedCurricularCourse> dismissals,
            Collection<IEnrolment> enrolments, Grade grade, ExecutionSemester executionSemester) {
        this();
        init(studentCurricularPlan, dismissals, enrolments, grade, executionSemester);
    }

    public Equivalence(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup, Collection<IEnrolment> enrolments,
            Collection<CurricularCourse> noEnrolCurricularCourses, Double credits, Grade grade,
            ExecutionSemester executionSemester) {
        this();
        init(studentCurricularPlan, courseGroup, enrolments, noEnrolCurricularCourses, credits, grade, executionSemester);
    }

    public Equivalence(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
            Collection<IEnrolment> enrolments, Double credits, Grade grade, ExecutionSemester executionSemester) {
        this();
        init(studentCurricularPlan, curriculumGroup, enrolments, new HashSet<CurricularCourse>(0), credits, grade,
                executionSemester);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup, Collection<IEnrolment> enrolments,
            Collection<CurricularCourse> noEnrolCurricularCourses, Double credits, Grade grade,
            ExecutionSemester executionSemester) {
        initGrade(enrolments, grade);
        super.init(studentCurricularPlan, courseGroup, enrolments, noEnrolCurricularCourses, credits, executionSemester);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
            Collection<IEnrolment> enrolments, Collection<CurricularCourse> noEnrolCurricularCourses, Double credits,
            Grade grade, ExecutionSemester executionSemester) {
        initGrade(enrolments, grade);
        super.init(studentCurricularPlan, curriculumGroup, enrolments, noEnrolCurricularCourses, credits, executionSemester);
    }

    protected void init(StudentCurricularPlan studentCurricularPlan, Collection<SelectedCurricularCourse> dismissals,
            Collection<IEnrolment> enrolments, Grade grade, ExecutionSemester executionSemester) {
        initGrade(enrolments, grade);
        super.init(studentCurricularPlan, dismissals, enrolments, executionSemester);
    }

    private void initGrade(Collection<IEnrolment> enrolments, Grade grade) {
        if (grade.isEmpty()) {
            throw new DomainException("error.equivalence.must.define.enrolments.and.grade");
        }
        setGrade(grade);
    }

    @Override
    public String getGivenGrade() {
        return getGrade() != null ? getGrade().getValue() : null;
    }

    @Override
    public boolean isCredits() {
        return false;
    }

    @Override
    public boolean isEquivalence() {
        return true;
    }

    @jvstm.cps.ConsistencyPredicate
    protected boolean checkGrade() {
        return getGrade() != null;
    }

    @Deprecated
    public boolean hasGrade() {
        return getGrade() != null;
    }

}
