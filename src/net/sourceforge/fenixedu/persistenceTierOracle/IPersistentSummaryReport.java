/*
 * Created on Feb 21, 2005
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
public interface IPersistentSummaryReport {
    public abstract List readByCoordinatorCode(ReportType reportType, Integer coordinatorCode) throws ExcepcaoPersistencia;

    public abstract List readByCoordinatorAndProjectCodes(ReportType reportType, Integer coordinatorCode, List projectCodes)
            throws ExcepcaoPersistencia;
}