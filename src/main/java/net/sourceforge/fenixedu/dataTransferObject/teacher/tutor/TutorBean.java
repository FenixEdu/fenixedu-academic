package net.sourceforge.fenixedu.dataTransferObject.teacher.tutor;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.Teacher;

public class TutorBean implements Serializable {
    private Teacher teacher;

    private String teacherId;

    private String teacherName;

    private String executionDegreeID;

    private String degreeCurricularPlanID;

    private TutorBean() {
    }

    public TutorBean(String executionDegreeID, String degreeCurricularPlanID, Teacher teacher) {
        this();
        setExecutionDegreeID(executionDegreeID);
        setDegreeCurricularPlanID(degreeCurricularPlanID);
        setTeacher(teacher);
        setTeacherId(teacher.getPerson().getIstUsername());
        setTeacherName(teacher.getPerson().getName());
    }

    public String getDegreeCurricularPlanID() {
        return degreeCurricularPlanID;
    }

    public void setDegreeCurricularPlanID(String degreeCurricularPlanID) {
        this.degreeCurricularPlanID = degreeCurricularPlanID;
    }

    public String getExecutionDegreeID() {
        return executionDegreeID;
    }

    public void setExecutionDegreeID(String executionDegreeID) {
        this.executionDegreeID = executionDegreeID;
    }

    public Teacher getTeacher() {
        return (teacher);
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String string) {
        this.teacherId = string;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
