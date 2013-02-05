/*
 * Created on Jan 12, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.IReportLine;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoMovementReport;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProjectBudgetaryBalanceReportLine;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProjectReport;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoRevenueReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.IMovementReport;
import net.sourceforge.fenixedu.domain.projectsManagement.IProjectBudgetaryBalanceReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.IRevenueReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentMovementReport;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentProject;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentProjectBudgetaryBalanceReport;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentRevenueReport;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 */
public class ReadReport extends FenixService {

    public InfoProjectReport run(String userView, String costCenter, ReportType reportType, String projectCode,
            BackendInstance instance, String userNumber) throws ExcepcaoPersistencia {
        InfoProjectReport infoReport = new InfoProjectReport();
        List<IReportLine> infoLines = new ArrayList<IReportLine>();

        PersistentProject persistentProject = new PersistentProject();
        if (projectCode != null
                && (persistentProject.isUserProject(new Integer(userNumber), projectCode, instance) || ProjectAccess
                        .getByUsernameAndProjectCode(userView, projectCode, instance) != null)
                || (costCenter != null && ProjectAccess.getAllByPersonUsernameAndDatesAndCostCenter(userView, costCenter,
                        instance) != null)) {
            infoReport.setInfoProject(persistentProject.readProject(projectCode, instance));
            if (reportType.equals(ReportType.REVENUE)) {
                List<IRevenueReportLine> lines =
                        new PersistentRevenueReport().getCompleteReport(reportType, projectCode, instance);
                for (IRevenueReportLine revenueReportLine : lines) {
                    infoLines.add(InfoRevenueReportLine.newInfoFromDomain(revenueReportLine));
                }
            } else if (reportType.equals(ReportType.ADIANTAMENTOS) || reportType.equals(ReportType.CABIMENTOS)) {
                List<IMovementReport> lines = new PersistentMovementReport().getCompleteReport(reportType, projectCode, instance);
                for (IMovementReport movementReport : lines) {
                    infoLines.add(InfoMovementReport.newInfoFromDomain(movementReport));
                }
            } else if (reportType.equals(ReportType.PROJECT_BUDGETARY_BALANCE)) {
                List<IProjectBudgetaryBalanceReportLine> lines =
                        new PersistentProjectBudgetaryBalanceReport().getCompleteReport(reportType, projectCode, instance);
                for (IProjectBudgetaryBalanceReportLine projectBudgetaryBalanceReportLine : lines) {
                    infoLines.add(InfoProjectBudgetaryBalanceReportLine.newInfoFromDomain(projectBudgetaryBalanceReportLine));
                }
            }
        }
        infoReport.setLines(infoLines);
        return infoReport;
    }
}