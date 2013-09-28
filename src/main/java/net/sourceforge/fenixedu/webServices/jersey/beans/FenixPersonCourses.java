package net.sourceforge.fenixedu.webServices.jersey.beans;

import java.util.List;

public class FenixPersonCourses {

    public static class FenixCourse {
        String id;
        String sigla;
        String name;

        public FenixCourse(String id, String sigla, String name) {
            super();
            this.id = id;
            this.sigla = sigla;
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSigla() {
            return sigla;
        }

        public void setSigla(String sigla) {
            this.sigla = sigla;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    public static class FenixEnrolment extends FenixCourse {
        String grade;

        public FenixEnrolment(String id, String sigla, String name, String grade) {
            super(id, sigla, name);
            setGrade(grade);
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

    }

    private String year;
    private Integer semester;
    private List<FenixEnrolment> enrolments;
    private List<FenixCourse> teaching;

    public FenixPersonCourses() {

    }

    public FenixPersonCourses(String year, Integer semester, List<FenixEnrolment> enrolments, List<FenixCourse> teaching) {
        super();
        this.year = year;
        this.semester = semester;
        this.enrolments = enrolments;
        this.teaching = teaching;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public List<FenixEnrolment> getEnrolments() {
        return enrolments;
    }

    public void setEnrolments(List<FenixEnrolment> enrolments) {
        this.enrolments = enrolments;
    }

    public List<FenixCourse> getTeaching() {
        return teaching;
    }

    public void setTeaching(List<FenixCourse> teaching) {
        this.teaching = teaching;
    }

}
