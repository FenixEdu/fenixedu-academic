package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.candidate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.commons.candidate.ReadMasterDegreeCandidateByID;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import pt.ist.fenixWebFramework.security.UserView;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class VisualizeCandidateAction extends FenixAction {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        IUserView userView = UserView.getUser();

        String candidateID = request.getParameter("candidateID");
        request.setAttribute("candidateID", candidateID);

        InfoMasterDegreeCandidate masterDegreeCandidate = ReadMasterDegreeCandidateByID.run(candidateID);

        request.setAttribute(PresentationConstants.MASTER_DEGREE_CANDIDATE, masterDegreeCandidate);

        return mapping.findForward("Success");

    }

}