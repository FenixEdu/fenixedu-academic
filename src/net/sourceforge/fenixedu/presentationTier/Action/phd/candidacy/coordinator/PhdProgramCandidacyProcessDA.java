package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.coordinator;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.CommonPhdCandidacyDA;

@Mapping(path = "/phdProgramCandidacyProcess", module = "coordinator")
@Forwards( {

@Forward(name = "manageCandidacyReview", path = "/phd/candidacy/coordinator/manageCandidacyReview.jsp")

})
public class PhdProgramCandidacyProcessDA extends CommonPhdCandidacyDA {

}
