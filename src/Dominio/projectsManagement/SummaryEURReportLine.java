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
public class SummaryEURReportLine implements Serializable, ISummaryEURReportLine {

    private Integer projectCode;

    private Double revenue;

    private Double expense;

    private Double tax;

    private Double adiantamentosPorJustificar;

    private Double total;

    public Double getAdiantamentosPorJustificar() {
        return adiantamentosPorJustificar;
    }

    public void setAdiantamentosPorJustificar(Double adiantamentosPorJustificar) {
        this.adiantamentosPorJustificar = adiantamentosPorJustificar;
    }

    public Double getExpense() {
        return expense;
    }

    public void setExpense(Double expense) {
        this.expense = expense;
    }

    public Integer getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(Integer projectCode) {
        this.projectCode = projectCode;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
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
}
