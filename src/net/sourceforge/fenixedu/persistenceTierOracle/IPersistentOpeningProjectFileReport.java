package net.sourceforge.fenixedu.persistenceTierOracle;

import java.util.List;

import net.sourceforge.fenixedu.domain.projectsManagement.IOpeningProjectFileReport;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

public interface IPersistentOpeningProjectFileReport {

    public abstract IOpeningProjectFileReport getCompleteReport(ReportType reportType, Integer projectCode) throws ExcepcaoPersistencia;

    public abstract List getReportRubricList(ReportType reportType, Integer projectCode, boolean getValue) throws ExcepcaoPersistencia;
}