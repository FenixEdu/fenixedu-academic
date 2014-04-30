package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.coordinator;

import net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.CommonPhdCandidacyDA;
import net.sourceforge.fenixedu.presentationTier.Action.phd.coordinator.PhdIndividualProgramProcessDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdProgramCandidacyProcess", module = "coordinator", functionality = PhdIndividualProgramProcessDA.class)
@Forwards({ @Forward(name = "manageCandidacyDocuments", path = "/phd/candidacy/coordinator/manageCandidacyDocuments.jsp"),
        @Forward(name = "manageCandidacyReview", path = "/phd/candidacy/coordinator/manageCandidacyReview.jsp") })
public class PhdProgramCandidacyProcessDA extends CommonPhdCandidacyDA {

}
