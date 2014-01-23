package net.sourceforge.fenixedu.webServices.jersey.beans;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;

public class FenixPersonCourses {

    public static class FenixEnrolment extends FenixCourse {
        String grade;

        public FenixEnrolment(ExecutionCourse course, String grade) {
            super(course);
            setGrade(grade);
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

    }

    private List<FenixEnrolment> enrolments;
    private List<FenixCourse> teaching;

    public FenixPersonCourses() {

    }

    public FenixPersonCourses(List<FenixEnrolment> enrolments, List<FenixCourse> teaching) {
        super();
        this.enrolments = enrolments;
        this.teaching = teaching;
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
