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

import java.util.Collection;
import java.util.HashSet;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.IEnrolment;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import org.fenixedu.bennu.core.i18n.BundleUtil;

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
    public String getDescription() {
        return BundleUtil.getString("resources.StudentResources", "label.dismissal.Equivalence");
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

}
