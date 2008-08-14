/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

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
public class InfoProjectReport extends InfoReport {

    private InfoProject infoProject;

    public InfoProject getInfoProject() {
	return infoProject;
    }

    public void setInfoProject(InfoProject infoProject) {
	this.infoProject = infoProject;
    }

    public void getReportToExcel(IUserView userView, HSSFWorkbook wb, ReportType reportType) {
	HSSFSheet sheet = wb.createSheet(reportType.getReportLabel());
	sheet.setGridsPrinted(false);
	ExcelStyle excelStyle = new ExcelStyle(wb);
	HSSFRow row = sheet.createRow((short) 0);
	HSSFCell cell = row.createCell((short) 0);
	cell.setCellValue(reportType.getReportLabel());
	cell.setCellStyle(excelStyle.getTitleStyle());
	sheet = infoProject.getProjectInformationToExcel(sheet, excelStyle);
	getReportToExcel(sheet, excelStyle, reportType);
    }
}
