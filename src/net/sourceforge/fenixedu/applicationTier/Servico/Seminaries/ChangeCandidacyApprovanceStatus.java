package net.sourceforge.fenixedu.applicationTier.Servico.Seminaries;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Seminaries.SeminaryCandidacy;

public class ChangeCandidacyApprovanceStatus extends Service {

    public void run(List<Integer> candidaciesIDs) {
	for (Integer candidacyID : candidaciesIDs) {
	    SeminaryCandidacy candidacy = rootDomainObject.readSeminaryCandidacyByOID(candidacyID);
	    if (candidacy.getApproved() == null) {
		candidacy.setApproved(Boolean.FALSE);
	    }

	    candidacy.setApproved(!candidacy.getApproved());
	}
    }

}
