package net.sourceforge.fenixedu.dataTransferObject.credits;

/**
 * @author Tânia Pousão
 *  
 */
public class InfoCreditsWrapper extends InfoCredits {
    private InfoCredits infoCredits;

    public InfoCreditsWrapper(InfoCredits infoCredits) {
        this.infoCredits = infoCredits;
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.credits.InfoCredits#getContainsManagementPositions()
     */
    public Boolean getContainsManagementPositions() {
        return infoCredits.getContainsManagementPositions();
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.credits.InfoCredits#getContainsServiceExemptionsSituations()
     */
    public Boolean getContainsServiceExemptionsSituations() {
        return infoCredits.getContainsServiceExemptionsSituations();
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.credits.InfoCredits#getDegreeFinalProjectStudents()
     */
    public Double getDegreeFinalProjectStudents() {
        return infoCredits.getDegreeFinalProjectStudents();
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.credits.InfoCredits#getDegreeFinalProjectStudentsFormatted()
     */
    public String getDegreeFinalProjectStudentsFormatted() {
        return infoCredits.getDegreeFinalProjectStudentsFormatted();
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.credits.InfoCredits#getInstitutionWorkTime()
     */
    public Double getInstitutionWorkTime() {
        return infoCredits.getInstitutionWorkTime();
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.credits.InfoCredits#getInstitutionWorkTimeFormatted()
     */
    public String getInstitutionWorkTimeFormatted() {
        return infoCredits.getInstitutionWorkTimeFormatted();
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.credits.InfoCredits#getLessons()
     */
    public Double getLessons() {
        return infoCredits.getLessons();
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.credits.InfoCredits#getLessonsFormatted()
     */
    public String getLessonsFormatted() {
        return infoCredits.getLessonsFormatted();
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.credits.InfoCredits#getMasterDegreeCredits()
     */
    public Double getMasterDegreeCredits() {
        return infoCredits.getMasterDegreeCredits();
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.credits.InfoCredits#getMasterDegreeCreditsFormatted()
     */
    public String getMasterDegreeCreditsFormatted() {
        return infoCredits.getMasterDegreeCreditsFormatted();
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.credits.InfoCredits#getOtherTypeCredits()
     */
    public Double getOtherTypeCredits() {
        return infoCredits.getOtherTypeCredits();
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.credits.InfoCredits#getOtherTypeCreditsFormatted()
     */
    public String getOtherTypeCreditsFormatted() {
        return infoCredits.getOtherTypeCreditsFormatted();
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.credits.InfoCredits#getSupportLessons()
     */
    public Double getSupportLessons() {
        return infoCredits.getSupportLessons();
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.credits.InfoCredits#getSupportLessonsFormatted()
     */
    public String getSupportLessonsFormatted() {
        return infoCredits.getSupportLessonsFormatted();
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.credits.InfoCredits#setContainsManagementPositions(java.lang.Boolean)
     */
    public void setContainsManagementPositions(Boolean containsManagementPositions) {
        infoCredits.setContainsManagementPositions(containsManagementPositions);
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.credits.InfoCredits#setContainsServiceExemptionsSituations(java.lang.Boolean)
     */
    public void setContainsServiceExemptionsSituations(Boolean containsServiceExemptionsSituations) {
        infoCredits.setContainsServiceExemptionsSituations(containsServiceExemptionsSituations);
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.credits.InfoCredits#setDegreeFinalProjectStudents(java.lang.Double)
     */
    public void setDegreeFinalProjectStudents(Double degreeFinalProjectStudents) {
        infoCredits.setDegreeFinalProjectStudents(degreeFinalProjectStudents);
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.credits.InfoCredits#setInstitutionWorkTime(java.lang.Double)
     */
    public void setInstitutionWorkTime(Double institutionWorkTime) {
        infoCredits.setInstitutionWorkTime(institutionWorkTime);
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.credits.InfoCredits#setLessons(java.lang.Double)
     */
    public void setLessons(Double lessons) {
        infoCredits.setLessons(lessons);
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.credits.InfoCredits#setMasterDegreeCredits(java.lang.Double)
     */
    public void setMasterDegreeCredits(Double masterDegreeCredits) {
        infoCredits.setMasterDegreeCredits(masterDegreeCredits);
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.credits.InfoCredits#setOtherTypeCredits(java.lang.Double)
     */
    public void setOtherTypeCredits(Double otherTypeCredits) {
        infoCredits.setOtherTypeCredits(otherTypeCredits);
    }

    /*
     * (non-Javadoc)
     * 
     * @see DataBeans.credits.InfoCredits#setSupportLessons(java.lang.Double)
     */
    public void setSupportLessons(Double supportLessons) {
        infoCredits.setSupportLessons(supportLessons);
    }
}