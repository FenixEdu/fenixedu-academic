/*
 * Created on Jan 31, 2005
 *
 */
package net.sourceforge.fenixedu.persistenceTierOracle;

import java.util.List;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 * 
 */
public interface IPersistentExpensesReport {
    public abstract List getCompleteReport(ReportType reportType, Integer projectCode) throws ExcepcaoPersistencia;

    public abstract List getReportByRubric(ReportType reportType, Integer projectCode, String rubric) throws ExcepcaoPersistencia;

    public abstract List getRubricList(ReportType reportType, Integer projectCode) throws ExcepcaoPersistencia;
}