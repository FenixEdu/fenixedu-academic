package net.sourceforge.fenixedu.presentationTier.Action.candidate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.CandidacyOperationType;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySituation;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySituationType;
import net.sourceforge.fenixedu.domain.candidacy.DegreeCandidacy;
import net.sourceforge.fenixedu.domain.candidacy.IMDCandidacy;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(module = "candidate", path = "/index", scope = "session")
@Forwards(value = { @Forward(name = "showCandidacyDetails", path = "/degreeCandidacyManagement.do?method=showCandidacyDetails"),
	@Forward(name = "fillData", path = "/degreeCandidacyManagement.do?method=doOperation"),
	@Forward(name = "showWelcome", path = "/candidate/index.jsp") })
public class IndexAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) throws Exception {

	if (getUserView(request).getPerson().getCandidaciesCount() == 1) {
	    final Candidacy candidacy = getUserView(request).getPerson().getCandidaciesIterator().next();

	    if (candidacy instanceof DegreeCandidacy || candidacy instanceof IMDCandidacy) {
		request.setAttribute("candidacyID", candidacy.getIdInternal());
		final CandidacySituation activeCandidacySituation = candidacy.getActiveCandidacySituation();
		if (activeCandidacySituation != null &&
			CandidacySituationType.STAND_BY == activeCandidacySituation.getCandidacySituationType()) {
		    request.setAttribute("operationType", CandidacyOperationType.FILL_PERSONAL_DATA.toString());
		    return mapping.findForward("fillData");
		} else {
		    return mapping.findForward("showCandidacyDetails");
		}
	    }

	}

	return mapping.findForward("showWelcome");
    }

}
