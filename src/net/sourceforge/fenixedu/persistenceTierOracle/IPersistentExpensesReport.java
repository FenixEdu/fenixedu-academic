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
	public abstract List<IExpensesReportLine> getCompleteReport(ReportType reportType, String projectCode,
			final BackendInstance instance) throws ExcepcaoPersistencia;

	public abstract List<IExpensesReportLine> getReportByRubric(ReportType reportType, String projectCode, String rubric,
			final BackendInstance instance) throws ExcepcaoPersistencia;

	public abstract List<LabelValueBean> getRubricList(ReportType reportType, String projectCode, final BackendInstance instance)
			throws ExcepcaoPersistencia;
}