/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package Dominio.degree.finalProject;

import Dominio.DomainObject;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.ITeacher;

/**
 * @author jpvl
 */
public class TeacherDegreeFinalProjectStudent
    extends DomainObject
    implements ITeacherDegreeFinalProjectStudent
{
    private IExecutionPeriod executionPeriod;
    private Integer keyExecutionPeriod;
    private Integer keyStudent;
    private Integer keyTeacher;
    private Double percentage;
    private IStudent student;
    private ITeacher teacher;

    public TeacherDegreeFinalProjectStudent()
    {
    }

    /**
     * @param teacherDegreeFinalProjectStudentId
     */
    public TeacherDegreeFinalProjectStudent(Integer teacherDegreeFinalProjectStudentId)
    {
        super(teacherDegreeFinalProjectStudentId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        if (obj instanceof TeacherDegreeFinalProjectStudent)
        {
            TeacherDegreeFinalProjectStudent teacherDegreeFinalProjectStudent =
                (TeacherDegreeFinalProjectStudent) obj;

            return (
                this.getStudent().equals(teacherDegreeFinalProjectStudent.getStudent().getIdInternal())
                    && this.getTeacher().equals(teacherDegreeFinalProjectStudent.getTeacher())
                    && this.getExecutionPeriod().equals(
                        teacherDegreeFinalProjectStudent.getExecutionPeriod()));

        }
        return false;
    }
    /**
     * @return Returns the executionPeriod.
     */
    public IExecutionPeriod getExecutionPeriod()
    {
        return executionPeriod;
    }

    /**
     * @return Returns the keyExecutionYear.
     */
    public Integer getKeyExecutionPeriod()
    {
        return this.keyExecutionPeriod;
    }

    /**
     * @return Returns the keyStudent.
     */
    public Integer getKeyStudent()
    {
        return this.keyStudent;
    }

    /**
     * @return Returns the keyTeacher.
     */
    public Integer getKeyTeacher()
    {
        return this.keyTeacher;
    }

    /**
     * @return Returns the percentage.
     */
    public Double getPercentage()
    {
        return this.percentage;
    }

    /**
     * @return Returns the student.
     */
    public IStudent getStudent()
    {
        return this.student;
    }

    /**
     * @return Returns the teacher.
     */
    public ITeacher getTeacher()
    {
        return this.teacher;
    }

    /**
     * @param executionPeriod
     *            The executionPeriod to set.
     */
    public void setExecutionPeriod(IExecutionPeriod executionPeriod)
    {
        this.executionPeriod = executionPeriod;
    }

    /**
     * @param keyExecutionYear
     *            The keyExecutionYear to set.
     */
    public void setKeyExecutionPeriod(Integer keyExecutionYear)
    {
        this.keyExecutionPeriod = keyExecutionYear;
    }

    /**
     * @param keyStudent
     *            The keyStudent to set.
     */
    public void setKeyStudent(Integer keyStudent)
    {
        this.keyStudent = keyStudent;
    }

    /**
     * @param keyTeacher
     *            The keyTeacher to set.
     */
    public void setKeyTeacher(Integer keyTeacher)
    {
        this.keyTeacher = keyTeacher;
    }
    /**
     * @param percentage
     *            The percentage to set.
     */
    public void setPercentage(Double percentage)
    {
        this.percentage = percentage;
    }

    /**
     * @param student
     *            The student to set.
     */
    public void setStudent(IStudent student)
    {
        this.student = student;
    }

    /**
     * @param teacher
     *            The teacher to set.
     */
    public void setTeacher(ITeacher teacher)
    {
        this.teacher = teacher;
    }

}