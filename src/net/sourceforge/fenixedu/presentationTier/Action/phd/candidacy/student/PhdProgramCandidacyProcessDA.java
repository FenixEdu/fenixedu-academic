package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.student;

import net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.CommonPhdCandidacyDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.ExceptionHandling;
import pt.ist.fenixWebFramework.struts.annotations.Exceptions;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixWebFramework.struts.annotations.Tile;

@Mapping(path = "/phdProgramCandidacyProcess", module = "student")
@Forwards( {

@Forward(name = "manageCandidacyDocuments", path = "/phd/candidacy/student/manageCandidacyDocuments.jsp")

})
public class PhdProgramCandidacyProcessDA extends CommonPhdCandidacyDA {

}
