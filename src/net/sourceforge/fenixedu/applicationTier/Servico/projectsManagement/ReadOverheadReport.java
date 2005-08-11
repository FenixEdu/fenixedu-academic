/*
 * Created on Jan 12, 2005
 */

package net.sourceforge.fenixedu.applicationTier.Servico.projectsManagement;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.IReportLine;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoGeneratedOverheadsReportLine;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoOverheadReport;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoOverheadsSummaryReportLine;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoRubric;
import net.sourceforge.fenixedu.dataTransferObject.projectsManagement.InfoTransferedOverheadsReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.IGeneratedOverheadsReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.IOverheadsSummaryReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.ITransferedOverheadsReportLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTierOracle.IPersistentSuportOracle;
import net.sourceforge.fenixedu.persistenceTierOracle.Oracle.PersistentSuportOracle;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadOverheadReport implements IService {

    public InfoOverheadReport run(String userView, String costCenter, ReportType reportType, Integer projectCode, String userNumber)
            throws ExcepcaoPersistencia {
        InfoOverheadReport infoReport = new InfoOverheadReport();
        List<IReportLine> infoLines = new ArrayList<IReportLine>();
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentSuportOracle p = PersistentSuportOracle.getInstance();
        infoReport.setInfoCostCenter(InfoRubric.newInfoFromDomain(p.getIPersistentProjectUser().getCostCenterByID(new Integer(userNumber))));
        if (userNumber.equals(costCenter)) {
            if (reportType.equals(ReportType.GENERATED_OVERHEADS)) {
                List<IGeneratedOverheadsReportLine> lines = p.getIPersistentGeneratedOverheadsReport().getCompleteReport(reportType,
                        new Integer(userNumber));
                for (IGeneratedOverheadsReportLine generatedOverheadsReportLine : lines) {
                    infoLines.add(InfoGeneratedOverheadsReportLine.newInfoFromDomain(generatedOverheadsReportLine));
                }
            } else if (reportType.equals(ReportType.TRANSFERED_OVERHEADS)) {
                List<ITransferedOverheadsReportLine> lines = p.getIPersistentTransferedOverheadsReport().getCompleteReport(reportType,
                        new Integer(userNumber));
                for (ITransferedOverheadsReportLine transferedOverheadsReportLine : lines) {
                    infoLines.add(InfoTransferedOverheadsReportLine.newInfoFromDomain(transferedOverheadsReportLine));
                }
            } else if (reportType.equals(ReportType.OVERHEADS_SUMMARY)) {
                List<IOverheadsSummaryReportLine> lines = p.getIPersistentOverheadsSummaryReport().getCompleteReport(reportType,
                        new Integer(userNumber));
                for (IOverheadsSummaryReportLine overheadsSummaryReportLine : lines) {
                    infoLines.add(InfoOverheadsSummaryReportLine.newInfoFromDomain(overheadsSummaryReportLine));
                }
            }
        }
        infoReport.setLines(infoLines);
        return infoReport;
    }
}