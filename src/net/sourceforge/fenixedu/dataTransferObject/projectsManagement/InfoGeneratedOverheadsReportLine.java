/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import net.sourceforge.fenixedu.domain.projectsManagement.IGeneratedOverheadsReportLine;
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
public class InfoGeneratedOverheadsReportLine extends InfoReportLine {
    private Integer explorationUnit;

    private Integer projectNumber;

    private String acronim;

    private Integer coordinatorNumber;

    private String coordinatorName;

    private String type;

    private String date;

    private String description;

    private Double revenue;

    private Double overheadPerscentage;

    private Double overheadValue;

    public String getAcronim() {
	return acronim;
    }

    public void setAcronim(String acronim) {
	this.acronim = acronim;
    }

    public String getCoordinatorName() {
	return coordinatorName;
    }

    public void setCoordinatorName(String coordinatorName) {
	this.coordinatorName = coordinatorName;
    }

    public Integer getCoordinatorNumber() {
	return coordinatorNumber;
    }

    public void setCoordinatorNumber(Integer coordinatorNumber) {
	this.coordinatorNumber = coordinatorNumber;
    }

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

    public Double getOverheadPerscentage() {
	return overheadPerscentage;
    }

    public void setOverheadPerscentage(Double overheadPerscentage) {
	this.overheadPerscentage = overheadPerscentage;
    }

    public Double getOverheadValue() {
	return overheadValue;
    }

    public void setOverheadValue(Double overheadValue) {
	this.overheadValue = overheadValue;
    }

    public Integer getProjectNumber() {
	return projectNumber;
    }

    public void setProjectNumber(Integer projectNumber) {
	this.projectNumber = projectNumber;
    }

    public Double getRevenue() {
	return revenue;
    }

    public void setRevenue(Double revenue) {
	this.revenue = revenue;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public void copyFromDomain(IGeneratedOverheadsReportLine generatedOverheadsReportLine) {
	if (generatedOverheadsReportLine != null) {
	    setExplorationUnit(generatedOverheadsReportLine.getExplorationUnit());
	    setProjectNumber(generatedOverheadsReportLine.getProjectNumber());
	    setAcronim(generatedOverheadsReportLine.getAcronim());
	    setCoordinatorNumber(generatedOverheadsReportLine.getCoordinatorNumber());
	    setCoordinatorName(generatedOverheadsReportLine.getCoordinatorName());
	    setType(generatedOverheadsReportLine.getType());
	    setDate(generatedOverheadsReportLine.getDate());
	    setDescription(generatedOverheadsReportLine.getDescription());
	    setRevenue(generatedOverheadsReportLine.getRevenue());
	    setOverheadPerscentage(generatedOverheadsReportLine.getOverheadPerscentage());
	    setOverheadValue(generatedOverheadsReportLine.getOverheadValue());
	}
    }

    public static InfoGeneratedOverheadsReportLine newInfoFromDomain(IGeneratedOverheadsReportLine generatedOverheadsReportLine) {
	InfoGeneratedOverheadsReportLine infoGeneratedOverheadsReportLine = null;
	if (generatedOverheadsReportLine != null) {
	    infoGeneratedOverheadsReportLine = new InfoGeneratedOverheadsReportLine();
	    infoGeneratedOverheadsReportLine.copyFromDomain(generatedOverheadsReportLine);
	}
	return infoGeneratedOverheadsReportLine;
    }

    public int getNumberOfColumns() {
	return 11;
    }

    public void getHeaderToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {

	HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
	HSSFCell cell = row.createCell((short) 0);
	cell.setCellValue(getString("label.explorUnit"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 1);
	cell.setCellValue(getString("label.projNum"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 2);
	cell.setCellValue(getString("label.acronym"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 3);
	cell.setCellValue(getString("label.coordinator"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 4);
	cell.setCellValue(getString("label.name"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 5);
	cell.setCellValue(getString("label.type"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 6);
	cell.setCellValue(getString("label.date"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 7);
	cell.setCellValue(getString("label.description"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 8);
	cell.setCellValue(getString("link.revenue"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 9);
	cell.setCellValue(getString("label.percentageSymbol") + " " + getString("label.ovh"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 10);
	cell.setCellValue(getString("label.value") + " " + getString("label.ovh"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
    }

    public void getLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
	HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
	HSSFCell cell = row.createCell((short) 0);
	cell.setCellValue(getExplorationUnit());
	cell.setCellStyle(excelStyle.getStringStyle());
	cell = row.createCell((short) 1);
	cell.setCellValue(getProjectNumber());
	cell.setCellStyle(excelStyle.getStringStyle());
	cell = row.createCell((short) 2);
	cell.setCellValue(getAcronim());
	cell.setCellStyle(excelStyle.getStringStyle());
	cell = row.createCell((short) 3);
	cell.setCellValue(getCoordinatorNumber());
	cell.setCellStyle(excelStyle.getStringStyle());
	cell = row.createCell((short) 4);
	cell.setCellValue(getCoordinatorName());
	cell.setCellStyle(excelStyle.getStringStyle());
	cell = row.createCell((short) 5);
	cell.setCellValue(getType());
	cell.setCellStyle(excelStyle.getStringStyle());
	cell = row.createCell((short) 6);
	cell.setCellValue(getDate());
	cell.setCellStyle(excelStyle.getStringStyle());
	cell = row.createCell((short) 7);
	cell.setCellValue(getDescription());
	cell.setCellStyle(excelStyle.getStringStyle());
	cell = row.createCell((short) 8);
	cell.setCellValue(getRevenue().doubleValue());
	if (getRevenue().doubleValue() < 0)
	    cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
	else
	    cell.setCellStyle(excelStyle.getDoubleStyle());
	cell = row.createCell((short) 9);
	cell.setCellValue(getOverheadPerscentage().doubleValue());
	if (getOverheadPerscentage().doubleValue() < 0)
	    cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
	else
	    cell.setCellStyle(excelStyle.getDoubleStyle());
	cell = row.createCell((short) 10);
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
	for (int i = 8; i <= 10; i++) {
	    CellReference cellRef1 = new CellReference(1, i);
	    CellReference cellRef2 = new CellReference(((short) row.getRowNum() - 1), i);
	    cell = row.createCell((short) i);
	    cell.setCellStyle(excelStyle.getDoubleStyle());
	    cell.setCellFormula("sum(" + cellRef1.toString() + ":" + cellRef2.toString() + ")");
	}
    }

    public Double getValue(int column) {
	switch (column) {
	case 8:
	    return getRevenue();
	case 9:
	    return getOverheadPerscentage();
	case 10:
	    return getOverheadValue();
	default:
	    return null;
	}
    }
}
