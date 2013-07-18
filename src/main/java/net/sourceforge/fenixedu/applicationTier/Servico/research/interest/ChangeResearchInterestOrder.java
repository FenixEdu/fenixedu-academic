package net.sourceforge.fenixedu.applicationTier.Servico.research.interest;

import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.research.ResearchInterest;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class ChangeResearchInterestOrder {

    @Checked("ResultPredicates.author")
    @Service
    public static void run(Party party, List<ResearchInterest> researchInterests) {
        if (party.getResearchInterests().size() != researchInterests.size()) {
            throw new DomainException("research.interests.size.mismatch");
        }

        for (int i = 0; i < researchInterests.size(); i++) {
            ResearchInterest researchInterest = researchInterests.get(i);

            if (!researchInterest.getParty().equals(party)) {
                throw new DomainException("research.interests.party.mismatch");
            }

            researchInterest.setOrder(i + 1);
        }
    }
}