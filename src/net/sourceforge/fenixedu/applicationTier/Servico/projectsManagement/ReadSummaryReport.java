/*
 * Created on Jan 12, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoCoordinatorReport;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoRubric;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoSummaryReportLine;
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

    public InfoCoordinatorReport run(String username, String costCenter, Integer coordinatorCode, String userNumber) throws ExcepcaoPersistencia {
        InfoCoordinatorReport infoReport = new InfoCoordinatorReport();
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();

        PersistentSuportOracle p = PersistentSuportOracle.getInstance();
        if (coordinatorCode == null)
            coordinatorCode = new Integer(userNumber);
        List lines = null;
        if (new Integer(userNumber).equals(coordinatorCode)) {
            lines = p.getIPersistentSummaryReport().readByCoordinatorCode(ReportType.SUMMARY, coordinatorCode);
        } else {
            List<Integer> projectCodes = persistentSuport.getIPersistentProjectAccess().readProjectCodesByPersonUsernameAndCoordinator(username,
                    coordinatorCode, false);
            if (projectCodes != null && projectCodes.size() != 0)
                lines = p.getIPersistentSummaryReport().readByCoordinatorAndProjectCodes(ReportType.SUMMARY, coordinatorCode, projectCodes);
        }
        if (lines != null) {
         //   infoReport = new InfoCoordinatorReport();
            infoReport.setInfoCoordinator(InfoRubric.newInfoFromDomain(p.getIPersistentProjectUser().readProjectCoordinator(coordinatorCode)));
            List<InfoSummaryReportLine> infoLines = new ArrayList<InfoSummaryReportLine>();

            for (int line = 0; line < lines.size(); line++)
                infoLines.add(InfoSummaryReportLine.newInfoFromDomain((ISummaryReportLine) lines.get(line)));
            infoReport.setLines(infoLines);
        }

        return infoReport;
    }

}