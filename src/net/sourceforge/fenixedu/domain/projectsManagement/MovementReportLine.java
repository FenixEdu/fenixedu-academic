/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.domain.projectsManagement;

import java.io.Serializable;

/**
 * @author Susana Fernandes
 * 
 */
public class MovementReportLine implements Serializable, IMovementReportLine {
    private String movementId;

    private Integer rubricId;

    private String type;

    private String date;

    private String description;

    private Double value;

    private Double tax;

    @Override
    public String getDate() {
        return date;
    }

    @Override
    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getMovementId() {
        return movementId;
    }

    @Override
    public void setMovementId(String movementId) {
        this.movementId = movementId;
    }

    @Override
    public Integer getRubricId() {
        return rubricId;
    }

    @Override
    public void setRubricId(Integer rubricId) {
        this.rubricId = rubricId;
    }

    @Override
    public Double getTax() {
        return tax;
    }

    @Override
    public void setTax(Double tax) {
        this.tax = tax;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public Double getValue() {
        return value;
    }

    @Override
    public void setValue(Double value) {
        this.value = value;
    }

}
