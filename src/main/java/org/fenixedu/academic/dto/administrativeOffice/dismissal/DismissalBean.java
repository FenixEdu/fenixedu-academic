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
package org.fenixedu.academic.dto.administrativeOffice.dismissal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.fenixedu.academic.domain.CurricularCourse;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.ExecutionInterval;
import org.fenixedu.academic.domain.Grade;
import org.fenixedu.academic.domain.IEnrolment;
import org.fenixedu.academic.domain.StudentCurricularPlan;
import org.fenixedu.academic.domain.degreeStructure.CourseGroup;
import org.fenixedu.academic.domain.degreeStructure.OptionalCurricularCourse;
import org.fenixedu.academic.domain.student.Student;
import org.fenixedu.academic.domain.studentCurriculum.CurriculumGroup;
import org.fenixedu.academic.domain.studentCurriculum.ExternalEnrolment;
import org.fenixedu.academic.dto.student.IStudentCurricularPlanBean;

public class DismissalBean implements Serializable, IStudentCurricularPlanBean {

    static private final long serialVersionUID = 1L;

    private StudentCurricularPlan studentCurricularPlan;
    private ExecutionInterval executionInterval;
    private Collection<SelectedCurricularCourse> dismissals;
    private Collection<SelectedOptionalCurricularCourse> optionalDismissals;
    private CourseGroup courseGroup;
    private CurriculumGroup curriculumGroup;
    private Collection<SelectedEnrolment> enrolments;
    private Collection<SelectedExternalEnrolment> externalEnrolments;
    private DismissalType dismissalType;
    private Double credits;
    private Grade grade;

    public Collection<SelectedCurricularCourse> getDismissals() {
        return dismissals;
    }

    public void setDismissals(Collection<SelectedCurricularCourse> dismissals) {
        this.dismissals = dismissals;
    }

    public boolean hasAnyDismissals() {
        return getDismissals() != null && !getDismissals().isEmpty();
    }

    public boolean containsDismissal(CurricularCourse curricularCourse) {
        if (getDismissals() != null) {
            for (SelectedCurricularCourse selectedCurricularCourse : getDismissals()) {
                if (selectedCurricularCourse.getCurricularCourse().equals(curricularCourse)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Collection<SelectedOptionalCurricularCourse> getOptionalDismissals() {
        return optionalDismissals;
    }

    public void setOptionalDismissals(Collection<SelectedOptionalCurricularCourse> optionalDismissals) {
        this.optionalDismissals = optionalDismissals;
    }

    public boolean hasAnyOptionalDismissals() {
        return getOptionalDismissals() != null && !getOptionalDismissals().isEmpty();
    }

    public boolean containsOptionalDismissal(CurricularCourse curricularCourse) {
        if (getOptionalDismissals() != null) {
            for (SelectedOptionalCurricularCourse selectedCurricularCourse : getOptionalDismissals()) {
                if (selectedCurricularCourse.getCurricularCourse().equals(curricularCourse)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsDismissalOrOptionalDismissal(final CurricularCourse curricularCourse) {
        return containsDismissal(curricularCourse) || containsOptionalDismissal(curricularCourse);
    }

    public Collection<SelectedCurricularCourse> getAllDismissals() {
        final Collection<SelectedCurricularCourse> result = new ArrayList<SelectedCurricularCourse>();
        if (getDismissals() != null) {
            result.addAll(getDismissals());
        }
        if (getOptionalDismissals() != null) {
            result.addAll(getOptionalDismissals());
        }
        return result;
    }

    public Collection<SelectedEnrolment> getEnrolments() {
        return enrolments;
    }

    public void setEnrolments(Collection<SelectedEnrolment> enrolments) {
        this.enrolments = enrolments;
    }

    public CourseGroup getCourseGroup() {
        return this.courseGroup;
    }

    public void setCourseGroup(CourseGroup courseGroup) {
        this.courseGroup = courseGroup;
    }

    public CurriculumGroup getCurriculumGroup() {
        return this.curriculumGroup;
    }

    public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
        this.curriculumGroup = curriculumGroup;
    }

    @Override
    public StudentCurricularPlan getStudentCurricularPlan() {
        return this.studentCurricularPlan;
    }

    public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
        this.studentCurricularPlan = studentCurricularPlan;
    }

    public ExecutionInterval getExecutionPeriod() {
        return this.executionInterval;
    }

    public void setExecutionPeriod(ExecutionInterval executionInterval) {
        this.executionInterval = executionInterval;
    }

    public DismissalType getDismissalType() {
        return dismissalType;
    }

    public void setDismissalType(DismissalType dismissalType) {
        this.dismissalType = dismissalType;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Double getCredits() {
        return credits;
    }

    public void setCredits(Double credits) {
        this.credits = credits;
    }

    public Collection<SelectedExternalEnrolment> getExternalEnrolments() {
        return externalEnrolments;
    }

    public void setExternalEnrolments(Collection<SelectedExternalEnrolment> externalEnrolments) {
        this.externalEnrolments = externalEnrolments;
    }

    public Collection<IEnrolment> getSelectedEnrolments() {
        final Collection<IEnrolment> result = new ArrayList<IEnrolment>();

        if (getEnrolments() != null) {
            for (final SelectedEnrolment selectedEnrolment : getEnrolments()) {
                if (selectedEnrolment.getSelected()) {
                    result.add(selectedEnrolment.getEnrolment());
                }
            }
        }

        if (getExternalEnrolments() != null) {
            for (final SelectedExternalEnrolment selectedEnrolment : getExternalEnrolments()) {
                if (selectedEnrolment.getSelected()) {
                    result.add(selectedEnrolment.getExternalEnrolment());
                }
            }
        }

        return result;
    }

    public boolean hasAnySelectedIEnrolments() {
        return !getSelectedEnrolments().isEmpty();
    }

    public Student getStudent() {
        return getStudentCurricularPlan().getRegistration().getStudent();
    }

    public Collection<? extends CurricularCourse> getAllCurricularCoursesToDismissal() {
        return studentCurricularPlan.getAllCurricularCoursesToDismissal(executionInterval);
    }

    public static class SelectedCurricularCourse implements Serializable {

        static private final long serialVersionUID = 1L;

        private Boolean selected = Boolean.FALSE;

        private CurricularCourse curricularCourse;
        private CurriculumGroup curriculumGroup;
        private StudentCurricularPlan studentCurricularPlan;

        public SelectedCurricularCourse(CurricularCourse curricularCourse, StudentCurricularPlan studentCurricularPlan) {
            setCurricularCourse(curricularCourse);
            setStudentCurricularPlan(studentCurricularPlan);
        }

        public CurricularCourse getCurricularCourse() {
            return this.curricularCourse;
        }

        public void setCurricularCourse(CurricularCourse curricularCourse) {
            this.curricularCourse = curricularCourse;
        }

        public Boolean getSelected() {
            return selected;
        }

        public void setSelected(Boolean selected) {
            this.selected = selected;
        }

        public CurriculumGroup getCurriculumGroup() {
            return this.curriculumGroup;
        }

        public void setCurriculumGroup(CurriculumGroup curriculumGroup) {
            this.curriculumGroup = curriculumGroup;
        }

        public StudentCurricularPlan getStudentCurricularPlan() {
            return this.studentCurricularPlan;
        }

        public void setStudentCurricularPlan(StudentCurricularPlan studentCurricularPlan) {
            this.studentCurricularPlan = studentCurricularPlan;
        }

        public String getKey() {
            StringBuilder stringBuilder = new StringBuilder();
            if (this.getCurricularCourse() != null) {
                stringBuilder.append(this.getCurricularCourse().getClass().getName()).append(":")
                        .append(this.getCurricularCourse().getExternalId());
            }
            stringBuilder.append(",");
            if (this.getCurriculumGroup() != null) {
                stringBuilder.append(this.getCurriculumGroup().getClass().getName()).append(":")
                        .append(this.getCurriculumGroup().getExternalId());
            }
            stringBuilder.append(",");
            if (this.getStudentCurricularPlan() != null) {
                stringBuilder.append(this.getStudentCurricularPlan().getClass().getName()).append(":")
                        .append(this.getStudentCurricularPlan().getExternalId());
            }
            return stringBuilder.toString();
        }

        public boolean isOptional() {
            return false;
        }
    }

    public static class SelectedOptionalCurricularCourse extends SelectedCurricularCourse {

        static private final long serialVersionUID = 1L;

        private Double credits;

        public SelectedOptionalCurricularCourse(final OptionalCurricularCourse curricularCourse,
                final StudentCurricularPlan studentCurricularPlan) {
            super(curricularCourse, studentCurricularPlan);
        }

        @Override
        public OptionalCurricularCourse getCurricularCourse() {
            return (OptionalCurricularCourse) super.getCurricularCourse();
        }

        public Double getCredits() {
            return credits;
        }

        public void setCredits(Double credits) {
            this.credits = credits;
        }

        @Override
        public boolean isOptional() {
            return true;
        }
    }

    public static class SelectedEnrolment implements Serializable {

        static private final long serialVersionUID = 1L;

        private Boolean selected = Boolean.FALSE;

        private Enrolment enrolment;

        public SelectedEnrolment(Enrolment enrolment) {
            setEnrolment(enrolment);
        }

        public Enrolment getEnrolment() {
            return this.enrolment;
        }

        public void setEnrolment(Enrolment enrolment) {
            this.enrolment = enrolment;
        }

        public Boolean getSelected() {
            return selected;
        }

        public void setSelected(Boolean selected) {
            this.selected = selected;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof SelectedEnrolment)) {
                return false;
            }
            return equals((SelectedEnrolment) obj);
        }

        public boolean equals(final SelectedEnrolment other) {
            return getEnrolment() == other.getEnrolment();
        }

        @Override
        public int hashCode() {
            return getEnrolment().hashCode();
        }
    }

    public static class SelectedExternalEnrolment implements Serializable {

        static private final long serialVersionUID = 1L;

        private Boolean selected = Boolean.FALSE;

        private ExternalEnrolment externalEnrolment;

        public SelectedExternalEnrolment(ExternalEnrolment externalEnrolment) {
            setExternalEnrolment(externalEnrolment);
        }

        public ExternalEnrolment getExternalEnrolment() {
            return this.externalEnrolment;
        }

        public void setExternalEnrolment(ExternalEnrolment externalEnrolment) {
            this.externalEnrolment = externalEnrolment;
        }

        public Boolean getSelected() {
            return selected;
        }

        public void setSelected(Boolean selected) {
            this.selected = selected;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof SelectedExternalEnrolment)) {
                return false;
            }
            return equals((SelectedExternalEnrolment) obj);
        }

        public boolean equals(final SelectedExternalEnrolment other) {
            return getExternalEnrolment() == other.getExternalEnrolment();
        }

        @Override
        public int hashCode() {
            return getExternalEnrolment().hashCode();
        }
    }

    public static enum DismissalType {
        CURRICULAR_COURSE_CREDITS, CURRICULUM_GROUP_CREDITS, NO_COURSE_GROUP_CURRICULUM_GROUP_CREDITS;

        public String getName() {
            return name();
        }
    }

}
