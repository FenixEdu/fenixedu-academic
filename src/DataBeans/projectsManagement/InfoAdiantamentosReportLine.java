/*
 * Created on Jan 12, 2005
 *
 */
package DataBeans.projectsManagement;

import org.apache.poi.hssf.usermodel.HSSFRow;

import DataBeans.DataTranferObject;
import Dominio.projectsManagement.IAdiantamentosReportLine;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoAdiantamentosReportLine extends DataTranferObject implements IReportLine {

    private Integer projectCode;

    private Double adiantamentos;

    private Double justifications;

    private Double total;

    public Double getAdiantamentos() {
        return adiantamentos;
    }

    public void setAdiantamentos(Double adiantamentos) {
        this.adiantamentos = adiantamentos;
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

    public void copyFromDomain(IAdiantamentosReportLine adiantamentosReportLine) {
        if (adiantamentosReportLine != null) {
            setProjectCode(adiantamentosReportLine.getProjectCode());
            setAdiantamentos(adiantamentosReportLine.getAdiantamentos());
            setJustifications(adiantamentosReportLine.getJustifications());
            setTotal(adiantamentosReportLine.getTotal());
        }
    }

    public static InfoAdiantamentosReportLine newInfoFromDomain(IAdiantamentosReportLine adiantamentosReportLine) {
        InfoAdiantamentosReportLine infoAdiantamentosReportLine = null;
        if (adiantamentosReportLine != null) {
            infoAdiantamentosReportLine = new InfoAdiantamentosReportLine();
            infoAdiantamentosReportLine.copyFromDomain(adiantamentosReportLine);
        }
        return infoAdiantamentosReportLine;
    }

    public Double getValue(int column) {
        return null;
    }

    public HSSFRow getHeaderToExcel(HSSFRow row) {
        return null;
    }

    public HSSFRow getLineToExcel(HSSFRow row) {
        return null;
    }

    public HSSFRow getTotalLineToExcel(HSSFRow row) {
        return null;
    }

}
