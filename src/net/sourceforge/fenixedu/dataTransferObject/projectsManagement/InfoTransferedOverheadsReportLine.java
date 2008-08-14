/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import net.sourceforge.fenixedu.domain.projectsManagement.ITransferedOverheadsReportLine;
import net.sourceforge.fenixedu.util.projectsManagement.ExcelStyle;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.CellReference;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoTransferedOverheadsReportLine extends InfoReportLine {
    private Integer explorationUnit;

    private String movementId;

    private String date;

    private String type;

    private String description;

    private Double overheadValue;

    public String getDate() {
	return date;
    }

    public void setDate(String date) {
	this.date = date;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Integer getExplorationUnit() {
	return explorationUnit;
    }

    public void setExplorationUnit(Integer explorationUnit) {
	this.explorationUnit = explorationUnit;
    }

    public String getMovementId() {
	return movementId;
    }

    public void setMovementId(String movementId) {
	this.movementId = movementId;
    }

    public Double getOverheadValue() {
	return overheadValue;
    }

    public void setOverheadValue(Double overheadValue) {
	this.overheadValue = overheadValue;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public void copyFromDomain(ITransferedOverheadsReportLine transferedOverheadsReportLine) {
	if (transferedOverheadsReportLine != null) {
	    setExplorationUnit(transferedOverheadsReportLine.getExplorationUnit());
	    setMovementId(transferedOverheadsReportLine.getMovementId());
	    setDate(transferedOverheadsReportLine.getDate());
	    setType(transferedOverheadsReportLine.getType());
	    setDescription(transferedOverheadsReportLine.getDescription());
	    setOverheadValue(transferedOverheadsReportLine.getOverheadValue());
	}
    }

    public static InfoTransferedOverheadsReportLine newInfoFromDomain(ITransferedOverheadsReportLine transferedOverheadsReportLine) {
	InfoTransferedOverheadsReportLine infoTransferedOverheadsReportLine = null;
	if (transferedOverheadsReportLine != null) {
	    infoTransferedOverheadsReportLine = new InfoTransferedOverheadsReportLine();
	    infoTransferedOverheadsReportLine.copyFromDomain(transferedOverheadsReportLine);
	}
	return infoTransferedOverheadsReportLine;
    }

    public int getNumberOfColumns() {
	return 6;
    }

    public void getHeaderToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {

	HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
	HSSFCell cell = row.createCell((short) 0);
	cell.setCellValue(getString("label.explorUnit"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 1);
	cell.setCellValue(getString("label.idMov"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 2);
	cell.setCellValue(getString("label.date"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 3);
	cell.setCellValue(getString("label.type"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 4);
	cell.setCellValue(getString("label.description"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 5);
	cell.setCellValue(getString("label.value") + " " + getString("label.ovh"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
    }

    public void getLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
	HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
	HSSFCell cell = row.createCell((short) 0);
	cell.setCellValue(getExplorationUnit());
	cell.setCellStyle(excelStyle.getStringStyle());
	cell = row.createCell((short) 1);
	cell.setCellValue(getMovementId());
	cell.setCellStyle(excelStyle.getStringStyle());
	cell = row.createCell((short) 2);
	cell.setCellValue(getDate());
	cell.setCellStyle(excelStyle.getStringStyle());
	cell = row.createCell((short) 3);
	cell.setCellValue(getType());
	cell.setCellStyle(excelStyle.getStringStyle());
	cell = row.createCell((short) 4);
	cell.setCellValue(getDescription());
	cell.setCellStyle(excelStyle.getStringStyle());
	cell = row.createCell((short) 5);
	cell.setCellValue(getOverheadValue().doubleValue());
	if (getOverheadValue().doubleValue() < 0)
	    cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
	else
	    cell.setCellStyle(excelStyle.getDoubleStyle());
    }

    public void getTotalLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
	HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
	HSSFCell cell = row.createCell((short) 0);
	cell.setCellStyle(excelStyle.getStringStyle());
	cell.setCellValue(getString("label.total"));
	for (int i = 5; i <= 5; i++) {
	    CellReference cellRef1 = new CellReference(1, i);
	    CellReference cellRef2 = new CellReference(((short) row.getRowNum() - 1), i);
	    cell = row.createCell((short) i);
	    cell.setCellStyle(excelStyle.getDoubleStyle());
	    cell.setCellFormula("sum(" + cellRef1.toString() + ":" + cellRef2.toString() + ")");
	}
    }

    public Double getValue(int column) {
	switch (column) {
	case 5:
	    return getOverheadValue();
	default:
	    return null;
	}
    }
}
