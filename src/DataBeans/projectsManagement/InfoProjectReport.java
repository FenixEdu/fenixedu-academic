/*
 * Created on Jan 12, 2005
 *
 */
package DataBeans.projectsManagement;

import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import DataBeans.DataTranferObject;
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

    public HSSFSheet getReportToExcel(HSSFWorkbook wb, ReportType reportType) {
        HSSFSheet sheet = wb.createSheet(reportType.getReportLabel());
        sheet.setGridsPrinted(false);
        if (lines != null && lines.size() > 0) {
            ExcelStyle excelStyle = new ExcelStyle(wb);
            HSSFRow row = sheet.createRow((short) 0);
            row = ((IReportLine) lines.get(0)).getHeaderToExcel(row);
            for (int i = 0; i < lines.size(); i++) {
                row = sheet.createRow((short) i + 1);
                row = ((IReportLine) lines.get(i)).getLineToExcel(row);
            }
            row = sheet.createRow((short) lines.size() + 1);
            row = ((IReportLine) lines.get(0)).getTotalLineToExcel(row);
        }
        return sheet;
    }
}
