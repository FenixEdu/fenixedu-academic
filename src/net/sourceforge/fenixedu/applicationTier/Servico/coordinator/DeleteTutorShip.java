/*
 * Created on 3/Fev/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.List;
import java.util.ListIterator;

import net.sourceforge.fenixedu.domain.ITutor;
import net.sourceforge.fenixedu.domain.Tutor;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTutor;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.SuportePersistenteOJB;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Tânia Pousão
 *  
 */
public class DeleteTutorShip implements IService {

    public DeleteTutorShip() {
    }

    public Object run(Integer executionDegreeId, Integer tutorNumber, List tutorIds2Delete)
            throws FenixServiceException {

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
                    ITutor tutor = (ITutor) persistentTutor.readByOID(Tutor.class, tutorId);
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