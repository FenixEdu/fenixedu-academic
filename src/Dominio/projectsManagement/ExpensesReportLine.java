/*
 * Created on Jan 12, 2005
 *
 */
package Dominio.projectsManagement;

import java.io.Serializable;

/**
 * @author Susana Fernandes
 * 
 */
public class ExpensesReportLine implements Serializable, IExpensesReportLine {

    private Integer projectCode;

    private String movementId;

    private String member;

    private Integer rubric;

    private String type;

    private String date;

    private String description;

    private Double value;

    private Double tax;

    private Double total;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getMovementId() {
        return movementId;
    }

    public void setMovementId(String movementId) {
        this.movementId = movementId;
    }

    public Integer getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(Integer projectCode) {
        this.projectCode = projectCode;
    }

    public Integer getRubric() {
        return rubric;
    }

    public void setRubric(Integer rubric) {
        this.rubric = rubric;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
