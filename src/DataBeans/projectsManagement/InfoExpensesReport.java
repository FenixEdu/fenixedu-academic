/*
 * Created on Jan 12, 2005
 *
 */
package DataBeans.projectsManagement;

import java.util.List;

/**
 * @author Susana Fernandes
 * 
 */
public class InfoExpensesReport extends InfoProjectReport {

    List summaryPTEReport;

    List summaryEURReport;

    List adiantamentosReport;

    List cabimentosReport;

    List rubricList;

    public List getAdiantamentosReport() {
        return adiantamentosReport;
    }

    public void setAdiantamentosReport(List adiantamentosReport) {
        this.adiantamentosReport = adiantamentosReport;
    }

    public List getCabimentosReport() {
        return cabimentosReport;
    }

    public void setCabimentosReport(List cabimentosReport) {
        this.cabimentosReport = cabimentosReport;
    }

    public List getRubricList() {
        return rubricList;
    }

    public void setRubricList(List rubricList) {
        this.rubricList = rubricList;
    }

    public List getSummaryEURReport() {
        return summaryEURReport;
    }

    public void setSummaryEURReport(List summaryEURReport) {
        this.summaryEURReport = summaryEURReport;
    }

    public List getSummaryPTEReport() {
        return summaryPTEReport;
    }

    public void setSummaryPTEReport(List summaryPTEReport) {
        this.summaryPTEReport = summaryPTEReport;
    }
}
