/*
 * Created on 2/Fev/2004
 *
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author Tânia Pousão
 *  
 */
public class Tutor extends DomainObject implements ITutor {
    private Integer teacherKey;

    private ITeacher teacher;

    private Integer studentKey;

    private IStudent student;

    public Tutor() {
    }

    /**
     * @param idInternal
     */
    public Tutor(Integer idInternal) {
        setIdInternal(idInternal);
    }

    /**
     * @return Returns the student.
     */
    public IStudent getStudent() {
        return student;
    }

    /**
     * @param student
     *            The student to set.
     */
    public void setStudent(IStudent student) {
        this.student = student;
    }

    /**
     * @return Returns the studentKey.
     */
    public Integer getStudentKey() {
        return studentKey;
    }

    /**
     * @param studentKey
     *            The studentKey to set.
     */
    public void setStudentKey(Integer studentKey) {
        this.studentKey = studentKey;
    }

    /**
     * @return Returns the teacher.
     */
    public ITeacher getTeacher() {
        return teacher;
    }

    /**
     * @param teacher
     *            The teacher to set.
     */
    public void setTeacher(ITeacher teacher) {
        this.teacher = teacher;
    }

    /**
     * @return Returns the teacherKey.
     */
    public Integer getTeacherKey() {
        return teacherKey;
    }

    /**
     * @param teacherKey
     *            The teacherKey to set.
     */
    public void setTeacherKey(Integer teacherKey) {
        this.teacherKey = teacherKey;
    }

}