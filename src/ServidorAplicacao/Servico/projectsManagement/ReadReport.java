/*
 * Created on Jan 12, 2005
 */

package ServidorAplicacao.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.projectsManagement.InfoAdiantamentosReportLine;
import DataBeans.projectsManagement.InfoCabimentosReportLine;
import DataBeans.projectsManagement.InfoExpensesReportLine;
import DataBeans.projectsManagement.InfoProject;
import DataBeans.projectsManagement.InfoProjectReport;
import DataBeans.projectsManagement.InfoRevenueReportLine;
import DataBeans.projectsManagement.InfoSummaryEURReportLine;
import DataBeans.projectsManagement.InfoSummaryPTEReportLine;
import DataBeans.projectsManagement.InfoSummaryReportLine;
import Dominio.IEmployee;
import Dominio.IPessoa;
import Dominio.ITeacher;
import Dominio.projectsManagement.IAdiantamentosReportLine;
import Dominio.projectsManagement.ICabimentosReportLine;
import Dominio.projectsManagement.IExpensesReportLine;
import Dominio.projectsManagement.IRevenueReportLine;
import Dominio.projectsManagement.ISummaryEURReportLine;
import Dominio.projectsManagement.ISummaryPTEReportLine;
import Dominio.projectsManagement.ISummaryReportLine;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistenteOracle.Oracle.PersistentSuportOracle;
import Util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 */
public class ReadReport implements IService {

    public ReadReport() {
    }

    public InfoProjectReport run(UserView userView, ReportType reportType, Integer projectCode) throws FenixServiceException,
            ExcepcaoPersistencia {
        InfoProjectReport infoReport = null;
        ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
        Integer userNumber = getUserNumber(persistentSuport, userView);

        if (userNumber != null) {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            infoReport = new InfoProjectReport();
            List infoLines = new ArrayList();
            if (reportType.equals(ReportType.REVENUE)) {
                if (projectCode != null && p.getIPersistentProject().isUserProject(userNumber, projectCode)) {
                    infoReport.setInfoProject(InfoProject.newInfoFromDomain(p.getIPersistentProject().readProject(projectCode)));
                    List lines = p.getIPersistentRevenueReport().getCompleteReport(reportType, projectCode);
                    for (int i = 0; i < lines.size(); i++)
                        infoLines.add(InfoRevenueReportLine.newInfoFromDomain((IRevenueReportLine) lines.get(i)));
                }
            } else if (reportType.equals(ReportType.SUMMARY)) {
                Integer coordinatorId = p.getIPersistentProjectUser().getUserCoordId(userNumber);
                if (coordinatorId != null) {
                    List lines = p.getIPersistentSummaryReport().getCompleteReport(reportType, coordinatorId);
                    for (int i = 0; i < lines.size(); i++)
                        infoLines.add(InfoSummaryReportLine.newInfoFromDomain((ISummaryReportLine) lines.get(i)));
                }
            } 
            infoReport.setLines(infoLines);
        }

        return infoReport;
    }

    private Integer getUserNumber(ISuportePersistente sp, IUserView userView) throws ExcepcaoPersistencia {
        Integer userNumber = null;
        ITeacher teacher = (ITeacher) sp.getIPersistentTeacher().readTeacherByUsername(userView.getUtilizador());
        if (teacher != null)
            userNumber = teacher.getTeacherNumber();
        else {
            IPessoa person = sp.getIPessoaPersistente().lerPessoaPorUsername(userView.getUtilizador());
            IEmployee employee = (IEmployee) sp.getIPersistentEmployee().readByPerson(person);
            if (employee != null)
                userNumber = employee.getEmployeeNumber();
        }
        return userNumber;
    }
}