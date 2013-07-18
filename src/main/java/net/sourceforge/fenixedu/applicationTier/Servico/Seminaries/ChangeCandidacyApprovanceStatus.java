package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ChangeCandidacyApprovanceStatus {

    @Checked("RolePredicates.SEMINARIES_COORDINATOR_PREDICATE")
    @Service
    public static void run(List<Integer> candidaciesIDs) {
        for (Integer candidacyID : candidaciesIDs) {
            SeminaryCandidacy candidacy = RootDomainObject.getInstance().readSeminaryCandidacyByOID(candidacyID);
            if (candidacy.getApproved() == null) {
                candidacy.setApproved(Boolean.FALSE);
            }

            candidacy.setApproved(!candidacy.getApproved());
        }
    }

}