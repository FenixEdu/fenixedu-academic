/*
 * Created on 31/Jul/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.Calendar;
import java.util.Collections;
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

import DataBeans.InfoDegreeCurricularPlan;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.DegreeCurricularPlanState;
import Util.MarkType;

/**
 * @author lmac1
 */
public class InsertDegreeCurricularPlanDispatchAction extends FenixDispatchAction {


	public ActionForward prepareInsert(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws FenixActionException {
				
				
//				DynaActionForm dynaForm = (DynaValidatorForm) form;
				
				System.out.println("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqANTES");
				Integer degreeId =new Integer(request.getParameter("degreeId"));
				
//				dynaForm.set("degreeId",degreeId);
				request.setAttribute("degreeId",degreeId);

			return mapping.findForward("insertDegreeCurricularPlan");
		}


	public ActionForward insert(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
			System.out.println("qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqDEPOIS");
		HttpSession session = request.getSession(false);
		UserView userView =	(UserView) session.getAttribute(SessionConstants.U_VIEW);
    	
		DynaActionForm dynaForm = (DynaValidatorForm) form;
		
		Integer degreeId =(Integer) dynaForm.get("degreeId");
		
//		Integer degreeId =(Integer)request.getAttribute("degreeId");
		System.out.println("DEGREEIDSSSSSSSSSSSS"+degreeId);
		
		
//		InfoDegree infoDegree = new InfoDegree();
//		infoDegree.setIdInternal(degreeId);

		
		String name = (String) dynaForm.get("name");
		Integer stateInt = new Integer((String) dynaForm.get("state"));
		String initialDateString = (String) dynaForm.get("initialDate");
		String endDateString = (String) dynaForm.get("endDate");
		Integer degreeDuration = new Integer((String) dynaForm.get("degreeDuration"));
		Integer minimalYearForOptionalCourses = new Integer((String) dynaForm.get("minimalYearForOptionalCourses"));
		String neededCreditsString = (String) dynaForm.get("neededCredits");
		Integer markTypeString = new Integer((String) dynaForm.get("markType"));
		Integer numerusClausus = new Integer ((String) dynaForm.get("numerusClausus"));
			
		DegreeCurricularPlanState state = new DegreeCurricularPlanState(stateInt);
		String[] initialDateTokens = initialDateString.split("/");

		Calendar initialDate = Calendar.getInstance();
				initialDate.set(
								Calendar.DAY_OF_MONTH,
								(new Integer(initialDateTokens[0])).intValue());
				initialDate.set(
								Calendar.MONTH,
								(new Integer(initialDateTokens[1])).intValue() - 1);
				initialDate.set(
								Calendar.YEAR,
								(new Integer(initialDateTokens[2])).intValue());

		String[] endDateTokens = endDateString.split("/");

		Calendar endDate = Calendar.getInstance();
				endDate.set(
							Calendar.DAY_OF_MONTH,
							(new Integer(endDateTokens[0])).intValue());
				endDate.set(
							Calendar.MONTH,
							(new Integer(endDateTokens[1])).intValue() - 1);
				endDate.set(
							Calendar.YEAR,
							(new Integer(endDateTokens[2])).intValue());


		Double neededCredits = new Double(neededCreditsString);
		MarkType markType = new MarkType(markTypeString);
		
		InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();											
																						
		infoDegreeCurricularPlan.setName(name);
//		infoDegreeCurricularPlan.setInfoDegree(infoDegree);
		infoDegreeCurricularPlan.setState(state);
		infoDegreeCurricularPlan.setInitialDate(initialDate.getTime());
		infoDegreeCurricularPlan.setEndDate(endDate.getTime());											
		infoDegreeCurricularPlan.setDegreeDuration(degreeDuration);
		infoDegreeCurricularPlan.setMinimalYearForOptionalCourses(minimalYearForOptionalCourses);
		infoDegreeCurricularPlan.setNeededCredits(neededCredits);
		infoDegreeCurricularPlan.setMarkType(markType);
		infoDegreeCurricularPlan.setNumerusClausus(numerusClausus);
																						
			
		Object args[] = { infoDegreeCurricularPlan, degreeId };
		GestorServicos manager = GestorServicos.manager();
		List serviceResult = null;
		System.out.println("Antes do 1º Serviço");
		try {
				serviceResult = (List) manager.executar(userView, "InsertDegreeCurricularPlanService", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		System.out.println("RESULTADO DO INSERT"+serviceResult);

		Object args1[] = { degreeId };
		try {	
				List degreeCurricularPlans = null;
			System.out.println("Antes do 2º Serviço");
				degreeCurricularPlans = (List) manager.executar(
													userView,
													"ReadDegreeCurricularPlansService",
													args1);
			
				if (serviceResult != null) {
					ActionErrors actionErrors = new ActionErrors();
					ActionError error = null;
					if(serviceResult.get(0) != null) {
						error = new ActionError("message.existingDegreeCPNameAndDegree", serviceResult.get(0));
						actionErrors.add("message.existingDegreeCPNameAndDegree", error);
					}			
					saveErrors(request, actionErrors);
				}
				Collections.sort(degreeCurricularPlans);
				request.setAttribute("lista de planos curriculares",degreeCurricularPlans);
				request.setAttribute("degreeId",degreeId);
			System.out.println("depois do 2º Serviço FIM DA ACTION");
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		System.out.println("FIM FIM FIMF FIM");
		return mapping.findForward("g");
	}			
}

