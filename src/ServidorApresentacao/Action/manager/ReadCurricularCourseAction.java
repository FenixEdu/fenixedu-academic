/*
 * Created on 16/Ago/2003
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

import DataBeans.InfoCurricularCourse;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author lmac1
 */
public class ReadCurricularCourseAction extends FenixAction  {
	
	public ActionForward execute(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws FenixActionException {
				
				HttpSession session = request.getSession(false);
//				DynaActionForm dynaForm = (DynaActionForm) form;
						
				UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
				
				Integer degreeCurricularPlanId = new Integer(request.getParameter("degreeCurricularPlanId"));
				Integer	curricularCourseId = new Integer(request.getParameter("curricularCourseId"));	
				Integer	degreeId = new Integer(request.getParameter("degreeId"));

				Object args[] = { curricularCourseId };
				
				GestorServicos manager = GestorServicos.manager();
				InfoCurricularCourse infoCurricularCourse = null;
				
				
				
				
				try {
					infoCurricularCourse = (InfoCurricularCourse) manager.executar(userView, "ReadCurricularCourse", args);
				} catch(FenixServiceException e) {
					throw new FenixActionException(e);
				}
			    
				// trying to read a CurricularCourse that doesn´t exist in the database
				if(infoCurricularCourse == null) {
						ActionErrors actionErrors = new ActionErrors();
						ActionError error = new ActionError("message.nonExistingCurricularCourse");
						actionErrors.add("message.nonExistingCurricularCourse", error);
						saveErrors(request, actionErrors);
					
						return mapping.findForward("readDegreeCurricularPlan");
				}
				
				
				
				// in case the CurricularCourse really exists
				List executionCourses = null;
				try {		
					executionCourses = (List) manager.executar(
													userView,
													"ReadExecutionCoursesByCurricularCourse",
													args);	
				} catch (FenixServiceException e) {
						throw new FenixActionException(e);
				}		
//				Collections.sort(executionCourses, new BeanComparator("infoExecutionYear.year"));
								
				List studentCurricularPlans = null;
				try {		
					studentCurricularPlans = (List) manager.executar(
													userView,
													"ReadStudentCurricularPlansByCurricularCourse",
													args);	
				} catch (FenixServiceException e) {
						throw new FenixActionException(e);
				}		
//				Collections.sort(studentCurricularPlans, new BeanComparator("name"));
				request.setAttribute("executionCoursesList", executionCourses);
				request.setAttribute("studentCurricularPlansList", studentCurricularPlans);
				request.setAttribute("degreeId", degreeId);
				request.setAttribute("curricularCourseId", curricularCourseId);
				request.setAttribute("degreeCurricularPlanId", degreeCurricularPlanId);
				request.setAttribute("infoCurricularCourse", infoCurricularCourse);					
				return mapping.findForward("viewCurricularCourse");
	}
}

