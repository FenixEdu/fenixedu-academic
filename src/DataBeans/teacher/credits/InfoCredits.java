package DataBeans.teacher.credits;

import DataBeans.InfoExecutionPeriod;
import DataBeans.InfoObject;
import DataBeans.InfoTeacher;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoCredits extends InfoObject
{
    private Double degreeFinalProjectStudents;
    private InfoExecutionPeriod infoExecutionPeriod;
    private InfoTeacher infoTeacher;
    private Double institutionWorkTime;
    private Double lessons;
    private Double masterDegreeCredits;
    private Double otherTypeCredits;
    private Double supportLessons;
    private Boolean containsManagementPositions;
    private Boolean containsServiceExemptionsSituations;
    
    /**
     * @param double1
     * @return
     */
    private String format(Double number)
    {
        if (number == null || number.doubleValue() == 0)
        {
            return "0";
        }
        return number.toString();
    }
    /**
     * @return Returns the degreeFinalProjectStudents.
     */
    public Double getDegreeFinalProjectStudents()
    {
        return degreeFinalProjectStudents;
    }
    public String getDegreeFinalProjectStudentsFormatted()
    {
        return format(this.getDegreeFinalProjectStudents());
    }

    /**
     * @return Returns the infoExecutionPeriod.
     */
    public InfoExecutionPeriod getInfoExecutionPeriod()
    {
        return infoExecutionPeriod;
    }

    /**
     * @return Returns the infoTeacher.
     */
    public InfoTeacher getInfoTeacher()
    {
        return infoTeacher;
    }

    /**
     * @return Returns the institutionWorkTime.
     */
    public Double getInstitutionWorkTime()
    {
        return institutionWorkTime;
    }
    public String getInstitutionWorkTimeFormatted()
    {
        return format(this.getInstitutionWorkTime());
    }

    /**
     * @return Returns the lessons.
     */
    public Double getLessons()
    {
        return lessons;
    }
    public String getLessonsFormatted()
    {
        return format(this.getLessons());
    }

    /**
     * @return Returns the otherTypeCredits.
     */
    public Double getOtherTypeCredits()
    {
        return otherTypeCredits;
    }
    
    public String getOtherTypeCreditsFormatted(){
        return format(this.getOtherTypeCredits());
    }

    /**
     * @return Returns the supportLessons.
     */
    public Double getSupportLessons()
    {
        return supportLessons;
    }

    public String getSupportLessonsFormatted()
    {
        return format(this.getSupportLessons());
    }

    /**
     * @param degreeFinalProjectStudents
     *            The degreeFinalProjectStudents to set.
     */
    public void setDegreeFinalProjectStudents(Double degreeFinalProjectStudents)
    {
        this.degreeFinalProjectStudents = degreeFinalProjectStudents;
    }

    /**
     * @param infoExecutionPeriod
     *            The infoExecutionPeriod to set.
     */
    public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod)
    {
        this.infoExecutionPeriod = infoExecutionPeriod;
    }

    /**
     * @param infoTeacher
     *            The infoTeacher to set.
     */
    public void setInfoTeacher(InfoTeacher infoTeacher)
    {
        this.infoTeacher = infoTeacher;
    }

    /**
     * @param institutionWorkTime
     *            The institutionWorkTime to set.
     */
    public void setInstitutionWorkTime(Double institutionWorkTime)
    {
        this.institutionWorkTime = institutionWorkTime;
    }

    /**
     * @param lessons
     *            The lessons to set.
     */
    public void setLessons(Double lessons)
    {
        this.lessons = lessons;
    }

    /**
     * @param otherTypeCredits The otherTypeCredits to set.
     */
    public void setOtherTypeCredits(Double otherTypeCredits)
    {
        this.otherTypeCredits = otherTypeCredits;
    }

    /**
     * @param supportLessons
     *            The supportLessons to set.
     */
    public void setSupportLessons(Double supportLessons)
    {
        this.supportLessons = supportLessons;
    }

    /**
     * @return Returns the containsServiceExemptionsSituations.
     */
    public Boolean getContainsServiceExemptionsSituations()
    {
        return containsServiceExemptionsSituations;
    }

    /**
     * @param containsServiceExemptionsSituations The containsServiceExemptionsSituations to set.
     */
    public void setContainsServiceExemptionsSituations(Boolean containsServiceExemptionsSituations)
    {
        this.containsServiceExemptionsSituations = containsServiceExemptionsSituations;
    }

    public Boolean getContainsManagementPositions()
    {
        return containsManagementPositions;
    }

    public void setContainsManagementPositions(Boolean containsManagementPositions)
    {
        this.containsManagementPositions = containsManagementPositions;
    }

    /**
     * @return Returns the masterDegreeCredits.
     */
    public Double getMasterDegreeCredits() {
        return masterDegreeCredits;
    }
    /**
     * @param masterDegreeCredits The masterDegreeCredits to set.
     */
    public void setMasterDegreeCredits(Double masterDegreeCredits) {
        this.masterDegreeCredits = masterDegreeCredits;
    }

    public String getMasterDegreeCreditsFormatted()
    {
        return format(this.getMasterDegreeCredits());
    }
    
}
