package ServidorApresentacao.Action.masterDegree.administrativeOffice.marksManagement;

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
import ServidorAplicacao.Servico.exceptions.NonExistingServiceException;
import ServidorAplicacao.Servico.exceptions.NotAuthorizedException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * 
 * @author Fernanda Quitério
 * 30/06/2003
 * 
 */

public class ShowMarkDispatchAction extends DispatchAction {

	public ActionForward prepareShowMark(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {

		HttpSession session = request.getSession(false);
		
		String executionYear = getFromRequest("executionYear", request);
		String degree = getFromRequest("degree", request);
		String curricularCourse = getFromRequest("curricularCourse", request);
		Integer curricularCourseCode = new Integer(getFromRequest("curricularCourseCode", request));

		
		
		//put request
		request.setAttribute("executionYear",executionYear);
		request.setAttribute("curricularCourseCode",curricularCourseCode);
		request.setAttribute("courseID",curricularCourseCode);
		request.setAttribute("curricularCourse", curricularCourse);	
		request.setAttribute("degree", degree);
		request.setAttribute("jspTitle", getFromRequest("jspTitle", request));

		// Get students List			
		
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		Object args[] = {userView,curricularCourseCode,executionYear};
		GestorServicos serviceManager = GestorServicos.manager();
		List listEnrolmentEvaluation = null;
		
		try {
		 listEnrolmentEvaluation =
				(List) serviceManager.executar(userView, "ReadStudentMarksListByCurricularCourse", args);
		} catch (NotAuthorizedException e){
			return mapping.findForward("NotAuthorized");
		} catch (NonExistingServiceException e) {
			ActionErrors errors = new ActionErrors();
			errors.add("nonExisting", new ActionError("error.exception.noStudents"));
			saveErrors(request, errors);
			return mapping.findForward("NoStudents");
		}
		
		if (listEnrolmentEvaluation.size() == 0){
			ActionErrors actionErrors = new ActionErrors();
			actionErrors.add(
					"StudentNotEnroled",
					new ActionError(
						"error.students.Mark.NotAvailable"));
			saveErrors(request, actionErrors);
			return mapping.findForward("NoStudents");
		}
		request.setAttribute("showMarks", "showMarks");
		request.setAttribute("studentList", listEnrolmentEvaluation);
		return mapping.findForward("displayStudentList");
	
		
	}
	

	private void sendErrors(HttpServletRequest request, String arg0, String arg1) {
		ActionErrors errors = new ActionErrors();
		errors.add(arg0, new ActionError(arg1));
		saveErrors(request, errors);
	}

	private String getFromRequest(String parameter, HttpServletRequest request) {
		String parameterString = request.getParameter(parameter);
		if (parameterString == null) {
			parameterString = (String) request.getAttribute(parameter);
		}
		return parameterString;
	}

}
