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
public interface ICredits extends IDomainObject
{

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
     * @return Returns the supportLessons.
     */
    public abstract Double getSupportLessons();

    public abstract ITeacher getTeacher();

    /**
     * @param degreeFinalProjectStudents
     *            The degreeFinalProjectStudents to set.
     */
    public abstract void setDegreeFinalProjectStudents(Double degreeFinalProjectStudents);

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
     * @param supportLessons
     *            The supportLessons to set.
     */
    public abstract void setSupportLessons(Double supportLessons);

    void setTeacher(ITeacher teacher);

    void setOtherTypeCredits(Double credits);
    Double getOtherTypeCredits();

    Boolean getContainsServiceExemptionsSituations();
    void setContainsServiceExemptionsSituations(Boolean containsServiceExemptionsSituations);
    Boolean getContainsManagementPositions();
    void setContainsManagementPositions(Boolean inSabattical);

}