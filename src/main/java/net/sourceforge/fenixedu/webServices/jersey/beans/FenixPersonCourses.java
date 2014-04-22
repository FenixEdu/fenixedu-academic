package net.sourceforge.fenixedu.webServices.jersey.beans;

import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;

public class FenixPersonCourses {

    public static class FenixEnrolment extends FenixCourse {
        String grade;
        double ects;

        public FenixEnrolment(ExecutionCourse course, String grade, double ects) {
            super(course);
            setGrade(grade);
            setEcts(ects);
        }

        public String getGrade() {
            return grade;
        }

        public double getEcts() {
            return ects;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public void setEcts(double ects) {
            this.ects = ects;
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
