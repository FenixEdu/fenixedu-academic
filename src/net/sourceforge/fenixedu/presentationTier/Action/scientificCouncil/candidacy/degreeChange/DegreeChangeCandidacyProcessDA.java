package net.sourceforge.fenixedu.presentationTier.Action.scientificCouncil.candidacy.degreeChange;

import net.sourceforge.fenixedu.presentationTier.Action.candidacy.CandidacyProcessDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/caseHandlingDegreeChangeCandidacyProcess", module = "scientificCouncil", formBeanClass = CandidacyProcessDA.CandidacyProcessForm.class)
@Forwards({ @Forward(name = "intro", path = "/scientificCouncil/candidacy/mainCandidacyProcess.jsp", tileProperties = @Tile(  title = "private.scientificcouncil.applications")) })
public class DegreeChangeCandidacyProcessDA extends
	net.sourceforge.fenixedu.presentationTier.Action.candidacy.degreeChange.DegreeChangeCandidacyProcessDA {

}
