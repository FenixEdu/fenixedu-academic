/*
 * Created on Jan 12, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoOpeningProjectFileReport;
import net.sourceforge.fenixedu.domain.projectsManagement.IOpeningProjectFileReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentOpeningProjectFileReport;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadOpeningProjectFileReport implements IService {

    public ReadOpeningProjectFileReport() {
    }

    public InfoOpeningProjectFileReport run(String userView, String costCenter, Integer projectCode, String userNumber) throws ExcepcaoPersistencia {

        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        PersistentSuportOracle p = PersistentSuportOracle.getInstance();
        InfoOpeningProjectFileReport infoOpeningProjectFileReport = new InfoOpeningProjectFileReport();
        if (userNumber != null
                && projectCode != null
                && (p.getIPersistentProject().isUserProject(new Integer(userNumber), projectCode) || persistentSuport.getIPersistentProjectAccess()
                        .hasPersonProjectAccess(userView, projectCode))) {

            IPersistentOpeningProjectFileReport persistentOpeningProjectFile = p.getIPersistentOpeningProjectFileReport();

            IOpeningProjectFileReport openingProjectFileReport = persistentOpeningProjectFile.getCompleteReport(ReportType.OPENING_PROJECT_FILE,
                    projectCode);
            if (openingProjectFileReport != null) {
                openingProjectFileReport.setProjectFinancialEntities(persistentOpeningProjectFile.getReportRubricList(
                        ReportType.PROJECT_FINANCIAL_ENTITIES, projectCode, true));

                openingProjectFileReport.setProjectRubricBudget(persistentOpeningProjectFile.getReportRubricList(ReportType.PROJECT_RUBRIC_BUDGET,
                        projectCode, true));

                openingProjectFileReport.setProjectInvestigationTeam(persistentOpeningProjectFile.getReportRubricList(
                        ReportType.PROJECT_INVESTIGATION_TEAM, projectCode, false));

                openingProjectFileReport.setProjectMembersBudget(p.getIPersistentProjectMemberBudget().getCompleteReport(ReportType.PROJECT_MEMBERS,
                        projectCode));

                infoOpeningProjectFileReport = InfoOpeningProjectFileReport.newInfoFromDomain(openingProjectFileReport);
            }
        }
        return infoOpeningProjectFileReport;
    }

}