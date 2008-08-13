package net.sourceforge.fenixedu.persistenceTierOracle;

import net.sourceforge.fenixedu.domain.projectsManagement.IAdiantamentosReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.ICabimentosReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.ISummaryEURReportLine;
import net.sourceforge.fenixedu.domain.projectsManagement.ISummaryPTEReportLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

public interface IPersistentExpensesResume {

    public abstract ISummaryPTEReportLine getSummaryPTEReportLine(ReportType reportType, Integer projectCode)
	    throws ExcepcaoPersistencia;

    public abstract ISummaryEURReportLine getSummaryEURReportLine(ReportType reportType, Integer projectCode)
	    throws ExcepcaoPersistencia;

    public abstract IAdiantamentosReportLine getAdiantamentosReportLine(ReportType reportType, Integer projectCode)
	    throws ExcepcaoPersistencia;

    public abstract ICabimentosReportLine getCabimentosReportLine(ReportType reportType, Integer projectCode)
	    throws ExcepcaoPersistencia;

}