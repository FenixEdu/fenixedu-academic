/*
 * Created on 5/Ago/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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
public class EditDegreeCurricularPlanDispatchAction extends FenixDispatchAction {


	public ActionForward prepareEdit(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws FenixActionException {
				
			HttpSession session = request.getSession(false);
			DynaActionForm dynaForm = (DynaActionForm) form;
			
			UserView userView =
				(UserView) session.getAttribute(SessionConstants.U_VIEW);
			Integer degreeCurricularPlanId =new Integer(request.getParameter("degreeCurricularPlanId"));
			
			InfoDegreeCurricularPlan oldInfoDegreeCP = null;

			Object args[] = { degreeCurricularPlanId };
			GestorServicos manager = GestorServicos.manager();
			try{
				oldInfoDegreeCP = (InfoDegreeCurricularPlan) manager.executar(userView, "ReadDegreeCurricularPlanService", args);
			}catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
			}
			
		
			dynaForm.set("name", (String) oldInfoDegreeCP.getName());
			dynaForm.set("state", (DegreeCurricularPlanState) oldInfoDegreeCP.getState());
			
			if(!oldInfoDegreeCP.getInitialDate().toString().equals(""))
			dynaForm.set("initialDate", (Date) oldInfoDegreeCP.getInitialDate());
			
			if(!oldInfoDegreeCP.getEndDate().toString().equals(""))
			dynaForm.set("endDate", (Date) oldInfoDegreeCP.getEndDate());
			
			dynaForm.set("degreeDuration", (Integer) oldInfoDegreeCP.getDegreeDuration());
			dynaForm.set("setMinimalYearForOptionalCourses", (Integer) oldInfoDegreeCP.getMinimalYearForOptionalCourses());
			
			if(!oldInfoDegreeCP.getNeededCredits().toString().equals(""))
			dynaForm.set("neddedCredits", (Double) oldInfoDegreeCP.getNeededCredits());
			
			if(!oldInfoDegreeCP.getMarkType().toString().equals(""))
			dynaForm.set("markType", (MarkType) oldInfoDegreeCP.getMarkType());
			
			if(!oldInfoDegreeCP.getNumerusClausus().toString().equals(""))
			dynaForm.set("numerusClausus", (Integer) oldInfoDegreeCP.getNumerusClausus());
						
		
			request.setAttribute("degreeCurricularPlanId",degreeCurricularPlanId);
			return mapping.findForward("editDegreeCP");
		}
//mm que esteja a editar e importante ter o id do degree

	public ActionForward edit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		UserView userView =	(UserView) session.getAttribute(SessionConstants.U_VIEW);
    	
		DynaActionForm dynaForm = (DynaValidatorForm) form;
		
		Integer oldDegreeCPId =(Integer) dynaForm.get("degreeCurricularPlanId");
		Integer degreeId =(Integer) dynaForm.get("degreeCurricularPlanId");
		
		
		InfoDegreeCurricularPlan newInfoDegreeCP = new InfoDegreeCurricularPlan();		

		
		String name = (String) dynaForm.get("name");
		Integer stateInt = new Integer((String) dynaForm.get("state"));
		
		
		String initialDateString = (String) dynaForm.get("initialDate");
		String endDateString = (String) dynaForm.get("endDate");
		
		Integer degreeDuration = new Integer((String) dynaForm.get("degreeDuration"));
		Integer minimalYearForOptionalCourses = new Integer((String) dynaForm.get("minimalYearForOptionalCourses"));
		
		String neededCreditsString = (String) dynaForm.get("neededCredits");
		
		String markTypeString = (String) dynaForm.get("markType");
		String numerusClaususString = (String) dynaForm.get("numerusClausus");
		
//System.out.println("initialDate"+initialDateString.compareTo(""));



		DegreeCurricularPlanState state = new DegreeCurricularPlanState(stateInt);


//		Calendar initialDate = null;
		if(initialDateString.compareTo("") != 0){
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
			newInfoDegreeCP.setInitialDate(initialDate.getTime());
		}

//		Calendar endDate = null;
		if(initialDateString.compareTo("") != 0){
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
			newInfoDegreeCP.setEndDate(endDate.getTime());
		}
		
//		 neededCredits = null;
		if(neededCreditsString.compareTo("") != 0) {
			Double neededCredits = new Double(neededCreditsString); 
			newInfoDegreeCP.setNeededCredits(neededCredits);
		}
		
		
//		MarkType markType = null;
//System.out.println("mark type string->"+markTypeString);

		if(markTypeString.compareTo("") != 0){
			
//			System.out.println("ENTROU NO MARK TYPE");
			Integer markTypeInt = new Integer(markTypeString);
			MarkType markType = new MarkType(markTypeInt);
			newInfoDegreeCP.setMarkType(markType);
		}
		
//		Integer numerusClausus = null;
		if(numerusClaususString.compareTo("") != 0){
			Integer numerusClausus = new Integer (numerusClaususString);
			newInfoDegreeCP.setNumerusClausus(numerusClausus);
		}
																						
		newInfoDegreeCP.setName(name);
		newInfoDegreeCP.setState(state);										
		newInfoDegreeCP.setDegreeDuration(degreeDuration);
		newInfoDegreeCP.setMinimalYearForOptionalCourses(minimalYearForOptionalCourses);
																						
			
		Object args[] = { oldDegreeCPId, newInfoDegreeCP, degreeId };
		GestorServicos manager = GestorServicos.manager();
		List serviceResult = null;
//		System.out.println("Antes do 1º Serviço");
		try {
				serviceResult = (List) manager.executar(userView, "EditDegreeCurricularPlanService", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}


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
						error = new ActionError("message.existingDegreeCPNameAndDegree", serviceResult.get(1),serviceResult.get(0));
						actionErrors.add("message.existingDegreeCPNameAndDegree", error);
					}			
					saveErrors(request, actionErrors);
				}
				Collections.sort(degreeCurricularPlans);
				request.setAttribute("lista de planos curriculares",degreeCurricularPlans);
				request.setAttribute("degreeId",degreeId);
			System.out.println("depois do 2º Serviço FIM DA ACTION");
			
			
			System.out.println("DEGREECP QUE KERO!!!!!!!111"+newInfoDegreeCP);
			
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		
		return mapping.findForward("readDegreeCP");
	}			
}

