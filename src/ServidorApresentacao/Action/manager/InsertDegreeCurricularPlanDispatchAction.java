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
				
				Integer degreeId = new Integer(request.getParameter("degreeId"));
				request.setAttribute("degreeId", degreeId);
				return mapping.findForward("insertDegreeCurricularPlan");
		}


	public ActionForward insert(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		UserView userView =	(UserView) session.getAttribute(SessionConstants.U_VIEW);
    	
		DynaActionForm dynaForm = (DynaValidatorForm) form;
		Integer degreeId = (Integer) dynaForm.get("degreeId");
		String name = (String) dynaForm.get("name");
		Integer stateInt = new Integer((String) dynaForm.get("state"));
		String initialDateString = (String) dynaForm.get("initialDate");
		String endDateString = (String) dynaForm.get("endDate");
		Integer degreeDuration = new Integer((String) dynaForm.get("degreeDuration"));
		Integer minimalYearForOptionalCourses = new Integer((String) dynaForm.get("minimalYearForOptionalCourses"));
		String neededCreditsString = (String) dynaForm.get("neededCredits");
		String markTypeString = (String) dynaForm.get("markType");
		String numerusClaususString = (String) dynaForm.get("numerusClausus");

  		InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
		DegreeCurricularPlanState state = new DegreeCurricularPlanState(stateInt);

 		if(initialDateString.compareTo("") != 0) {
			String[] initialDateTokens = initialDateString.split("/");
			Calendar initialDate = Calendar.getInstance();
			initialDate.set(Calendar.DAY_OF_MONTH, (new Integer(initialDateTokens[0])).intValue());
			initialDate.set(Calendar.MONTH, (new Integer(initialDateTokens[1])).intValue() - 1);
			initialDate.set(Calendar.YEAR, (new Integer(initialDateTokens[2])).intValue());
			infoDegreeCurricularPlan.setInitialDate(initialDate.getTime());
 		}

		if(endDateString.compareTo("") != 0) {
			String[] endDateTokens = endDateString.split("/");
			Calendar endDate = Calendar.getInstance();
			endDate.set(Calendar.DAY_OF_MONTH, (new Integer(endDateTokens[0])).intValue());
			endDate.set(Calendar.MONTH, (new Integer(endDateTokens[1])).intValue() - 1);
			endDate.set(Calendar.YEAR, (new Integer(endDateTokens[2])).intValue());
			infoDegreeCurricularPlan.setEndDate(endDate.getTime());
		}

		if(neededCreditsString.compareTo("") != 0) {
			Double neededCredits = new Double(neededCreditsString); 
			infoDegreeCurricularPlan.setNeededCredits(neededCredits);
		}

		if(markTypeString.compareTo("") != 0) {
			MarkType markType = new MarkType(new Integer(markTypeString));
			infoDegreeCurricularPlan.setMarkType(markType);
		}
		
		if(numerusClaususString.compareTo("") != 0){
			Integer numerusClausus = new Integer (numerusClaususString);
			infoDegreeCurricularPlan.setNumerusClausus(numerusClausus);
		}
																						
		infoDegreeCurricularPlan.setName(name);
		infoDegreeCurricularPlan.setState(state);										
		infoDegreeCurricularPlan.setDegreeDuration(degreeDuration);
		infoDegreeCurricularPlan.setMinimalYearForOptionalCourses(minimalYearForOptionalCourses);
																						
		Object args[] = { infoDegreeCurricularPlan, degreeId };
		GestorServicos manager = GestorServicos.manager();
		List serviceResult = null;
		try {
				serviceResult = (List) manager.executar(userView, "InsertDegreeCurricularPlanService", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		
		Object arguments[] = { degreeId };
		try {	
				List degreeCurricularPlans = null;
				degreeCurricularPlans = (List) manager.executar(
													userView,
													"ReadDegreeCurricularPlansService",
													arguments);
			
				if (serviceResult != null) {
					ActionErrors actionErrors = new ActionErrors();
					ActionError error = null;
					if(serviceResult.get(0) != null) {
						error = new ActionError("message.existingDegreeCPNameAndDegree", serviceResult.get(1), serviceResult.get(0));
						actionErrors.add("message.existingDegreeCPNameAndDegree", error);
					}			
					saveErrors(request, actionErrors);
				}
				Collections.sort(degreeCurricularPlans);
				request.setAttribute("lista de planos curriculares", degreeCurricularPlans);
				request.setAttribute("degreeId", degreeId);
			
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		return mapping.findForward("readDegree");
	}			
}

