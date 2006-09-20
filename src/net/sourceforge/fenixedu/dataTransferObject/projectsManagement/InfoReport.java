/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.util.LanguageUtils;
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
public class InfoReport extends DataTranferObject {
    private List<IReportLine> lines;

    public List getLines() {
        return lines;
    }

    public void setLines(List<IReportLine> lines) {
        this.lines = lines;
    }

    public Integer getLinesSize() {
        return new Integer(lines.size());
    }

    public void getReportToExcel(HSSFSheet sheet, ExcelStyle excelStyle, ReportType reportType) {
        sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) ((IReportLine) lines.get(0)).getNumberOfColumns()));
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            sheet.addMergedRegion(new Region(i, (short) 1, i, (short) (((IReportLine) lines.get(0)).getNumberOfColumns())));
        }
        HSSFRow row = null;
        HSSFCell cell = null;
        int lastRowNum = sheet.getLastRowNum() + 2;
        if (lines != null && lines.size() > 0) {
            row = sheet.createRow(lastRowNum);
            ((IReportLine) lines.get(0)).getHeaderToExcel(sheet, excelStyle, reportType);
            lastRowNum++;
            for (IReportLine reportLine : lines) {
                reportLine.getLineToExcel(sheet, excelStyle, reportType);
            }
            ((IReportLine) lines.get(0)).getTotalLineToExcel(sheet, excelStyle, reportType);
        }

        row = sheet.createRow((short) sheet.getLastRowNum() + 2);
        row.setHeight((short) 0x349);
        cell = row.createCell((short) 0);
        cell.setCellValue(reportType.getReportNote());
        cell.setCellStyle(excelStyle.getValueStyle());
        sheet.addMergedRegion(new Region(sheet.getLastRowNum(), (short) 0, sheet.getLastRowNum(), (short) ((IReportLine) lines.get(0))
                .getNumberOfColumns()));
    }

    protected String getString(String label) {
        return (ResourceBundle.getBundle("resources.ProjectsManagementResources", LanguageUtils.getLocale())).getString(label);
    }
}
