/*
 * Created on Jan 12, 2005
 */

package ServidorAplicacao.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.projectsManagement.InfoAdiantamentosReportLine;
import DataBeans.projectsManagement.InfoCabimentosReportLine;
import DataBeans.projectsManagement.InfoExpensesReport;
import DataBeans.projectsManagement.InfoExpensesReportLine;
import DataBeans.projectsManagement.InfoProject;
import DataBeans.projectsManagement.InfoSummaryEURReportLine;
import DataBeans.projectsManagement.InfoSummaryPTEReportLine;
import Dominio.IEmployee;
import Dominio.IPessoa;
import Dominio.ITeacher;
import Dominio.projectsManagement.IAdiantamentosReportLine;
import Dominio.projectsManagement.ICabimentosReportLine;
import Dominio.projectsManagement.IExpensesReportLine;
import Dominio.projectsManagement.ISummaryEURReportLine;
import Dominio.projectsManagement.ISummaryPTEReportLine;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;
import ServidorPersistenteOracle.IPersistentExpensesReport;
import ServidorPersistenteOracle.Oracle.PersistentSuportOracle;
import Util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 */
public class ReadExpensesReport implements IService {

    public ReadExpensesReport() {
    }

    public InfoExpensesReport run(InfoExpensesReport infoReport, UserView userView, ReportType reportType, Integer projectCode, String rubric)
            throws FenixServiceException, ExcepcaoPersistencia {

        ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
        Integer userNumber = getUserNumber(persistentSuport, userView);
        PersistentSuportOracle p = PersistentSuportOracle.getInstance();
        if (userNumber != null && projectCode != null && p.getIPersistentProject().isUserProject(userNumber, projectCode)) {
            List infoLines = new ArrayList();

            if (infoReport == null) {
                infoReport = new InfoExpensesReport();

                infoReport.setInfoProject(InfoProject.newInfoFromDomain(p.getIPersistentProject().readProject(projectCode)));
                List lines = null;
                IPersistentExpensesReport persistentExpensesReport = p.getIPersistentExpensesReport();
                if (rubric == null || rubric.equals(""))
                    lines = persistentExpensesReport.getCompleteReport(reportType, projectCode);
                else
                    lines = persistentExpensesReport.getReportByRubric(reportType, projectCode, rubric);
                for (int i = 0; i < lines.size(); i++)
                    infoLines.add(InfoExpensesReportLine.newInfoFromDomain((IExpensesReportLine) lines.get(i)));
                infoReport.setLines(infoLines);
                infoReport.setRubricList(persistentExpensesReport.getRubricList(reportType, projectCode));
            } else {

                List lines = p.getIPersistentAdiantamentosReport().getCompleteReport(ReportType.SUMMARY_ADIANTAMENTOS, projectCode);
                for (int i = 0; i < lines.size(); i++)
                    infoLines.add(InfoAdiantamentosReportLine.newInfoFromDomain((IAdiantamentosReportLine) lines.get(i)));
                infoReport.setAdiantamentosReport(infoLines);
                infoLines = new ArrayList();
                lines = p.getIPersistentCabimentosReport().getCompleteReport(ReportType.SUMMARY_CABIMENTOS, projectCode);
                for (int i = 0; i < lines.size(); i++)
                    infoLines.add(InfoCabimentosReportLine.newInfoFromDomain((ICabimentosReportLine) lines.get(i)));
                infoReport.setCabimentosReport(infoLines);
                infoLines = new ArrayList();
                lines = p.getIPersistentSummaryPTEReport().getCompleteReport(ReportType.SUMMARY_PTE, projectCode);
                for (int i = 0; i < lines.size(); i++)
                    infoLines.add(InfoSummaryPTEReportLine.newInfoFromDomain((ISummaryPTEReportLine) lines.get(i)));
                infoReport.setSummaryPTEReport(infoLines);
                infoLines = new ArrayList();
                lines = p.getIPersistentSummaryEURReport().getCompleteReport(ReportType.SUMMARY_EUR, projectCode);
                for (int i = 0; i < lines.size(); i++)
                    infoLines.add(InfoSummaryEURReportLine.newInfoFromDomain((ISummaryEURReportLine) lines.get(i)));
                infoReport.setSummaryEURReport(infoLines);
            }

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