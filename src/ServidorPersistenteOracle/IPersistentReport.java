/*
 * Created on Jan 28, 2005
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
public interface IPersistentReport {
    public abstract List getCompleteReport(ReportType reportType, Integer projectCode) throws ExcepcaoPersistencia;
}