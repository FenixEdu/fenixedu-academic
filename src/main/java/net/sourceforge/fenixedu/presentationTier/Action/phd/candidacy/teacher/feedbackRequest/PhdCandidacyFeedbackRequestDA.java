package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.teacher.feedbackRequest;

import net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.CommonPhdCandidacyDA;
import net.sourceforge.fenixedu.presentationTier.Action.phd.teacher.PhdIndividualProgramProcessDA;
import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdCandidacyFeedbackRequest", module = "teacher", functionality = PhdIndividualProgramProcessDA.class)
@Forwards(@Forward(name = "uploadCandidacyFeedback", path = "/phd/candidacy/teacher/feedbackRequest/uploadCandidacyFeedback.jsp"))
public class PhdCandidacyFeedbackRequestDA extends CommonPhdCandidacyDA {

}
