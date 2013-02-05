/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.domain.projectsManagement;

import java.io.Serializable;
import java.util.List;

/**
 * @author Susana Fernandes
 * 
 */
public class MovementReport implements Serializable, IMovementReport {
    private String parentMovementId;

    private String parentProjectCode;

    private Integer parentRubricId;

    private String parentType;

    private String parentDate;

    private String parentDescription;

    private Double parentValue;

    private List movements;

    @Override
    public String getParentDate() {
        return parentDate;
    }

    @Override
    public void setParentDate(String parentDate) {
        this.parentDate = parentDate;
    }

    @Override
    public String getParentDescription() {
        return parentDescription;
    }

    @Override
    public void setParentDescription(String parentDescription) {
        this.parentDescription = parentDescription;
    }

    @Override
    public String getParentMovementId() {
        return parentMovementId;
    }

    @Override
    public void setParentMovementId(String parentMovementId) {
        this.parentMovementId = parentMovementId;
    }

    @Override
    public String getParentProjectCode() {
        return parentProjectCode;
    }

    @Override
    public void setParentProjectCode(String parentProjectCode) {
        this.parentProjectCode = parentProjectCode;
    }

    @Override
    public Integer getParentRubricId() {
        return parentRubricId;
    }

    @Override
    public void setParentRubricId(Integer parentRubricId) {
        this.parentRubricId = parentRubricId;
    }

    @Override
    public String getParentType() {
        return parentType;
    }

    @Override
    public void setParentType(String parentType) {
        this.parentType = parentType;
    }

    @Override
    public Double getParentValue() {
        return parentValue;
    }

    @Override
    public void setParentValue(Double parentValue) {
        this.parentValue = parentValue;
    }

    @Override
    public List getMovements() {
        return movements;
    }

    @Override
    public void setMovements(List movements) {
        this.movements = movements;
    }
}
