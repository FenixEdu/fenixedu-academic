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
public class RevenueReportLine implements Serializable, IRevenueReportLine {

    private String projectCode;

    private String movementId;

    private String financialEntity;

    private Integer rubric;

    private String date;

    private String description;

    private Double value;

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
    public String getFinancialEntity() {
        return financialEntity;
    }

    @Override
    public void setFinancialEntity(String financialEntity) {
        this.financialEntity = financialEntity;
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
    public String getProjectCode() {
        return projectCode;
    }

    @Override
    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    @Override
    public Integer getRubric() {
        return rubric;
    }

    @Override
    public void setRubric(Integer rubric) {
        this.rubric = rubric;
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
