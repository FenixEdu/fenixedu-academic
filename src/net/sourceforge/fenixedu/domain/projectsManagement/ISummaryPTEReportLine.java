/*
 * Created on Jan 28, 2005
 *
 */
package net.sourceforge.fenixedu.domain.projectsManagement;

/**
 * @author Susana Fernandes
 *
 */
public interface ISummaryPTEReportLine {
    public abstract Double getExpense();

    public abstract void setExpense(Double expense);

    public abstract Integer getProjectCode();

    public abstract void setProjectCode(Integer projectCode);

    public abstract Double getRevenue();

    public abstract void setRevenue(Double revenue);

    public abstract Double getTax();

    public abstract void setTax(Double tax);

    public abstract Double getTotal();

    public abstract void setTotal(Double total);
}