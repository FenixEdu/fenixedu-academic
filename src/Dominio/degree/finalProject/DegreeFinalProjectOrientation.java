/*
 * Created on Nov 11, 2003 by jpvl
 *  
 */
package Dominio.degree.finalProject;

import Dominio.DomainObject;
import Dominio.ITeacher;

/**
 * @author jpvl
 */
public class DegreeFinalProjectOrientation extends DomainObject implements IDegreeFinalProjectOrientation
{
	private Integer keyTeacher;
	private ITeacher teacher;
	private Double percentage;
	private Integer keyDegreeFinalProject;
	private IDegreeFinalProject degreeFinalProject;
	private Boolean coOrientation;

	public DegreeFinalProjectOrientation()
	{}
	
	/**
	 * @return Returns the degreeFinalProject.
	 */
	public IDegreeFinalProject getDegreeFinalProject()
	{
		return this.degreeFinalProject;
	}

	/**
	 * @param degreeFinalProject
	 *                   The degreeFinalProject to set.
	 */
	public void setDegreeFinalProject(IDegreeFinalProject degreeFinalProject)
	{
		this.degreeFinalProject = degreeFinalProject;
	}

	/**
	 * @return Returns the keyDegreeFinalProject.
	 */
	public Integer getKeyDegreeFinalProject()
	{
		return this.keyDegreeFinalProject;
	}

	/**
	 * @param keyDegreeFinalProject
	 *                   The keyDegreeFinalProject to set.
	 */
	public void setKeyDegreeFinalProject(Integer keyDegreeFinalProject)
	{
		this.keyDegreeFinalProject = keyDegreeFinalProject;
	}

	/**
	 * @return Returns the keyTeacher.
	 */
	public Integer getKeyTeacher()
	{
		return this.keyTeacher;
	}

	/**
	 * @param keyTeacher
	 *                   The keyTeacher to set.
	 */
	public void setKeyTeacher(Integer keyTeacher)
	{
		this.keyTeacher = keyTeacher;
	}

	/**
	 * @return Returns the percentage.
	 */
	public Double getPercentage()
	{
		return this.percentage;
	}

	/**
	 * @param percentage
	 *                   The percentage to set.
	 */
	public void setPercentage(Double percentage)
	{
		this.percentage = percentage;
	}

	/**
	 * @return Returns the teacher.
	 */
	public ITeacher getTeacher()
	{
		return this.teacher;
	}

	/**
	 * @param teacher
	 *                   The teacher to set.
	 */
	public void setTeacher(ITeacher teacher)
	{
		this.teacher = teacher;
	}

	/**
	 * @return Returns the coOrientation.
	 */
	public Boolean getCoOrientation()
	{
		return this.coOrientation;
	}

	/**
	 * @param coOrientation The coOrientation to set.
	 */
	public void setCoOrientation(Boolean coOrientation)
	{
		this.coOrientation = coOrientation;
	}

}
