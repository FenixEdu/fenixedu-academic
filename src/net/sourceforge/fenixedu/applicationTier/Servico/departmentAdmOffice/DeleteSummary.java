/*
 * Created on 21/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSummary;

/**
 * @author João Mota
 * @author Susana Fernandes
 * 
 * 21/Jul/2003 fenix-head ServidorAplicacao.Servico.teacher
 * 
 */
public class DeleteSummary extends Service {

    public boolean run(Integer executionCourseId, Integer summaryID) throws FenixServiceException,
            ExcepcaoPersistencia {
        final IPersistentSummary persistentSummary = persistentSupport.getIPersistentSummary();

        final Summary summary = (Summary) persistentSummary.readByOID(Summary.class, summaryID);
        if (summary == null)
            throw new InvalidArgumentsServiceException();

        summary.delete();
        persistentSummary.deleteByOID(Summary.class, summary.getIdInternal());

        return true;
    }
}