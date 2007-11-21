/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import net.sourceforge.fenixedu.domain.projectsManagement.ISummaryEURReportLine;
import net.sourceforge.fenixedu.util.projectsManagement.ExcelStyle;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.Region;

import pt.utl.ist.fenix.tools.util.StringAppender;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoSummaryEURReportLine extends InfoReportLine {

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

    public void getLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
        int nextRow = sheet.getLastRowNum() + 2;
        HSSFRow row = sheet.createRow(nextRow);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue(StringAppender.append(getString("link.revenue"), " ", getString("label.eur"), ":"));
        //sheet.addMergedRegion(new Region((short) row.getRowNum(), (short) 0, (short) row.getRowNum(), (short) 2));
        cell.setCellStyle(excelStyle.getLabelStyle());
        cell = row.createCell((short) 3);
        cell.setCellValue(getRevenue().doubleValue());
        if (getRevenue().doubleValue() < 0)
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        else
            cell.setCellStyle(excelStyle.getDoubleStyle());
        nextRow++;
        row = sheet.createRow(nextRow);
        cell = row.createCell((short) 0);
        cell.setCellValue(StringAppender.append(getString("link.expenses"), " ", getString("label.eur"), ":"));
        //sheet.addMergedRegion(new Region((short) row.getRowNum(), (short) 0, (short) row.getRowNum(), (short) 2));
        cell.setCellStyle(excelStyle.getLabelStyle());
        cell = row.createCell((short) 3);
        cell.setCellValue(getExpense().doubleValue());
        if (getExpense().doubleValue() < 0)
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        else
            cell.setCellStyle(excelStyle.getDoubleStyle());
        nextRow++;
        row = sheet.createRow(nextRow);
        cell = row.createCell((short) 0);
        cell.setCellValue(StringAppender.append(getString("label.tax"), " ", getString("label.eur"), ":"));
        //sheet.addMergedRegion(new Region((short) row.getRowNum(), (short) 0, (short) row.getRowNum(), (short) 2));
        cell.setCellStyle(excelStyle.getLabelStyle());
        cell = row.createCell((short) 3);
        cell.setCellValue(getTax().doubleValue());
        if (getTax().doubleValue() < 0)
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        else
            cell.setCellStyle(excelStyle.getDoubleStyle());
        nextRow++;
        row = sheet.createRow(nextRow);
        cell = row.createCell((short) 0);
        cell.setCellValue(getString("label.toExecute.adiantamentosReport"));
        //sheet.addMergedRegion(new Region((short) row.getRowNum(), (short) row.getRowNum(), (short) 1, (short) 3));
        cell.setCellStyle(excelStyle.getLabelStyle());
        cell = row.createCell((short) 3);
        cell.setCellValue(getAdiantamentosPorJustificar().doubleValue());
        if (getAdiantamentosPorJustificar().doubleValue() < 0)
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        else
            cell.setCellStyle(excelStyle.getDoubleStyle());
        nextRow++;
        row = sheet.createRow(nextRow);
        cell = row.createCell((short) 3);
        cell.setCellValue(getTotal().doubleValue());
        if (getTotal().doubleValue() < 0)
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        else
            cell.setCellStyle(excelStyle.getDoubleStyle());
    }
}
