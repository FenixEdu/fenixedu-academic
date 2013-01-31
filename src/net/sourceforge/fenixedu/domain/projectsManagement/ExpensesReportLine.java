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

	private String projectCode;

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
	public String getDocumentNumber() {
		return documentNumber;
	}

	@Override
	public void setDocumentNumber(String documentNumber) {
		this.documentNumber = documentNumber;
	}

	@Override
	public String getDocumentType() {
		return documentType;
	}

	@Override
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	@Override
	public String getFinancingSource() {
		return financingSource;
	}

	@Override
	public void setFinancingSource(String financingSource) {
		this.financingSource = financingSource;
	}

	@Override
	public Double getImputedPercentage() {
		return imputedPercentage;
	}

	@Override
	public void setImputedPercentage(Double imputedPercentage) {
		this.imputedPercentage = imputedPercentage;
	}

	@Override
	public Double getIvaPercentage() {
		return ivaPercentage;
	}

	@Override
	public void setIvaPercentage(Double ivaPercentage) {
		this.ivaPercentage = ivaPercentage;
	}

	@Override
	public String getMember() {
		return member;
	}

	@Override
	public void setMember(String member) {
		this.member = member;
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
	public String getMovementType() {
		return movementType;
	}

	@Override
	public void setMovementType(String movementType) {
		this.movementType = movementType;
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
	public String getSupplier() {
		return supplier;
	}

	@Override
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	@Override
	public String getSupplierDescription() {
		return supplierDescription;
	}

	@Override
	public void setSupplierDescription(String supplierDescription) {
		this.supplierDescription = supplierDescription;
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
	public Double getTotal() {
		return total;
	}

	@Override
	public void setTotal(Double total) {
		this.total = total;
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
