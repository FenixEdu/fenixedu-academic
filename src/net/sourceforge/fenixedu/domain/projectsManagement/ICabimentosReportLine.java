/*
 * Created on Jan 28, 2005
 *
 */
package net.sourceforge.fenixedu.domain.projectsManagement;

/**
 * @author Susana Fernandes
 * 
 */
public interface ICabimentosReportLine {
    public abstract Double getCabimentos();

    public abstract void setCabimentos(Double cabimentos);

    public abstract Double getJustifications();

    public abstract void setJustifications(Double justifications);

    public abstract String getProjectCode();

    public abstract void setProjectCode(String projectCode);

    public abstract Double getTotal();

    public abstract void setTotal(Double total);
}