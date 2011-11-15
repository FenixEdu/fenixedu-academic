/*
 * Created on Jan 12, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoAdiantamentosReportLine;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoCabimentosReportLine;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoExpensesReport;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoExpensesReportLine;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoSummaryEURReportLine;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoSummaryPTEReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.IExpensesReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentExpensesReport;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentCompleteExpensesReport;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentExpensesReport;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentExpensesResume;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentProject;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 */
public class ReadExpensesReport extends FenixService {

    public InfoExpensesReport run(String username, String costCenter, ReportType reportType, String projectCode, String rubric,
	    BackendInstance instance, String userNumber) throws ExcepcaoPersistencia {
	Integer coordID = new Integer(userNumber);
	InfoExpensesReport infoReport = new InfoExpensesReport();
	PersistentProject persistentProject = new PersistentProject();
	if (coordID != null
		&& projectCode != null
		&& (persistentProject.isUserProject(coordID, projectCode, instance)
			|| ProjectAccess.getByUsernameAndProjectCode(username, projectCode, instance) != null || (costCenter != null && ProjectAccess
			.getAllByPersonUsernameAndDatesAndCostCenter(username, costCenter, instance) != null))) {
	    List infoLines = new ArrayList();
	    infoReport.setInfoProject(persistentProject.readProject(projectCode, instance));
	    List lines = null;
	    IPersistentExpensesReport persistentExpensesReport = null;
	    if (reportType.equals(ReportType.EXPENSES)) {
		persistentExpensesReport = new PersistentExpensesReport();
	    } else {
		persistentExpensesReport = new PersistentCompleteExpensesReport();
	    }
	    if (rubric == null || rubric.equals(""))
		lines = persistentExpensesReport.getCompleteReport(reportType, projectCode, instance);
	    else
		lines = persistentExpensesReport.getReportByRubric(reportType, projectCode, rubric, instance);
	    for (int i = 0; i < lines.size(); i++)
		infoLines.add(InfoExpensesReportLine.newInfoFromDomain((IExpensesReportLine) lines.get(i)));
	    infoReport.setLines(infoLines);
	    infoReport.setRubricList(persistentExpensesReport.getRubricList(reportType, projectCode, instance));

	    PersistentExpensesResume persistentExpensesResume = new PersistentExpensesResume();

	    infoReport.setAdiantamentosReport(InfoAdiantamentosReportLine.newInfoFromDomain(persistentExpensesResume
		    .getAdiantamentosReportLine(ReportType.SUMMARY_ADIANTAMENTOS, projectCode, instance)));
	    infoReport.setCabimentosReport(InfoCabimentosReportLine.newInfoFromDomain(persistentExpensesResume
		    .getCabimentosReportLine(ReportType.SUMMARY_CABIMENTOS, projectCode, instance)));
	    infoReport.setSummaryEURReport(InfoSummaryEURReportLine.newInfoFromDomain(persistentExpensesResume
		    .getSummaryEURReportLine(ReportType.SUMMARY_EUR, projectCode, instance)));
	    infoReport.setSummaryPTEReport(InfoSummaryPTEReportLine.newInfoFromDomain(persistentExpensesResume
		    .getSummaryPTEReportLine(ReportType.SUMMARY_PTE, projectCode, instance)));
	}
	return infoReport;
    }
}