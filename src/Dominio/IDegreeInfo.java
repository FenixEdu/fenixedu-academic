package Dominio;

import java.sql.Timestamp;

/**
 * @author Tânia Pousão
 * Created on 30/Out/2003
 */
public interface IDegreeInfo extends IDomainObject {

	public ICurso getDegree();
	public String getObjectives();
	public String getHistory();
	public String getProfessionalExits();
	public String getAdditionalInfo();
	public String getLinks();
	public String getTestIngression();
	public Integer getDriftsInitial();
	public Integer getDriftsFirst();
	public Integer getDriftsSecond();
	public String getClassifications();
	public Float getMarkMin();
	public Float getMarkMax();
	public Float getMarkAverage();
	public Timestamp getLastModificationDate();

	public void setDegree(ICurso degree);
	public void setObjectives(String objectives);
	public void setHistory(String history);
	public void setProfessionalExits(String professionalExits);
	public void setAdditionalInfo(String additionalInfo);
	public void setLinks(String links);
	public void setTestIngression(String testIngression);
	public void setDriftsInitial(Integer driftsInitial);
	public void setDriftsFirst(Integer driftsFirst);
	public void setDriftsSecond(Integer driftsSecond);
	public void setClassifications(String classifications);
	public void setMarkMin(Float markMin);
	public void setMarkMax(Float markMax);
	public void setMarkAverage(Float markAverage);
	public void setLastModificationDate(Timestamp lastModificationDate);		
}
