/*
 * Created on 9/Set/2003
 */
package ServidorApresentacao.Action.manager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.base.FenixAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.exceptions.NonExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author lmac1
 */

public class AssociateExecutionCoursesToCurricularCourseAction extends FenixAction {
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws FenixActionException {

			HttpSession session = request.getSession(false);
			DynaActionForm associateForm = (DynaActionForm) form;
			
			Integer curricularCourseId = new Integer(request.getParameter("curricularCourseId"));
			Integer executionPeriodId = new Integer(request.getParameter("executionPeriodId"));
			
			UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
			Integer executionCourseId = new Integer((String) associateForm.get("executionCourseId"));

			Object args[] = { executionCourseId, curricularCourseId, executionPeriodId };
			GestorServicos manager = GestorServicos.manager();

			try {
					manager.executar(userView, "AssociateExecutionCoursesToCurricularCourse", args);
			} catch (NonExistingServiceException ex) {
				throw new NonExistingActionException(ex.getMessage());
			} catch (FenixServiceException fenixServiceException) {
				throw new FenixActionException(fenixServiceException.getMessage());
			}
			return mapping.findForward("readCurricularCourse");
		}
	}