/*
 * Created on Nov 15, 2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.inquiries;

import java.util.List;

import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;

/**
 * @author João Fialho & Rita Ferreira
 */
public interface IPersistentOldInquiriesSummary extends IPersistentObject {
    public List readAll() throws ExcepcaoPersistencia;
    public List readByDegreeIdAndExecutionPeriod(Integer degreeID, Integer executionPeriodID) throws ExcepcaoPersistencia;    
    public List readByDegreeId(Integer degreeID) throws ExcepcaoPersistencia;

}
