package ServidorApresentacao.Action.masterDegree.administrativeOffice.marksManagement;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import Dominio.ICurricularCourse;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * 
 * @author Fernanda Quitério
 * 30/06/2003
 * 
 */

public class MarksManagementDispatchAction extends DispatchAction {

	public ActionForward chooseMasterDegree(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);

		if (session != null) {

			String executionYear = (String) session.getAttribute(SessionConstants.EXECUTION_YEAR);

			// Get the Degree List			
			Object args[] = { executionYear };
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			GestorServicos serviceManager = GestorServicos.manager();
			ArrayList degreeList = null;
			try {
				degreeList = (ArrayList) serviceManager.executar(userView, "ReadMasterDegrees", args);
			} catch (NonExistingServiceException e) {
				ActionErrors errors = new ActionErrors();
				errors.add("nonExisting", new ActionError("message.public.notfound.degrees", executionYear));
				saveErrors(request, errors);
				return mapping.getInputForward();

		}catch (ExistingServiceException e) {
				throw new ExistingActionException(e);
			}
			request.setAttribute("executionYear", executionYear);
			request.setAttribute(SessionConstants.DEGREE_LIST, degreeList);

			return mapping.findForward("ChooseMasterDegree");
		} else
			throw new Exception();

	}

	public ActionForward chooseCurricularCourse(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
		MessageResources messages = getResources(request);

		String executionYear = (String) request.getParameter("executionYear");

		DynaActionForm chooseMasterDegreeForm = (DynaActionForm) form;
		String degree = (String) chooseMasterDegreeForm.get("degree");

		request.setAttribute("executionYear", executionYear);
		request.setAttribute("degree", degree);

		// Get the Execution Course List			
		Object args[] = { executionYear, degree };
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		GestorServicos serviceManager = GestorServicos.manager();
		ArrayList curricularCourseList = null;
		try {
			curricularCourseList = (ArrayList) serviceManager.executar(userView, "ReadCurricularCoursesByDegree", args);
		} catch (NonExistingServiceException e) {
			ActionErrors errors = new ActionErrors();
			errors.add("nonExisting", new ActionError("message.public.notfound.curricularCourses"));
			saveErrors(request, errors);
			return mapping.findForward("ChooseCurricularCourse");

		} catch (ExistingServiceException e) {
			throw new ExistingActionException(e);
		}
		request.setAttribute("curricularCourses", curricularCourseList);

		return mapping.findForward("ChooseCurricularCourse");
	}

	public ActionForward showMarksManagementMenu(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);

		String executionYear = (String) request.getParameter("executionYear");

		DynaActionForm chooseCurricularCourseForm = (DynaActionForm) form;
		Integer curricularCourseCode = (Integer) chooseCurricularCourseForm.get("curricularCourseCode");

		//			Get curricular course 
		Object args[] = { curricularCourseCode };
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		GestorServicos serviceManager = GestorServicos.manager();
		ICurricularCourse curricularCourse = null;
		try {
			curricularCourse = (ICurricularCourse) serviceManager.executar(userView, "ReadCurricularCourseByIdInternal", args);
		} catch (ExistingServiceException e) {
			throw new ExistingActionException(e);
		}

		request.setAttribute("executionYear", executionYear);
		request.setAttribute("degree", request.getParameter("degree"));
		request.setAttribute("curricularCourse", curricularCourse.getName());
		request.setAttribute("curricularCourseCode", curricularCourseCode);

		return mapping.findForward("ShowMarksManagementMenu");
	}
}