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
package net.sourceforge.fenixedu.dataTransferObject.student;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;

public class ExecutionPeriodStatisticsBean implements Serializable {

    private ExecutionSemester executionSemester;
    private List<Enrolment> enrolmentsWithinExecutionPeriod;
    private Integer totalEnrolmentsNumber;
    private Integer approvedEnrolmentsNumber;
    private Integer approvedRatio;
    private Double aritmeticAverage;

    public ExecutionPeriodStatisticsBean() {
        setExecutionPeriod(null);
        this.totalEnrolmentsNumber = 0;
        this.approvedEnrolmentsNumber = 0;
        this.approvedRatio = 0;
        this.aritmeticAverage = 0.0;
    }

    public ExecutionPeriodStatisticsBean(ExecutionSemester executionSemester) {
        setExecutionPeriod(executionSemester);
        this.enrolmentsWithinExecutionPeriod = new ArrayList<Enrolment>();
    }

    public ExecutionSemester getExecutionPeriod() {
        return (executionSemester);
    }

    public void setExecutionPeriod(ExecutionSemester executionSemester) {
        this.executionSemester = executionSemester;
    }

    public List<Enrolment> getEnrolmentsWithinExecutionPeriod() {
        return this.enrolmentsWithinExecutionPeriod;
    }

    public void setEnrolmentsWithinExecutionPeriod(List<Enrolment> enrolmentsWithinExecutionPeriod) {
        this.enrolmentsWithinExecutionPeriod = enrolmentsWithinExecutionPeriod;
    }

    public Integer getTotalEnrolmentsNumber() {
        return totalEnrolmentsNumber;
    }

    public void setTotalEnrolmentsNumber(Integer totalEnrolmentsNumber) {
        this.totalEnrolmentsNumber = totalEnrolmentsNumber;
    }

    public Integer getApprovedEnrolmentsNumber() {
        return approvedEnrolmentsNumber;
    }

    public void setApprovedEnrolmentsNumber(Integer approvedEnrolmentsNumber) {
        this.approvedEnrolmentsNumber = approvedEnrolmentsNumber;
    }

    public Integer getApprovedRatio() {
        return approvedRatio;
    }

    public void setApprovedRatio(Integer approvedRatio) {
        this.approvedRatio = approvedRatio;
    }

    public Double getAritmeticAverage() {
        return aritmeticAverage;
    }

    public void setAritmeticAverage(Double aritmeticAverage) {
        this.aritmeticAverage = aritmeticAverage;
    }

    private void calculateTotalEnrolmentsNumber() {
        setTotalEnrolmentsNumber(getEnrolmentsWithinExecutionPeriod().size());
    }

    private void calculateApprovedEnrolmentsNumber() {
        int approvedEnrolmentsNumber = 0;
        for (Enrolment enrolment : getEnrolmentsWithinExecutionPeriod()) {
            if (enrolment.isApproved()) {
                approvedEnrolmentsNumber++;
            }
        }
        setApprovedEnrolmentsNumber(approvedEnrolmentsNumber);
    }

    private void calculateAritmeticAverage() {
        int concludedCurricularCourses = 0, gradesAcumulator = 0;
        double aritmeticAverage = 0;

        for (Enrolment enrolment : getEnrolmentsWithinExecutionPeriod()) {
            if (enrolment.isApproved() && enrolment.getFinalGrade() != null) {
                concludedCurricularCourses++;
                gradesAcumulator += enrolment.getFinalGrade();
            }
        }
        aritmeticAverage = ((double) gradesAcumulator / (double) concludedCurricularCourses);
        setAritmeticAverage((int) (aritmeticAverage * 100) / 100.0);
    }

    private void calculateApprovedRatio() {
        int concludedEnrolments = 0;

        for (Enrolment enrolment : getEnrolmentsWithinExecutionPeriod()) {
            if (enrolment.getEnrollmentState().equals(EnrollmentState.APROVED)) {
                concludedEnrolments++;
            }
        }
        int ratio = Math.round(((float) concludedEnrolments / (float) getTotalEnrolmentsNumber()) * 100);
        setApprovedRatio(ratio);
    }

    public void addEnrolmentsWithinExecutionPeriod(List<Enrolment> enrolments) {
        // Checks if already exists enrolments for this execution period
        if (!this.enrolmentsWithinExecutionPeriod.isEmpty()) {
            for (Enrolment newEnrolment : enrolments) {
                StudentCurricularPlan newEnrolmentSCP = newEnrolment.getStudentCurricularPlan();
                Enrolment previousAddedEnrolment = getPreviousEnrolmentGivenCurricularCourse(newEnrolment.getCurricularCourse());
                // Checks if exists a repeated enrolment
                if (previousAddedEnrolment != null) {
                    StudentCurricularPlan previousEnrolmentSCP = previousAddedEnrolment.getStudentCurricularPlan();
                    if (newEnrolmentSCP.getStartExecutionYear().isAfterOrEquals(previousEnrolmentSCP.getStartExecutionYear())) {
                        this.enrolmentsWithinExecutionPeriod.remove(previousAddedEnrolment);
                        this.enrolmentsWithinExecutionPeriod.add(newEnrolment);
                    }
                } else {
                    this.enrolmentsWithinExecutionPeriod.add(newEnrolment);
                }
            }
        } else {
            this.enrolmentsWithinExecutionPeriod = enrolments;
        }
        recalculateStatistics();
    }

    private Enrolment getPreviousEnrolmentGivenCurricularCourse(CurricularCourse curricular) {
        for (Enrolment enrolment : this.enrolmentsWithinExecutionPeriod) {
            if (enrolment.getCurricularCourse().equals(curricular)) {
                return enrolment;
            }
        }
        return null;
    }

    private void recalculateStatistics() {
        calculateTotalEnrolmentsNumber();
        calculateApprovedEnrolmentsNumber();
        calculateApprovedRatio();
        calculateAritmeticAverage();
    }

}
