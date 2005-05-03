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
public class ExpensesReportLine implements Serializable, IExpensesReportLine {

    private Integer projectCode;

    private String movementId;

    private String member;

    private String supplier;

    private String supplierDescription;

    private String documentType;

    private String documentNumber;

    private String financingSource;

    private Integer rubric;

    private String movementType;

    private Double ivaPercentage;

    private String date;

    private String description;

    private Double value;

    private Double tax;

    private Double total;

    private Double imputedPercentage;

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

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getFinancingSource() {
        return financingSource;
    }

    public void setFinancingSource(String financingSource) {
        this.financingSource = financingSource;
    }

    public Double getImputedPercentage() {
        return imputedPercentage;
    }

    public void setImputedPercentage(Double imputedPercentage) {
        this.imputedPercentage = imputedPercentage;
    }

    public Double getIvaPercentage() {
        return ivaPercentage;
    }

    public void setIvaPercentage(Double ivaPercentage) {
        this.ivaPercentage = ivaPercentage;
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

    public String getMovementType() {
        return movementType;
    }

    public void setMovementType(String movementType) {
        this.movementType = movementType;
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

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getSupplierDescription() {
        return supplierDescription;
    }

    public void setSupplierDescription(String supplierDescription) {
        this.supplierDescription = supplierDescription;
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

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

}
