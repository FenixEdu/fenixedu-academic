/*
 * Created on 2004/08/30
 * 
 */
package ServidorAplicacao.Servico.manager;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.support.GlossaryEntry;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentObject;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Luis Cruz
 */
public class DeleteGlossaryEntry implements IService {

    public void run(Integer entryId) throws FenixServiceException, ExcepcaoPersistencia {
        ISuportePersistente sp = SuportePersistenteOJB.getInstance();
        IPersistentObject dao = sp.getIPersistentObject();

        dao.deleteByOID(GlossaryEntry.class, entryId);
    }

}