/*
 * Created on 21/Jul/2003
 *
 * 
 */
package net.sourceforge.fenixedu.applicationTier.Servico.departmentAdmOffice;

import net.sourceforge.fenixedu.domain.ISummary;
import net.sourceforge.fenixedu.domain.Summary;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentSummary;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author João Mota
 * @author Susana Fernandes
 * 
 * 21/Jul/2003 fenix-head ServidorAplicacao.Servico.teacher
 *  
 */
public class DeleteSummary implements IService {

    /**
     *  
     */
    public DeleteSummary() {
    }

    public boolean run(Integer executionCourseId, Integer summaryId) throws FenixServiceException {

        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB.getInstance();

            IPersistentSummary persistentSummary = persistentSuport.getIPersistentSummary();

            ISummary summary = (ISummary) persistentSummary.readByOID(Summary.class, summaryId, true);
            if (summary != null) {
                persistentSummary.delete(summary);
            }
            return true;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

}