package net.sourceforge.fenixedu.dataTransferObject.teacher.tutor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Teacher;

public class TutorTutorshipsHistoryBean implements Serializable {

    private DomainReference<Teacher> teacher;

    private List<StudentsByTutorBean> activeTutorshipsByEntryYear;

    private List<StudentsByTutorBean> pastTutorshipsByEntryYear;

    private Integer numberOfCurrentTutorships;

    private Integer numberOfPastTutorships;

    public TutorTutorshipsHistoryBean(Teacher teacher) {
	setTeacher(teacher);
	this.activeTutorshipsByEntryYear = new ArrayList<StudentsByTutorBean>();
	this.pastTutorshipsByEntryYear = new ArrayList<StudentsByTutorBean>();
    }

    public List<StudentsByTutorBean> getActiveTutorshipsByEntryYear() {
	return this.activeTutorshipsByEntryYear;
    }

    public void setActiveTutorshipsByEntryYear(List<StudentsByTutorBean> tutorshipsByEntryYear) {
	this.activeTutorshipsByEntryYear = tutorshipsByEntryYear;
	setNumberOfCurrentTutorships();
    }

    public List<StudentsByTutorBean> getPastTutorshipsByEntryYear() {
	return this.pastTutorshipsByEntryYear;
    }

    public void setPastTutorshipsByEntryYear(List<StudentsByTutorBean> tutorshipsByEntryYear) {
	this.pastTutorshipsByEntryYear = tutorshipsByEntryYear;
	setNumberOfPastTutorships();
    }

    public Teacher getTeacher() {
	return (teacher == null ? null : teacher.getObject());
    }

    public void setTeacher(Teacher teacher) {
	this.teacher = new DomainReference<Teacher>(teacher);
    }

    public Integer getNumberOfCurrentTutorships() {
	return this.numberOfCurrentTutorships;
    }

    private void setNumberOfCurrentTutorships() {
	Integer numberOfCurrentTutorships = 0;
	for (StudentsByTutorBean studentsByEntryYear : this.activeTutorshipsByEntryYear) {
	    numberOfCurrentTutorships += studentsByEntryYear.getStudentsList().size();
	}
	this.numberOfCurrentTutorships = numberOfCurrentTutorships;
    }

    public Integer getNumberOfPastTutorships() {
	return this.numberOfPastTutorships;
    }

    private void setNumberOfPastTutorships() {
	Integer numberOfPastTutorships = 0;
	for (StudentsByTutorBean studentsByEntryYear : this.pastTutorshipsByEntryYear) {
	    numberOfPastTutorships += studentsByEntryYear.getStudentsList().size();
	}
	this.numberOfPastTutorships = numberOfPastTutorships;
    }

    public Integer getNumberOfTutorships() {
	return this.numberOfCurrentTutorships + this.numberOfPastTutorships;
    }
}
