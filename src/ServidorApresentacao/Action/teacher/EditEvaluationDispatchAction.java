/*
 * Created on 24/Abr/2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ServidorApresentacao.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import DataBeans.InfoEvaluation;
import DataBeans.InfoExecutionCourse;
import DataBeans.InfoSite;
import ServidorAplicacao.FenixServiceException;
import ServidorAplicacao.IUserView;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author João Mota
 *
 * 
 */
public class EditEvaluationDispatchAction extends FenixDispatchAction {

	public ActionForward viewEvaluation(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		
		HttpSession session = request.getSession(false);	
		//	get executionCourse from session
		InfoSite infoSite = (InfoSite) session.getAttribute(SessionConstants.INFO_SITE);
		
		InfoExecutionCourse infoExecutionCourse = infoSite.getInfoExecutionCourse();

		//execute action main service
		Object[] args = { infoExecutionCourse };
		InfoEvaluation infoEvaluation = null;
		try {
			infoEvaluation =
				(InfoEvaluation) ServiceUtils.executeService(
					null,
					"ReadEvaluation",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		//place in request/session everything needed	
		if (infoEvaluation != null) {
			session.setAttribute(SessionConstants.INFO_EVALUATION,infoEvaluation);
			return mapping.findForward("viewEvaluation");
		}else {
		
		return mapping.findForward("editEvaluation");}
	}

	public ActionForward prepareEditEvaluation(
			ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws FenixActionException {
				
				return mapping.findForward("editEvaluation");
				 }

	
	public ActionForward editEvaluation(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
			HttpSession session = request.getSession(false);	
			
			//get old evaluation from session
			InfoEvaluation oldEvaluation = (InfoEvaluation) session.getAttribute(SessionConstants.INFO_EVALUATION);
			
			//get new infoEvaluation data from the form
			DynaActionForm evaluationForm = (DynaActionForm) form;
			InfoExecutionCourse executionCourse = null;
			String evaluationElements = (String) evaluationForm.get("evaluationElements");
			String evaluationElementsEn = (String) evaluationForm.get("evaluationElementsEn");
			if (oldEvaluation==null) {
			 executionCourse = ((InfoSite) session.getAttribute(SessionConstants.INFO_SITE)).getInfoExecutionCourse();
			
			}
			else {
				executionCourse = oldEvaluation.getInfoExecutionCourse();
			}
			InfoEvaluation newEvaluation = new InfoEvaluation(executionCourse,evaluationElements,evaluationElementsEn);
			
			//get userView
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			
			//execute action main service
			Object[] args = { oldEvaluation,newEvaluation };
					
					try {
						Boolean bool =
							(Boolean) ServiceUtils.executeService(
								userView,
								"EditEvaluation",
								args);
					} catch (FenixServiceException e) {
						throw new FenixActionException(e);
					}
			//place in request/session everything needed	
			session.setAttribute(SessionConstants.INFO_EVALUATION,newEvaluation);
			
			//find forward

		return mapping.findForward("viewEvaluation");
	}

}
