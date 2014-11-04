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
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Grade;
import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.curriculum.ICurriculumEntry;

public class Substitution extends Substitution_Base {

    public Substitution() {
        super();
    }

    public Substitution(final StudentCurricularPlan studentCurricularPlan, final Collection<SelectedCurricularCourse> dismissals,
            final Collection<IEnrolment> enrolments, ExecutionSemester executionSemester) {
        this();
        init(studentCurricularPlan, dismissals, enrolments, executionSemester);
    }

    public Substitution(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup, Collection<IEnrolment> enrolments,
            Collection<CurricularCourse> noEnrolCurricularCourses, Double credits, ExecutionSemester executionSemester) {
        this();
        init(studentCurricularPlan, courseGroup, enrolments, noEnrolCurricularCourses, credits, executionSemester);
    }

    public Substitution(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
            Collection<IEnrolment> enrolments, Double credits, ExecutionSemester executionSemester) {
        this();
        init(studentCurricularPlan, curriculumGroup, enrolments, new HashSet<CurricularCourse>(0), credits, executionSemester);
    }

    @Override
    protected void init(StudentCurricularPlan studentCurricularPlan, CourseGroup courseGroup, Collection<IEnrolment> enrolments,
            Collection<CurricularCourse> noEnrolCurricularCourses, Double credits, ExecutionSemester executionSemester) {
        checkEnrolments(enrolments);
        super.init(studentCurricularPlan, courseGroup, enrolments, noEnrolCurricularCourses, credits, executionSemester);
    }

    @Override
    protected void init(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
            Collection<IEnrolment> enrolments, Collection<CurricularCourse> noEnrolCurricularCourses, Double credits,
            ExecutionSemester executionSemester) {
        checkEnrolments(enrolments);
        super.init(studentCurricularPlan, curriculumGroup, enrolments, noEnrolCurricularCourses, credits, executionSemester);
    }

    @Override
    protected void init(final StudentCurricularPlan studentCurricularPlan, final Collection<SelectedCurricularCourse> dismissals,
            final Collection<IEnrolment> enrolments, ExecutionSemester executionSemester) {

        checkEnrolments(enrolments);
        super.init(studentCurricularPlan, dismissals, enrolments, executionSemester);
    }

    private void checkEnrolments(final Collection<IEnrolment> enrolments) {
        if (enrolments == null || enrolments.isEmpty()) {
            throw new DomainException("error.substitution.wrong.arguments");
        }
    }

    @Override
    final public boolean isSubstitution() {
        return true;
    }

    @Override
    public boolean isEquivalence() {
        return false;
    }

    @Override
    public Grade getGrade() {
        return getEnrolmentsAverageGrade();
    }

    private Grade getEnrolmentsAverageGrade() {
        return null;
    }

    @Override
    @jvstm.cps.ConsistencyPredicate
    protected boolean checkGrade() {
        return true;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<ICurriculumEntry> getAverageEntries(final ExecutionYear executionYear) {
        final Collection<ICurriculumEntry> result = new HashSet<ICurriculumEntry>();

        for (final EnrolmentWrapper enrolmentWrapper : this.getEnrolmentsSet()) {
            final IEnrolment enrolment = enrolmentWrapper.getIEnrolment();
            if (enrolment != null
                    && (executionYear == null || enrolment.getExecutionYear() == null || enrolment.getExecutionYear().isBefore(
                            executionYear))) {
                result.add(enrolmentWrapper.getIEnrolment());
            }
        }

        return result;
    }

}
