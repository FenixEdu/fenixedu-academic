package Dominio;

import java.io.Serializable;

/**
 * @author Fernanda & Tânia
 *
 */
public interface ICreditsTeacher extends Serializable {
	IProfessorship getProfessorShip();
	void setProfessorShip(IProfessorship professorShip);
	 
	ITurno getShift();
	void setShift(ITurno shift);
	
	public Double getPercentage();
	public void setPercentage(Double percentage);
}
