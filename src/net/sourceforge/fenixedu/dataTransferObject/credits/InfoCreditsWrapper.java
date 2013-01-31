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
	 * @seenet.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits#
	 * getContainsManagementPositions()
	 */
	@Override
	public Boolean getContainsManagementPositions() {
		return infoCredits.getContainsManagementPositions();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenet.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits#
	 * getContainsServiceExemptionsSituations()
	 */
	@Override
	public Boolean getContainsServiceExemptionsSituations() {
		return infoCredits.getContainsServiceExemptionsSituations();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenet.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits#
	 * getDegreeFinalProjectStudents()
	 */
	@Override
	public Double getDegreeFinalProjectStudents() {
		return infoCredits.getDegreeFinalProjectStudents();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenet.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits#
	 * getDegreeFinalProjectStudentsFormatted()
	 */
	@Override
	public String getDegreeFinalProjectStudentsFormatted() {
		return infoCredits.getDegreeFinalProjectStudentsFormatted();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenet.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits#
	 * getInstitutionWorkTime()
	 */
	@Override
	public Double getInstitutionWorkTime() {
		return infoCredits.getInstitutionWorkTime();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenet.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits#
	 * getInstitutionWorkTimeFormatted()
	 */
	@Override
	public String getInstitutionWorkTimeFormatted() {
		return infoCredits.getInstitutionWorkTimeFormatted();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits#getLessons
	 * ()
	 */
	@Override
	public Double getLessons() {
		return infoCredits.getLessons();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenet.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits#
	 * getLessonsFormatted()
	 */
	@Override
	public String getLessonsFormatted() {
		return infoCredits.getLessonsFormatted();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenet.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits#
	 * getMasterDegreeCredits()
	 */
	@Override
	public Double getMasterDegreeCredits() {
		return infoCredits.getMasterDegreeCredits();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenet.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits#
	 * getMasterDegreeCreditsFormatted()
	 */
	@Override
	public String getMasterDegreeCreditsFormatted() {
		return infoCredits.getMasterDegreeCreditsFormatted();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenet.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits#
	 * getOtherTypeCredits()
	 */
	@Override
	public Double getOtherTypeCredits() {
		return infoCredits.getOtherTypeCredits();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenet.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits#
	 * getOtherTypeCreditsFormatted()
	 */
	@Override
	public String getOtherTypeCreditsFormatted() {
		return infoCredits.getOtherTypeCreditsFormatted();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenet.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits#
	 * getSupportLessons()
	 */
	@Override
	public Double getSupportLessons() {
		return infoCredits.getSupportLessons();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenet.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits#
	 * getSupportLessonsFormatted()
	 */
	@Override
	public String getSupportLessonsFormatted() {
		return infoCredits.getSupportLessonsFormatted();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenet.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits#
	 * setContainsManagementPositions(java.lang.Boolean)
	 */
	@Override
	public void setContainsManagementPositions(Boolean containsManagementPositions) {
		infoCredits.setContainsManagementPositions(containsManagementPositions);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenet.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits#
	 * setContainsServiceExemptionsSituations(java.lang.Boolean)
	 */
	@Override
	public void setContainsServiceExemptionsSituations(Boolean containsServiceExemptionsSituations) {
		infoCredits.setContainsServiceExemptionsSituations(containsServiceExemptionsSituations);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenet.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits#
	 * setDegreeFinalProjectStudents(java.lang.Double)
	 */
	@Override
	public void setDegreeFinalProjectStudents(Double degreeFinalProjectStudents) {
		infoCredits.setDegreeFinalProjectStudents(degreeFinalProjectStudents);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenet.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits#
	 * setInstitutionWorkTime(java.lang.Double)
	 */
	@Override
	public void setInstitutionWorkTime(Double institutionWorkTime) {
		infoCredits.setInstitutionWorkTime(institutionWorkTime);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits#setLessons
	 * (java.lang.Double)
	 */
	@Override
	public void setLessons(Double lessons) {
		infoCredits.setLessons(lessons);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenet.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits#
	 * setMasterDegreeCredits(java.lang.Double)
	 */
	@Override
	public void setMasterDegreeCredits(Double masterDegreeCredits) {
		infoCredits.setMasterDegreeCredits(masterDegreeCredits);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenet.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits#
	 * setOtherTypeCredits(java.lang.Double)
	 */
	@Override
	public void setOtherTypeCredits(Double otherTypeCredits) {
		infoCredits.setOtherTypeCredits(otherTypeCredits);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenet.sourceforge.fenixedu.dataTransferObject.credits.InfoCredits#
	 * setSupportLessons(java.lang.Double)
	 */
	@Override
	public void setSupportLessons(Double supportLessons) {
		infoCredits.setSupportLessons(supportLessons);
	}
}