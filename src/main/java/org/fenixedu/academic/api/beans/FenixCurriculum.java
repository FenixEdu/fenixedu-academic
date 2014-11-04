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
package net.sourceforge.fenixedu.webServices.jersey.beans;

import java.math.BigDecimal;
import java.util.List;

import net.sourceforge.fenixedu.webServices.jersey.beans.publico.FenixDegree;

public class FenixCurriculum {

    public static class ApprovedCourse {
        FenixCourse course;
        private String grade;
        private BigDecimal ects;

        public ApprovedCourse(FenixCourse course, String grade, BigDecimal ects) {
            super();
            this.grade = grade;
            this.ects = ects;
            this.course = course;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public BigDecimal getEcts() {
            return ects;
        }

        public void setEcts(BigDecimal ects) {
            this.ects = ects;
        }

        public FenixCourse getCourse() {
            return course;
        }

        public void setCourse(FenixCourse course) {
            this.course = course;
        }

    }

    private FenixDegree degree;
    private String start;
    private String end;
    private BigDecimal credits;
    private BigDecimal average;
    private Integer calculatedAverage;
    private Boolean isFinished;
    private Integer numberOfApprovedCourses;
    private Integer curricularYear;
    private List<ApprovedCourse> approvedCourses;

    public FenixCurriculum() {

    }

    public FenixCurriculum(FenixDegree degree, String start, String end, BigDecimal credits, BigDecimal average,
            Integer calculatedAverage, Boolean isFinished, Integer curricularYear, List<ApprovedCourse> approvedCourses) {
        super();
        this.degree = degree;
        this.start = start;
        this.end = end;
        this.credits = credits;
        this.average = average;
        this.calculatedAverage = calculatedAverage;
        this.isFinished = isFinished;
        this.approvedCourses = approvedCourses;
        this.curricularYear = curricularYear;
        this.numberOfApprovedCourses = approvedCourses.size();
    }

    public FenixDegree getDegree() {
        return degree;
    }

    public void setDegree(FenixDegree degree) {
        this.degree = degree;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public BigDecimal getCredits() {
        return credits;
    }

    public void setCredits(BigDecimal ects) {
        this.credits = ects;
    }

    public BigDecimal getAverage() {
        return average;
    }

    public void setAverage(BigDecimal average) {
        this.average = average;
    }

    public Integer getCalculatedAverage() {
        return calculatedAverage;
    }

    public void setCalculatedAverage(Integer calculatedAverage) {
        this.calculatedAverage = calculatedAverage;
    }

    public Boolean getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(Boolean isFinished) {
        this.isFinished = isFinished;
    }

    public Integer getCurrentYear() {
        return curricularYear;
    }

    public void setCurrentYear(Integer curricularYear) {
        this.curricularYear = curricularYear;
    }

    public Integer getNumberOfApprovedCourses() {
        return numberOfApprovedCourses;
    }

    public void setNumberOfApprovedCourses(Integer numberOfApprovedCourses) {
        this.numberOfApprovedCourses = numberOfApprovedCourses;
    }

    public List<ApprovedCourse> getApprovedCourses() {
        return approvedCourses;
    }

    public void setApprovedCourses(List<ApprovedCourse> approvedCourses) {
        this.approvedCourses = approvedCourses;
    }

}
