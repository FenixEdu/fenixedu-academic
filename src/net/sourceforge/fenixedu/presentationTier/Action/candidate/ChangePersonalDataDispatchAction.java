/**
 * 
 */
package net.sourceforge.fenixedu.presentationTier.Action.candidate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.commons.StateMachineRunner;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean;
import net.sourceforge.fenixedu.domain.candidacy.Candidacy;
import net.sourceforge.fenixedu.domain.candidacy.CandidacySituationType;
import net.sourceforge.fenixedu.domain.candidacy.DFACandidacy;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class ChangePersonalDataDispatchAction extends FenixDispatchAction {

    public ActionForward prepare(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) {

	final Candidacy candidacy = getCandidacy(request);
	if (candidacy instanceof DFACandidacy
		&& candidacy.getActiveCandidacySituation().canChangePersonalData()) {

	    request.setAttribute("candidacy", candidacy);

	    PrecedentDegreeInformation precedentDegreeInformation = ((DFACandidacy) candidacy)
		    .getPrecedentDegreeInformation();
	    request.setAttribute("precedentDegreeInformation", new PrecedentDegreeInformationBean(
		    precedentDegreeInformation));

	    return mapping.findForward("change");
	}

	return mapping.findForward("cannotChange");
    }

    public ActionForward change(ActionMapping mapping, ActionForm form, HttpServletRequest request,
	    HttpServletResponse response) throws FenixFilterException, FenixServiceException {

	IUserView userView = SessionUtils.getUserView(request);

	PrecedentDegreeInformationBean precedentDegreeInformation = (PrecedentDegreeInformationBean) RenderUtils
		.getViewState("precedentDegreeInformation").getMetaObject().getObject();

	if (!precedentDegreeInformation.isInstitutionFilled()) {
	    request.setAttribute("precedentDegreeInformation", precedentDegreeInformation);

	    addActionMessage(request, "error.candidacy.institution.must.be.choosed.or.created",
		    new String[] {});

	    return mapping.findForward("change");
	}

	if (precedentDegreeInformation.getNewInstitutionName() != null
		|| precedentDegreeInformation.getInstitution() != null) {
	    Object[] argsInstitution = { precedentDegreeInformation };
	    ServiceUtils.executeService(userView, "EditPrecedentDegreeInformation", argsInstitution);
	}

	Object[] argsStateMachine = { new StateMachineRunner.RunnerArgs(precedentDegreeInformation
		.getPrecedentDegreeInformation().getStudentCandidacy().getActiveCandidacySituation(),
		CandidacySituationType.STAND_BY_FILLED_DATA.toString()) };
	try {
	    ServiceUtils.executeService(userView, "StateMachineRunner", argsStateMachine);
	} catch (DomainException e) {
	    // Didn't move to next state
	}

	request.setAttribute("candidacy", precedentDegreeInformation.getPrecedentDegreeInformation()
		.getStudentCandidacy());

	return mapping.findForward("changeSuccess");
    }

    private Candidacy getCandidacy(HttpServletRequest request) {
	final Integer candidacyId = getRequestParameterAsInteger(request, "candidacyID");

	for (final Candidacy candidacy : getUserView(request).getPerson().getCandidaciesSet()) {
	    if (candidacy.getIdInternal().equals(candidacyId)) {
		return candidacy;
	    }
	}

	return null;
    }

}
