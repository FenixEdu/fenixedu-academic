/*
 * Created on Nov 11, 2003 by jpvl
 *
 */
package Dominio.degree.finalProject;

import Dominio.IDomainObject;
import Dominio.ITeacher;

/**
 * @author jpvl
 */
public interface IDegreeFinalProjectOrientation extends IDomainObject
{
	/**
	 * @return Returns the degreeFinalProject.
	 */
	public abstract IDegreeFinalProject getDegreeFinalProject();
	/**
	 * @param degreeFinalProject
	 *                   The degreeFinalProject to set.
	 */
	public abstract void setDegreeFinalProject(IDegreeFinalProject degreeFinalProject);
	/**
	 * @return Returns the percentage.
	 */
	public abstract Double getPercentage();
	/**
	 * @param percentage
	 *                   The percentage to set.
	 */
	public abstract void setPercentage(Double percentage);
	/**
	 * @return Returns the teacher.
	 */
	public abstract ITeacher getTeacher();
	/**
	 * @param teacher
	 *                   The teacher to set.
	 */
	public abstract void setTeacher(ITeacher teacher);
	/**
	 * @return Returns the coOrientation.
	 */
	public abstract Boolean getCoOrientation();
	/**
	 * @param coOrientation The coOrientation to set.
	 */
	public abstract void setCoOrientation(Boolean coOrientation);
}