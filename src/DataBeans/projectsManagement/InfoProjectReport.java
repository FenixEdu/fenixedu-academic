/*
 * Created on Jan 12, 2005
 *
 */
package DataBeans.projectsManagement;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;

import DataBeans.DataTranferObject;
import ServidorAplicacao.IUserView;
import Util.projectsManagement.ExcelStyle;
import Util.projectsManagement.ReportType;

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

    public HSSFSheet getReportToExcel(IUserView userView, HSSFWorkbook wb, ReportType reportType) {
        HSSFSheet sheet = wb.createSheet(reportType.getReportLabel());
        sheet.setGridsPrinted(false);
        ExcelStyle excelStyle = new ExcelStyle(wb);

        HSSFRow row = sheet.createRow((short) 0);
        HSSFCell cell = row.createCell((short) 0);
        cell.setCellValue(reportType.getReportLabel());
        cell.setCellStyle(ExcelStyle.TITLE_STYLE);

        if (infoProject != null) {
            sheet = infoProject.getProjectInformationToExcel(sheet);
        } else {
            row = sheet.createRow((short) 2);
            cell = row.createCell((short) 0);
            cell.setCellValue("Coordenador:");
            cell.setCellStyle(ExcelStyle.LABEL_STYLE);
            cell = row.createCell((short) 1);
            cell.setCellValue(userView.getFullName());
            cell.setCellStyle(ExcelStyle.VALUE_STYLE);
            row = sheet.createRow((short) 3);
            cell = row.createCell((short) 0);
            cell.setCellValue("Data:");
            cell.setCellStyle(ExcelStyle.LABEL_STYLE);
            cell = row.createCell((short) 1);
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy 'às' HH:mm");
            cell.setCellValue(formatter.format(new Date()));
            cell.setCellStyle(ExcelStyle.VALUE_STYLE);
        }
        sheet.addMergedRegion(new Region(0, (short) 0, 0, (short) ((IReportLine) lines.get(0)).getNumberOfColumns()));
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            sheet.addMergedRegion(new Region(i, (short) 1, i, (short) (((IReportLine) lines.get(0)).getNumberOfColumns())));
        }

        int lastRowNum = sheet.getLastRowNum() + 2;
        if (lines != null && lines.size() > 0) {
            row = sheet.createRow(lastRowNum);
            row = ((IReportLine) lines.get(0)).getHeaderToExcel(row);
            lastRowNum++;
            for (int i = 0; i < lines.size(); i++) {
                row = sheet.createRow((short) i + lastRowNum);
                row = ((IReportLine) lines.get(i)).getLineToExcel(row);
            }
            row = sheet.createRow((short) lines.size() + lastRowNum);
            row = ((IReportLine) lines.get(0)).getTotalLineToExcel(row);
        }

        return sheet;
    }
}
