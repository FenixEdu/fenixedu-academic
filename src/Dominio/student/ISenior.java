/*
 * Created on Dec 10, 2004
 *
 */
package Dominio.student;

import java.util.Date;
import Dominio.IDomainObject;
import Dominio.IStudent;

/**
 * @author Luis Egidio, luis.egidio@ist.utl.pt
 *
 */
public interface ISenior extends IDomainObject {
   
	public IStudent getStudent();
   	public void setStudent(IStudent student);
	
	public Date getExpectedDegreeTermination();
	public void setExpectedDegreeTermination(Date expectedDegreeTermination);
	
	public Integer getExpectedDegreeAverageGrade();
	public void setExpectedDegreeAverageGrade(Integer expectedDegreeAverageGrade);
	
	public String getSpecialtyField();
	public void setSpecialtyField(String specialtyField);
	
	public String getProfessionalInterests();
	public void setProfessionalInterests(String professionalInterests);
	
	public String getLanguageSkills();
	public void setLanguageSkills(String languageSkills);
	
	public String getInformaticsSkills();
	public void setInformaticsSkills(String informaticsSkills);
	
	public String getExtracurricularActivities();
	public void setExtracurricularActivities(String extracurricularActivities);
	
	public String getProfessionalExperience();
	public void setProfessionalExperience(String professionalExperience);

	public Date getLastModificationDate();
	public void setLastModificationDate(Date lastModificationDate);
}
