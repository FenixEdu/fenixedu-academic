/*
 * Created on Jul 9, 2004
 *
 */
package DataBeans.enrollment;

import java.io.Serializable;
import java.util.List;

import DataBeans.DataTranferObject;

/**
 * @author João Mota
 *
 */
public class InfoAreas2Choose extends DataTranferObject implements Serializable {
    
    private List finalSpecializationAreas;
    private List finalSecundaryAreas;

    /**
     * @return Returns the finalSecundaryAreas.
     */
    public List getFinalSecundaryAreas() {
        return finalSecundaryAreas;
    }
    /**
     * @param finalSecundaryAreas The finalSecundaryAreas to set.
     */
    public void setFinalSecundaryAreas(List finalSecundaryAreas) {
        this.finalSecundaryAreas = finalSecundaryAreas;
    }
    /**
     * @return Returns the finalSpecializationAreas.
     */
    public List getFinalSpecializationAreas() {
        return finalSpecializationAreas;
    }
    /**
     * @param finalSpecializationAreas The finalSpecializationAreas to set.
     */
    public void setFinalSpecializationAreas(List finalSpecializationAreas) {
        this.finalSpecializationAreas = finalSpecializationAreas;
    }
}
