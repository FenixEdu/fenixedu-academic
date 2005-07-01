package net.sourceforge.fenixedu.presentationTier.Action.masterDegree.candidate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeCandidate;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionUtils;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 *
 */
public class VisualizeCandidateAction extends FenixAction {
    
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        IUserView userView = SessionUtils.getUserView(request);
        
        Integer candidateID = Integer.valueOf(request.getParameter("candidateID"));
        request.setAttribute("candidateID", candidateID);
        
        InfoMasterDegreeCandidate masterDegreeCandidate = null;
        try {
            Object args[] = { candidateID };
            masterDegreeCandidate = (InfoMasterDegreeCandidate) ServiceUtils.executeService(userView,
                    "ReadMasterDegreeCandidateByID", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        
        request.setAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE, masterDegreeCandidate);
        
        return mapping.findForward("Success");

    }
    
}
