/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import net.sourceforge.fenixedu.domain.projectsManagement.ICabimentosReportLine;
import net.sourceforge.fenixedu.util.projectsManagement.ExcelStyle;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.Region;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoCabimentosReportLine extends InfoReportLine {

    private Integer projectCode;

    private Double cabimentos;

    private Double justifications;

    private Double total;

    public Double getCabimentos() {
        return cabimentos;
    }

    public void setCabimentos(Double cabimentos) {
        this.cabimentos = cabimentos;
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

    public void copyFromDomain(ICabimentosReportLine cabimentosReportLine) {
        if (cabimentosReportLine != null) {
            setProjectCode(cabimentosReportLine.getProjectCode());
            setCabimentos(cabimentosReportLine.getCabimentos());
            setJustifications(cabimentosReportLine.getJustifications());
            setTotal(cabimentosReportLine.getTotal());
        }
    }

    public static InfoCabimentosReportLine newInfoFromDomain(ICabimentosReportLine cabimentosReportLine) {
        InfoCabimentosReportLine infoCabimentosReportLine = null;
        if (cabimentosReportLine != null) {
            infoCabimentosReportLine = new InfoCabimentosReportLine();
            infoCabimentosReportLine.copyFromDomain(cabimentosReportLine);
        }
        return infoCabimentosReportLine;
    }

    public void getLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
        int nextRow = sheet.getLastRowNum() + 2;
        HSSFRow row = sheet.createRow(nextRow);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue(getString("label.total.cabimentosReport"));
        //sheet.addMergedRegion(new Region((short) row.getRowNum(), (short) 0, (short) row.getRowNum(), (short) 2));
        cell.setCellStyle(excelStyle.getLabelStyle());
        cell = row.createCell((short) 3);
        cell.setCellValue(getCabimentos().doubleValue());
        if (getCabimentos().doubleValue() < 0)
            cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
        else
            cell.setCellStyle(excelStyle.getDoubleStyle());
        nextRow++;
        row = sheet.createRow(nextRow);
        cell = row.createCell((short) 0);
        cell.setCellValue(getString("label.returnsExecuted.cabimentosReport"));
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
        cell.setCellValue(getString("label.toExecute.cabimentosReport"));
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
