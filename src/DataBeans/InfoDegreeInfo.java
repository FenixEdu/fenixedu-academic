package DataBeans;

import java.sql.Timestamp;

/**
 * @author Tânia Pousão
 * Created on 30/Out/2003
 */
public class InfoDegreeInfo extends InfoObject implements ISiteComponent{	
	private InfoDegree infoDegree;

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
		result += " objectivos= " + getObjectives();
		result += " historial= " + getHistory();
		result += " saidas profissionais=" + getProfessionalExits();
		result += " informação adicional= " + getAdditionalInfo();
		result += " links= " + getLinks();
		result += " provas de ingresso= " + getTestIngression();
		result += " vagas iniciais= " + getDriftsInitial();
		result += " vagas 1ª fase= " + getDriftsFirst();
		result += " vagas 2ªfase= " + getDriftsSecond();
		result += " classificações= " + getClassifications(); 
		result += " nota minima= " + getMarkMin();
		result += " nota máxima= " + getMarkMax();
		result += " nota média= " + getMarkAverage();
		result += " data última modificação= " + getLastModificationDate();
		result += "]";
		return result;
	}
}
