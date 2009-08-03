package net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.coordinator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.caseHandling.ExecuteProcessActivity;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.PhdProgramCandidacyProcessState;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessStateBean;
import net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcess.CoordinatorRejectCandidacy;
import net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.CommonPhdCandidacyDA;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;

@Mapping(path = "/phdProgramCandidacyProcess", module = "coordinator")
@Forwards( {

@Forward(name = "manageCandidacyDocuments", path = "phd.manageCandidacyDocuments"),

@Forward(name = "manageCandidacyReview", path = "phd.manageCandidacyReview")

})
public class PhdProgramCandidacyProcessDA extends CommonPhdCandidacyDA {

    public ActionForward rejectCandidacy(ActionMapping mapping, ActionForm actionForm, HttpServletRequest request,
	    HttpServletResponse response) {

	try {
	    final PhdProgramCandidacyProcessStateBean bean = (PhdProgramCandidacyProcessStateBean) getRenderedObject("stateBean");
	    bean.setState(PhdProgramCandidacyProcessState.REJECTED_BY_COORDINATOR);
	    ExecuteProcessActivity.run(getProcess(request), CoordinatorRejectCandidacy.class, bean);
	} catch (final DomainException e) {
	    addErrorMessage(request, e.getKey(), e.getArgs());
	    return uploadCandidacyReviewInvalid(mapping, actionForm, request, response);
	}

	return viewIndividualProgramProcess(request, getProcess(request));
    }

}
