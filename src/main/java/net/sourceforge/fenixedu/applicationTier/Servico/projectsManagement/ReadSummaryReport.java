/*
 * Created on Jan 12, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.IReportLine;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoCoordinatorReport;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoRubric;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoSummaryReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.ISummaryReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.BackendInstance;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentProjectUser;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSummaryReport;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 */
public class ReadSummaryReport extends FenixService {

    public InfoCoordinatorReport run(String username, String costCenter, Integer coordinatorCode, BackendInstance instance,
            String userNumber) throws ExcepcaoPersistencia {
        InfoCoordinatorReport infoReport = new InfoCoordinatorReport();

        if (coordinatorCode == null) {
            coordinatorCode = new Integer(userNumber);
        }
        List lines = null;
        if (Integer.valueOf(userNumber).equals(coordinatorCode)) {
            lines = new PersistentSummaryReport().readByCoordinatorCode(ReportType.SUMMARY, coordinatorCode, instance);
        } else {
            List<String> projectCodes = new ArrayList<String>();

            List<ProjectAccess> accesses =
                    ProjectAccess.getAllByPersonUsernameAndCoordinator(username, coordinatorCode, false, instance);
            for (ProjectAccess access : accesses) {
                String keyProject = access.getKeyProject();
                if (keyProject == null && access.getCostCenter()) {
                    projectCodes = null;
                    break;
                } else if (!projectCodes.contains(keyProject)) {
                    projectCodes.add(keyProject);
                }
            }
            if (projectCodes.size() != 0) {
                lines =
                        new PersistentSummaryReport().readByCoordinatorAndProjectCodes(ReportType.SUMMARY, coordinatorCode,
                                projectCodes, instance);
            }

        }
        if (lines != null) {
            infoReport.setInfoCoordinator(InfoRubric.newInfoFromDomain(new PersistentProjectUser().readProjectCoordinator(
                    coordinatorCode, instance)));
            List<IReportLine> infoLines = new ArrayList<IReportLine>();
            for (int line = 0; line < lines.size(); line++) {
                infoLines.add(InfoSummaryReportLine.newInfoFromDomain((ISummaryReportLine) lines.get(line)));
            }
            infoReport.setLines(infoLines);
        }

        return infoReport;
    }

}