/*
 * Created on 31/Jul/2003
 */
package ServidorApresentacao.Action.manager;

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

import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.util.Cloner;
import Dominio.Curso;
import Dominio.ICurso;
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
		
		ICurso degree = new Curso(degreeId);
		InfoDegree infoDegree = Cloner.copyIDegree2InfoDegree((ICurso) degree);

		
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
		String[] initialDateArgs =  initialDateString.split("/",2);
		System.out.println("SPLITSPLIT11111111111"+initialDateArgs);
		
		Date initialDate = new Date("initialDateArgs.");
		Date endDate = new Date(endDateString);
		Double neededCredits = new Double(neededCreditsString);
		MarkType markType = new MarkType(markTypeString);
		
		InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();											
																						
		infoDegreeCurricularPlan.setName(name);
		infoDegreeCurricularPlan.setInfoDegree(infoDegree);
		infoDegreeCurricularPlan.setState(state);
		infoDegreeCurricularPlan.setInitialDate(initialDate);
		infoDegreeCurricularPlan.setEndDate(endDate);											
		infoDegreeCurricularPlan.setDegreeDuration(degreeDuration);
		infoDegreeCurricularPlan.setMinimalYearForOptionalCourses(minimalYearForOptionalCourses);
		infoDegreeCurricularPlan.setNeededCredits(neededCredits);
		infoDegreeCurricularPlan.setMarkType(markType);
		infoDegreeCurricularPlan.setNumerusClausus(numerusClausus);
																						
			
		Object args[] = { infoDegreeCurricularPlan };
		GestorServicos manager = GestorServicos.manager();
		List serviceResult = null;
		try {
				serviceResult = (List) manager.executar(userView, "InsertDegreeCurricularPlanService", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e.getMessage());
		}
		System.out.println("RESULTADO DO INSERT"+serviceResult);

		try {	
				List degreeCurricularPlans = null;
				degreeCurricularPlans = (List) manager.executar(
													userView,
													"ReadDegreeCurricularPlansService",
													null);
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
			
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		return mapping.findForward("readDegree");
	}			
}

