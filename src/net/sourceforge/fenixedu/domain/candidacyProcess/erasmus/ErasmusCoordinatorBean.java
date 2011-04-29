package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Teacher;

public class ErasmusCoordinatorBean implements Serializable {
    private Teacher teacher;
    private Degree degree;

    private ErasmusCoordinator erasmusCoordinator;

    private String teacherId;

    public ErasmusCoordinatorBean() {

    }

    public ErasmusCoordinatorBean(final Teacher teacher, final Degree degree) {
	this.teacher = teacher;
	this.degree = degree;
    }

    public ErasmusCoordinatorBean(final ErasmusCoordinator coordinator) {
	this.erasmusCoordinator = coordinator;
    }

    public Teacher getTeacher() {
	return teacher;
    }

    public void setTeacher(Teacher teacher) {
	this.teacher = teacher;
    }

    public Degree getDegree() {
	return degree;
    }

    public void setDegree(Degree degree) {
	this.degree = degree;
    }

    public ErasmusCoordinator getErasmusCoordinator() {
	return erasmusCoordinator;
    }

    public void setErasmusCoordinator(ErasmusCoordinator erasmusCoordinator) {
	this.erasmusCoordinator = erasmusCoordinator;
    }

    public String getTeacherId() {
	return teacherId;
    }

    public void setTeacherNumber(String teacherId) {
	this.teacherId = teacherId;
    }
}
