/*
 * 
 * Created on 27 of March de 2003
 * 
 * 
 * Autores : - Nuno Nunes (nmsn@rnl.ist.utl.pt) - Joana Mota
 * (jccm@rnl.ist.utl.pt)
 *  
 */

package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.candidate;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pt.ist.bennu.core.domain.User;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.candidate.ReadPersonCandidates;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.bennu.core.security.Authenticate;

public class ReadPersonCandidatesAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        User userView = Authenticate.getUser();

        List<InfoMasterDegreeCandidate> candidates = null;
        try {
            candidates = ReadPersonCandidates.run(userView.getUsername());
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (candidates.size() == 1) {
            request.setAttribute("candidateID", candidates.iterator().next().getExternalId());
            return mapping.findForward("Success");
        }

        request.setAttribute(PresentationConstants.MASTER_DEGREE_CANDIDATE_LIST, candidates);
        return mapping.findForward("ChooseCandidate");

    }

}