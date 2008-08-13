/*
 * Created on Jan 12, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.IReportLine;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoMovementReport;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProject;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProjectBudgetaryBalanceReportLine;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProjectReport;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoRevenueReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.IMovementReport;
import net.sourceforge.fenixedu.domain.projectsManagement.IProjectBudgetaryBalanceReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.IRevenueReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 */
public class ReadReport extends Service {

    public InfoProjectReport run(String userView, String costCenter, ReportType reportType, Integer projectCode, String userNumber)
	    throws ExcepcaoPersistencia {
	InfoProjectReport infoReport = new InfoProjectReport();
	List<IReportLine> infoLines = new ArrayList<IReportLine>();

	PersistentSuportOracle p = PersistentSuportOracle.getProjectDBInstance();
	if (projectCode != null
		&& (p.getIPersistentProject().isUserProject(new Integer(userNumber), projectCode) || ProjectAccess
			.getByUsernameAndProjectCode(userView, projectCode) != null)
		|| (costCenter != null && ProjectAccess.getAllByPersonUsernameAndDatesAndCostCenter(userView, costCenter) != null)) {
	    infoReport.setInfoProject(InfoProject.newInfoFromDomain(p.getIPersistentProject().readProject(projectCode)));
	    if (reportType.equals(ReportType.REVENUE)) {
		List<IRevenueReportLine> lines = p.getIPersistentRevenueReport().getCompleteReport(reportType, projectCode);
		for (IRevenueReportLine revenueReportLine : lines) {
		    infoLines.add(InfoRevenueReportLine.newInfoFromDomain(revenueReportLine));
		}
	    } else if (reportType.equals(ReportType.ADIANTAMENTOS) || reportType.equals(ReportType.CABIMENTOS)) {
		List<IMovementReport> lines = p.getIPersistentMovementReport().getCompleteReport(reportType, projectCode);
		for (IMovementReport movementReport : lines) {
		    infoLines.add(InfoMovementReport.newInfoFromDomain(movementReport));
		}
	    } else if (reportType.equals(ReportType.PROJECT_BUDGETARY_BALANCE)) {
		List<IProjectBudgetaryBalanceReportLine> lines = p.getIPersistentProjectBudgetaryBalanceReport()
			.getCompleteReport(reportType, projectCode);
		for (IProjectBudgetaryBalanceReportLine projectBudgetaryBalanceReportLine : lines) {
		    infoLines.add(InfoProjectBudgetaryBalanceReportLine.newInfoFromDomain(projectBudgetaryBalanceReportLine));
		}
	    }
	}
	infoReport.setLines(infoLines);
	return infoReport;
    }
}