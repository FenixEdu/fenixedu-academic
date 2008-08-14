/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.util.projectsManagement.ExcelStyle;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoOverheadReport extends InfoReport {

    private InfoRubric infoCostCenter;

    public InfoRubric getInfoCostCenter() {
	return infoCostCenter;
    }

    public void setInfoCostCenter(InfoRubric infoCostCenter) {
	this.infoCostCenter = infoCostCenter;
    }

    public void getReportToExcel(IUserView userView, HSSFWorkbook wb, ReportType reportType) {
	HSSFSheet sheet = wb.createSheet(infoCostCenter.getCode());
	sheet.setGridsPrinted(false);
	ExcelStyle excelStyle = new ExcelStyle(wb);
	HSSFRow row = sheet.createRow((short) 0);
	HSSFCell cell = row.createCell((short) 0);
	cell.setCellValue(reportType.getReportLabel());
	cell.setCellStyle(excelStyle.getTitleStyle());
	row = sheet.createRow((short) 2);
	cell = row.createCell((short) 0);
	cell.setCellValue(getString("label.unit") + ":");
	cell.setCellStyle(excelStyle.getLabelStyle());
	cell = row.createCell((short) 1);
	cell.setCellValue(infoCostCenter.getDescription());
	cell.setCellStyle(excelStyle.getValueStyle());
	row = sheet.createRow((short) 3);
	cell = row.createCell((short) 0);
	cell.setCellValue(getString("label.costCenter") + ":");
	cell.setCellStyle(excelStyle.getLabelStyle());
	cell = row.createCell((short) 1);
	cell.setCellValue(infoCostCenter.getCode());
	cell.setCellStyle(excelStyle.getValueStyle());
	row = sheet.createRow((short) 4);
	cell = row.createCell((short) 0);
	cell.setCellValue(getString("label.date") + ":");
	cell.setCellStyle(excelStyle.getLabelStyle());
	cell = row.createCell((short) 1);
	SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy 'às' HH:mm");
	cell.setCellValue(formatter.format(new Date()));
	cell.setCellStyle(excelStyle.getValueStyle());
	getReportToExcel(sheet, excelStyle, reportType);
    }
}
