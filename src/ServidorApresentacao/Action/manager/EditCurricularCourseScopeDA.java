/*
 * Created on 21/Ago/2003
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

import DataBeans.InfoBranch;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoCurricularSemester;
import DataBeans.InfoCurricularYear;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author lmac1
 */
public class EditCurricularCourseScopeDA extends FenixDispatchAction {


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
			Integer curricularCourseId = new Integer(request.getParameter("curricularCourseId"));
			Integer curricularCourseScopeId = new Integer(request.getParameter("curricularCourseScopeId"));
			Integer degreeId = new Integer(request.getParameter("degreeId"));
			

			InfoCurricularCourseScope oldInfoCurricularCourseScope = null;

			Object args[] = { curricularCourseScopeId };
			GestorServicos manager = GestorServicos.manager();
			
			try {
				oldInfoCurricularCourseScope = (InfoCurricularCourseScope) manager.executar(userView, "ReadCurricularCourseScope", args);
			} catch (FenixServiceException fenixServiceException) {
				throw new FenixActionException(fenixServiceException.getMessage());
			}
//			System.out.println("VELHO CURRICULAR COURSEScope"+oldInfoCurricularCourseScope);
			

			if(oldInfoCurricularCourseScope.getTheoreticalHours() != null)
				dynaForm.set("theoreticalHours", oldInfoCurricularCourseScope.getTheoreticalHours().toString());
			
			if(oldInfoCurricularCourseScope.getPraticalHours() != null)
				dynaForm.set("praticalHours", oldInfoCurricularCourseScope.getPraticalHours().toString());
			
			if(oldInfoCurricularCourseScope.getTheoPratHours() != null)
				dynaForm.set("theoPratHours", oldInfoCurricularCourseScope.getTheoPratHours().toString());
			
			if(oldInfoCurricularCourseScope.getLabHours() != null)
				dynaForm.set("labHours", oldInfoCurricularCourseScope.getLabHours().toString());
			
			if(oldInfoCurricularCourseScope.getMaxIncrementNac() != null)
				dynaForm.set("maxIncrementNac", oldInfoCurricularCourseScope.getMaxIncrementNac());
			
			if(oldInfoCurricularCourseScope.getMinIncrementNac() != null)
				dynaForm.set("minIncrementNac", oldInfoCurricularCourseScope.getMinIncrementNac());
			
			if(oldInfoCurricularCourseScope.getWeigth() != null)
				dynaForm.set("weight", oldInfoCurricularCourseScope.getWeigth());

			if(oldInfoCurricularCourseScope.getCredits() != null)
				dynaForm.set("credits", oldInfoCurricularCourseScope.getCredits().toString());
				
			dynaForm.set("branchCode", oldInfoCurricularCourseScope.getInfoBranch().getCode().toString());
				
			if(oldInfoCurricularCourseScope.getInfoCurricularSemester() != null)
				dynaForm.set("curricularSemester", oldInfoCurricularCourseScope.getInfoCurricularSemester().getSemester().toString());
			
			if(oldInfoCurricularCourseScope.getInfoCurricularSemester().getInfoCurricularYear() != null)
				dynaForm.set("curricularYear", oldInfoCurricularCourseScope.getInfoCurricularSemester().getInfoCurricularYear().getYear().toString());
			
			
			request.setAttribute("degreeCurricularPlanId", degreeCurricularPlanId);
			request.setAttribute("degreeId", degreeId);
			request.setAttribute("curricularCourseId", curricularCourseId);
			request.setAttribute("curricularCourseScopeId", curricularCourseScopeId);
			return mapping.findForward("editCurricularCourse");
		}
				

	public ActionForward edit(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		UserView userView =	(UserView) session.getAttribute(SessionConstants.U_VIEW);
    	
		DynaActionForm dynaForm = (DynaValidatorForm) form;
		
		Integer degreeCPId = (Integer) dynaForm.get("degreeCurricularPlanId");
		Integer curricularCourseId = new Integer(request.getParameter("curricularCourseId"));
		Integer degreeId = (Integer) dynaForm.get("degreeId");
		Integer oldCurricularCourseScopeId = new Integer(request.getParameter("curricularCourseScopeId"));
		
		InfoCurricularCourseScope newInfoCurricularCourseScope = new InfoCurricularCourseScope();	
//			
//		Object arg[] = { degreeCPId };
//			
//		InfoDegreeCurricularPlan infoDegreeCP = null;
//		GestorServicos manager = GestorServicos.manager();
//			
//		try {
//					infoDegreeCP = (InfoDegreeCurricularPlan) manager.executar(userView, "ReadDegreeCurricularPlan", arg);
//			} catch(FenixServiceException e) {
//				throw new FenixActionException(e);
//			}
			    
		String creditsString = (String) dynaForm.get("credits");
		String theoreticalHoursString = (String) dynaForm.get("theoreticalHours");
		String praticalHoursString = (String) dynaForm.get("praticalHours");
		String theoPratHoursString = (String) dynaForm.get("theoPratHours");
		String labHoursString = (String) dynaForm.get("labHours");
		String maxIncrementNacString = (String) dynaForm.get("maxIncrementNac");
		String minIncrementNacString = (String) dynaForm.get("minIncrementNac");
		String weightString = (String) dynaForm.get("weight");
		String branchCode = (String) dynaForm.get("branchCode");
		String curricularSemesterString = (String) dynaForm.get("curricularSemester");
		String curricularYearString = (String) dynaForm.get("curricularYear");
		
		
		if(theoreticalHoursString.compareTo("") != 0) {
			Double theoreticalHours = new Double(theoreticalHoursString); 
			newInfoCurricularCourseScope.setTheoreticalHours(theoreticalHours);
		}
				
		if(praticalHoursString.compareTo("") != 0) {
			Double praticalHours = new Double(praticalHoursString); 
			newInfoCurricularCourseScope.setPraticalHours(praticalHours);
		}
		
		if(theoPratHoursString.compareTo("") != 0) {
			Double theoPratHours = new Double(theoPratHoursString); 
			newInfoCurricularCourseScope.setTheoPratHours(theoPratHours);
		}
		
		if(labHoursString.compareTo("") != 0) {
			Double labHours = new Double(labHoursString); 
			newInfoCurricularCourseScope.setLabHours(labHours);
		}
		
		if(maxIncrementNacString.compareTo("") != 0) {
			Integer maxIncrementNac = new Integer(maxIncrementNacString);
			newInfoCurricularCourseScope.setMaxIncrementNac(maxIncrementNac);
		
		}
		
		if(minIncrementNacString.compareTo("") != 0) {
			Integer minIncrementNac = new Integer(minIncrementNacString);
			newInfoCurricularCourseScope.setMinIncrementNac(minIncrementNac);
		
		}

		if(weightString.compareTo("") != 0) {
			Integer weight = new Integer(weightString);
			newInfoCurricularCourseScope.setWeigth(weight);
		}
		
		if(creditsString.compareTo("") != 0) {
			Double credits = new Double(creditsString); 
			newInfoCurricularCourseScope.setCredits(credits);
		}
		
		//ponho so o que eh unico para poder ler no servico
		//pq embora nenhum das propriedades do branch possa tar a null a nivel do java nao tem problema
		
		if(branchCode.compareTo("") != 0) {
					InfoBranch infoBranch = new InfoBranch();
					infoBranch.setCode(branchCode);
					newInfoCurricularCourseScope.setInfoBranch(infoBranch);
				}

//se nao tiver os years nao faz sentido ter os semesters
				
		if(curricularYearString.compareTo("") != 0) {
			InfoCurricularYear infoCurricularYear = new InfoCurricularYear(new Integer(curricularYearString));

			if(curricularSemesterString.compareTo("") != 0) {
				InfoCurricularSemester infoCurricularSemester = new InfoCurricularSemester(); 
				infoCurricularSemester.setSemester(new Integer(curricularSemesterString));
				infoCurricularSemester.setInfoCurricularYear(infoCurricularYear);
				newInfoCurricularCourseScope.setInfoCurricularSemester(infoCurricularSemester);
			}
	    }
		
			
		Object args[] = { oldCurricularCourseScopeId,  newInfoCurricularCourseScope, curricularCourseId, degreeCPId };
		GestorServicos manager = GestorServicos.manager();
		List serviceResult = null;
		try {
				serviceResult = (List) manager.executar(userView, "EditCurricularCourseScope", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		
		if(serviceResult != null) {
			ActionErrors actionErrors = new ActionErrors();
			ActionError error = null;
			if(serviceResult.get(0) != null) {
				error = new ActionError("message.existingCurricularCourseScope", serviceResult.get(0),  serviceResult.get(1));
				actionErrors.add("message.existingCurricularCourseScope", error);
			}			
			saveErrors(request, actionErrors);
		}
		request.setAttribute("degreeId", degreeId);
		//nao sei s se pode apagar
		request.setAttribute("degreeCPId", degreeCPId);
		return mapping.findForward("readCurricularCourse");
	}			
}
