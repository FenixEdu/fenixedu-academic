package ServidorApresentacao.Action.commons;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.ExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorApresentacao.Action.exceptions.ExistingActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * 
 * @author Fernanda Quitério
 * 03/07/2003
 * 
 */
public class ChooseCurricularCourseDispatchAction extends DispatchAction {

	public ActionForward prepareChooseCurricularCourse(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession();

		String executionYear = getFromRequest("executionYear", request);
		String degree = getFromRequest("degree", request);

		request.setAttribute("jspTitle", getFromRequest("jspTitle", request));
		request.setAttribute("executionYear", executionYear);
		request.setAttribute("degree", degree);

		// Get the Curricular Course List			
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
			return mapping.getInputForward();

		} catch (ExistingServiceException e) {
			throw new ExistingActionException(e);
		}
		request.setAttribute("curricularCourses", curricularCourseList);

		return mapping.findForward("PrepareSuccess");
	}

	public ActionForward chooseCurricularCourse(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession();


		request.setAttribute("scopeCode", getFromRequest("scopeCode", request));

		//		parameters necessary to write in jsp
		request.setAttribute("curricularCourse", getFromRequest("curricularCourse", request));
		request.setAttribute("executionYear", getFromRequest("executionYear", request));
		request.setAttribute("degree", getFromRequest("degree", request));

		
		Integer courseScope = new Integer(getFromRequest("scopeCode", request));
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		GestorServicos serviceManager = GestorServicos.manager();


		List studentList = null;
		try {
			Object args[] = { userView, courseScope };
			studentList = (List) serviceManager.executar(userView, "ReadStudentListByCurricularCourseScope", args);
		} catch (NotAuthorizedException e){
			return mapping.findForward("NotAuthorized");
		} catch (NonExistingServiceException e) {
			ActionErrors errors = new ActionErrors();
			errors.add("nonExisting", new ActionError("error.exception.noStudents"));
			saveErrors(request, errors);
			return mapping.findForward("NoStudents");
		}

		request.setAttribute("enrolment_list", studentList);

		return mapping.findForward("ChooseSuccess");
	}

	private String getFromRequest(String parameter, HttpServletRequest request) {
		String parameterString = request.getParameter(parameter);
		if (parameterString == null) {
			parameterString = (String) request.getAttribute(parameter);
		}
		return parameterString;
	}
}