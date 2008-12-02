/*
 * Created on Jan 31, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTierOracle;

import java.util.List;

import net.sourceforge.fenixedu.domain.projectsManagement.IExpensesReportLine;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

import org.apache.struts.util.LabelValueBean;

/**
 * @author Susana Fernandes
 * 
 */
public interface IPersistentExpensesReport {
    public abstract List<IExpensesReportLine> getCompleteReport(ReportType reportType, Integer projectCode, Boolean it)
	    throws ExcepcaoPersistencia;

    public abstract List<IExpensesReportLine> getReportByRubric(ReportType reportType, Integer projectCode, String rubric,
	    Boolean it) throws ExcepcaoPersistencia;

    public abstract List<LabelValueBean> getRubricList(ReportType reportType, Integer projectCode, Boolean it)
	    throws ExcepcaoPersistencia;
}