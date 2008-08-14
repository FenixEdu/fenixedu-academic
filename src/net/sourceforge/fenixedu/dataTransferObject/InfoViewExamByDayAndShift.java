/*
 * InfoViewExamByDayAndShift.java
 *
 * Created on 2003/03/24
 */

package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */

public class InfoViewExamByDayAndShift extends InfoObject {

    protected InfoExam infoExam;
    protected List infoExecutionCourses;
    protected List infoDegrees;
    protected Integer numberStudentesAttendingCourse;
    protected Integer availableRoomOccupation;

    public InfoViewExamByDayAndShift() {
    }

    public InfoViewExamByDayAndShift(InfoExam infoExam, List infoExecutionCourses, List infoDegrees,
	    Integer numberStudentesAttendingCourse, Integer availableRoomOccupation) {
	this.setInfoExam(infoExam);
	this.setInfoExecutionCourses(infoExecutionCourses);
	this.setInfoDegrees(infoDegrees);
	this.setNumberStudentesAttendingCourse(numberStudentesAttendingCourse);
	this.setAvailableRoomOccupation(availableRoomOccupation);
    }

    public boolean equals(Object obj) {
	if (obj instanceof InfoViewExamByDayAndShift) {
	    InfoViewExamByDayAndShift examObj = (InfoViewExamByDayAndShift) obj;
	    return this.getInfoExam().getSeason().equals(examObj.getInfoExam().getSeason());
	}

	return false;
    }

    public String toString() {
	return "[INFOVIEWEXAMBYDAYANDSHIFT:" + " exam= '" + this.getInfoExam() + "'" + "]";
    }

    public InfoExam getInfoExam() {
	return infoExam;
    }

    public void setInfoExam(InfoExam exam) {
	infoExam = exam;
    }

    public List getInfoDegrees() {
	return infoDegrees;
    }

    public void setInfoDegrees(List list) {
	infoDegrees = list;
    }

    public Integer getNumberStudentesAttendingCourse() {
	return numberStudentesAttendingCourse;
    }

    public void setNumberStudentesAttendingCourse(Integer integer) {
	numberStudentesAttendingCourse = integer;
    }

    public List getInfoExecutionCourses() {
	return infoExecutionCourses;
    }

    public void setInfoExecutionCourses(List list) {
	infoExecutionCourses = list;
    }

    public Integer getAvailableRoomOccupation() {
	return availableRoomOccupation;
    }

    public void setAvailableRoomOccupation(Integer availableRoomOccupation) {
	this.availableRoomOccupation = availableRoomOccupation;
    }

}