/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.domain.projectsManagement.IRubric;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoRubric extends DataTranferObject {
    String code;

    String description;

    Double value;

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

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public void copyFromDomain(IRubric rubric) {
        if (rubric != null) {
            setCode(rubric.getCode());
            setDescription(rubric.getDescription());
            setValue(rubric.getValue());
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
