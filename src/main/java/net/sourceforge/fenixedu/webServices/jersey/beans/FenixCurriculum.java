package net.sourceforge.fenixedu.webServices.jersey.beans;

import java.math.BigDecimal;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.DegreeType;

public class FenixCurriculum {

    public static class FenixCourseInfo {
        private String name;
        private String grade;
        private BigDecimal ects;
        private String id;
        private Integer semester;
        private String year;

        public FenixCourseInfo(String name, String grade, BigDecimal ects, String id, Integer semester, String year) {
            super();
            this.name = name;
            this.grade = grade;
            this.ects = ects;
            this.id = id;
            this.semester = semester;
            this.year = year;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Integer getSemester() {
            return semester;
        }

        public void setSemester(Integer semester) {
            this.semester = semester;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

    }

    private String id;
    private String name;
    private DegreeType degreeType;
    private String campus;
    private String presentationName;
    private String start;
    private String end;
    private BigDecimal ects;
    private BigDecimal average;
    private Integer calculatedAverage;
    private Boolean isFinished;
    private String approvedCourses;
    private List<FenixCourseInfo> courseInfo;

    public FenixCurriculum() {

    }

    public FenixCurriculum(String id, String name, DegreeType degreeType, String campus, String presentationName, String start,
            String end, BigDecimal ects, BigDecimal average, Integer calculatedAverage, Boolean isFinished,
            String approvedCourses, List<FenixCourseInfo> courseInfo) {
        super();
        this.id = id;
        this.name = name;
        this.degreeType = degreeType;
        this.campus = campus;
        this.presentationName = presentationName;
        this.start = start;
        this.end = end;
        this.ects = ects;
        this.average = average;
        this.calculatedAverage = calculatedAverage;
        this.isFinished = isFinished;
        this.approvedCourses = approvedCourses;
        this.courseInfo = courseInfo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DegreeType getDegreeType() {
        return degreeType;
    }

    public void setDegreeType(DegreeType degreeType) {
        this.degreeType = degreeType;
    }

    public String getCampus() {
        return campus;
    }

    public void setCampus(String campus) {
        this.campus = campus;
    }

    public String getPresentationName() {
        return presentationName;
    }

    public void setPresentationName(String presentationName) {
        this.presentationName = presentationName;
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

    public String getApprovedCourses() {
        return approvedCourses;
    }

    public void setApprovedCourses(String approvedCourses) {
        this.approvedCourses = approvedCourses;
    }

    public List<FenixCourseInfo> getCourseInfo() {
        return courseInfo;
    }

    public void setCourseInfo(List<FenixCourseInfo> courseInfo) {
        this.courseInfo = courseInfo;
    }

}
