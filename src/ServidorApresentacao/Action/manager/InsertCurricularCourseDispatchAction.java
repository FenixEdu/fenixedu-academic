/*
 * Created on 8/Ago/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.validator.DynaValidatorForm;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author lmac1
 */

public class InsertCurricularCourseDispatchAction extends FenixDispatchAction {


	public ActionForward prepareInsert(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws FenixActionException {
				
				Integer degreeId = new Integer(request.getParameter("degreeId"));
				Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));
				request.setAttribute("degreeId", degreeId);
				request.setAttribute("degreeCurricularPlanId", degreeCurricularPlanId);
				return mapping.findForward("insertCurricularCourse");
	}


	public ActionForward insert(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		UserView userView =	(UserView) session.getAttribute(SessionConstants.U_VIEW);
		
		Integer degreeId = new Integer(request.getParameter("degreeId"));
		Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));
    	
		DynaActionForm dynaForm = (DynaValidatorForm) form;
//		A universidade ainda não está bem pois não existe universityOJB. implica + tard alterar o jsp...
//		ver melhor o departamento
		
		Object args[] = { (String) dynaForm.get("name"), (String) dynaForm.get("code"),
			            	(String) dynaForm.get("credits"), (String) dynaForm.get("theoreticalHours"),
							(String) dynaForm.get("praticalHours"), (String) dynaForm.get("theoPratHours"),
							(String) dynaForm.get("labHours"), (String) dynaForm.get("type"),
							(String) dynaForm.get("mandatory"), (String) dynaForm.get("basic"),
							degreeCurricularPlanId };
		GestorServicos manager = GestorServicos.manager();
		List serviceResult = null;
		try {
				serviceResult = (List) manager.executar(userView, "InsertCurricularCourseService", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		
		if(serviceResult != null) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = new ActionError("message.existingCurricularCourse", serviceResult.get(0), serviceResult.get(1));
			actionErrors.add("message.existingCurricularCourse", error);			
			saveErrors(request, actionErrors);
		}
		return mapping.findForward("readDegreeCurricularPlan");
	}			
}