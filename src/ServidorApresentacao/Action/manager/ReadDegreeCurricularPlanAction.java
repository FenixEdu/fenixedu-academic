/*
 * Created on 1/Ago/2003
 */
package ServidorApresentacao.Action.manager;

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

import DataBeans.InfoDegreeCurricularPlan;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author lmac1
 */

public class ReadDegreeCurricularPlanAction extends FenixAction  {
	
	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws FenixActionException {
				
				HttpSession session = request.getSession(false);
				DynaActionForm readDegreeForm = (DynaActionForm) form;
						
				UserView userView =
					(UserView) session.getAttribute(SessionConstants.U_VIEW);
					
				Integer degreeCurricularPlanId = (Integer) readDegreeForm.get("degreeCurricularPlanId");
//				Integer degreeId = (Integer) readDegreeForm.get("degreeId");				
					
				Object args[] = { degreeCurricularPlanId };
				
				GestorServicos manager = GestorServicos.manager();
				InfoDegreeCurricularPlan infoDegreeCurricularPlan = null;
				
				try {
						infoDegreeCurricularPlan = (InfoDegreeCurricularPlan) manager.executar(userView, "ReadDegreeCurricularPlanService", args);
				} catch(FenixServiceException e) {
					throw new FenixActionException(e);
				}
			    
				// trying to read a degreeCurricularPlan that doesn´t exist in the database
				if(infoDegreeCurricularPlan == null) {
						ActionErrors actionErrors = new ActionErrors();
						ActionError error = new ActionError("message.nonExistingDegreeCurricularPlan");
						actionErrors.add("message.nonExistingDegreeCurricularPlan", error);
						saveErrors(request, actionErrors);
						return mapping.findForward("readDegree");
				}
				
				// in case the degreeCurricularPlan really exists
				List executionDegrees = null;
				try {		
						executionDegrees = (List) manager.executar(
													userView,
													"ReadExecutionDegreesService",
													args);	
				} catch (FenixServiceException e) {
						throw new FenixActionException(e);
				}
				if(executionDegrees!=null)		
					Collections.sort(executionDegrees);
								
				List curricularCourses = null;
				try {		
						curricularCourses = (List) manager.executar(
													userView,
													"ReadCurricularCoursesService",
													args);	
				} catch (FenixServiceException e) {
						throw new FenixActionException(e);
				}		
//				Collections.sort(curricularCourses);
			
				request.setAttribute("curricularCoursesList", curricularCourses);
				request.setAttribute("executionDegreesList", executionDegrees);
//				request.setAttribute("degreeId", degreeId);
				request.setAttribute("degreeCurricularPlanId", degreeCurricularPlanId);
				request.setAttribute("infoDegreeCurricularPlan", infoDegreeCurricularPlan);					
				return mapping.findForward("viewDegreeCurricularPlan");
	}
}
