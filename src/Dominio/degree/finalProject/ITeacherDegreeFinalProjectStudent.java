/*
 * Created on Nov 24, 2003 by jpvl
 *  
 */
package Dominio.degree.finalProject;

import Dominio.IDomainObject;
import Dominio.IExecutionPeriod;
import Dominio.IStudent;
import Dominio.ITeacher;

/**
 * @author jpvl
 */
public interface ITeacherDegreeFinalProjectStudent extends IDomainObject {

    public abstract IExecutionPeriod getExecutionPeriod();

    public Double getPercentage();

    /**
     * @return Returns the student.
     */
    public abstract IStudent getStudent();

    /**
     * @return Returns the teacher.
     */
    public abstract ITeacher getTeacher();

    /**
     * @param executionPeriod
     *            The executionPeriod to set.
     */
    public abstract void setExecutionPeriod(IExecutionPeriod executionPeriod);

    public void setPercentage(Double percentage);

    /**
     * @param student
     *            The student to set.
     */
    public abstract void setStudent(IStudent student);

    /**
     * @param teacher
     *            The teacher to set.
     */
    public abstract void setTeacher(ITeacher teacher);

}