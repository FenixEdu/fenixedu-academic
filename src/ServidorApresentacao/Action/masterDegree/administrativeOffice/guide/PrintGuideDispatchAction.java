
package ServidorApresentacao.Action.masterDegree.administrativeOffice.guide;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoGuide;
import DataBeans.InfoMasterDegreeCandidate;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import ServidorApresentacao.Action.sop.utils.SessionUtils;
import Util.GuideRequester;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * 
 */
public class PrintGuideDispatchAction extends DispatchAction {

	public ActionForward prepare(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		SessionUtils.validSessionVerification(request, mapping);
		HttpSession session = request.getSession(false);

		if (session != null) {
			DynaActionForm createContributorForm = (DynaActionForm) form;
			GestorServicos serviceManager = GestorServicos.manager();
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

			Boolean passwordPrint = (Boolean) session.getAttribute(SessionConstants.PRINT_PASSWORD);
			InfoGuide infoGuide = (InfoGuide) session.getAttribute(SessionConstants.GUIDE);

System.out.println("Numero de linhas da guia : " + infoGuide.getInfoGuideEntries().size());
			
			if (infoGuide.getGuideRequester().equals(GuideRequester.CANDIDATE_TYPE)){
				// Read The Candidate
				InfoMasterDegreeCandidate infoMasterDegreeCandidate = null;
				try {
					Object args[] = { infoGuide.getInfoExecutionDegree(), infoGuide.getInfoPerson()};
					infoMasterDegreeCandidate = (InfoMasterDegreeCandidate) serviceManager.executar(userView, "ReadMasterDegreeCandidate", args);
				} catch (FenixServiceException e) {
					throw new FenixActionException();
				}
				session.setAttribute(SessionConstants.MASTER_DEGREE_CANDIDATE, infoMasterDegreeCandidate);
				
			} else if (infoGuide.getGuideRequester().equals(GuideRequester.STUDENT_TYPE)){
				// TODO: Comming soon :)
			}
			
			
			return mapping.findForward("PrintReady");
		  } else
			throw new Exception();   

	}
		

	  
}
