/*
 * Created on Jan 12, 2005
 *
 */
package DataBeans.projectsManagement;

import DataBeans.DataTranferObject;
import Dominio.projectsManagement.IRubric;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoRubric extends DataTranferObject {
    String code;

    String description;

    public InfoRubric() {
    }

    public InfoRubric(String code, String description) {
        setCode(code);
        setDescription(description);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void copyFromDomain(IRubric rubric) {
        if (rubric != null) {
            setCode(rubric.getCode());
            setDescription(rubric.getDescription());
        }
    }

    public static InfoRubric newInfoFromDomain(IRubric rubric) {
        InfoRubric infoRubric = null;
        if (rubric != null) {
            infoRubric = new InfoRubric();
            infoRubric.copyFromDomain(rubric);
        }
        return infoRubric;
    }
}
