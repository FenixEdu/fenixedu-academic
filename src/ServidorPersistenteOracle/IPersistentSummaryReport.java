/*
 * Created on Feb 21, 2005
 *
 */
package ServidorPersistenteOracle;

import java.util.List;

import ServidorPersistente.ExcepcaoPersistencia;
import Util.projectsManagement.ReportType;

/**
 * @author Susana Fernandes
 * 
 */
public interface IPersistentSummaryReport {
    public abstract List readByCoordinatorCode(ReportType reportType, Integer coordinatorCode) throws ExcepcaoPersistencia;

    public abstract List readByCoordinatorAndProjectCodes(ReportType reportType, Integer coordinatorCode, List projectCodes)
            throws ExcepcaoPersistencia;
}