package org.fenixedu.academic.ui.spring.controller.teacher.professorship;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionSemester;
import org.fenixedu.academic.domain.Person;
import org.springframework.beans.factory.annotation.Value;

public class CreateProfessorshipBean {

    private ExecutionSemester period;
    private Person teacher;
    private ExecutionDegree degree;
    private ExecutionCourse course;

    @Value("false")
    private Boolean responsibleFor;

    public ExecutionSemester getPeriod() {
        return period;
    }

    public void setPeriod(ExecutionSemester period) {
        this.period = period;
    }

    public ExecutionDegree getDegree() {
        return degree;
    }

    public void setDegree(ExecutionDegree degree) {
        this.degree = degree;
    }

    public ExecutionCourse getCourse() {
        return course;
    }

    public void setCourse(ExecutionCourse course) {
        this.course = course;
    }

    public Boolean getResponsibleFor() {
        return responsibleFor;
    }

    public void setResponsibleFor(Boolean responsibleFor) {
        this.responsibleFor = responsibleFor;
    }

    public Person getTeacher() {
        return teacher;
    }

    public void setTeacher(Person teacher) {
        this.teacher = teacher;
    }

}
