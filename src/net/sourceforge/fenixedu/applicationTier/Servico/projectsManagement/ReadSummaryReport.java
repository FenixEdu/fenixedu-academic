/*
 * Created on Jan 12, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoCoordinatorReport;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoRubric;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoSummaryReportLine;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.projectsManagement.ISummaryReportLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadSummaryReport implements IService {

    public ReadSummaryReport() {
    }

    public InfoCoordinatorReport run(String userView, Integer coordinatorCode) throws ExcepcaoPersistencia {
        InfoCoordinatorReport infoReport = null;
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Integer userNumber = getUserNumber(persistentSuport, userView);
        if (userNumber != null) {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();
            if (coordinatorCode == null)
                coordinatorCode = userNumber;
            List lines = null;
            if (userNumber.equals(coordinatorCode)) {
                lines = p.getIPersistentSummaryReport().readByCoordinatorCode(ReportType.SUMMARY, coordinatorCode);
            } else {
                List<Integer> projectCodes = persistentSuport.getIPersistentProjectAccess().readProjectCodesByPersonUsernameAndCoordinator(userView,
                        coordinatorCode, false);
                if (projectCodes != null && projectCodes.size() != 0)
                    lines = p.getIPersistentSummaryReport().readByCoordinatorAndProjectCodes(ReportType.SUMMARY, coordinatorCode, projectCodes);
            }
            if (lines != null) {
                infoReport = new InfoCoordinatorReport();
                infoReport.setInfoCoordinator(InfoRubric.newInfoFromDomain(p.getIPersistentProjectUser().readProjectCoordinator(coordinatorCode)));
                List<InfoSummaryReportLine> infoLines = new ArrayList<InfoSummaryReportLine>();

                for (int line = 0; line < lines.size(); line++)
                    infoLines.add(InfoSummaryReportLine.newInfoFromDomain((ISummaryReportLine) lines.get(line)));
                infoReport.setLines(infoLines);
            }
        }
        return infoReport;
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