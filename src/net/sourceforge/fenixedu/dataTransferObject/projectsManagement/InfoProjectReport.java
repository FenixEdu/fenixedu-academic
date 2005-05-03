/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.dataTransferObject.DataTranferObject;
import net.sourceforge.fenixedu.util.projectsManagement.ExcelStyle;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoProjectReport extends DataTranferObject {
    private List lines;

    private InfoProject infoProject;

    public List getLines() {
        return lines;
    }

    public void setLines(List lines) {
        this.lines = lines;
    }

    public InfoProject getInfoProject() {
        return infoProject;
    }

    public void setInfoProject(InfoProject infoProject) {
        this.infoProject = infoProject;
    }

    public Integer getLinesSize() {
        return new Integer(lines.size());
    }

    public void getReportToExcel(IUserView userView, HSSFWorkbook wb, ReportType reportType) {
        HSSFSheet sheet = wb.createSheet(reportType.getReportLabel());
        sheet.setGridsPrinted(false);
        ExcelStyle excelStyle = new ExcelStyle(wb);
        HSSFRow row = sheet.createRow((short) 0);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue(reportType.getReportLabel());
        cell.setCellStyle(excelStyle.getTitleStyle());

        if (infoProject != null) {
            sheet = infoProject.getProjectInformationToExcel(sheet, excelStyle);
        } else {
            row = sheet.createRow((short) 2);
            cell = row.createCell((short) 0);
            cell.setCellValue("Coordenador:");
            cell.setCellStyle(excelStyle.getLabelStyle());
            cell = row.createCell((short) 1);
            cell.setCellValue(userView.getFullName());
            cell.setCellStyle(excelStyle.getValueStyle());
            row = sheet.createRow((short) 3);
            cell = row.createCell((short) 0);
            cell.setCellValue("Data:");
            cell.setCellStyle(excelStyle.getLabelStyle());
            cell = row.createCell((short) 1);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy 'às' HH:mm");
            cell.setCellValue(formatter.format(new Date()));
            cell.setCellStyle(excelStyle.getValueStyle());
        }

        sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) ((IReportLine) lines.get(0)).getNumberOfColumns()));
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            sheet.addMergedRegion(new Region(i, (short) 1, i, (short) (((IReportLine) lines.get(0)).getNumberOfColumns())));
        }

        int lastRowNum = sheet.getLastRowNum() + 2;
        if (lines != null && lines.size() > 0) {
            row = sheet.createRow(lastRowNum);
            ((IReportLine) lines.get(0)).getHeaderToExcel(sheet, excelStyle, reportType);
            lastRowNum++;
            for (int i = 0; i < lines.size(); i++) {
                ((IReportLine) lines.get(i)).getLineToExcel(sheet, excelStyle, reportType);
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
}
