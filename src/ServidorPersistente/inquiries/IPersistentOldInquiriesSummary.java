/*
 * Created on Nov 15, 2004
 *
 */
package ServidorPersistente.inquiries;

import java.util.List;

import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;

/**
 * @author João Fialho & Rita Ferreira
 */
public interface IPersistentOldInquiriesSummary extends IPersistentObject {
    public List readAll() throws ExcepcaoPersistencia;
    public List readByDegreeIdAndExecutionPeriod(Integer degreeID, Integer executionPeriodID) throws ExcepcaoPersistencia;    
    public List readByDegreeId(Integer degreeID) throws ExcepcaoPersistencia;

}
