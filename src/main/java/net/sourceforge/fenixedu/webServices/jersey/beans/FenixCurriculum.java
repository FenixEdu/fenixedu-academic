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
    private BigDecimal ects;
    private BigDecimal average;
    private Integer calculatedAverage;
    private Boolean isFinished;
    private Integer numberOfApprovedCourses;
    private List<ApprovedCourse> approvedCourses;

    public FenixCurriculum() {

    }

    public FenixCurriculum(FenixDegree degree, String start, String end, BigDecimal ects, BigDecimal average,
            Integer calculatedAverage, Boolean isFinished, List<ApprovedCourse> approvedCourses) {
        super();
        this.degree = degree;
        this.start = start;
        this.end = end;
        this.ects = ects;
        this.average = average;
        this.calculatedAverage = calculatedAverage;
        this.isFinished = isFinished;
        this.approvedCourses = approvedCourses;
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

    public BigDecimal getEcts() {
        return ects;
    }

    public void setEcts(BigDecimal ects) {
        this.ects = ects;
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
