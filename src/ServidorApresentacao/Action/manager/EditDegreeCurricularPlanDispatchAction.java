/*
 * Created on 5/Ago/2003
 */
package ServidorApresentacao.Action.manager;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ojb.broker.accesslayer.conversions.JavaDate2SqlDateFieldConversion;
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
import ServidorPersistente.Conversores.Calendar2DateFieldConversion;
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
			
			UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
			Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));
			Integer degreeId = new Integer(request.getParameter("degreeId"));

			InfoDegreeCurricularPlan oldInfoDegreeCP = null;

			Object args[] = { degreeCurricularPlanId };
			GestorServicos manager = GestorServicos.manager();
			
			try {
					oldInfoDegreeCP = (InfoDegreeCurricularPlan) manager.executar(userView, "ReadDegreeCurricularPlanService", args);
			} catch (FenixServiceException fenixServiceException) {
				throw new FenixActionException(fenixServiceException.getMessage());
			}
			
//TENHO QUE CRIAR OBJECTOS COMO NO EDIT DEGREE PARA O DEGREETYPE

//como ainda temos Date no DegreeCP Date java->DateSql->Calendar
//e ponho no form como string para poder mostrar
			dynaForm.set("name", oldInfoDegreeCP.getName());
			dynaForm.set("state", oldInfoDegreeCP.getState().getDegreeState().toString());
			
			if(oldInfoDegreeCP.getInitialDate() != null) {
				
				JavaDate2SqlDateFieldConversion iJavaDate = new JavaDate2SqlDateFieldConversion();
				Date iSqlDate = (Date) iJavaDate.javaToSql(oldInfoDegreeCP.getInitialDate());
				Calendar2DateFieldConversion initialCal = new Calendar2DateFieldConversion();
				Calendar initialCalendar= (Calendar) initialCal.sqlToJava(iSqlDate);
			
				String day = (new Integer(initialCalendar.get(Calendar.DAY_OF_MONTH))).toString();
				String month = (new Integer(initialCalendar.get(Calendar.MONTH) + 1)).toString();
				String year = (new Integer(initialCalendar.get(Calendar.YEAR))).toString();
				String initialDateString = day+"/"+month+"/"+year;
			
				dynaForm.set("initialDate", initialDateString);
			}		
						
			if(oldInfoDegreeCP.getEndDate() != null){
			
				JavaDate2SqlDateFieldConversion javaDate = new JavaDate2SqlDateFieldConversion();
				Date sqlDate = (Date) javaDate.javaToSql(oldInfoDegreeCP.getEndDate());
				Calendar2DateFieldConversion endCal = new Calendar2DateFieldConversion();
				Calendar endCalendar= (Calendar) endCal.sqlToJava(sqlDate);
			
				String day = (new Integer(endCalendar.get(Calendar.DAY_OF_MONTH))).toString();
				String month = (new Integer(endCalendar.get(Calendar.MONTH) + 1)).toString();
				String year = (new Integer(endCalendar.get(Calendar.YEAR))).toString();
				String endDateString = day+"/"+month+"/"+year;
			
				dynaForm.set("endDate", endDateString);
			
			}
			
			dynaForm.set("degreeDuration", (String) oldInfoDegreeCP.getDegreeDuration().toString());
			dynaForm.set("minimalYearForOptionalCourses", (String) oldInfoDegreeCP.getMinimalYearForOptionalCourses().toString());
			
			if(oldInfoDegreeCP.getNeededCredits() != null)
				dynaForm.set("neededCredits", (String) oldInfoDegreeCP.getNeededCredits().toString());
			
			if(oldInfoDegreeCP.getMarkType() != null)
				dynaForm.set("markType", oldInfoDegreeCP.getMarkType().getType().toString());
			
			if(oldInfoDegreeCP.getNumerusClausus() != null)
				dynaForm.set("numerusClausus", (String) oldInfoDegreeCP.getNumerusClausus().toString());

			request.setAttribute("degreeCurricularPlanId", degreeCurricularPlanId);
			request.setAttribute("degreeId", degreeId);
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
		
		Integer oldDegreeCPId = (Integer) dynaForm.get("degreeCurricularPlanId");
		Integer degreeId = (Integer) dynaForm.get("degreeId");
		
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

		DegreeCurricularPlanState state = new DegreeCurricularPlanState(stateInt);

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

		if(endDateString.compareTo("") != 0){
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
		
		if(neededCreditsString.compareTo("") != 0) {
			Double neededCredits = new Double(neededCreditsString); 
			newInfoDegreeCP.setNeededCredits(neededCredits);
		}
		
		if(markTypeString.compareTo("") != 0){

			Integer markTypeInt = new Integer(markTypeString);
			MarkType markType = new MarkType(markTypeInt);
			newInfoDegreeCP.setMarkType(markType);
		}
		

		if(numerusClaususString.compareTo("") != 0){
			Integer numerusClausus = new Integer (numerusClaususString);
			newInfoDegreeCP.setNumerusClausus(numerusClausus);
		}
																						
		newInfoDegreeCP.setName(name);
		newInfoDegreeCP.setState(state);										
		newInfoDegreeCP.setDegreeDuration(degreeDuration);
		newInfoDegreeCP.setMinimalYearForOptionalCourses(minimalYearForOptionalCourses);
																						
			
//			TIRO DAQUI O DEGREEID E LEIO-O NO SERVICO---PENSAR
			
		Object args[] = { oldDegreeCPId, newInfoDegreeCP, degreeId };
		GestorServicos manager = GestorServicos.manager();
		List serviceResult = null;
		try {
				serviceResult = (List) manager.executar(userView, "EditDegreeCurricularPlanService", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		
		if(serviceResult != null) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			if(serviceResult.get(0) != null) {
				error = new ActionError("message.existingDegreeCPName", serviceResult.get(0));
				actionErrors.add("message.existingDegreeCPName", error);
			}			
			saveErrors(request, actionErrors);
		}
		return mapping.findForward("readDegree");
	}			
}