/*
 * Created on 21/Jul/2003
 *
 * 
 */
package ServidorAplicacao.Servico.departmentAdmOffice;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.ISummary;
import Dominio.Summary;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentSummary;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

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