package DataBeans;

import java.sql.Timestamp;

import Dominio.IDegreeInfo;

/**
 * @author Tânia Pousão
 * Created on 30/Out/2003
 */
public class InfoDegreeInfo extends InfoObject implements ISiteComponent{	
	private InfoDegree infoDegree;

	private String description;
	private String objectives;
	private String history;
	private String professionalExits;
	private String additionalInfo;
	private String links;
	private String testIngression;
	private Integer driftsInitial;
	private Integer driftsFirst;
	private Integer driftsSecond;
	private String classifications;
	private Double markMin;
	private Double markMax;
	private Double markAverage;
	private Timestamp lastModificationDate;

	//Data in english
	private String descriptionEn;
	private String objectivesEn;
	private String historyEn;
	private String professionalExitsEn;
	private String additionalInfoEn;
	private String linksEn;
	private String testIngressionEn;
	private String classificationsEn;
	
	public InfoDegreeInfo() {
	} 

	public InfoDegreeInfo(
		InfoDegree infoDegree,
		String objectives,
		String history,
		String professionalExits,
		String additionalInfo,
		String links,
		String testIngression,
		Integer driftsInitial,
		Integer driftsFirst,
		Integer driftsSecond,
		String classifications,
		Double markMin,
		Double markMax,
		Double markAverage,
		Timestamp lastModificationDate) {
		this.infoDegree = infoDegree;
		this.objectives = objectives;
		this.history = history;
		this.professionalExits = professionalExits;
		this.additionalInfo = additionalInfo;
		this.links = links;
		this.testIngression = testIngression;
		this.driftsInitial = driftsInitial;
		this.driftsFirst = driftsFirst;
		this.driftsSecond = driftsSecond;
		this.classifications = classifications;
		this.markMin = markMin;
		this.markMax = markMax;
		this.markAverage = markAverage;
		this.lastModificationDate = lastModificationDate;
	} 

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public String getClassifications() {
		return classifications;
	}

	public void setClassifications(String classifications) {
		this.classifications = classifications;
	}

	public Integer getDriftsFirst() {
		return driftsFirst;
	}

	public void setDriftsFirst(Integer driftsFirst) {
		this.driftsFirst = driftsFirst;
	}

	public Integer getDriftsInitial() {
		return driftsInitial;
	}

	public void setDriftsInitial(Integer driftsInitial) {
		this.driftsInitial = driftsInitial;
	}

	public Integer getDriftsSecond() {
		return driftsSecond;
	}

	public void setDriftsSecond(Integer driftsSecond) {
		this.driftsSecond = driftsSecond;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}

	public InfoDegree getInfoDegree() {
		return infoDegree;
	}

	public void setInfoDegree(InfoDegree infoDegree) {
		this.infoDegree = infoDegree;
	}

	public Timestamp getLastModificationDate() {
		return lastModificationDate;
	}

	public void setLastModificationDate(Timestamp lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}

	public String getLinks() {
		return links;
	}

	public void setLinks(String links) {
		this.links = links;
	}

	public Double getMarkAverage() {
		return markAverage;
	}

	public void setMarkAverage(Double markAverage) {
		this.markAverage = markAverage;
	}

	public Double getMarkMax() {
		return markMax;
	}

	public void setMarkMax(Double markMax) {
		this.markMax = markMax;
	}

	public Double getMarkMin() {
		return markMin;
	}

	public void setMarkMin(Double markMin) {
		this.markMin = markMin;
	}

	public String getObjectives() {
		return objectives;
	}

	public void setObjectives(String objectives) {
		this.objectives = objectives;
	}

	public String getProfessionalExits() {
		return professionalExits;
	}

	public void setProfessionalExits(String professionalExits) {
		this.professionalExits = professionalExits;
	}

	public String getTestIngression() {
		return testIngression;
	}

	public void setTestIngression(String testIngression) {
		this.testIngression = testIngression;
	}

	public String getAdditionalInfoEn() {
		return additionalInfoEn;
	}

	public void setAdditionalInfoEn(String additionalInfoEn) {
		this.additionalInfoEn = additionalInfoEn;
	}

	public String getClassificationsEn() {
		return classificationsEn;
	}

	public void setClassificationsEn(String classificationsEn) {
		this.classificationsEn = classificationsEn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescriptionEn() {
		return descriptionEn;
	}

	public void setDescriptionEn(String descriptionEn) {
		this.descriptionEn = descriptionEn;
	}
	
	public String getHistoryEn() {
		return historyEn;
	}

	public void setHistoryEn(String historyEn) {
		this.historyEn = historyEn;
	}

	public String getLinksEn() {
		return linksEn;
	}

	public void setLinksEn(String linksEn) {
		this.linksEn = linksEn;
	}

	public String getObjectivesEn() {
		return objectivesEn;
	}

	public void setObjectivesEn(String objectivesEn) {
		this.objectivesEn = objectivesEn;
	}

	public String getProfessionalExitsEn() {
		return professionalExitsEn;
	}

	public void setProfessionalExitsEn(String professionalExitsEn) {
		this.professionalExitsEn = professionalExitsEn;
	}

	public String getTestIngressionEn() {
		return testIngressionEn;
	}

	public void setTestIngressionEn(String testIngressionEn) {
		this.testIngressionEn = testIngressionEn;
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoDegreeInfo) {
			InfoDegreeInfo infoDegreeInfo = (InfoDegreeInfo) obj;
			result =
				getInfoDegree().equals(infoDegreeInfo.getInfoDegree()) ;
		}
		return result;
	}
	
	public String toString() {
		String result = "[INFODEGREE_INFO:";
		result += " codigo interno= " + getIdInternal();
		result += " degree= " + getInfoDegree();
		result += " descrição= " + getDescription();
		result += " objectivos= " + getObjectives();
		result += " historial= " + getHistory();
		result += " saidas profissionais=" + getProfessionalExits();
		result += " informação adicional= " + getAdditionalInfo();
		result += " links= " + getLinks();
		result += " provas de ingresso= " + getTestIngression();
		result += " classificações= " + getClassifications();		
		result += " descrição(En)= " + getDescriptionEn();
		result += " objectivos(En)= " + getObjectivesEn();
		result += " historial(En)= " + getHistoryEn();
		result += " saidas profissionais(En)=" + getProfessionalExitsEn();
		result += " informação adicional(En)= " + getAdditionalInfoEn();
		result += " links(En)= " + getLinksEn();
		result += " provas de ingresso(En)= " + getTestIngressionEn();
		result += " classificações(En)= " + getClassificationsEn();			
		result += " vagas iniciais= " + getDriftsInitial();
		result += " vagas 1ª fase= " + getDriftsFirst();
		result += " vagas 2ªfase= " + getDriftsSecond(); 
		result += " nota minima= " + getMarkMin();
		result += " nota máxima= " + getMarkMax();
		result += " nota média= " + getMarkAverage();
		result += " data última modificação= " + getLastModificationDate();
		result += "]";
		return result;
	}
	
	
	//Once copyProperties in Cloner transform a null Integer in Zero Integer 
	//This method recapture all null values
	public void recaptureNULLs(IDegreeInfo degreeInfo) {
	    if(degreeInfo.getDriftsInitial() == null) {
	        this.setDriftsInitial(null);
	    }
		if(degreeInfo.getDriftsFirst() == null) {
			this.setDriftsFirst(null);
		}
		if(degreeInfo.getDriftsSecond() == null) {
			this.setDriftsSecond(null);
		}

		if(degreeInfo.getMarkAverage() == null) {
			this.setMarkAverage(null);
		}
		if(degreeInfo.getMarkMax() == null) {
			this.setMarkMax(null);
		}
		if(degreeInfo.getMarkMin() == null) {
			this.setMarkMin(null);
		}
	}
}
