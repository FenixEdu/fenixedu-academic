/*
 * Created on 14/Mar/2003
 *
 */
package ServidorApresentacao.Action.masterDegree.administrativeOffice.guide;

import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * This is the Action to Choose choose, visualize and edit a Guide.
 * 
 */
public class GuideListingDispatchAction extends DispatchAction {

	public ActionForward prepareChooseYear(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
		HttpSession session = request.getSession(false);



		if (session != null) {

			DynaActionForm chooseYearForm = (DynaActionForm) form;
			
			chooseYearForm.set("year", String.valueOf(Calendar.getInstance().get(Calendar.YEAR)));			
			
			return mapping.findForward("PrepareReady");
		  } else
			throw new Exception();   

	}

	public ActionForward chooseYear(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
		HttpSession session = request.getSession(false);

		if (session != null) {
			
			DynaActionForm chooseGuide = (DynaActionForm) form;
			
			GestorServicos serviceManager = GestorServicos.manager();
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			
			// Get the Information
			Integer guideYear = new Integer((String) chooseGuide.get("year"));
					
			Object args[] = { guideYear };
	  
			List result = null;
			try {
				result = (List) serviceManager.executar(userView, "ChooseGuide", args);
			} catch (NonExistingServiceException e) {
				throw new NonExistingActionException("A Guia", e);
			}

			request.setAttribute(SessionConstants.GUIDE_LIST, result);
		  
			return mapping.findForward("ShowGuideList");
		} else
		  throw new Exception();   
	  }


	public ActionForward chooseGuide(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
		HttpSession session = request.getSession(false);

		if (session != null) {
			
			DynaActionForm chooseGuide = (DynaActionForm) form;
			
			GestorServicos serviceManager = GestorServicos.manager();
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			Integer guideYear = new Integer(request.getParameter("year"));
			Integer guideNumber = new Integer(request.getParameter("number"));
				
			Object args[] = { guideNumber, guideYear };
	  
			List result = null;
			try {
				result = (List) serviceManager.executar(userView, "ChooseGuide", args);
			} catch (NonExistingServiceException e) {
				throw new NonExistingActionException("A Guia", e);
			}

			session.setAttribute(SessionConstants.GUIDE_LIST, result);
			request.setAttribute(SessionConstants.GUIDE_NUMBER, guideNumber);
			request.setAttribute(SessionConstants.GUIDE_YEAR, guideYear);
		  
			return mapping.findForward("ShowVersionList");
		} else
		  throw new Exception();   
	  }

	
	  
}
