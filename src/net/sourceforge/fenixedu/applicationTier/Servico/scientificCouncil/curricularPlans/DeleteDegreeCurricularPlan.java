package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.curricularPlans;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class DeleteDegreeCurricularPlan implements IService {

    public void run(Integer idInternal) throws FenixServiceException, ExcepcaoPersistencia {
        if (idInternal == null) {
            throw new InvalidArgumentsServiceException();
        }

        final ISuportePersistente persistentSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();
        final DegreeCurricularPlan dcpToDelete = (DegreeCurricularPlan) persistentSupport.getIPersistentObject().readByOID(
                DegreeCurricularPlan.class, idInternal);

        if (dcpToDelete == null) {
            throw new NonExistingServiceException();
        } else {
            dcpToDelete.delete();
        }
    }

}
