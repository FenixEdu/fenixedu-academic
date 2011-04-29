package net.sourceforge.fenixedu.dataTransferObject.teacher.tutor;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Teacher;

public class TutorBean implements Serializable {
    private Teacher teacher;

    private String teacherId;

    private String teacherName;

    private Integer executionDegreeID;

    private Integer degreeCurricularPlanID;

    private TutorBean() {
    }

    public TutorBean(Integer executionDegreeID, Integer degreeCurricularPlanID, Teacher teacher) {
	this();
	setExecutionDegreeID(executionDegreeID);
	setDegreeCurricularPlanID(degreeCurricularPlanID);
	setTeacher(teacher);
	setTeacherId(teacher.getPerson().getIstUsername());
	setTeacherName(teacher.getPerson().getName());
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

    public Teacher getTeacher() {
	return (teacher);
    }

    public void setTeacher(Teacher teacher) {
	this.teacher = teacher;
    }

    public String getTeacherNumber() {
	return teacherId;
    }

    public void setTeacherId(String string) {
	this.teacherId = string;
    }

    public String getTeacherId () {
	return teacherName;
    }

    public void setTeacherName(String teacherName) {
	this.teacherName = teacherName;
    }
}
