/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import net.sourceforge.fenixedu.domain.projectsManagement.IOverheadsSummaryReportLine;
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
public class InfoOverheadsSummaryReportLine extends InfoReportLine {
    private Integer year;

    private Integer explorationUnit;

    private String costCenter;

    private Double OGRevenue;

    private Double OGOverhead;

    private Double OARevenue;

    private Double OAOverhead;

    private Double OORevenue;

    private Double OOOverhead;

    private Double OERevenue;

    private Double OEOverhead;

    private Double totalOverheads;

    private Double transferedOverheads;

    private Double balance;

    public Double getBalance() {
	return balance;
    }

    public void setBalance(Double balance) {
	this.balance = balance;
    }

    public String getCostCenter() {
	return costCenter;
    }

    public void setCostCenter(String costCenter) {
	this.costCenter = costCenter;
    }

    public Integer getExplorationUnit() {
	return explorationUnit;
    }

    public void setExplorationUnit(Integer explorationUnit) {
	this.explorationUnit = explorationUnit;
    }

    public Double getOAOverhead() {
	return OAOverhead;
    }

    public void setOAOverhead(Double overhead) {
	OAOverhead = overhead;
    }

    public Double getOARevenue() {
	return OARevenue;
    }

    public void setOARevenue(Double revenue) {
	OARevenue = revenue;
    }

    public Double getOEOverhead() {
	return OEOverhead;
    }

    public void setOEOverhead(Double overhead) {
	OEOverhead = overhead;
    }

    public Double getOERevenue() {
	return OERevenue;
    }

    public void setOERevenue(Double revenue) {
	OERevenue = revenue;
    }

    public Double getOGOverhead() {
	return OGOverhead;
    }

    public void setOGOverhead(Double overhead) {
	OGOverhead = overhead;
    }

    public Double getOGRevenue() {
	return OGRevenue;
    }

    public void setOGRevenue(Double revenue) {
	OGRevenue = revenue;
    }

    public Double getOOOverhead() {
	return OOOverhead;
    }

    public void setOOOverhead(Double overhead) {
	OOOverhead = overhead;
    }

    public Double getOORevenue() {
	return OORevenue;
    }

    public void setOORevenue(Double revenue) {
	OORevenue = revenue;
    }

    public Double getTotalOverheads() {
	return totalOverheads;
    }

    public void setTotalOverheads(Double totalOverheads) {
	this.totalOverheads = totalOverheads;
    }

    public Double getTransferedOverheads() {
	return transferedOverheads;
    }

    public void setTransferedOverheads(Double transferedOverheads) {
	this.transferedOverheads = transferedOverheads;
    }

    public Integer getYear() {
	return year;
    }

    public void setYear(Integer year) {
	this.year = year;
    }

    public void copyFromDomain(IOverheadsSummaryReportLine overheadsSummaryReportLine) {
	if (overheadsSummaryReportLine != null) {
	    setYear(overheadsSummaryReportLine.getYear());
	    setExplorationUnit(overheadsSummaryReportLine.getExplorationUnit());
	    setCostCenter(overheadsSummaryReportLine.getCostCenter());
	    setOGRevenue(overheadsSummaryReportLine.getOGRevenue());
	    setOGOverhead(overheadsSummaryReportLine.getOGOverhead());
	    setOARevenue(overheadsSummaryReportLine.getOARevenue());
	    setOAOverhead(overheadsSummaryReportLine.getOAOverhead());
	    setOORevenue(overheadsSummaryReportLine.getOORevenue());
	    setOOOverhead(overheadsSummaryReportLine.getOOOverhead());
	    setOERevenue(overheadsSummaryReportLine.getOERevenue());
	    setOEOverhead(overheadsSummaryReportLine.getOEOverhead());
	    setTotalOverheads(overheadsSummaryReportLine.getTotalOverheads());
	    setTransferedOverheads(overheadsSummaryReportLine.getTransferedOverheads());
	    setBalance(overheadsSummaryReportLine.getBalance());
	}
    }

    public static InfoOverheadsSummaryReportLine newInfoFromDomain(IOverheadsSummaryReportLine overheadsSummaryReportLine) {
	InfoOverheadsSummaryReportLine infoOverheadsSummaryReportLine = null;
	if (overheadsSummaryReportLine != null) {
	    infoOverheadsSummaryReportLine = new InfoOverheadsSummaryReportLine();
	    infoOverheadsSummaryReportLine.copyFromDomain(overheadsSummaryReportLine);
	}
	return infoOverheadsSummaryReportLine;
    }

    public int getNumberOfColumns() {
	return 14;
    }

    public void getHeaderToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
	HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
	HSSFCell cell = row.createCell((short) 0);
	cell.setCellValue(getString("label.year"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 1);
	cell.setCellValue(getString("label.explorUnit"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 2);
	cell.setCellValue(getString("label.costCenter"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 3);
	cell.setCellValue(getString("label.OGRevenue"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 4);
	cell.setCellValue(getString("label.OGOverhead"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 5);
	cell.setCellValue(getString("label.OARevenue"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 6);
	cell.setCellValue(getString("label.OAOverhead"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 7);
	cell.setCellValue(getString("label.OORevenue"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 8);
	cell.setCellValue(getString("label.OOOverhead"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 9);
	cell.setCellValue(getString("label.OERevenue"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 10);
	cell.setCellValue(getString("label.OEOverhead"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 11);
	cell.setCellValue(getString("label.totalOverheads"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 12);
	cell.setCellValue(getString("label.transferedOverheads"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
	cell = row.createCell((short) 13);
	cell.setCellValue(getString("label.balance"));
	cell.setCellStyle(excelStyle.getHeaderStyle());
    }

    public void getLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
	HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
	HSSFCell cell = row.createCell((short) 0);
	cell.setCellValue(getYear());
	cell.setCellStyle(excelStyle.getStringStyle());
	cell = row.createCell((short) 1);
	cell.setCellValue(getExplorationUnit());
	cell.setCellStyle(excelStyle.getStringStyle());
	cell = row.createCell((short) 2);
	cell.setCellValue(getCostCenter());
	cell.setCellStyle(excelStyle.getStringStyle());
	cell = row.createCell((short) 3);
	cell.setCellValue(getOGRevenue());
	if (getOGRevenue().doubleValue() < 0)
	    cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
	else
	    cell.setCellStyle(excelStyle.getDoubleStyle());
	cell = row.createCell((short) 4);
	cell.setCellValue(getOGOverhead());
	if (getOGOverhead().doubleValue() < 0)
	    cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
	else
	    cell.setCellStyle(excelStyle.getDoubleStyle());
	cell = row.createCell((short) 5);
	cell.setCellValue(getOARevenue());
	if (getOARevenue().doubleValue() < 0)
	    cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
	else
	    cell.setCellStyle(excelStyle.getDoubleStyle());
	cell = row.createCell((short) 6);
	cell.setCellValue(getOAOverhead());
	if (getOAOverhead().doubleValue() < 0)
	    cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
	else
	    cell.setCellStyle(excelStyle.getDoubleStyle());
	cell = row.createCell((short) 7);
	cell.setCellValue(getOORevenue().doubleValue());
	if (getOORevenue().doubleValue() < 0)
	    cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
	else
	    cell.setCellStyle(excelStyle.getDoubleStyle());
	cell = row.createCell((short) 8);
	cell.setCellValue(getOOOverhead().doubleValue());
	if (getOOOverhead().doubleValue() < 0)
	    cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
	else
	    cell.setCellStyle(excelStyle.getDoubleStyle());
	cell = row.createCell((short) 9);
	cell.setCellValue(getOERevenue().doubleValue());
	if (getOERevenue().doubleValue() < 0)
	    cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
	else
	    cell.setCellStyle(excelStyle.getDoubleStyle());
	cell = row.createCell((short) 10);
	cell.setCellValue(getOEOverhead().doubleValue());
	if (getOEOverhead().doubleValue() < 0)
	    cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
	else
	    cell.setCellStyle(excelStyle.getDoubleStyle());
	cell = row.createCell((short) 11);
	cell.setCellValue(getTotalOverheads().doubleValue());
	if (getTotalOverheads().doubleValue() < 0)
	    cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
	else
	    cell.setCellStyle(excelStyle.getDoubleStyle());
	cell = row.createCell((short) 12);
	cell.setCellValue(getTransferedOverheads().doubleValue());
	if (getTransferedOverheads().doubleValue() < 0)
	    cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
	else
	    cell.setCellStyle(excelStyle.getDoubleStyle());
	cell = row.createCell((short) 13);
	cell.setCellValue(getBalance().doubleValue());
	if (getBalance().doubleValue() < 0)
	    cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
	else
	    cell.setCellStyle(excelStyle.getDoubleStyle());
    }

    public void getTotalLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
	HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
	HSSFCell cell = row.createCell((short) 0);
	cell.setCellStyle(excelStyle.getStringStyle());
	cell.setCellValue(getString("label.total"));
	for (int i = 3; i <= 13; i++) {
	    CellReference cellRef1 = new CellReference(1, i);
	    CellReference cellRef2 = new CellReference(((short) row.getRowNum() - 1), i);
	    cell = row.createCell((short) i);
	    cell.setCellStyle(excelStyle.getDoubleStyle());
	    cell.setCellFormula("sum(" + cellRef1.toString() + ":" + cellRef2.toString() + ")");
	}
    }

    public Double getValue(int column) {
	switch (column) {
	case 3:
	    return getOGRevenue();
	case 4:
	    return getOGOverhead();
	case 5:
	    return getOARevenue();
	case 6:
	    return getOAOverhead();
	case 7:
	    return getOORevenue();
	case 8:
	    return getOOOverhead();
	case 9:
	    return getOERevenue();
	case 10:
	    return getOEOverhead();
	case 11:
	    return getTotalOverheads();
	case 12:
	    return getTransferedOverheads();
	case 13:
	    return getBalance();
	default:
	    return null;
	}
    }
}
