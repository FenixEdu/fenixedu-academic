package Dominio;

/**
 * @author Fernanda & Tânia
 *  
 */
public interface IShiftProfessorship extends IDomainObject {
    IProfessorship getProfessorship();

    void setProfessorship(IProfessorship professorship);

    ITurno getShift();

    void setShift(ITurno shift);

    public Double getPercentage();

    public void setPercentage(Double percentage);
}