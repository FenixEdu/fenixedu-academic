/*
 * Created on 21/Jul/2003
 *
 * 
 */
package ServidorAplicacao.Servico.teacher;

import Dominio.ISummary;
import Dominio.Summary;
import ServidorAplicacao.IServico;
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
public class DeleteSummary implements IServico {

    private static DeleteSummary service = new DeleteSummary();

    public static DeleteSummary getService() {

        return service;
    }

    /**
     *  
     */
    public DeleteSummary() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see ServidorAplicacao.IServico#getNome()
     */
    public String getNome() {
        return "DeleteSummary";
    }

    public boolean run(Integer executionCourseId, Integer summaryId)
            throws FenixServiceException {

        try {
            ISuportePersistente persistentSuport = SuportePersistenteOJB
                    .getInstance();

            IPersistentSummary persistentSummary = persistentSuport
                    .getIPersistentSummary();

            ISummary summary = (ISummary) persistentSummary.readByOID(
                    Summary.class, summaryId, true);
            if (summary != null) {
                persistentSummary.delete(summary);
            }
            return true;
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
    }

}