
package ServidorApresentacao.Action.commons;

import java.util.ArrayList;

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
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;


/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ChooseExecutionYearDispatchAction extends DispatchAction {

	public ActionForward prepareChooseExecutionYear(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
		HttpSession session = request.getSession(false);

		if (session != null) {
			DynaActionForm chooseExecutionYearForm = (DynaActionForm) form;
			GestorServicos serviceManager = GestorServicos.manager();
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			
			// Get Execution Year List
			
			ArrayList executionYearList = null; 			
			try {
				executionYearList = (ArrayList) serviceManager.executar(userView, "ReadExecutionYears", null);
			} catch (ExistingServiceException e) {
				throw new ExistingActionException(e);
			}

			request.setAttribute(SessionConstants.EXECUTION_YEAR_LIST, executionYearList);
						
			return mapping.findForward("PrepareSuccess");
		  } else
			throw new Exception();   

	}



	public ActionForward chooseExecutionYear(ActionMapping mapping, ActionForm form,
									HttpServletRequest request,
									HttpServletResponse response)
		throws Exception {

		
		HttpSession session = request.getSession(false);

		if (session != null) {
			
			session.setAttribute(SessionConstants.EXECUTION_YEAR, request.getParameter("executionYear"));
						
			return mapping.findForward("ChooseSuccess");
		  } else
			throw new Exception();   
	}
}
