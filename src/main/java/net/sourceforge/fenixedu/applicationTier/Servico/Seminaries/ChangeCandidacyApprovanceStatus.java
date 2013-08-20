package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class ChangeCandidacyApprovanceStatus {

    @Checked("RolePredicates.SEMINARIES_COORDINATOR_PREDICATE")
    @Service
    public static void run(List<String> candidaciesIDs) {
        for (String candidacyID : candidaciesIDs) {
            SeminaryCandidacy candidacy = AbstractDomainObject.fromExternalId(candidacyID);
            if (candidacy.getApproved() == null) {
                candidacy.setApproved(Boolean.FALSE);
            }

            candidacy.setApproved(!candidacy.getApproved());
        }
    }

}