/*
 * Created on Jan 20, 2005
 *
 */
package Dominio.projectsManagement;

import java.io.Serializable;

/**
 * @author Susana Fernandes
 * 
 */
public interface IRubric extends Serializable {
    public abstract String getCode();

    public abstract void setCode(String code);

    public abstract String getDescription();

    public abstract void setDescription(String description);
}