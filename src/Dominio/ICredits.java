/*
 * Created on 27/Mai/2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package Dominio;

/**
 * @author Alexandra Alves
 */
public interface ICredits extends IDomainObject {

	Boolean getContainsManagementPositions();

	Boolean getContainsServiceExemptionsSituations();

	/**
	 * @return Returns the degreeFinalProjectStudents.
	 */
	public abstract Double getDegreeFinalProjectStudents();

	public abstract IExecutionPeriod getExecutionPeriod();

	/**
	 * @return Returns the institutionWorkTime.
	 */
	public abstract Double getInstitutionWorkTime();

	/**
	 * @return Returns the lessons.
	 */
	public abstract Double getLessons();

	/**
	 * @return Returns the masterDegreeCredits.
	 */
	public Double getMasterDegreeCredits();

	Double getOtherTypeCredits();

	/**
	 * @return Returns the supportLessons.
	 */
	public abstract Double getSupportLessons();

	public abstract ITeacher getTeacher();

	void setContainsManagementPositions(Boolean inSabattical);

	void setContainsServiceExemptionsSituations(
			Boolean containsServiceExemptionsSituations);

	/**
	 * @param degreeFinalProjectStudents
	 *            The degreeFinalProjectStudents to set.
	 */
	public abstract void setDegreeFinalProjectStudents(
			Double degreeFinalProjectStudents);

	void setExecutionPeriod(IExecutionPeriod executionPeriod);

	/**
	 * @param institutionWorkTime
	 *            The institutionWorkTime to set.
	 */
	public abstract void setInstitutionWorkTime(Double institutionWorkTime);

	/**
	 * @param lessons
	 *            The lessons to set.
	 */
	public abstract void setLessons(Double lessons);

	/**
	 * @param masterDegreeCredits
	 *            The masterDegreeCredits to set.
	 */
	public void setMasterDegreeCredits(Double masterDegreeCredits);

	void setOtherTypeCredits(Double credits);

	/**
	 * @param supportLessons
	 *            The supportLessons to set.
	 */
	public abstract void setSupportLessons(Double supportLessons);

	void setTeacher(ITeacher teacher);
}