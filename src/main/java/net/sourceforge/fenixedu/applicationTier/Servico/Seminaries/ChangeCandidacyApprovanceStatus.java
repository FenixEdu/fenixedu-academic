package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ChangeCandidacyApprovanceStatus {

    @Atomic
    public static void run(List<String> candidaciesIDs) {
        check(RolePredicates.SEMINARIES_COORDINATOR_PREDICATE);
        for (String candidacyID : candidaciesIDs) {
            SeminaryCandidacy candidacy = FenixFramework.getDomainObject(candidacyID);
            if (candidacy.getApproved() == null) {
                candidacy.setApproved(Boolean.FALSE);
            }

            candidacy.setApproved(!candidacy.getApproved());
        }
    }

}