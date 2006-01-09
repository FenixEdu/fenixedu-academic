package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.domain.Seminaries.Candidacy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

public class ChangeCandidacyApprovanceStatus implements IService {

    public void run(List<Integer> candidaciesIDs) throws ExcepcaoPersistencia {
        final ISuportePersistente persistenceSupport = PersistenceSupportFactory
                .getDefaultPersistenceSupport();

        for (Integer candidacyID : candidaciesIDs) {
            Candidacy candidacy = (Candidacy) persistenceSupport.getIPersistentObject().readByOID(
                    Candidacy.class, candidacyID);
            if (candidacy.getApproved() == null) {
                candidacy.setApproved(Boolean.FALSE);
            }

            candidacy.setApproved(!candidacy.getApproved());
        }
    }

}
