/*
 * Created on Nov 24, 2003 by jpvl
 *
 */
package Dominio.degree.finalProject;

import Dominio.IDomainObject;
import Dominio.IExecutionYear;
import Dominio.IStudent;
import Dominio.ITeacher;

/**
 * @author jpvl
 */
public interface ITeacherDegreeFinalProjectStudent extends IDomainObject
{
	public void setPercentage(Double percentage);
	public Double getPercentage();
  
    /**
     * @return Returns the executionYear.
     */
    public abstract IExecutionYear getExecutionYear();

    /**
     * @return Returns the student.
     */
    public abstract IStudent getStudent();
    /**
     * @return Returns the teacher.
     */
    public abstract ITeacher getTeacher();
    /**
     * @param executionYear
     *                   The executionYear to set.
     */
    public abstract void setExecutionYear(IExecutionYear executionYear);
    /**
     * @param student
     *                   The student to set.
     */
    public abstract void setStudent(IStudent student);
    /**
     * @param teacher
     *                   The teacher to set.
     */
    public abstract void setTeacher(ITeacher teacher);
}