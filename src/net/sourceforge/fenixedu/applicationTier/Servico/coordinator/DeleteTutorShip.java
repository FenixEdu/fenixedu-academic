package net.sourceforge.fenixedu.applicationTier.Servico.coordinator;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.Tutor;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentObject;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeleteTutorShip implements IService {

    public void run(Integer executionDegreeId, Integer tutorNumber, List<Integer> tutorIds2Delete)
            throws FenixServiceException, ExcepcaoPersistencia {

        if (tutorNumber == null) {
            throw new FenixServiceException("error.tutor.impossibleOperation");
        }

        if (tutorIds2Delete != null && tutorIds2Delete.size() > 0) {
            final ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            final IPersistentObject persistentObject = persistentSupport.getIPersistentObject();

            for (Integer tutorId : tutorIds2Delete) {
                Tutor tutor = (Tutor) persistentObject.readByOID(Tutor.class, tutorId);
                if (tutor != null) {
                    tutor.delete();
                }
            }
        }
    }

}
