/*
 * Created on Jan 12, 2005
 *
 */
package DataBeans.projectsManagement;

import org.apache.poi.hssf.usermodel.HSSFRow;

/**
 * @author Susana Fernandes
 * 
 */
public interface IReportLine {
    public Double getValue(int column);

    public HSSFRow getHeaderToExcel(HSSFRow row);

    public HSSFRow getLineToExcel(HSSFRow row);

    public HSSFRow getTotalLineToExcel(HSSFRow row);
}
