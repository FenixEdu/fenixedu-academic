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
public class SummaryPTEReportLine implements Serializable, ISummaryPTEReportLine {

    private String projectCode;

    private Double revenue;

    private Double expense;

    private Double tax;

    private Double total;

    @Override
    public Double getExpense() {
        return expense;
    }

    @Override
    public void setExpense(Double expense) {
        this.expense = expense;
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
    public Double getRevenue() {
        return revenue;
    }

    @Override
    public void setRevenue(Double revenue) {
        this.revenue = revenue;
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
}
