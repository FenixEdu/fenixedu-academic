/*
 * Created on Jan 12, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.UserView;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoCoordinatorReport;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoRubric;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoSummaryReportLine;
import net.sourceforge.fenixedu.domain.IEmployee;
import net.sourceforge.fenixedu.domain.IPerson;
import net.sourceforge.fenixedu.domain.ITeacher;
import net.sourceforge.fenixedu.domain.projectsManagement.ISummaryReportLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadSummaryReport implements IService {

    public ReadSummaryReport() {
    }

    public List run(UserView userView) throws ExcepcaoPersistencia {
        List infoProjectReportList = new ArrayList();
        ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();
        Integer userNumber = getUserNumber(persistentSuport, userView);
        if (userNumber != null) {
            PersistentSuportOracle p = PersistentSuportOracle.getInstance();

            Integer thisCoordinator = p.getIPersistentProjectUser().getUserCoordId(userNumber);
            List coordinatorsCodes = persistentSuport.getIPersistentProjectAccess().readCoordinatorsCodesByPersonUsernameAndDates(userView.getUtilizador());
            if (thisCoordinator != null)
                coordinatorsCodes.add(thisCoordinator);
            for (int coord = 0; coord < coordinatorsCodes.size(); coord++) {
                InfoCoordinatorReport infoReport = new InfoCoordinatorReport();
                List infoLines = new ArrayList();
                infoReport.setInfoCoordinator(InfoRubric.newInfoFromDomain(p.getIPersistentProjectUser().readProjectCoordinator(
                        (Integer) coordinatorsCodes.get(coord))));
                List projectCodes = persistentSuport.getIPersistentProjectAccess().readProjectCodesByPersonUsernameAndCoordinator(
                        userView.getUtilizador(), new Integer(infoReport.getInfoCoordinator().getCode()), false);
                List lines = p.getIPersistentSummaryReport().readByCoordinatorAndProjectCodes(ReportType.SUMMARY,
                        new Integer(infoReport.getInfoCoordinator().getCode()), projectCodes);
                for (int line = 0; line < lines.size(); line++)
                    infoLines.add(InfoSummaryReportLine.newInfoFromDomain((ISummaryReportLine) lines.get(line)));
                infoReport.setLines(infoLines);
                infoProjectReportList.add(infoReport);
            }
        }
        return infoProjectReportList;
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