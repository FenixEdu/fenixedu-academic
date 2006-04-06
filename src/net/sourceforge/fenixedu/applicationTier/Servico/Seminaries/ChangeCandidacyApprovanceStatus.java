package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Seminaries.Candidacy;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ChangeCandidacyApprovanceStatus extends Service {

    public void run(List<Integer> candidaciesIDs) throws ExcepcaoPersistencia {
        for (Integer candidacyID : candidaciesIDs) {
            Candidacy candidacy = rootDomainObject.readCandidacyByOID(candidacyID);
            if (candidacy.getApproved() == null) {
                candidacy.setApproved(Boolean.FALSE);
            }

            candidacy.setApproved(!candidacy.getApproved());
        }
    }

}
