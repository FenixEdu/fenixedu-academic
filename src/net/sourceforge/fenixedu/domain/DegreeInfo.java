package net.sourceforge.fenixedu.domain;

/**
 * @author Tania Pousao Created on 30/Out/2003
 */
public class DegreeInfo extends DegreeInfo_Base {

    protected DegreeInfo(Degree degree, ExecutionYear executionYear) {
	super();
	setRootDomainObject(RootDomainObject.getInstance());

	setDegree(degree);
	setExecutionYear(executionYear);
    }

    protected DegreeInfo(DegreeInfo degreeInfo, ExecutionYear executionYear) {
	this(degreeInfo.getDegree(), executionYear);

	setDescription(degreeInfo.getDescription());
	setHistory(degreeInfo.getHistory());
	setObjectives(degreeInfo.getObjectives());
	setDesignedFor(degreeInfo.getDesignedFor());
	setProfessionalExits(degreeInfo.getProfessionalExits());
	setOperationalRegime(degreeInfo.getOperationalRegime());
	setGratuity(degreeInfo.getGratuity());
	setAdditionalInfo(degreeInfo.getAdditionalInfo());
	setLinks(degreeInfo.getLinks());

	setTestIngression(degreeInfo.getTestIngression());
	setClassifications(degreeInfo.getClassifications());
	setAccessRequisites(degreeInfo.getAccessRequisites());
	setCandidacyDocuments(degreeInfo.getCandidacyDocuments());
	setDriftsInitial(degreeInfo.getDriftsInitial());
	setDriftsFirst(degreeInfo.getDriftsFirst());
	setDriftsSecond(degreeInfo.getDriftsSecond());
	setMarkMin(degreeInfo.getMarkMin());
	setMarkMax(degreeInfo.getMarkMax());
	setMarkAverage(degreeInfo.getMarkAverage());

	setQualificationLevel(degreeInfo.getQualificationLevel());
	setRecognitions(degreeInfo.getRecognitions());
	/*
	 * setDescriptionEn(degreeInfo.getDescriptionEn());
	 * setHistoryEn(degreeInfo.getHistoryEn());
	 * setObjectivesEn(degreeInfo.getObjectivesEn());
	 * setProfessionalExitsEn(degreeInfo.getProfessionalExitsEn());
	 * setAdditionalInfoEn(degreeInfo.getAdditionalInfoEn());
	 * setLinksEn(degreeInfo.getLinksEn());
	 * 
	 * setTestIngressionEn(degreeInfo.getTestIngressionEn());
	 * setClassificationsEn(degreeInfo.getClassificationsEn());
	 * 
	 * setQualificationLevelEn(degreeInfo.getQualificationLevelEn());
	 * setRecognitionsEn(degreeInfo.getRecognitionsEn());
	 */
    }

    public void delete() {
	removeRootDomainObject();
	removeDegree();
	removeExecutionYear();
	deleteDomainObject();
    }
}
