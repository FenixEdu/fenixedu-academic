package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.teacher.feedbackRequest;

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

@Mapping(path = "/phdCandidacyFeedbackRequest", module = "teacher")
@Forwards(tileProperties = @Tile(navLocal = "/teacher/commons/navigationBarIndex.jsp"), value = {

@Forward(name = "uploadCandidacyFeedback", path = "/phd/candidacy/teacher/feedbackRequest/uploadCandidacyFeedback.jsp")

})
public class PhdCandidacyFeedbackRequestDA extends CommonPhdCandidacyDA {

}
