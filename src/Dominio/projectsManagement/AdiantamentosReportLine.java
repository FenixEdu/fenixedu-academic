/*
 * Created on Jan 12, 2005
 *
 */
package Dominio.projectsManagement;

import java.io.Serializable;

/**
 * @author Susana Fernandes
 * 
 */
public class AdiantamentosReportLine implements Serializable, IAdiantamentosReportLine {

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
}
