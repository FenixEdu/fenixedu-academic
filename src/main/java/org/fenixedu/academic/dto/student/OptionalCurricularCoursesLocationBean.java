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
package org.fenixedu.academic.dto.student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.OptionalEnrolment;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degreeStructure.OptionalCurricularCourse;
import org.fenixedu.academic.domain.enrolment.CurriculumModuleMoveWrapper;
import org.fenixedu.academic.domain.enrolment.IDegreeModuleToEvaluate;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;

public class OptionalCurricularCoursesLocationBean implements Serializable {

    private StudentCurricularPlan studentCurricularPlan;

    private List<EnrolmentLocationBean> enrolmentBeans;

    private List<OptionalEnrolmentLocationBean> optionalEnrolmentBeans;

    public OptionalCurricularCoursesLocationBean(final StudentCurricularPlan studentCurricularPlan) {
        setStudentCurricularPlan(studentCurricularPlan);
        setEnrolmentBeans(new ArrayList<EnrolmentLocationBean>());
        setOptionalEnrolmentBeans(new ArrayList<OptionalEnrolmentLocationBean>());
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
        return this.studentCurricularPlan;
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }

    public List<EnrolmentLocationBean> getEnrolmentBeans() {
        return enrolmentBeans;
    }

    public void setEnrolmentBeans(List<EnrolmentLocationBean> enrolmentBeans) {
        this.enrolmentBeans = enrolmentBeans;
    }

    public List<OptionalEnrolmentLocationBean> getOptionalEnrolmentBeans() {
        return optionalEnrolmentBeans;
    }

    public void setOptionalEnrolmentBeans(List<OptionalEnrolmentLocationBean> optionalEnrolmentBeans) {
        this.optionalEnrolmentBeans = optionalEnrolmentBeans;
    }

    public void addEnrolment(Enrolment enrolment) {
        if (enrolment.isOptional()) {
            getOptionalEnrolmentBeans().add(new OptionalEnrolmentLocationBean((OptionalEnrolment) enrolment));
        } else {
            getEnrolmentBeans().add(new EnrolmentLocationBean(enrolment));
        }
    }

    public void addEnrolments(final Collection<Enrolment> enrolments) {
        for (final Enrolment enrolment : enrolments) {
            addEnrolment(enrolment);
        }
    }

    public Set<IDegreeModuleToEvaluate> getIDegreeModulesToEvaluate(final ExecutionInterval executionPeriod) {
        final Set<IDegreeModuleToEvaluate> result = new HashSet<IDegreeModuleToEvaluate>();
        for (final EnrolmentLocationBean bean : this.enrolmentBeans) {
            result.add(CurriculumModuleMoveWrapper.create(bean.getCurriculumGroup(getStudentCurricularPlan()), executionPeriod));
        }
        for (final OptionalEnrolmentLocationBean bean : this.optionalEnrolmentBeans) {
            result.add(CurriculumModuleMoveWrapper.create(bean.getCurriculumGroup(), executionPeriod));
        }
        return result;
    }

    static public class EnrolmentLocationBean implements Serializable {
        private Enrolment enrolment;

        private OptionalCurricularCourse optionalCurricularCourse;

        public EnrolmentLocationBean(final Enrolment enrolment) {
            setEnrolment(enrolment);
        }

        public Enrolment getEnrolment() {
            return this.enrolment;
        }

        public void setEnrolment(Enrolment enrolment) {
            this.enrolment = enrolment;
        }

        public OptionalCurricularCourse getOptionalCurricularCourse() {
            return this.optionalCurricularCourse;
        }

        public void setOptionalCurricularCourse(OptionalCurricularCourse optionalCurricularCourse) {
            this.optionalCurricularCourse = optionalCurricularCourse;
        }

        public CurriculumGroup getCurriculumGroup(final StudentCurricularPlan studentCurricularPlan) {
            final List<CurriculumGroup> curriculumGroups =
                    new ArrayList<CurriculumGroup>(
                            studentCurricularPlan
                                    .getCurricularCoursePossibleGroupsWithoutNoCourseGroupCurriculumGroups(getOptionalCurricularCourse()));
            Collections.sort(curriculumGroups, CurriculumGroup.COMPARATOR_BY_NAME_AND_ID);
            return curriculumGroups.isEmpty() ? null : curriculumGroups.iterator().next();
        }

        public StudentCurricularPlan getStudentCurricularPlan() {
            return getEnrolment().getStudentCurricularPlan();
        }

        public Student getStudent() {
            return getStudentCurricularPlan().getRegistration().getStudent();
        }
    }

    static public class OptionalEnrolmentLocationBean implements Serializable {
        private OptionalEnrolment enrolment;

        private CurriculumGroup curriculumGroup;

        public OptionalEnrolmentLocationBean(final OptionalEnrolment enrolment) {
            setEnrolment(enrolment);
        }

        public OptionalEnrolment getEnrolment() {
            return this.enrolment;
        }

        public void setEnrolment(OptionalEnrolment enrolment) {
            this.enrolment = enrolment;
        }

        public CurriculumGroup getCurriculumGroup() {
            return this.curriculumGroup;
        }

        public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
            this.curriculumGroup = curriculumGroup;
        }

        public StudentCurricularPlan getStudentCurricularPlan() {
            return getEnrolment().getStudentCurricularPlan();
        }

        public Student getStudent() {
            return getStudentCurricularPlan().getRegistration().getStudent();
        }
    }

}
