/*
 * Created on 22/Ago/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.ArrayList;
import java.util.Iterator;
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
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoBranch;
import DataBeans.InfoCurricularCourse;
import DataBeans.InfoCurricularCourseScope;
import DataBeans.InfoCurricularSemester;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author lmac1
 */

public class InsertCurricularCourseScopeDispatchAction extends FenixDispatchAction {


	public ActionForward prepareInsert(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

			HttpSession session = request.getSession(false);
			UserView userView =	(UserView) session.getAttribute(SessionConstants.U_VIEW);
			
			Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));
		
			
			Object[] args = { degreeCurricularPlanId };
			
			GestorServicos manager = GestorServicos.manager();
			List result = null;
			try {
					result = (List) manager.executar(userView, "ReadBranchesByDegreeCurricularPlan", args);
			
			} catch (NonExistingServiceException ex) {
				ActionErrors errors = new ActionErrors();
				ActionError actionError = new ActionError("message.insert.degreeCurricularCourseScope.error");
				errors.add("message.insert.degreeCurricularCourseScope.error", actionError);
				saveErrors(request, errors);
				return mapping.findForward("readCurricularCourse");
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}
			
//			creation of bean of InfoBranches for use in jsp
			ArrayList branchesList = new ArrayList();
			
			InfoBranch infoBranch;
			Iterator iter = result.iterator();
			String label, value;
			while(iter.hasNext()) {
				infoBranch = (InfoBranch) iter.next();
				value = infoBranch.getIdInternal().toString();
				label = infoBranch.getCode() + " - " + infoBranch.getName();
				branchesList.add(new LabelValueBean(label, value));
			}
			
			request.setAttribute("branchesList", branchesList);
			
			return mapping.findForward("insertCurricularCourseScope");
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
			
			InfoCurricularCourseScope infoCurricularCourseScope = new InfoCurricularCourseScope();
			
			InfoBranch infoBranch = new InfoBranch();
			infoBranch.setIdInternal(new Integer((String) dynaForm.get("branchId")));
			infoCurricularCourseScope.setInfoBranch(infoBranch);
			
			String credits = (String) dynaForm.get("credits");
			if(credits.compareTo("") != 0)
				infoCurricularCourseScope.setCredits(new Double(credits));
				
			InfoCurricularCourse infoCurricularCourse = new InfoCurricularCourse();
			infoCurricularCourse.setIdInternal(new Integer(request.getParameter("curricularCourseId")));
			infoCurricularCourseScope.setInfoCurricularCourse(infoCurricularCourse);
			
			InfoCurricularSemester infoCurricularSemester = new InfoCurricularSemester();
			infoCurricularSemester.setIdInternal(new Integer((String) dynaForm.get("curricularSemesterId")));
			infoCurricularCourseScope.setInfoCurricularSemester(infoCurricularSemester);
			
			String labHours = (String) dynaForm.get("labHours");
			if(labHours.compareTo("") != 0)
				infoCurricularCourseScope.setLabHours(new Double(labHours));
				
			infoCurricularCourseScope.setMaxIncrementNac(new Integer((String) dynaForm.get("maxIncrementNac")));
			infoCurricularCourseScope.setMinIncrementNac(new Integer((String) dynaForm.get("minIncrementNac")));
			
			String praticalHours = (String) dynaForm.get("praticalHours");
			if(praticalHours.compareTo("") != 0)
				infoCurricularCourseScope.setPraticalHours(new Double(praticalHours));
				
			String theoPratHours = (String) dynaForm.get("theoPratHours");
			if(theoPratHours.compareTo("") != 0)
				infoCurricularCourseScope.setTheoPratHours(new Double(theoPratHours));
				
			String theoreticalHours = (String) dynaForm.get("theoreticalHours");
			if(theoreticalHours.compareTo("") != 0)
				infoCurricularCourseScope.setTheoreticalHours(new Double(theoreticalHours));
				
			infoCurricularCourseScope.setWeigth(new Integer((String) dynaForm.get("weight")));
		
			Object args[] = { infoCurricularCourseScope };
							
			GestorServicos manager = GestorServicos.manager();
		
			try {
				 	manager.executar(userView, "InsertCurricularCourseScopeAtCurricularCourse", args);
				 	
			} catch (ExistingServiceException ex) {
				throw new ExistingActionException(ex.getMessage(), ex);
			} catch (NonExistingServiceException exception) {
				throw new NonExistingActionException("message.nonExistingDegreeCurricularPlan", mapping.findForward("readDegreeCurricularPlan"));
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}
			
			return mapping.findForward("readCurricularCourse");
		}			
	}