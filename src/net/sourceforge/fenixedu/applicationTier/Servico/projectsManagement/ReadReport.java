/*
 * Created on Jan 12, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.UserView;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoMovementReport;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProject;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProjectReport;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoRevenueReportLine;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.projectsManagement.IMovementReport;
import net.sourceforge.fenixedu.domain.projectsManagement.IRevenueReportLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadReport implements IService {

    public ReadReport() {
    }

    public InfoProjectReport run(UserView userView, ReportType reportType, Integer projectCode) throws ExcepcaoPersistencia {
        InfoProjectReport infoReport = null;
        ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
        Integer userNumber = getUserNumber(persistentSuport, userView);

        if (userNumber != null) {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            infoReport = new InfoProjectReport();
            List infoLines = new ArrayList();
            if (reportType.equals(ReportType.REVENUE)) {
                if (projectCode != null
                        && (p.getIPersistentProject().isUserProject(userNumber, projectCode) || persistentSuport.getIPersistentProjectAccess()
                                .hasPersonProjectAccess(userView.getUtilizador(), projectCode))) {
                    infoReport.setInfoProject(InfoProject.newInfoFromDomain(p.getIPersistentProject().readProject(projectCode)));
                    List lines = p.getIPersistentRevenueReport().getCompleteReport(reportType, projectCode);
                    for (int i = 0; i < lines.size(); i++)
                        infoLines.add(InfoRevenueReportLine.newInfoFromDomain((IRevenueReportLine) lines.get(i)));
                }
            } else if (reportType.equals(ReportType.ADIANTAMENTOS) || reportType.equals(ReportType.CABIMENTOS)) {
                if (projectCode != null
                        && (p.getIPersistentProject().isUserProject(userNumber, projectCode) || persistentSuport.getIPersistentProjectAccess()
                                .hasPersonProjectAccess(userView.getUtilizador(), projectCode))) {
                    infoReport.setInfoProject(InfoProject.newInfoFromDomain(p.getIPersistentProject().readProject(projectCode)));
                    List lines = p.getIPersistentMovementReport().getCompleteReport(reportType, projectCode);
                    for (int i = 0; i < lines.size(); i++)
                        infoLines.add(InfoMovementReport.newInfoFromDomain((IMovementReport) lines.get(i)));
                }
            }
            infoReport.setLines(infoLines);
        }

        return infoReport;
    }

    private Integer getUserNumber(ISuportePersistente sp, IUserView userView) throws ExcepcaoPersistencia {
        Integer userNumber = null;
        ITeacher teacher = sp.getIPersistentTeacher().readTeacherByUsername(userView.getUtilizador());
        if (teacher != null)
            userNumber = teacher.getTeacherNumber();
        else {
            IPerson person = sp.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());
            IEmployee employee = sp.getIPersistentEmployee().readByPerson(person);
            if (employee != null)
                userNumber = employee.getEmployeeNumber();
        }
        return userNumber;
    }
}