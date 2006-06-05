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
        setObjectives(degreeInfo.getObjectives());
        setHistory(degreeInfo.getHistory());
        setProfessionalExits(degreeInfo.getProfessionalExits());
        setAdditionalInfo(degreeInfo.getAdditionalInfo());
        setLinks(degreeInfo.getLinks());
        
        setDescriptionEn(degreeInfo.getDescriptionEn());
        setObjectivesEn(degreeInfo.getObjectivesEn());
        setHistoryEn(degreeInfo.getHistoryEn());
        setProfessionalExitsEn(degreeInfo.getProfessionalExitsEn());
        setAdditionalInfoEn(degreeInfo.getAdditionalInfoEn());
        setLinksEn(degreeInfo.getLinksEn());
        
        setTestIngression(degreeInfo.getTestIngression());
        setClassifications(degreeInfo.getClassifications());
        
        setTestIngressionEn(degreeInfo.getTestIngressionEn());
        setClassificationsEn(degreeInfo.getClassificationsEn());
        
        setDriftsInitial(degreeInfo.getDriftsInitial());
        setDriftsFirst(degreeInfo.getDriftsFirst());
        setDriftsSecond(degreeInfo.getDriftsSecond());
        setMarkMin(degreeInfo.getMarkMin());
        setMarkMax(degreeInfo.getMarkMax());
        setMarkAverage(degreeInfo.getMarkAverage());
        
        setQualificationLevel(degreeInfo.getQualificationLevel());
        setRecognitions(degreeInfo.getRecognitions());

        setQualificationLevelEn(degreeInfo.getQualificationLevelEn());
        setRecognitionsEn(degreeInfo.getRecognitionsEn());
    }

    public void delete() {
        removeRootDomainObject();
        
        removeDegree();
        removeExecutionYear();
        
		deleteDomainObject();
	}

}
