/*
 * Created on Jan 12, 2005
 *
 */
package DataBeans.projectsManagement;

import org.apache.poi.hssf.usermodel.HSSFRow;

import DataBeans.DataTranferObject;
import Dominio.projectsManagement.ISummaryEURReportLine;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoSummaryEURReportLine extends DataTranferObject implements IReportLine {

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

    public void copyFromDomain(ISummaryEURReportLine summaryEURReportLine) {
        if (summaryEURReportLine != null) {
            setProjectCode(summaryEURReportLine.getProjectCode());
            setExpense(summaryEURReportLine.getExpense());
            setRevenue(summaryEURReportLine.getRevenue());
            setTax(summaryEURReportLine.getTax());
            setAdiantamentosPorJustificar(summaryEURReportLine.getAdiantamentosPorJustificar());
            setTotal(summaryEURReportLine.getTotal());
        }
    }

    public static InfoSummaryEURReportLine newInfoFromDomain(ISummaryEURReportLine summaryEURReportLine) {
        InfoSummaryEURReportLine infoSummaryEURReportLine = null;
        if (summaryEURReportLine != null) {
            infoSummaryEURReportLine = new InfoSummaryEURReportLine();
            infoSummaryEURReportLine.copyFromDomain(summaryEURReportLine);
        }
        return infoSummaryEURReportLine;
    }

    public Double getValue(int column) {
        return null;
    }

    public HSSFRow getHeaderToExcel(HSSFRow row) {
        return null;
    }

    public HSSFRow getLineToExcel(HSSFRow row) {
        return null;
    }

    public HSSFRow getTotalLineToExcel(HSSFRow row) {
        return null;
    }

}
