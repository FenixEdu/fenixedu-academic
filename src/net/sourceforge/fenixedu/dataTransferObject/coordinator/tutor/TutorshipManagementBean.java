package net.sourceforge.fenixedu.dataTransferObject.coordinator.tutor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Teacher;
import net.sourceforge.fenixedu.domain.Tutorship;
import net.sourceforge.fenixedu.util.Month;

public class TutorshipManagementBean implements Serializable {
    private Integer teacherNumber;

    private DomainReference<Teacher> teacher;

    private Integer executionDegreeID;

    private Integer degreeCurricularPlanID;

    private Month tutorshipEndMonth;

    private Integer tutorshipEndYear;

    private Integer studentNumber;

    private Integer numberOfPastTutorships;

    private Integer numberOfCurrentTutorships;

    private List<DomainReference<Tutorship>> studentsList;

    private TutorshipManagementBean() {
	this.studentsList = new ArrayList<DomainReference<Tutorship>>();
	this.setDefaultFields();
    }

    public TutorshipManagementBean(Integer executionDegreeID, Integer degreeCurricularPlanID) {
	this();
	this.executionDegreeID = executionDegreeID;
	this.degreeCurricularPlanID = degreeCurricularPlanID;
    }

    public TutorshipManagementBean(Integer executionDegreeID, Integer degreeCurricularPlanID, Integer teacherNumber) {
	this();
	this.executionDegreeID = executionDegreeID;
	this.degreeCurricularPlanID = degreeCurricularPlanID;
	this.teacherNumber = teacherNumber;
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
	for (DomainReference<Tutorship> tutor : this.studentsList) {
	    students.add(tutor.getObject());
	}
	return students;
    }

    public void setStudentsList(List<Tutorship> students) {
	this.studentsList = new ArrayList<DomainReference<Tutorship>>();
	for (Tutorship tutor : students) {
	    this.studentsList.add(new DomainReference<Tutorship>(tutor));
	}
    }

    public Integer getTeacherNumber() {
	return teacherNumber;
    }

    public void setTeacherNumber(Integer teacherNumber) {
	this.teacherNumber = teacherNumber;
    }

    public Integer getStudentNumber() {
	return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
	this.studentNumber = studentNumber;
    }

    public Teacher getTeacher() {
	return (teacher == null ? null : teacher.getObject());
    }

    public void setTeacher(Teacher teacher) {
	this.teacher = new DomainReference<Teacher>(teacher);
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
