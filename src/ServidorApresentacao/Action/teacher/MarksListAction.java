package ServidorApresentacao.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import DataBeans.TeacherAdministrationSiteView;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Tânia Pousão
 *
 */
public class MarksListAction extends DispatchAction {
	
	public ActionForward loadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		return null;
	}

	/** 
	 * @author Tânia Pousão 
	 *
	 */
	public ActionForward loadMarksOnline(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		HttpSession session = request.getSession();

		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer executionCourseCode = null;
		String executionCourseCodeString = request.getParameter("objectCode");
		if (executionCourseCodeString == null) {
			executionCourseCodeString = (String) request.getAttribute("objectCode");
		}
		executionCourseCode = new Integer(executionCourseCodeString);
	
		Integer examCode = null;
		String examCodeString = request.getParameter("examCode");
		if (examCodeString == null) {
			examCodeString = (String) request.getAttribute("examCode");
		}
		examCode = new Integer(examCodeString);
	
		Object[] args = { executionCourseCode, examCode };

		GestorServicos gestorServicos = GestorServicos.manager();
		TeacherAdministrationSiteView siteView = null;

		try {
			siteView = (TeacherAdministrationSiteView) gestorServicos.executar(userView, "ReadStudentsAndMarksByExam", args);
		} catch (FenixServiceException e) {
			e.printStackTrace();
			throw new FenixActionException(e.getMessage());
		}

		request.setAttribute("siteView", siteView);
		request.setAttribute("objectCode", executionCourseCode);
		request.setAttribute("examCode", examCode);
		
		return mapping.findForward("marksList");
	}

	public ActionForward publishMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		return null;
	}

	public ActionForward submitMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		return null;
	}

}
