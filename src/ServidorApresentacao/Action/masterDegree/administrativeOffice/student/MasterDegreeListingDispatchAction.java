
package ServidorApresentacao.Action.masterDegree.administrativeOffice.student;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoCurso;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 * This is the Action to display all the master degrees.
 * 
 */
public class MasterDegreeListingDispatchAction extends DispatchAction {

	public ActionForward chooseDegreeFromList(ActionMapping mapping, ActionForm form,
											HttpServletRequest request,
											HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
		GestorServicos serviceManager = GestorServicos.manager();
		
		if (session != null) {
			
			session.removeAttribute(SessionConstants.MASTER_DEGREE_LIST);
			
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			
			TipoCurso degreeType = TipoCurso.MESTRADO_OBJ;
			
			Object args[] = {degreeType};
	  
			List result = null;
			try {
				result = (List) serviceManager.executar(userView, "ReadAllMasterDegrees", args);
			} catch (NonExistingServiceException e) {
				throw new NonExistingActionException("O Curso de Mestrado", e);
			}

			request.setAttribute(SessionConstants.MASTER_DEGREE_LIST, result);
		  
			return mapping.findForward("DisplayMasterDegreeList");
		} else
		  throw new Exception();   
	  }

	public ActionForward chooseMasterDegree(ActionMapping mapping, ActionForm form,
										HttpServletRequest request,
										HttpServletResponse response)
		throws Exception {

	
		HttpSession session = request.getSession(false);

		if (session != null) {
		
			GestorServicos serviceManager = GestorServicos.manager();
		
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			
			//Get the Chosen Master Degree
			Integer masterDegreeID = new Integer((String) request.getParameter("degreeID"));
			
			Object args[] = { masterDegreeID };
			List result = null;
			
			try {

			result = (List) serviceManager.executar(userView, "ReadCPlanFromChosenMasterDegree", args);

			} catch (NonExistingServiceException e) {
				throw new NonExistingActionException("O plano curricular ", e);
			}

			request.setAttribute(SessionConstants.MASTER_DEGREE_CURRICULAR_PLAN_LIST, result);
			
			return mapping.findForward("MasterDegreeReady");
		} else
		  throw new Exception();   
	  }
	  
	/**
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return The Student's from a Degree Curricular Plan
	 * @throws Exception
	 */
	public ActionForward getStudentsFromDCP(ActionMapping mapping, ActionForm form,
											HttpServletRequest request,
											HttpServletResponse response)
			throws Exception {

	
			HttpSession session = request.getSession(false);

			if (session != null) {
		
				GestorServicos serviceManager = GestorServicos.manager();
		
				IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			
				//Get the Selected Degree Curricular Plan
				Integer degreeCurricularPlanID = new Integer((String) request.getParameter("curricularPlanID"));
			
				Object args[] = { degreeCurricularPlanID , TipoCurso.MESTRADO_OBJ };
				List result = null;
			
				try {

				result = (List) serviceManager.executar(userView, "ReadStudentsFromDegreeCurricularPlan", args);

				} catch (NonExistingServiceException e) {
					throw new NonExistingActionException("error.exception.noStudents", "");
				}

				BeanComparator numberComparator = new BeanComparator("infoStudent.number");
				Collections.sort(result, numberComparator);

				request.setAttribute(SessionConstants.STUDENT_LIST, result);
			
				return mapping.findForward("CurricularPlanReady");
			} else
			  throw new Exception();   
	  }
  
	  
}