/*
 * Created on Jan 12, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoOpeningProjectFileReport;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
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

    public InfoOpeningProjectFileReport run(String userView, Integer projectCode) throws ExcepcaoPersistencia {

        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Integer userNumber = getUserNumber(persistentSuport, userView);
        PersistentSuportOracle p = PersistentSuportOracle.getInstance();
        InfoOpeningProjectFileReport infoOpeningProjectFileReport = new InfoOpeningProjectFileReport();
        if (userNumber != null
                && projectCode != null
                && (p.getIPersistentProject().isUserProject(userNumber, projectCode) || persistentSuport.getIPersistentProjectAccess()
                        .hasPersonProjectAccess(userView, projectCode))) {

            IPersistentOpeningProjectFileReport persistentOpeningProjectFile = p.getIPersistentOpeningProjectFileReport();

            IOpeningProjectFileReport openingProjectFileReport = persistentOpeningProjectFile.getCompleteReport(ReportType.OPENING_PROJECT_FILE,
                    projectCode);

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
        return infoOpeningProjectFileReport;
    }

    private Integer getUserNumber(ISuportePersistente sp, String userView) throws ExcepcaoPersistencia {
        Integer userNumber = null;
        ITeacher teacher = sp.getIPersistentTeacher().readTeacherByUsername(userView);
        if (teacher != null)
            userNumber = teacher.getTeacherNumber();
        else {
            IPerson person = sp.getIPessoaPersistente().lerPessoaPorUsername(userView);
            IEmployee employee = sp.getIPersistentEmployee().readByPerson(person);
            if (employee != null)
                userNumber = employee.getEmployeeNumber();
        }
        return userNumber;
    }
}