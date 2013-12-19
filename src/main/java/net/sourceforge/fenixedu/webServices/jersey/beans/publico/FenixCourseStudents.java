package net.sourceforge.fenixedu.webServices.jersey.beans.publico;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.student.Registration;

public class FenixCourseStudents {

    public static class FenixCourseStudent {

        String username;
        String degree;
        String degreeId;

        public FenixCourseStudent(final Attends attends) {
            final Registration registration = attends.getRegistration();
            final Person person = registration.getPerson();
            this.username = person.getUsername();
            final Degree degree = registration.getDegree();
            this.degree = degree.getSigla();
            this.degree = degree.getExternalId();
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getDegree() {
            return degree;
        }

        public void setDegree(String degree) {
            this.degree = degree;
        }

        public String getDegreeId() {
            return degreeId;
        }

        public void setDegreeId(String degreeId) {
            this.degreeId = degreeId;
        }

    }

    int enrolmentCount;
    int attendingCount;
    List<FenixCourseStudent> students = new ArrayList<>();

    public FenixCourseStudents(final ExecutionCourse executionCourse) {
        final Set<Attends> attendsSet = executionCourse.getAttendsSet();
        attendingCount = attendsSet.size();

        for (final Attends attends : attendsSet) {
            final Enrolment enrolment = attends.getEnrolment();
            if (enrolment != null) {
                enrolmentCount++;
            }

            students.add(new FenixCourseStudent(attends));
        }
    }

    public int getEnrolmentCount() {
        return enrolmentCount;
    }

    public void setEnrolmentCount(int enrolmentCount) {
        this.enrolmentCount = enrolmentCount;
    }

    public int getAttendingCount() {
        return attendingCount;
    }

    public void setAttendingCount(int attendingCount) {
        this.attendingCount = attendingCount;
    }

    public List<FenixCourseStudent> getStudents() {
        return students;
    }

    public void setStudents(List<FenixCourseStudent> students) {
        this.students = students;
    }

}
