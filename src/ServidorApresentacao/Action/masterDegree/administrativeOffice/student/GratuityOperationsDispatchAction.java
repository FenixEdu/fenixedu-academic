
package ServidorApresentacao.Action.masterDegree.administrativeOffice.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import DataBeans.InfoGratuity;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * This is the Action to display all the master degrees.
 * 
 */
public class GratuityOperationsDispatchAction extends DispatchAction {

	public ActionForward getInformation(ActionMapping mapping, ActionForm form,
												HttpServletRequest request,
												HttpServletResponse response)
			throws Exception {

			HttpSession session = request.getSession(false);
			GestorServicos serviceManager = GestorServicos.manager();
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			
			Integer studentCPID = new Integer(request.getParameter("studentCPID"));
			if (studentCPID == null){
				studentCPID = (Integer) request.getAttribute("studentCPID");
			}
			
			
			DynaActionForm gratuityForm = (DynaActionForm) form;
			
		
			
			List result = null;
		
			try {
				Object args[] = { studentCPID };

				result = (List) serviceManager.executar(userView, "ReadGratuityByStudentCurricularPlanID", args);

			} catch (NonExistingServiceException e) {
				
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}


			List gratuityInformation = null;
			if ((result != null) && (result.size() != 0)){
				request.setAttribute("gratuity", (InfoGratuity) result.get(0));
				
				try {
					Object args[] = { studentCPID };
					gratuityInformation = (List) serviceManager.executar(userView, "ReadGratuityInformationByStudentCurricularPlanID", args);
				} catch (FenixServiceException e) {
					throw new FenixActionException(e);
				}
				
				if (result != null){
					request.setAttribute("gratuityInformation", gratuityInformation);
				} 
			}
			
			
		

			return mapping.findForward("Success");
	  }
	
		
}