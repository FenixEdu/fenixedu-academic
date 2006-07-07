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

        IUserView userView = SessionUtils.getUserView(request);

        for (Candidacy candidacy : userView.getPerson().getCandidacies()) {
            if (candidacy.getActiveCandidacySituation().canChangePersonalData()) {
                request.setAttribute("candidacy", candidacy);
                if (candidacy instanceof DFACandidacy) {
                    PrecedentDegreeInformation precedentDegreeInformation = ((DFACandidacy) candidacy)
                            .getPrecedentDegreeInformation();
                    request.setAttribute("precedentDegreeInformation",
                            new PrecedentDegreeInformationBean(precedentDegreeInformation));
                }
                return mapping.findForward("change");
            }
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
            // TODO: put also some error message!
            return mapping.findForward("change");
        }

        if (precedentDegreeInformation.getNewInstitutionName() != null
                || precedentDegreeInformation.getInstitution() != null) {
            Object[] argsInstitution = { precedentDegreeInformation };
            ServiceUtils.executeService(userView, "EditPrecedentDegreeInformation", argsInstitution);
        }

        Object[] argsStateMachine = { new StateMachineRunner.RunnerArgs(precedentDegreeInformation.getPrecedentDegreeInformation()
                .getDfaCandidacy().getActiveCandidacySituation(), CandidacySituationType.STAND_BY_FILLED_DATA.toString()) };
        try {
            ServiceUtils.executeService(userView, "StateMachineRunner", argsStateMachine);
        } catch (DomainException e) {
            // Didn't move to next state
        }

        return mapping.findForward("changeSuccess");
    }

}
