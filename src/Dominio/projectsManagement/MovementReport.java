/*
 * Created on Jan 12, 2005
 *
 */
package Dominio.projectsManagement;

import java.io.Serializable;
import java.util.List;

/**
 * @author Susana Fernandes
 * 
 */
public class MovementReport implements Serializable, IMovementReport {
    private String parentMovementId;

    private Integer parentProjectCode;

    private Integer parentRubricId;

    private String parentType;

    private String parentDate;

    private String parentDescription;

    private Double parentValue;

    private List movements;

    public String getParentDate() {
        return parentDate;
    }

    public void setParentDate(String parentDate) {
        this.parentDate = parentDate;
    }

    public String getParentDescription() {
        return parentDescription;
    }

    public void setParentDescription(String parentDescription) {
        this.parentDescription = parentDescription;
    }

    public String getParentMovementId() {
        return parentMovementId;
    }

    public void setParentMovementId(String parentMovementId) {
        this.parentMovementId = parentMovementId;
    }

    public Integer getParentProjectCode() {
        return parentProjectCode;
    }

    public void setParentProjectCode(Integer parentProjectCode) {
        this.parentProjectCode = parentProjectCode;
    }

    public Integer getParentRubricId() {
        return parentRubricId;
    }

    public void setParentRubricId(Integer parentRubricId) {
        this.parentRubricId = parentRubricId;
    }

    public String getParentType() {
        return parentType;
    }

    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    public Double getParentValue() {
        return parentValue;
    }

    public void setParentValue(Double parentValue) {
        this.parentValue = parentValue;
    }

    public List getMovements() {
        return movements;
    }

    public void setMovements(List movements) {
        this.movements = movements;
    }
}
