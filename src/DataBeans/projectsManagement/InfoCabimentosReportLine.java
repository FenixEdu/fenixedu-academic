/*
 * Created on Jan 12, 2005
 *
 */
package DataBeans.projectsManagement;

import org.apache.poi.hssf.usermodel.HSSFSheet;

import DataBeans.DataTranferObject;
import Dominio.projectsManagement.ICabimentosReportLine;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoCabimentosReportLine extends DataTranferObject implements IReportLine {

    private Integer projectCode;

    private Double cabimentos;

    private Double justifications;

    private Double total;

    public Double getCabimentos() {
        return cabimentos;
    }

    public void setCabimentos(Double cabimentos) {
        this.cabimentos = cabimentos;
    }

    public Double getJustifications() {
        return justifications;
    }

    public void setJustifications(Double justifications) {
        this.justifications = justifications;
    }

    public Integer getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(Integer projectCode) {
        this.projectCode = projectCode;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void copyFromDomain(ICabimentosReportLine cabimentosReportLine) {
        if (cabimentosReportLine != null) {
            setProjectCode(cabimentosReportLine.getProjectCode());
            setCabimentos(cabimentosReportLine.getCabimentos());
            setJustifications(cabimentosReportLine.getJustifications());
            setTotal(cabimentosReportLine.getTotal());
        }
    }

    public static InfoCabimentosReportLine newInfoFromDomain(ICabimentosReportLine cabimentosReportLine) {
        InfoCabimentosReportLine infoCabimentosReportLine = null;
        if (cabimentosReportLine != null) {
            infoCabimentosReportLine = new InfoCabimentosReportLine();
            infoCabimentosReportLine.copyFromDomain(cabimentosReportLine);
        }
        return infoCabimentosReportLine;
    }

    public Double getValue(int column) {
        return null;
    }

    public void getHeaderToExcel(HSSFSheet sheet) {
    }

    public void getLineToExcel(HSSFSheet sheet) {
    }

    public void getTotalLineToExcel(HSSFSheet sheet) {
    }

    public int getNumberOfColumns() {
        return 0;
    }

}
