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

import Dominio.IDisciplinaExecucao;
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

			// Create the Degree Type List
			String executionYear = (String) session.getAttribute(SessionConstants.EXECUTION_YEAR);

			// Get the Degree List			
			Object args[] = { executionYear };
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			GestorServicos serviceManager = GestorServicos.manager();
			ArrayList degreeList = null;
			try {
				degreeList = (ArrayList) serviceManager.executar(userView, "ReadMasterDegrees", args);
			} catch (ExistingServiceException e) {
				throw new ExistingActionException(e);
			}
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

		if (session != null) {

			String executionYear = (String) session.getAttribute(SessionConstants.EXECUTION_YEAR);

			DynaActionForm chooseMasterDegreeForm = (DynaActionForm) form;
			String degree = (String) chooseMasterDegreeForm.get("degree");

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
				errors.add("nonExisting" ,new ActionError("message.public.notfound.curricularCourses"));
				saveErrors(request, errors);
				return mapping.findForward("ChooseCurricularCourse");

			} catch (ExistingServiceException e) {
				throw new ExistingActionException(e);
			}
			request.setAttribute("curricularCourses", curricularCourseList);

			return mapping.findForward("ChooseCurricularCourse");
		} else
			throw new Exception();
	}

	public ActionForward showMarksManagementMenu(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);

		if (session != null) {

			String executionYear = (String) session.getAttribute(SessionConstants.EXECUTION_YEAR);

			DynaActionForm chooseCurricularCourseForm = (DynaActionForm) form;
			Integer curricularCourseCode = (Integer) chooseCurricularCourseForm.get("curricularCourseCode");

			//			Get execution course 
			Object args[] = { executionYear, curricularCourseCode};
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			GestorServicos serviceManager = GestorServicos.manager();
			IDisciplinaExecucao executionCourse = null;
			try {
				executionCourse = (IDisciplinaExecucao) serviceManager.executar(userView, "ReadExecutionCourseByCurricularCourseAndYear", args);
			} catch (ExistingServiceException e) {
				throw new ExistingActionException(e);
			}
			
			request.setAttribute("curricularCourse", executionCourse.getNome());
			request.setAttribute("degree", request.getParameter("degree"));
			request.setAttribute("objectCode", executionCourse.getIdInternal());

			return mapping.findForward("ShowMarksManagementMenu");
		} else
			throw new Exception();
	}

	public ActionForward getStudentsAndMarksByCurricularCourse(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);

		if (session != null) {

			String executionYear = (String) session.getAttribute(SessionConstants.EXECUTION_YEAR);

			DynaActionForm chooseExecutionCourseForm = (DynaActionForm) form;
			String executionCourse = (String) chooseExecutionCourseForm.get("executionCourse");

			// Get 			
			Object args[] = { executionCourse };
			IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
			GestorServicos serviceManager = GestorServicos.manager();
			ArrayList executionCourseList = null;
			try {
				executionCourseList = (ArrayList) serviceManager.executar(userView, "ReadExecutionCoursesByMasterDegree", args);
			} catch (ExistingServiceException e) {
				throw new ExistingActionException(e);
			}
			request.setAttribute("useCase", request.getAttribute("useCase"));
			request.setAttribute(SessionConstants.EXECUTION_COURSE_LIST_KEY, executionCourseList);

			return mapping.findForward("ChooseExecutionCourse");
		} else
			throw new Exception();
	}
}
