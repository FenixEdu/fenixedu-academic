/*
 * Created on Jan 28, 2005
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
public interface IPersistentReport {
    public abstract List getCompleteReport(ReportType reportType, Integer projectCode) throws ExcepcaoPersistencia;
}