package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.coordinator;

import net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.CommonPhdCandidacyDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdProgramCandidacyProcess", module = "coordinator")
@Forwards( {

	@Forward(name = "manageCandidacyDocuments", path = "/phd/candidacy/coordinator/manageCandidacyDocuments.jsp", extend = "phd.navLocal"),

	@Forward(name = "manageCandidacyReview", path = "/phd/candidacy/coordinator/manageCandidacyReview.jsp", extend = "phd.navLocal")

})
public class PhdProgramCandidacyProcessDA extends CommonPhdCandidacyDA {

}
