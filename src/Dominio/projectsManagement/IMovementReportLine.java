/*
 * Created on Feb 14, 2005
 *
 */
package Dominio.projectsManagement;

/**
 * @author Susana Fernandes
 * 
 */
public interface IMovementReportLine {
    public abstract String getDate();

    public abstract void setDate(String date);

    public abstract String getDescription();

    public abstract void setDescription(String description);

    public abstract String getMovementId();

    public abstract void setMovementId(String movementId);

    public abstract Integer getRubricId();

    public abstract void setRubricId(Integer rubricId);

    public abstract Double getTax();

    public abstract void setTax(Double tax);

    public abstract String getType();

    public abstract void setType(String type);

    public abstract Double getValue();

    public abstract void setValue(Double value);

}