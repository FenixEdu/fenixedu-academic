/*
 * Created on 18/Ago/2003
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

import DataBeans.InfoExecutionDegree;
import DataBeans.InfoExecutionYear;
import DataBeans.InfoTeacher;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author lmac1
 */

public class EditExecutionDegreeDispatchAction extends FenixDispatchAction {

	public ActionForward prepareEdit(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws FenixActionException {
				
				HttpSession session = request.getSession(false);
				UserView userView =	(UserView) session.getAttribute(SessionConstants.U_VIEW);
				
				Integer degreeId = new Integer(request.getParameter("degreeId"));
				Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));
				Integer executionDegreeId = new Integer(request.getParameter("executionDegreeId"));
				
				InfoExecutionDegree oldInfoExecutionDegree;
				Object args[] = { executionDegreeId };
				GestorServicos manager = GestorServicos.manager();
			
				try {
						oldInfoExecutionDegree = (InfoExecutionDegree) manager.executar(userView, "ReadExecutionDegree", args);
				} catch (FenixServiceException fenixServiceException) {
					throw new FenixActionException(fenixServiceException.getMessage());
				}
				
				if(oldInfoExecutionDegree == null) {
					ActionErrors actionErrors = new ActionErrors();
					ActionError error = new ActionError("message.nonExistingExecutionDegree");
					actionErrors.add("message.nonExistingExecutionDegree", error);			
					saveErrors(request, actionErrors);
					return mapping.findForward("readDegreeCurricularPlan");
				}		
				
				String label, value;
				List result;
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
					infoTeachersList.add(0, new LabelValueBean("(escolher)", ""));
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
					infoExecutionYearsList.add(0, new LabelValueBean("(escolher)", ""));
					request.setAttribute("infoExecutionYearsList", infoExecutionYearsList);
				}
				
				request.setAttribute("degreeId", degreeId);
				request.setAttribute("degreeCurricularPlanId", degreeCurricularPlanId);
				request.setAttribute("executionDegreeId", executionDegreeId.toString());
				request.setAttribute("infoExecutionDegree", oldInfoExecutionDegree);
				return mapping.findForward("editExecutionDegree");
	}
	
	public ActionForward edit(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws FenixActionException {

			HttpSession session = request.getSession(false);
			UserView userView =	(UserView) session.getAttribute(SessionConstants.U_VIEW);
		
			Integer degreeId = new Integer(request.getParameter("degreeId"));
			Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));
			Integer executionDegreeId = new Integer(request.getParameter("executionDegreeId"));
    	
			DynaActionForm dynaForm = (DynaValidatorForm) form;
		
			Object args[] = { (String) dynaForm.get("executionYear"), (String) dynaForm.get("coordinatorId"),
								(String) dynaForm.get("tempExamMap"), degreeCurricularPlanId,
								executionDegreeId };
		
			GestorServicos manager = GestorServicos.manager();
			List serviceResult = null;
		
			try {
					serviceResult = (List) manager.executar(userView, "EditExecutionDegree", args);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}
		
			if(serviceResult != null) {
				ActionErrors actionErrors = new ActionErrors();
				if(serviceResult.get(0) == "alreadyExisting") {
					ActionError error = new ActionError("message.existingExecutionDegree", serviceResult.get(1));
					actionErrors.add("message.existingExecutionDegree", error);			
					saveErrors(request, actionErrors);
				}
				else {
					ActionError error = new ActionError("message.nonExistingExecutionDegree");
					actionErrors.add("message.nonExistingExecutionDegree", error);			
					saveErrors(request, actionErrors);
				}
			}
		
			return mapping.findForward("readDegreeCurricularPlan");
		}			
	}