package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramDocumentType;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessStateBean;
import net.sourceforge.fenixedu.presentationTier.Action.phd.PhdProcessDA;

abstract public class CommonPhdCandidacyDA extends PhdProcessDA {

    public ActionForward manageCandidacyReview(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	final PhdCandidacyDocumentUploadBean bean = new PhdCandidacyDocumentUploadBean();
	bean.setType(PhdIndividualProgramDocumentType.CANDIDACY_REVIEW);

	final PhdProgramCandidacyProcessStateBean stateBean = new PhdProgramCandidacyProcessStateBean();
	stateBean.setState(PhdProgramCandidacyProcessState.WAITING_FOR_CIENTIFIC_COUNCIL_RATIFICATION);

	request.setAttribute("documentToUpload", bean);
	request.setAttribute("stateBean", stateBean);

	return mapping.findForward("manageCandidacyReview");
    }

}
