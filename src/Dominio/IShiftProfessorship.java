package Dominio;

/**
 * @author Fernanda & Tânia
 *  
 */
public interface IShiftProfessorship extends IDomainObject {
    IProfessorship getProfessorship();

    void setProfessorship(IProfessorship professorship);

    IShift getShift();

    void setShift(IShift shift);

    public Double getPercentage();

    public void setPercentage(Double percentage);
}