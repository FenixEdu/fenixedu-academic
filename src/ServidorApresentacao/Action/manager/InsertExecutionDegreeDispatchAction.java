/*
 * Created on 14/Ago/2003
 */
package ServidorApresentacao.Action.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoTeacher;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author lmac1
 */

public class InsertExecutionDegreeDispatchAction extends FenixDispatchAction {


	public ActionForward prepareInsert(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws FenixActionException {

				HttpSession session = request.getSession(false);
				UserView userView =	(UserView) session.getAttribute(SessionConstants.U_VIEW);
				
				Integer degreeId = new Integer(request.getParameter("degreeId"));
				Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));
				
				GestorServicos manager = GestorServicos.manager();
				String label, value;
				List result = null;
				
		/*   Needed service and creation of bean of InfoTeachers for use in jsp   */    
				try {
						result = (List) manager.executar(userView, "ReadAllTeachers", null);
				} catch (FenixServiceException e) {
					throw new FenixActionException(e);
				}
							
				ArrayList infoTeachersList = new ArrayList();
				if(result != null) {
					InfoTeacher infoTeacher;
					Iterator iter = result.iterator();
					while(iter.hasNext()) {
						infoTeacher = (InfoTeacher) iter.next();
						value = infoTeacher.getIdInternal().toString();
						label = infoTeacher.getTeacherNumber() + " - " + infoTeacher.getInfoPerson().getNome();
						infoTeachersList.add(new LabelValueBean(label, value));
					}
					request.setAttribute("infoTeachersList", infoTeachersList);
				}
				
		/*   Needed service and creation of bean of InfoExecutionYears for use in jsp   */
				try {
						result = (List) manager.executar(userView, "ReadAllExecutionYears", null);
				} catch (FenixServiceException e) {
					throw new FenixActionException(e);
				}
							
				ArrayList infoExecutionYearsList = new ArrayList();
				if(result != null) {
					InfoExecutionYear infoExecutionYear;
					Iterator iter = result.iterator();
					while(iter.hasNext()) {
						infoExecutionYear = (InfoExecutionYear) iter.next();
						value = infoExecutionYear.getYear();
						label = infoExecutionYear.getYear();
						infoExecutionYearsList.add(new LabelValueBean(label, value));
					}
					request.setAttribute("infoExecutionYearsList", infoExecutionYearsList);
				}
				
				return mapping.findForward("insertExecutionDegree");
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
		
		InfoExecutionYear infoExecutionYear = new InfoExecutionYear((String) dynaForm.get("executionYear"));
		InfoTeacher infoTeacher = new InfoTeacher();
		infoTeacher.setIdInternal(new Integer((String) dynaForm.get("coordinatorId")));
		InfoDegreeCurricularPlan infoDegreeCurricularPlan = new InfoDegreeCurricularPlan();
		infoDegreeCurricularPlan.setIdInternal(degreeCurricularPlanId);
		
		InfoExecutionDegree infoExecutionDegree = new InfoExecutionDegree();
		infoExecutionDegree.setInfoExecutionYear(infoExecutionYear);
		infoExecutionDegree.setInfoCoordinator(infoTeacher);
		infoExecutionDegree.setInfoDegreeCurricularPlan(infoDegreeCurricularPlan);
		infoExecutionDegree.setTemporaryExamMap(new Boolean((String) dynaForm.get("tempExamMap")));
		
		Object args[] = { infoExecutionDegree };
		
		GestorServicos manager = GestorServicos.manager();
		String serviceResult = null;
		
		try {
				 manager.executar(userView, "InsertExecutionDegreeAtDegreeCurricularPlan", args);
				 
		} catch (ExistingServiceException ex) {
			throw new ExistingActionException(ex.getMessage(), ex);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		
		return mapping.findForward("readDegreeCurricularPlan");
	}			
}