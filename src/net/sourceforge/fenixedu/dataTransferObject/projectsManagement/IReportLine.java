/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

import org.apache.poi.hssf.usermodel.HSSFSheet;

import pt.utl.ist.fenix.tools.util.excel.ExcelStyle;

/**
 * @author Susana Fernandes
 * 
 */
public interface IReportLine {
	public int getNumberOfColumns();

	public Double getValue(int column);

	public void getHeaderToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType);

	public void getLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType);

	public void getTotalLineToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType);

}
