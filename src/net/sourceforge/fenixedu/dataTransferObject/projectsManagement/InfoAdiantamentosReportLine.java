/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import net.sourceforge.fenixedu.domain.projectsManagement.IAdiantamentosReportLine;
import net.sourceforge.fenixedu.util.projectsManagement.ExcelStyle;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoAdiantamentosReportLine extends InfoReportLine {

    private Integer projectCode;

    private Double adiantamentos;

    private Double justifications;

    private Double total;

    public Double getAdiantamentos() {
        return adiantamentos;
    }

    public void setAdiantamentos(Double adiantamentos) {
        this.adiantamentos = adiantamentos;
    }

    public Double getJustifications() {
        return justifications;
    }

    public void setJustifications(Double justifications) {
        this.justifications = justifications;
    }

    public Integer getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(Integer projectCode) {
        this.projectCode = projectCode;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void copyFromDomain(IAdiantamentosReportLine adiantamentosReportLine) {
        if (adiantamentosReportLine != null) {
            setProjectCode(adiantamentosReportLine.getProjectCode());
            setAdiantamentos(adiantamentosReportLine.getAdiantamentos());
            setJustifications(adiantamentosReportLine.getJustifications());
            setTotal(adiantamentosReportLine.getTotal());
        }
    }

    public static InfoAdiantamentosReportLine newInfoFromDomain(IAdiantamentosReportLine adiantamentosReportLine) {
        InfoAdiantamentosReportLine infoAdiantamentosReportLine = null;
        if (adiantamentosReportLine != null) {
            infoAdiantamentosReportLine = new InfoAdiantamentosReportLine();
            infoAdiantamentosReportLine.copyFromDomain(adiantamentosReportLine);
        }
        return infoAdiantamentosReportLine;
    }

    public void getLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
        int nextRow = sheet.getLastRowNum() + 2;
        HSSFRow row = sheet.createRow(nextRow);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue(getString("label.total.adiantamentosReport"));
        //sheet.addMergedRegion(new Region((short) row.getRowNum(), (short) 0, (short) row.getRowNum(), (short) 2));
        cell.setCellStyle(excelStyle.getLabelStyle());
        cell = row.createCell((short) 3);
        cell.setCellValue(getAdiantamentos().doubleValue());
        if (getAdiantamentos().doubleValue() < 0)
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        else
            cell.setCellStyle(excelStyle.getDoubleStyle());
        nextRow++;
        row = sheet.createRow(nextRow);
        cell = row.createCell((short) 0);
        cell.setCellValue(getString("label.returnsExecuted.adiantamentosReport"));
        //sheet.addMergedRegion(new Region((short) row.getRowNum(), (short) 0, (short) row.getRowNum(), (short) 2));
        cell.setCellStyle(excelStyle.getLabelStyle());
        cell = row.createCell((short) 3);
        cell.setCellValue(getJustifications().doubleValue());
        if (getJustifications().doubleValue() < 0)
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        else
            cell.setCellStyle(excelStyle.getDoubleStyle());
        nextRow++;
        row = sheet.createRow(nextRow);
        cell = row.createCell((short) 0);
        cell.setCellValue(getString("label.total"));
        //sheet.addMergedRegion(new Region((short) row.getRowNum(), (short) 0, (short) row.getRowNum(), (short) 2));
        cell.setCellStyle(excelStyle.getLabelStyle());
        cell = row.createCell((short) 3);
        cell.setCellValue(getTotal().doubleValue());
        if (getTotal().doubleValue() < 0)
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        else
            cell.setCellStyle(excelStyle.getDoubleStyle());
    }
}
