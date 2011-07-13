package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.coordinator;

import net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.CommonPhdCandidacyDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/phdProgramCandidacyProcess", module = "coordinator")
@Forwards(tileProperties = @Tile(navLocal = "/coordinator/localNavigationBar.jsp"), value = {

@Forward(name = "manageCandidacyDocuments", path = "/phd/candidacy/coordinator/manageCandidacyDocuments.jsp"),

@Forward(name = "manageCandidacyReview", path = "/phd/candidacy/coordinator/manageCandidacyReview.jsp")

})
public class PhdProgramCandidacyProcessDA extends CommonPhdCandidacyDA {

}
