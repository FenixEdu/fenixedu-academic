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
package org.fenixedu.academic.domain.credits.util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.fenixedu.academic.domain.Department;
import org.fenixedu.academic.domain.DepartmentCreditsPool;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Professorship;
import org.fenixedu.academic.domain.credits.AnnualCreditsState;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.teacher.DegreeTeachingService;
import org.fenixedu.academic.domain.teacher.DegreeTeachingServiceCorrection;

import org.apache.commons.lang.StringUtils;

import pt.ist.fenixframework.Atomic;

public class DepartmentCreditsPoolBean implements Serializable {

    protected Department department;

    protected DepartmentCreditsPool departmentCreditsPool;

    protected AnnualCreditsState annualCreditsState;

    private SortedSet<DepartmentExecutionCourse> departmentSharedExecutionCourses;

    private SortedSet<DepartmentExecutionCourse> otherDepartmentSharedExecutionCourses;

    private SortedSet<DepartmentExecutionCourse> departmentExecutionCourses;

    protected BigDecimal availableCredits;

    protected BigDecimal assignedCredits;

    public DepartmentCreditsPoolBean(DepartmentCreditsBean departmentCreditsBean) {
        setDepartment(departmentCreditsBean.getDepartment());
        ExecutionYear executionYear = departmentCreditsBean.getExecutionYear();
        setDepartmentCreditsPool(DepartmentCreditsPool.getDepartmentCreditsPool(getDepartment(), executionYear));
        setAnnualCreditsState(AnnualCreditsState.getAnnualCreditsState(executionYear));
        setValues();
    }

    protected void setValues() {
        departmentSharedExecutionCourses = new TreeSet<DepartmentExecutionCourse>();
        departmentExecutionCourses = new TreeSet<DepartmentExecutionCourse>();
        otherDepartmentSharedExecutionCourses = new TreeSet<DepartmentExecutionCourse>();
        availableCredits = BigDecimal.ZERO;
        assignedCredits = BigDecimal.ZERO;
        if (departmentCreditsPool != null) {
            for (ExecutionSemester executionSemester : getAnnualCreditsState().getExecutionYear().getExecutionPeriodsSet()) {
                for (ExecutionCourse executionCourse : executionSemester.getAssociatedExecutionCoursesSet()) {
                    if (!(executionCourse.isDissertation() || executionCourse.getProjectTutorialCourse())) {
                        if (executionCourse.getDepartments().contains(getDepartment())) {
                            if (isSharedExecutionCourse(executionCourse)) {
                                addToSet(departmentSharedExecutionCourses, executionCourse);
                            } else {
                                addToSet(departmentExecutionCourses, executionCourse);
                            }
                        } else if (isTaughtByTeacherFromThisDepartment(executionCourse)) {
                            addToSet(otherDepartmentSharedExecutionCourses, executionCourse);
                        }
                    }
                }
            }
            assignedCredits = assignedCredits.setScale(2, BigDecimal.ROUND_HALF_UP);
            availableCredits =
                    departmentCreditsPool.getCreditsPool().subtract(assignedCredits).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
    }

    private void addToSet(SortedSet<DepartmentExecutionCourse> set, ExecutionCourse executionCourse) {
        DepartmentExecutionCourse departmentExecutionCourse = new DepartmentExecutionCourse(executionCourse);
        set.add(departmentExecutionCourse);
        final BigDecimal departmentEffectiveLoad = departmentExecutionCourse.getDepartmentEffectiveLoad();
        final BigDecimal unitCreditValue = executionCourse.getUnitCreditValue();
        if (departmentEffectiveLoad != null && unitCreditValue != null) {
            assignedCredits = assignedCredits.add(departmentEffectiveLoad.multiply(unitCreditValue));
        }
    }

    private boolean isTaughtByTeacherFromThisDepartment(ExecutionCourse executionCourse) {
        for (Professorship professorship : executionCourse.getProfessorshipsSet()) {
            Department professorshipDepartment =
                    professorship.getTeacher().getLastDepartment(executionCourse.getAcademicInterval());
            if (professorshipDepartment != null && professorshipDepartment.equals(getDepartment())) {
                return true;
            }
        }
        return false;
    }

    private boolean isSharedExecutionCourse(ExecutionCourse executionCourse) {
        for (Professorship professorship : executionCourse.getProfessorshipsSet()) {
            Department professorshipDepartment =
                    professorship.getTeacher().getLastDepartment(executionCourse.getAcademicInterval());
            if (professorshipDepartment != null && !professorshipDepartment.equals(getDepartment())) {
                return true;
            }
        }
        return false;
    }

    public boolean getCanEditSharedUnitCredits() {
        return getAnnualCreditsState().getSharedUnitCreditsInterval() != null
                && getAnnualCreditsState().getSharedUnitCreditsInterval().containsNow();
    }

    public boolean getCanEditUnitCredits() {
        return getAnnualCreditsState().getUnitCreditsInterval() != null
                && getAnnualCreditsState().getUnitCreditsInterval().containsNow();
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public AnnualCreditsState getAnnualCreditsState() {
        return annualCreditsState;
    }

    public void setAnnualCreditsState(AnnualCreditsState annualCreditsState) {
        this.annualCreditsState = annualCreditsState;
    }

    public DepartmentCreditsPool getDepartmentCreditsPool() {
        return departmentCreditsPool;
    }

    public void setDepartmentCreditsPool(DepartmentCreditsPool departmentCreditsPool) {
        this.departmentCreditsPool = departmentCreditsPool;
    }

    public Set<DepartmentExecutionCourse> getDepartmentSharedExecutionCourses() {
        return departmentSharedExecutionCourses;
    }

    public Set<DepartmentExecutionCourse> getOtherDepartmentSharedExecutionCourses() {
        return otherDepartmentSharedExecutionCourses;
    }

    public Set<DepartmentExecutionCourse> getDepartmentExecutionCourses() {
        return departmentExecutionCourses;
    }

    public BigDecimal getAvailableCredits() {
        return availableCredits;
    }

    public void setAvailableCredits(BigDecimal availableCredits) {
        this.availableCredits = availableCredits;
    }

    public BigDecimal getAssignedCredits() {
        return assignedCredits;
    }

    public void setAssignedCredits(BigDecimal assignedCredits) {
        this.assignedCredits = assignedCredits;
    }

    public class DepartmentExecutionCourse implements Serializable, Comparable<DepartmentExecutionCourse> {
        protected ExecutionCourse executionCourse;
        protected BigDecimal departmentEffectiveLoad = BigDecimal.ZERO;
        protected BigDecimal totalEffectiveLoad = BigDecimal.ZERO;
        protected BigDecimal unitCreditValue = BigDecimal.ZERO;
        protected String unitCreditJustification;

        public DepartmentExecutionCourse(ExecutionCourse executionCourse) {
            super();
            this.executionCourse = executionCourse;
            setUnitCreditValue(getExecutionCourse().getUnitCreditValue());
            setUnitCreditJustification(getExecutionCourse().getUnitCreditValueNotes());
            setEfectiveLoads();
        }

        public ExecutionCourse getExecutionCourse() {
            return executionCourse;
        }

        public void setExecutionCourse(ExecutionCourse executionCourse) {
            this.executionCourse = executionCourse;
        }

        public BigDecimal getDepartmentEffectiveLoad() {
            return departmentEffectiveLoad;
        }

        public BigDecimal getTotalEffectiveLoad() {
            return totalEffectiveLoad;
        }

        public BigDecimal getUnitCreditValue() {
            return unitCreditValue;
        }

        public void setUnitCreditValue(BigDecimal unitCreditValue) {
            this.unitCreditValue = unitCreditValue;
        }

        public String getUnitCreditJustification() {
            return unitCreditJustification;
        }

        public void setUnitCreditJustification(String unitCreditJustification) {
            this.unitCreditJustification = unitCreditJustification;
        }

        public void setEfectiveLoads() {
            for (Professorship professorship : getExecutionCourse().getProfessorshipsSet()) {
                Department lastDepartment =
                        professorship.getTeacher().getLastDepartment(getExecutionCourse().getAcademicInterval());
                for (DegreeTeachingService degreeTeachingService : professorship.getDegreeTeachingServicesSet()) {
                    double efectiveLoad = degreeTeachingService.getEfectiveLoad();
                    if (lastDepartment != null && lastDepartment.equals(getDepartment())) {
                        this.departmentEffectiveLoad = this.departmentEffectiveLoad.add(new BigDecimal(efectiveLoad));
                    }
                    this.totalEffectiveLoad = this.totalEffectiveLoad.add(new BigDecimal(efectiveLoad));
                }
                for (DegreeTeachingServiceCorrection degreeTeachingServiceCorrection : professorship
                        .getDegreeTeachingServiceCorrectionsSet()) {
                    BigDecimal efectiveLoad = degreeTeachingServiceCorrection.getCorrection();
                    if (lastDepartment != null && lastDepartment.equals(getDepartment())) {
                        this.departmentEffectiveLoad = this.departmentEffectiveLoad.add(efectiveLoad);
                    }
                    this.totalEffectiveLoad = this.totalEffectiveLoad.add(efectiveLoad);
                }
            }
            this.departmentEffectiveLoad = this.departmentEffectiveLoad.setScale(2, BigDecimal.ROUND_HALF_UP);
            this.totalEffectiveLoad = this.totalEffectiveLoad.setScale(2, BigDecimal.ROUND_HALF_UP);
        }

        @Override
        public int compareTo(DepartmentExecutionCourse o) {
            if (o == null) {
                return -1;
            }
            int compareTo = getExecutionCourse().getName().compareTo(o.getExecutionCourse().getName());
            if (compareTo == 0) {
                compareTo =
                        getExecutionCourse().getDegreePresentationString().compareTo(
                                o.getExecutionCourse().getDegreePresentationString());
            }
            return compareTo == 0 ? getExecutionCourse().getExternalId().compareTo(o.getExecutionCourse().getExternalId()) : compareTo;
        }

    }

    @Atomic
    public void editUnitCredits() {
        BigDecimal newAssignedCredits = BigDecimal.ZERO;
        for (DepartmentExecutionCourse departmentExecutionCourse : otherDepartmentSharedExecutionCourses) {
            BigDecimal newExecutionCourseCLE =
                    departmentExecutionCourse.getDepartmentEffectiveLoad().multiply(
                            departmentExecutionCourse.getUnitCreditValue());
            newAssignedCredits = newAssignedCredits.add(newExecutionCourseCLE);
        }

        for (DepartmentExecutionCourse departmentExecutionCourse : departmentSharedExecutionCourses) {
            newAssignedCredits =
                    setExecutionCourseUnitCredit(departmentExecutionCourse, getCanEditSharedUnitCredits(), newAssignedCredits,
                            true);
        }
        for (DepartmentExecutionCourse departmentExecutionCourse : departmentExecutionCourses) {
            newAssignedCredits =
                    setExecutionCourseUnitCredit(departmentExecutionCourse, getCanEditUnitCredits(), newAssignedCredits, false);
        }
        if (newAssignedCredits.compareTo(getDepartmentCreditsPool().getCreditsPool()) > 0) {
            throw new DomainException("label.excededDepartmentCreditsPool", getDepartmentCreditsPool().getCreditsPool()
                    .toString(), newAssignedCredits.toString());
        }
        setValues();
    }

    protected BigDecimal setExecutionCourseUnitCredit(DepartmentExecutionCourse departmentExecutionCourse,
            boolean canEditUnitValue, BigDecimal newAssignedCredits, Boolean shared) {
        if (canEditUnitValue) {
            BigDecimal newExecutionCourseCLE =
                    departmentExecutionCourse.getDepartmentEffectiveLoad().multiply(
                            departmentExecutionCourse.getUnitCreditValue());
            newAssignedCredits = newAssignedCredits.add(newExecutionCourseCLE);
            if (departmentExecutionCourse.executionCourse.getUnitCreditValue().compareTo(
                    departmentExecutionCourse.getUnitCreditValue()) != 0
                    || StringUtils.equals(departmentExecutionCourse.executionCourse.getUnitCreditValueNotes(),
                            departmentExecutionCourse.getUnitCreditJustification())) {
                departmentExecutionCourse.executionCourse.setUnitCreditValue(departmentExecutionCourse.getUnitCreditValue(),
                        departmentExecutionCourse.getUnitCreditJustification());
            }
        } else {
            BigDecimal oldExecutionCourseCLE =
                    departmentExecutionCourse.getDepartmentEffectiveLoad().multiply(
                            departmentExecutionCourse.executionCourse.getUnitCreditValue());
            newAssignedCredits = newAssignedCredits.add(oldExecutionCourseCLE);
        }
        return newAssignedCredits;
    }
}