/*
 * Created on 11/Set/2003
 */
package ServidorApresentacao.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import DataBeans.InfoExecutionCourse;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author lmac1
 */

public class DissociateExecutionCourseDA extends FenixDispatchAction {
	
	public ActionForward prepareDissociate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

			HttpSession session = request.getSession(false);


			UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
			
			Integer executionDegreeId = new Integer(request.getParameter("executionCourseId"));

			Object args[] = { executionDegreeId };
			InfoExecutionCourse infoExecutionCourse = null;
			GestorServicos manager = GestorServicos.manager();

			try {
					infoExecutionCourse = (InfoExecutionCourse) manager.executar(userView, "ReadExecutionCourse", args);
		
			} catch (NonExistingServiceException e) {
				throw new NonExistingActionException("message.nonExisting.executionCourse", mapping.findForward("readCurricularCourse"));
			}  catch (FenixServiceException fenixServiceException) {
				throw new FenixActionException(fenixServiceException.getMessage());
			}
				
			request.setAttribute("infoExecutionCourse", infoExecutionCourse);
			return mapping.findForward("dissociateExecutionCourse");
		}
		
	public ActionForward dissociate(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws FenixActionException {

		HttpSession session = request.getSession(false);

		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer executionCourseId = new Integer(request.getParameter("executionCourseId"));
		Integer curricularCourseId = new Integer(request.getParameter("curricularCourseId"));
		
		Object args[] = { executionCourseId, curricularCourseId };
		GestorServicos manager = GestorServicos.manager();

		try {
				manager.executar(userView, "DissociateExecutionCourse", args);
			
		} catch (NonExistingServiceException e) {
			throw new NonExistingActionException(e.getMessage(), mapping.findForward("readCurricularCourse"));
		} catch (FenixServiceException fenixServiceException) {
			throw new FenixActionException(fenixServiceException.getMessage());
		}
		return mapping.findForward("readCurricularCourse");
	}

}
