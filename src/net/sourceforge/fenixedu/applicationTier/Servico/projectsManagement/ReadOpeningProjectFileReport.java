/*
 * Created on Jan 12, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoOpeningProjectFileReport;
import net.sourceforge.fenixedu.domain.projectsManagement.OpeningProjectFileReport;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentOpeningProjectFileReport;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentProject;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentProjectMemberBudget;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 */
public class ReadOpeningProjectFileReport extends FenixService {

    public InfoOpeningProjectFileReport run(String username, String costCenter, Integer projectCode, Boolean it, String userNumber)
	    throws ExcepcaoPersistencia {
	PersistentProject persistentProject = new PersistentProject();
	InfoOpeningProjectFileReport infoOpeningProjectFileReport = new InfoOpeningProjectFileReport();
	if (userNumber != null
		&& projectCode != null
		&& (persistentProject.isUserProject(new Integer(userNumber), projectCode, it) || ProjectAccess
			.getByUsernameAndProjectCode(username, projectCode, it) != null)
		|| (costCenter != null && ProjectAccess.getAllByPersonUsernameAndDatesAndCostCenter(username, costCenter, it) != null)) {

	    PersistentOpeningProjectFileReport persistentOpeningProjectFile = new PersistentOpeningProjectFileReport();

	    OpeningProjectFileReport openingProjectFileReport = persistentOpeningProjectFile.getCompleteReport(
		    ReportType.OPENING_PROJECT_FILE, projectCode, it);
	    if (openingProjectFileReport != null) {
		openingProjectFileReport.setProjectFinancialEntities(persistentOpeningProjectFile.getReportRubricList(
			ReportType.PROJECT_FINANCIAL_ENTITIES, projectCode, true, it));

		openingProjectFileReport.setProjectRubricBudget(persistentOpeningProjectFile.getReportRubricList(
			ReportType.PROJECT_RUBRIC_BUDGET, projectCode, true, it));

		openingProjectFileReport.setProjectInvestigationTeam(persistentOpeningProjectFile.getReportRubricList(
			ReportType.PROJECT_INVESTIGATION_TEAM, projectCode, false, it));

		openingProjectFileReport.setProjectMembersBudget(new PersistentProjectMemberBudget().getCompleteReport(
			ReportType.PROJECT_MEMBERS, projectCode, it));

		infoOpeningProjectFileReport = InfoOpeningProjectFileReport.newInfoFromDomain(openingProjectFileReport);
	    }
	}
	return infoOpeningProjectFileReport;
    }

}