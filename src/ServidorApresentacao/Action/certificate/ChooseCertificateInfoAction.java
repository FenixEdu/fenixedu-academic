
package ServidorApresentacao.Action.certificate;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoGuide;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.Specialization;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 */
public class ChooseCertificateInfoAction extends DispatchAction {

	public ActionForward prepare(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
		HttpSession session = request.getSession(false);



		if (session != null) {
			
			session.removeAttribute(SessionConstants.SPECIALIZATIONS);
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			
			ArrayList specializations = Specialization.toArrayList();
			session.setAttribute(SessionConstants.SPECIALIZATIONS, specializations);
					
			return mapping.findForward("PrepareReady");
		  } else
			throw new Exception();   

	}




	public ActionForward choose(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
		HttpSession session = request.getSession(false);

		if (session != null) {
			
			DynaActionForm chooseGuide = (DynaActionForm) form;
			
			GestorServicos serviceManager = GestorServicos.manager();
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			
			// Get the Information
			Integer guideNumber = new Integer((String) chooseGuide.get("guideNumber"));
			Integer guideYear = new Integer((String) chooseGuide.get("guideYear"));
					
			Object args[] = { guideNumber, guideYear };
	  
	  		List result = null;
			try {
				result = (List) serviceManager.executar(userView, "ChooseGuide", args);
			} catch (NonExistingServiceException e) {
				throw new NonExistingActionException("A Guia", e);
			}

			session.setAttribute(SessionConstants.GUIDE_LIST, result);
			if (result.size() == 1) {

				request.setAttribute(SessionConstants.GUIDE, result.get(0));
				return mapping.findForward("ActionReady");
			}

			request.setAttribute(SessionConstants.GUIDE_YEAR, guideYear);
			request.setAttribute(SessionConstants.GUIDE_NUMBER, guideNumber);
		  
		  	return mapping.findForward("ShowVersionList");
		} else
		  throw new Exception();   
	  }
	  
	  
	public ActionForward chooseVersion(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
		HttpSession session = request.getSession(false);

		if (session != null) {
			
			DynaActionForm chooseGuide = (DynaActionForm) form;
			
			GestorServicos serviceManager = GestorServicos.manager();
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			
			// Get the Information
			Integer guideNumber = new Integer((String) request.getParameter("number"));
			Integer guideYear = new Integer((String) request.getParameter("year"));
			Integer guideVersion = new Integer((String) request.getParameter("version"));
					
			Object args[] = { guideNumber, guideYear , guideVersion };
	  
			InfoGuide infoGuide = null;

			try {
				infoGuide = (InfoGuide) serviceManager.executar(userView, "ChooseGuide", args);
			} catch (NonExistingServiceException e) {
				throw new NonExistingActionException("A Versão da Guia", e);
			}

			request.setAttribute(SessionConstants.GUIDE, infoGuide);
			return mapping.findForward("ActionReady");
		} else
		  throw new Exception();   
	  }

	  
}
