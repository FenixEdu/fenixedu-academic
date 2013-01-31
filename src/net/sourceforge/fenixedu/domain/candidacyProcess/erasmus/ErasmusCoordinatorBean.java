package net.sourceforge.fenixedu.domain.candidacyProcess.erasmus;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityCoordinator;

public class ErasmusCoordinatorBean implements Serializable {
	private Teacher teacher;
	private Degree degree;

	private MobilityCoordinator erasmusCoordinator;

	private String teacherId;

	public ErasmusCoordinatorBean() {

	}

	public ErasmusCoordinatorBean(final Teacher teacher, final Degree degree) {
		this.teacher = teacher;
		this.degree = degree;
	}

	public ErasmusCoordinatorBean(final MobilityCoordinator coordinator) {
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

	public MobilityCoordinator getErasmusCoordinator() {
		return erasmusCoordinator;
	}

	public void setErasmusCoordinator(MobilityCoordinator erasmusCoordinator) {
		this.erasmusCoordinator = erasmusCoordinator;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
}
