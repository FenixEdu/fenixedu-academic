/*
 * Created on Jan 12, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoAdiantamentosReportLine;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoCabimentosReportLine;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoExpensesReport;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoExpensesReportLine;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoProject;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoSummaryEURReportLine;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoSummaryPTEReportLine;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.projectsManagement.IAdiantamentosReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.ICabimentosReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.IExpensesReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.ISummaryEURReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.ISummaryPTEReportLine;
import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.UserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentExpensesReport;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

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
        if (userNumber != null
                && projectCode != null
                && (p.getIPersistentProject().isUserProject(userNumber, projectCode) || persistentSuport.getIPersistentProjectAccess()
                        .hasPersonProjectAccess(userView.getUtilizador(), projectCode))) {

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