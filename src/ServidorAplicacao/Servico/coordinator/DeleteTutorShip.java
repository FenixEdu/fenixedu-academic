/*
 * Created on 3/Fev/2004
 *  
 */
package ServidorAplicacao.Servico.coordinator;

import java.util.List;
import java.util.ListIterator;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import Dominio.ITutor;
import Dominio.Tutor;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTutor;
import ServidorPersistente.ISuportePersistente;
import ServidorPersistente.OJB.SuportePersistenteOJB;

/**
 * @author Tânia Pousão
 *  
 */
public class DeleteTutorShip implements IService {

    public DeleteTutorShip() {
    }

    public Object run(Integer executionDegreeId, Integer tutorNumber,
            List tutorIds2Delete) throws FenixServiceException {

        if (tutorNumber == null) {
            throw new FenixServiceException("error.tutor.impossibleOperation");
        }

        Boolean result = Boolean.FALSE;
        if (tutorIds2Delete != null && tutorIds2Delete.size() > 0) {
            ISuportePersistente sp;

            try {
                sp = SuportePersistenteOJB.getInstance();
                IPersistentTutor persistentTutor = sp.getIPersistentTutor();

                ListIterator iterator = tutorIds2Delete.listIterator();
                while (iterator.hasNext()) {
                    Integer tutorId = (Integer) iterator.next();
                    ITutor tutor = (ITutor) persistentTutor.readByOID(
                            Tutor.class, tutorId);
                    if (tutor != null) {
                        persistentTutor.deleteByOID(Tutor.class, tutorId);
                    }
                }

                result = Boolean.TRUE;
            } catch (ExcepcaoPersistencia e) {
                e.printStackTrace();
                throw new FenixServiceException("error.tutor.removeTutor");
            }
        }
        return result;
    }
}