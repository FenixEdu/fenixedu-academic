/*
 * Created on Jan 12, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
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
import net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentGeneratedOverheadsReport;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentOverheadsSummaryReport;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentProjectUser;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentTransferedOverheadsReport;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 */
public class ReadOverheadReport extends FenixService {

    public InfoOverheadReport run(String userView, String costCenter, ReportType reportType, String projectCode, BackendInstance instance,
	    String userNumber) throws ExcepcaoPersistencia {
	InfoOverheadReport infoReport = new InfoOverheadReport();
	List<IReportLine> infoLines = new ArrayList<IReportLine>();
	PersistentProjectUser persistentProjectUser = new PersistentProjectUser();
	infoReport.setInfoCostCenter(InfoRubric.newInfoFromDomain(persistentProjectUser.getCostCenterByID(
		new Integer(costCenter), instance)));
	if (userNumber.equals(costCenter) || hasFullCostCenterAccess(userView, costCenter, instance)) {
	    if (reportType.equals(ReportType.GENERATED_OVERHEADS)) {
		List<IGeneratedOverheadsReportLine> lines = new PersistentGeneratedOverheadsReport().getCompleteReport(
			reportType, new Integer(costCenter), instance);
		for (IGeneratedOverheadsReportLine generatedOverheadsReportLine : lines) {
		    infoLines.add(InfoGeneratedOverheadsReportLine.newInfoFromDomain(generatedOverheadsReportLine));
		}
	    } else if (reportType.equals(ReportType.TRANSFERED_OVERHEADS)) {
		List<ITransferedOverheadsReportLine> lines = new PersistentTransferedOverheadsReport().getCompleteReport(
			reportType, new Integer(costCenter), instance);
		for (ITransferedOverheadsReportLine transferedOverheadsReportLine : lines) {
		    infoLines.add(InfoTransferedOverheadsReportLine.newInfoFromDomain(transferedOverheadsReportLine));
		}
	    } else if (reportType.equals(ReportType.OVERHEADS_SUMMARY)) {
		List<IOverheadsSummaryReportLine> lines = new PersistentOverheadsSummaryReport().getCompleteReport(reportType,
			new Integer(costCenter), instance);
		for (IOverheadsSummaryReportLine overheadsSummaryReportLine : lines) {
		    infoLines.add(InfoOverheadsSummaryReportLine.newInfoFromDomain(overheadsSummaryReportLine));
		}
	    }
	}
	infoReport.setLines(infoLines);
	return infoReport;
    }

    private boolean hasFullCostCenterAccess(String username, String costCenter, BackendInstance instance) {
	List<ProjectAccess> accesses = ProjectAccess.getAllByPersonUsernameAndDatesAndCostCenter(username, costCenter, instance);
	for (ProjectAccess access : accesses) {
	    if (access.getKeyProject() == null && access.getCostCenter()
		    && access.getKeyProjectCoordinator().equals(new Integer(costCenter))) {
		return true;
	    }
	}
	return false;
    }
}