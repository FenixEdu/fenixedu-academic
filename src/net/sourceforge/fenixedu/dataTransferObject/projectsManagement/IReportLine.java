/*
 * Created on Jan 12, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject.projectsManagement;

import org.apache.poi.hssf.usermodel.HSSFSheet;

/**
 * @author Susana Fernandes
 * 
 */
public interface IReportLine {
    public int getNumberOfColumns();

    public Double getValue(int column);

    public void getHeaderToExcel(HSSFSheet sheet);

    public void getLineToExcel(HSSFSheet sheet);

    public void getTotalLineToExcel(HSSFSheet sheet);

}
