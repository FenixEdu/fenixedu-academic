/*
 * Created on Jan 12, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.IReportLine;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoCoordinatorReport;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoRubric;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoSummaryReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.ISummaryReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.ProjectAccess;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 */
public class ReadSummaryReport extends Service {

    public InfoCoordinatorReport run(String username, String costCenter, Integer coordinatorCode,
	    String userNumber) throws ExcepcaoPersistencia {
	InfoCoordinatorReport infoReport = new InfoCoordinatorReport();

	PersistentSuportOracle p = PersistentSuportOracle.getProjectDBInstance();
	if (coordinatorCode == null)
	    coordinatorCode = new Integer(userNumber);
	List lines = null;
	if (Integer.valueOf(userNumber).equals(coordinatorCode)) {
	    lines = p.getIPersistentSummaryReport().readByCoordinatorCode(ReportType.SUMMARY,
		    coordinatorCode);
	} else {
	    List<Integer> projectCodes = new ArrayList<Integer>();

	    List<ProjectAccess> accesses = ProjectAccess.getAllByPersonUsernameAndCoordinator(username,
		    coordinatorCode, false);
	    for (ProjectAccess access : accesses) {
		Integer keyProject = access.getKeyProject();
		if (keyProject == null && access.getCostCenter()) {
		    projectCodes = null;
		    break;
		} else if (!projectCodes.contains(keyProject)) {
		    projectCodes.add(keyProject);
		}
	    }
	    lines = p.getIPersistentSummaryReport().readByCoordinatorAndProjectCodes(ReportType.SUMMARY,
		    coordinatorCode, projectCodes);

	}
	if (lines != null) {
	    infoReport.setInfoCoordinator(InfoRubric.newInfoFromDomain(p.getIPersistentProjectUser()
		    .readProjectCoordinator(coordinatorCode)));
	    List<IReportLine> infoLines = new ArrayList<IReportLine>();

	    for (int line = 0; line < lines.size(); line++)
		infoLines.add(InfoSummaryReportLine.newInfoFromDomain((ISummaryReportLine) lines
			.get(line)));
	    infoReport.setLines(infoLines);
	}

	return infoReport;
    }

}