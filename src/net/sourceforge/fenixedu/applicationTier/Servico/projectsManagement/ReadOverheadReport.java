/*
 * Created on Jan 12, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.IReportLine;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoGeneratedOverheadsReportLine;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoOverheadReport;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoOverheadsSummaryReportLine;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoRubric;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoTransferedOverheadsReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.IGeneratedOverheadsReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.IOverheadsSummaryReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.ITransferedOverheadsReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentSuportOracle;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 */
public class ReadOverheadReport extends Service {

    public InfoOverheadReport run(String userView, String costCenter, ReportType reportType, Integer projectCode,
	    String userNumber) throws ExcepcaoPersistencia {
	InfoOverheadReport infoReport = new InfoOverheadReport();
	List<IReportLine> infoLines = new ArrayList<IReportLine>();
	IPersistentSuportOracle p = PersistentSuportOracle.getProjectDBInstance();
	infoReport.setInfoCostCenter(InfoRubric.newInfoFromDomain(p.getIPersistentProjectUser().getCostCenterByID(
		new Integer(costCenter))));
	if (userNumber.equals(costCenter) || hasFullCostCenterAccess(userView, costCenter)) {
	    if (reportType.equals(ReportType.GENERATED_OVERHEADS)) {
		List<IGeneratedOverheadsReportLine> lines = p.getIPersistentGeneratedOverheadsReport().getCompleteReport(
			reportType, new Integer(costCenter));
		for (IGeneratedOverheadsReportLine generatedOverheadsReportLine : lines) {
		    infoLines.add(InfoGeneratedOverheadsReportLine.newInfoFromDomain(generatedOverheadsReportLine));
		}
	    } else if (reportType.equals(ReportType.TRANSFERED_OVERHEADS)) {
		List<ITransferedOverheadsReportLine> lines = p.getIPersistentTransferedOverheadsReport().getCompleteReport(
			reportType, new Integer(costCenter));
		for (ITransferedOverheadsReportLine transferedOverheadsReportLine : lines) {
		    infoLines.add(InfoTransferedOverheadsReportLine.newInfoFromDomain(transferedOverheadsReportLine));
		}
	    } else if (reportType.equals(ReportType.OVERHEADS_SUMMARY)) {
		List<IOverheadsSummaryReportLine> lines = p.getIPersistentOverheadsSummaryReport().getCompleteReport(reportType,
			new Integer(costCenter));
		for (IOverheadsSummaryReportLine overheadsSummaryReportLine : lines) {
		    infoLines.add(InfoOverheadsSummaryReportLine.newInfoFromDomain(overheadsSummaryReportLine));
		}
	    }
	}
	infoReport.setLines(infoLines);
	return infoReport;
    }

    private boolean hasFullCostCenterAccess(String username, String costCenter) {
	List<ProjectAccess> accesses = ProjectAccess.getAllByPersonUsernameAndDatesAndCostCenter(username, costCenter);
	for (ProjectAccess access : accesses) {
	    if (access.getKeyProject() == null && access.getCostCenter()
		    && access.getKeyProjectCoordinator().equals(new Integer(costCenter))) {
		return true;
	    }
	}
	return false;
    }
}