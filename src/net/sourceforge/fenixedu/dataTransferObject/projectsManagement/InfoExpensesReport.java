/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.util.projectsManagement.ExcelStyle;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import pt.utl.ist.fenix.tools.util.StringAppender;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoExpensesReport extends InfoProjectReport {

    InfoSummaryPTEReportLine summaryPTEReport;

    InfoSummaryEURReportLine summaryEURReport;

    InfoAdiantamentosReportLine adiantamentosReport;

    InfoCabimentosReportLine cabimentosReport;

    List rubricList;

    public InfoAdiantamentosReportLine getAdiantamentosReport() {
	return adiantamentosReport;
    }

    public void setAdiantamentosReport(InfoAdiantamentosReportLine adiantamentosReport) {
	this.adiantamentosReport = adiantamentosReport;
    }

    public InfoCabimentosReportLine getCabimentosReport() {
	return cabimentosReport;
    }

    public void setCabimentosReport(InfoCabimentosReportLine cabimentosReport) {
	this.cabimentosReport = cabimentosReport;
    }

    public List getRubricList() {
	return rubricList;
    }

    public void setRubricList(List rubricList) {
	this.rubricList = rubricList;
    }

    public InfoSummaryEURReportLine getSummaryEURReport() {
	return summaryEURReport;
    }

    public void setSummaryEURReport(InfoSummaryEURReportLine summaryEURReport) {
	this.summaryEURReport = summaryEURReport;
    }

    public InfoSummaryPTEReportLine getSummaryPTEReport() {
	return summaryPTEReport;
    }

    public void setSummaryPTEReport(InfoSummaryPTEReportLine summaryPTEReport) {
	this.summaryPTEReport = summaryPTEReport;
    }

    public void getReportToExcel(IUserView userView, HSSFWorkbook wb, ReportType reportType) {
	ExcelStyle excelStyle = new ExcelStyle(wb);
	super.getReportToExcel(userView, wb, reportType);
	HSSFSheet sheet = wb.getSheetAt(0);
	HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 3);

	// sheet.addMergedRegion(new Region((short) row.getRowNum(), (short) 0,
	// (short) row.getRowNum(), (short) 3));
	HSSFCell cell = row.createCell((short) 0);
	cell.setCellValue(getString("label.treasury"));
	cell.setCellStyle(excelStyle.getTitleStyle());
	summaryPTEReport.getLineToExcel(sheet, excelStyle, reportType);
	summaryEURReport.getLineToExcel(sheet, excelStyle, reportType);
	row = sheet.createRow(sheet.getLastRowNum() + 3);
	cell = row.createCell((short) 0);
	cell.setCellValue(StringAppender.append(getString("label.treasuryBalance"), "(a)(b)", ":"));
	// sheet.addMergedRegion(new Region((short) row.getRowNum(), (short) 0,
	// (short) row.getRowNum(), (short) 2));
	cell.setCellStyle(excelStyle.getLabelStyle());
	cell = row.createCell((short) 3);
	Double total = (summaryPTEReport.getTotal() / 200.482 + summaryEURReport.getTotal());
	cell.setCellValue(total);
	if (total.doubleValue() < 0)
	    cell.setCellStyle(excelStyle.getDoubleNegativeStyle());
	else
	    cell.setCellStyle(excelStyle.getDoubleStyle());
	row = sheet.createRow(sheet.getLastRowNum() + 3);
	cell = row.createCell((short) 0);
	cell.setCellValue(getString("label.adiantamentos"));
	// sheet.addMergedRegion(new Region((short) row.getRowNum(), (short) 0,
	// (short) row.getRowNum(), (short) 3));
	cell.setCellStyle(excelStyle.getTitleStyle());
	adiantamentosReport.getLineToExcel(sheet, excelStyle, reportType);
	row = sheet.createRow(sheet.getLastRowNum() + 1);
	cell = row.createCell((short) 0);
	// sheet.addMergedRegion(new Region((short) row.getRowNum(), (short) 0,
	// (short) row.getRowNum() + 1, (short) ((IReportLine)
	// getLines().get(0))
	// .getNumberOfColumns()));
	cell.setCellValue(getString("message.extraInformation.a"));

	HSSFCellStyle valueStyleNoWrap = excelStyle.getValueStyle();
	valueStyleNoWrap.setWrapText(false);
	cell.setCellStyle(valueStyleNoWrap);
	row = sheet.createRow(sheet.getLastRowNum() + 4);
	cell = row.createCell((short) 0);
	cell.setCellValue(getString("label.cabimentos"));
	// sheet.addMergedRegion(new Region((short) row.getRowNum(), (short) 0,
	// (short) row.getRowNum(), (short) 3));
	cell.setCellStyle(excelStyle.getTitleStyle());
	cabimentosReport.getLineToExcel(sheet, excelStyle, reportType);
	row = sheet.createRow(sheet.getLastRowNum() + 1);
	cell = row.createCell((short) 0);
	// sheet.addMergedRegion(new Region((short) row.getRowNum(), (short) 0,
	// (short) row.getRowNum() + 1, (short) ((IReportLine)
	// getLines().get(0))
	// .getNumberOfColumns()));
	cell.setCellValue(getString("message.extraInformation.b"));
	cell.setCellStyle(valueStyleNoWrap);
    }

}
