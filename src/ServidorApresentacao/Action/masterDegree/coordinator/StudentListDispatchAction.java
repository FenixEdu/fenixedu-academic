
package ServidorApresentacao.Action.masterDegree.coordinator;

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

import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoCurso;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class StudentListDispatchAction extends DispatchAction {
	
	
	public ActionForward getDegreeCurricularPlan (ActionMapping mapping, ActionForm form,
													HttpServletRequest request,
													HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);

		if (session != null) {
			
			InfoExecutionDegree infoExecutionDegree = (InfoExecutionDegree) session.getAttribute(SessionConstants.MASTER_DEGREE);

System.out.println(infoExecutionDegree);

			InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
			infoDegreeCurricularPlan = infoExecutionDegree.getInfoDegreeCurricularPlan(); 

System.out.println(infoDegreeCurricularPlan);

			request.setAttribute(SessionConstants.MASTER_DEGREE_CURRICULAR_PLAN_LIST, infoDegreeCurricularPlan);

			return mapping.findForward("MasterDegreeReady");
		} else
		  throw new Exception();   
	  }


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

			BeanComparator numberComparator = new BeanComparator("number");
				Collections.sort(result, numberComparator);

			request.setAttribute(SessionConstants.STUDENT_LIST, result);
	
			return mapping.findForward("CurricularPlanReady");
		} else
		  throw new Exception();   
  }
			
}
