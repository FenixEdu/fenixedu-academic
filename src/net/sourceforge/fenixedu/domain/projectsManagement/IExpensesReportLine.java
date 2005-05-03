package net.sourceforge.fenixedu.domain.projectsManagement;

import java.io.Serializable;

public interface IExpensesReportLine extends Serializable {

    public abstract String getDate();

    public abstract void setDate(String date);

    public abstract String getDescription();

    public abstract void setDescription(String description);

    public abstract String getDocumentNumber();

    public abstract void setDocumentNumber(String documentNumber);

    public abstract String getDocumentType();

    public abstract void setDocumentType(String documentType);

    public abstract String getFinancingSource();

    public abstract void setFinancingSource(String financingSource);

    public abstract Double getImputedPercentage();

    public abstract void setImputedPercentage(Double imputedPercentage);

    public abstract Double getIvaPercentage();

    public abstract void setIvaPercentage(Double ivaPercentage);

    public abstract String getMember();

    public abstract void setMember(String member);

    public abstract String getMovementId();

    public abstract void setMovementId(String movementId);

    public abstract String getMovementType();

    public abstract void setMovementType(String movementType);

    public abstract Integer getProjectCode();

    public abstract void setProjectCode(Integer projectCode);

    public abstract Integer getRubric();

    public abstract void setRubric(Integer rubric);

    public abstract String getSupplier();

    public abstract void setSupplier(String supplier);

    public abstract String getSupplierDescription();

    public abstract void setSupplierDescription(String supplierDescription);

    public abstract Double getTax();

    public abstract void setTax(Double tax);

    public abstract Double getTotal();

    public abstract void setTotal(Double total);

    public abstract Double getValue();

    public abstract void setValue(Double value);

}