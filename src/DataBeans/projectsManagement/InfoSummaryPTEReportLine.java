/*
 * Created on Jan 12, 2005
 *
 */
package DataBeans.projectsManagement;

import org.apache.poi.hssf.usermodel.HSSFSheet;

import DataBeans.DataTranferObject;
import Dominio.projectsManagement.ISummaryPTEReportLine;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoSummaryPTEReportLine extends DataTranferObject implements IReportLine {

    private Integer projectCode;

    private Double revenue;

    private Double expense;

    private Double tax;

    private Double total;

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

    public void copyFromDomain(ISummaryPTEReportLine summaryPTEReportLine) {
        if (summaryPTEReportLine != null) {
            setProjectCode(summaryPTEReportLine.getProjectCode());
            setExpense(summaryPTEReportLine.getExpense());
            setRevenue(summaryPTEReportLine.getRevenue());
            setTax(summaryPTEReportLine.getTax());
            setTotal(summaryPTEReportLine.getTotal());
        }
    }

    public static InfoSummaryPTEReportLine newInfoFromDomain(ISummaryPTEReportLine summaryPTEReportLine) {
        InfoSummaryPTEReportLine infoSummaryPTEReportLine = null;
        if (summaryPTEReportLine != null) {
            infoSummaryPTEReportLine = new InfoSummaryPTEReportLine();
            infoSummaryPTEReportLine.copyFromDomain(summaryPTEReportLine);
        }
        return infoSummaryPTEReportLine;
    }

    public Double getValue(int column) {
        return null;
    }

    public void getHeaderToExcel(HSSFSheet sheet) {
    }

    public void getLineToExcel(HSSFSheet sheet) {
    }

    public void getTotalLineToExcel(HSSFSheet sheet) {
    }

    public int getNumberOfColumns() {
        return 0;
    }

}
