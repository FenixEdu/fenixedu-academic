package net.sourceforge.fenixedu.webServices.jersey.beans;

import java.util.List;

public class FenixPersonCourses {

    public static class FenixEnrolment extends FenixCourse {
        String grade;

        public FenixEnrolment(String id, String acronym, String name, String grade) {
            super(id, acronym, name);
            setGrade(grade);
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

    }

    private String academicTerm;
    private List<FenixEnrolment> enrolments;
    private List<FenixCourse> teaching;

    public FenixPersonCourses() {

    }

    public FenixPersonCourses(String academicTerm, List<FenixEnrolment> enrolments, List<FenixCourse> teaching) {
        super();
        this.academicTerm = academicTerm;
        this.enrolments = enrolments;
        this.teaching = teaching;
    }

    public String getAcademicTerm() {
        return academicTerm;
    }

    public void setAcademicTerm(String academicTerm) {
        this.academicTerm = academicTerm;
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
