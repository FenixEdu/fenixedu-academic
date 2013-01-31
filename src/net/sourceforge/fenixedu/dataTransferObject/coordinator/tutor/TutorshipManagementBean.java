package net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.util.Month;

public class TutorshipManagementBean implements Serializable {
	private String teacherId;

	private Teacher teacher;

	private Integer executionDegreeID;

	private Integer degreeCurricularPlanID;

	private Month tutorshipEndMonth;

	private Integer tutorshipEndYear;

	private Integer studentNumber;

	private Integer numberOfPastTutorships;

	private Integer numberOfCurrentTutorships;

	private List<Tutorship> studentsList;

	private TutorshipManagementBean() {
		this.studentsList = new ArrayList<Tutorship>();
		this.setDefaultFields();
	}

	public TutorshipManagementBean(Integer executionDegreeID, Integer degreeCurricularPlanID) {
		this();
		this.executionDegreeID = executionDegreeID;
		this.degreeCurricularPlanID = degreeCurricularPlanID;
	}

	public TutorshipManagementBean(Integer executionDegreeID, Integer degreeCurricularPlanID, String teacherId) {
		this();
		this.executionDegreeID = executionDegreeID;
		this.degreeCurricularPlanID = degreeCurricularPlanID;
		this.teacherId = teacherId;
	}

	public Integer getDegreeCurricularPlanID() {
		return degreeCurricularPlanID;
	}

	public void setDegreeCurricularPlanID(Integer degreeCurricularPlanID) {
		this.degreeCurricularPlanID = degreeCurricularPlanID;
	}

	public Integer getExecutionDegreeID() {
		return executionDegreeID;
	}

	public void setExecutionDegreeID(Integer executionDegreeID) {
		this.executionDegreeID = executionDegreeID;
	}

	public List<Tutorship> getStudentsList() {
		List<Tutorship> students = new ArrayList<Tutorship>();
		for (Tutorship tutor : this.studentsList) {
			students.add(tutor);
		}
		return students;
	}

	public void setStudentsList(List<Tutorship> students) {
		this.studentsList = new ArrayList<Tutorship>();
		for (Tutorship tutor : students) {
			this.studentsList.add(tutor);
		}
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public Integer getStudentNumber() {
		return studentNumber;
	}

	public void setStudentNumber(Integer studentNumber) {
		this.studentNumber = studentNumber;
	}

	public Teacher getTeacher() {
		return (teacher);
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}

	public Integer getNumberOfCurrentTutorships() {
		return numberOfCurrentTutorships;
	}

	public void setNumberOfCurrentTutorships(Integer numberOfCurrentTutorships) {
		this.numberOfCurrentTutorships = numberOfCurrentTutorships;
	}

	public Integer getNumberOfPastTutorships() {
		return numberOfPastTutorships;
	}

	public void setNumberOfPastTutorships(Integer numberOfPastTutorships) {
		this.numberOfPastTutorships = numberOfPastTutorships;
	}

	public Month getTutorshipEndMonth() {
		return tutorshipEndMonth;
	}

	public void setTutorshipEndMonth(Month tutorshipEndMonth) {
		this.tutorshipEndMonth = tutorshipEndMonth;
	}

	public Integer getTutorshipEndYear() {
		return tutorshipEndYear;
	}

	public void setTutorshipEndYear(Integer tutorshipEndYear) {
		this.tutorshipEndYear = tutorshipEndYear;
	}

	private void setDefaultFields() {
		setTutorshipEndMonth(Month.SEPTEMBER);
		setTutorshipEndYear(Tutorship.getLastPossibleTutorshipYear());
	}
}
