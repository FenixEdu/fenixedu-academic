package Dominio;


/**
 * @author Fernanda & Tânia
 *
 */
public interface ITeacherShiftPercentage extends IDomainObject {
	IProfessorship getProfessorShip();
	void setProfessorShip(IProfessorship professorShip);
	 
	ITurno getShift();
	void setShift(ITurno shift);
	
	public Double getPercentage();
	public void setPercentage(Double percentage);
}
