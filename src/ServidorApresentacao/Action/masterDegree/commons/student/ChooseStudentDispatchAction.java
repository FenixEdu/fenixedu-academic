package ServidorApresentacao.Action.masterDegree.commons.student;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;

import framework.factory.ServiceManagerServiceFactory;

import DataBeans.InfoStudentCurricularPlan;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.TipoCurso;

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 *         Joana Mota (jccm@rnl.ist.utl.pt)
 */

public class ChooseStudentDispatchAction extends DispatchAction {

	public ActionForward choose(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession();

		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		DynaActionForm studentForm = (DynaActionForm) form;

		Integer studentNumber = (Integer) studentForm.get("number");

		List result = null;
		
		try {
			Object args[] = { studentNumber , TipoCurso.MESTRADO_OBJ};
			result = (List) ServiceManagerServiceFactory.executeService(userView, "ReadStudentCurricularPlansByNumberAndDegreeType", args);
		} catch (NonExistingServiceException e) {
			throw new NonExistingActionException("O Aluno");
		}

		

		if (result.size() == 1){
			request.setAttribute("studentCPID", ((InfoStudentCurricularPlan) result.get(0)).getIdInternal());
			return mapping.findForward("StudentCurricularPlanChosen");	
		}

		request.setAttribute("studentCurricularPlans", result);
		return mapping.findForward("ShowCurricularPlans");	
	}

}