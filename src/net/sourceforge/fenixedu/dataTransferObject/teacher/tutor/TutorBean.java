package net.sourceforge.fenixedu.dataTransferObject.teacher.tutor;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Teacher;

public class TutorBean implements Serializable {
    private DomainReference<Teacher> teacher;

    private Integer teacherNumber;

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
	setTeacherNumber(teacher.getTeacherNumber());
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
	return (teacher == null ? null : teacher.getObject());
    }

    public void setTeacher(Teacher teacher) {
	this.teacher = new DomainReference<Teacher>(teacher);
    }

    public Integer getTeacherNumber() {
	return teacherNumber;
    }

    public void setTeacherNumber(Integer teacherNumber) {
	this.teacherNumber = teacherNumber;
    }

    public String getTeacherName() {
	return teacherName;
    }

    public void setTeacherName(String teacherName) {
	this.teacherName = teacherName;
    }
}
