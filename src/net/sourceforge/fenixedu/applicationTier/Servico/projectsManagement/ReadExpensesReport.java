/*
 * Created on Jan 12, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoAdiantamentosReportLine;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoCabimentosReportLine;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoExpensesReport;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoExpensesReportLine;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProject;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoSummaryEURReportLine;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoSummaryPTEReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.IExpensesReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentExpensesReport;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentExpensesResume;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentSuportOracle;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 */
public class ReadExpensesReport extends Service {

    public InfoExpensesReport run(String username, String costCenter, ReportType reportType, Integer projectCode, String rubric, String userNumber)
            throws ExcepcaoPersistencia {
        IPersistentSuportOracle p = PersistentSuportOracle.getProjectDBInstance();
        Integer coordID = new Integer(userNumber);
        InfoExpensesReport infoReport = new InfoExpensesReport();
        if (coordID != null
                && projectCode != null
                && (p.getIPersistentProject().isUserProject(coordID, projectCode) 
                        || ProjectAccess.getByUsernameAndProjectCode(username, projectCode) != null
                        || (costCenter!=null && ProjectAccess.getAllByPersonUsernameAndDatesAndCostCenter(username, costCenter)!= null))){
            List infoLines = new ArrayList();
            infoReport.setInfoProject(InfoProject.newInfoFromDomain(p.getIPersistentProject().readProject(projectCode)));
            List lines = null;
            IPersistentExpensesReport persistentExpensesReport = null;
            if (reportType.equals(ReportType.EXPENSES)) {
                persistentExpensesReport = p.getIPersistentExpensesReport();
            } else {
                persistentExpensesReport = p.getIPersistentCompleteExpensesReport();
            }
            if (rubric == null || rubric.equals(""))
                lines = persistentExpensesReport.getCompleteReport(reportType, projectCode);
            else
                lines = persistentExpensesReport.getReportByRubric(reportType, projectCode, rubric);
            for (int i = 0; i < lines.size(); i++)
                infoLines.add(InfoExpensesReportLine.newInfoFromDomain((IExpensesReportLine) lines.get(i)));
            infoReport.setLines(infoLines);
            infoReport.setRubricList(persistentExpensesReport.getRubricList(reportType, projectCode));

            IPersistentExpensesResume persistentExpensesResume = p.getIPersistentExpensesResume();

            infoReport.setAdiantamentosReport(InfoAdiantamentosReportLine.newInfoFromDomain(persistentExpensesResume.getAdiantamentosReportLine(
                    ReportType.SUMMARY_ADIANTAMENTOS, projectCode)));
            infoReport.setCabimentosReport(InfoCabimentosReportLine.newInfoFromDomain(persistentExpensesResume.getCabimentosReportLine(
                    ReportType.SUMMARY_CABIMENTOS, projectCode)));
            infoReport.setSummaryEURReport(InfoSummaryEURReportLine.newInfoFromDomain(persistentExpensesResume.getSummaryEURReportLine(
                    ReportType.SUMMARY_EUR, projectCode)));
            infoReport.setSummaryPTEReport(InfoSummaryPTEReportLine.newInfoFromDomain(persistentExpensesResume.getSummaryPTEReportLine(
                    ReportType.SUMMARY_PTE, projectCode)));
        }
        return infoReport;
    }
}