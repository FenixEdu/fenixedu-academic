package ServidorApresentacao.Action.teacher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.DynaValidatorForm;

import DataBeans.ISiteComponent;
import DataBeans.InfoSiteCommon;
import DataBeans.TeacherAdministrationSiteView;
import ServidorAplicacao.GestorServicos;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Tânia Pousão
 *
 */
public class MarksListAction extends DispatchAction {

	public ActionForward loadFile(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			
			HttpSession session = request.getSession();

			UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

			Integer executionCourseCode = getFromRequest("objectCode", request);

			Integer examCode = getFromRequest("examCode", request);
			
			ISiteComponent commonComponent = new InfoSiteCommon();
			Object[] args = { executionCourseCode, commonComponent, null, null, null, null };

			try {
				TeacherAdministrationSiteView siteView =
					(TeacherAdministrationSiteView) ServiceUtils.executeService(userView, "TeacherAdministrationSiteComponentService", args);

				request.setAttribute("siteView", siteView);
				request.setAttribute("objectCode", ((InfoSiteCommon) siteView.getCommonComponent()).getExecutionCourse().getIdInternal());
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}

			request.setAttribute("examCode", examCode);

			return mapping.findForward("loadMarks");
		
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

		Integer executionCourseCode = (Integer) getFromRequest("objectCode", request);

		Integer examCode = (Integer) getFromRequest("examCode", request);

		Object[] args = { executionCourseCode, examCode };

		GestorServicos gestorServicos = GestorServicos.manager();
		TeacherAdministrationSiteView siteView = null;

		try {
			siteView = (TeacherAdministrationSiteView) gestorServicos.executar(userView, "ReadStudentsAndMarksByExam", args);
		} catch (FenixServiceException e) {
			e.printStackTrace();
			throw new FenixActionException(e.getMessage());
		}

		InfoSiteMarks infoSiteMarks = (InfoSiteMarks) siteView.getComponent();
		Collections.sort(infoSiteMarks.getMarksList(), new BeanComparator("infoFrequenta.aluno.number"));

		request.setAttribute("siteView", siteView);
		request.setAttribute("objectCode", executionCourseCode);
		request.setAttribute("examCode", examCode);

		return mapping.findForward("marksList");
	}

	/** 
	 * @author Fernanda Quitério 
	 *
	 */
	public ActionForward preparePublishMarks(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		HttpSession session = request.getSession();

		Integer examCode = getFromRequest("examCode", request);

		Integer infoExecutionCourseCode = getFromRequest("objectCode", request);

		ISiteComponent commonComponent = new InfoSiteCommon();
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		Object[] args = { infoExecutionCourseCode, commonComponent, null, null, null, null };
		TeacherAdministrationSiteView siteView = null;
		try {
			siteView =
				(TeacherAdministrationSiteView) ServiceUtils.executeService(
					userView,
					"TeacherAdministrationSiteComponentService",
					args);

		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		request.setAttribute("siteView", siteView);
		request.setAttribute("objectCode", infoExecutionCourseCode);
		request.setAttribute("examCode", examCode);

		return mapping.findForward("preparePublishMarks");
	}

	private Integer getFromRequest(String parameter, HttpServletRequest request) {
		Integer parameterCode = null;
		String parameterCodeString = request.getParameter(parameter);
		if (parameterCodeString == null) {
			parameterCodeString = (String) request.getAttribute(parameter);
		}
		if (parameterCodeString != null) {
			parameterCode = new Integer(parameterCodeString);
		}
		return parameterCode;

	}

	/** 
	 * @author Fernanda Quitério 
	 *
	 */
	public ActionForward publishMarks(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception {
		HttpSession session = request.getSession();

		Integer examCode = getFromRequest("examCode", request);

		Integer objectCode = getFromRequest("objectCode", request);

		DynaValidatorForm publishForm = (DynaValidatorForm) form;
		String publishmentMessage = (String) publishForm.get("publishmentMessage");
		Boolean sendSMS = (Boolean) publishForm.get("sendSMS");

		String announcementTitle = null;
		if (publishmentMessage != null && publishmentMessage.length() > 0) {
			MessageResources messages = getResources(request);
			announcementTitle = messages.getMessage("message.publishment");
		}
		
		Object[] args = { objectCode, examCode, publishmentMessage, sendSMS, announcementTitle };
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		GestorServicos gestorServicos = GestorServicos.manager();
		try {
			gestorServicos.executar(userView, "PublishMarks", args);
		} catch (FenixServiceException e) {
			e.printStackTrace();
			throw new FenixActionException(e.getMessage());
		}

		request.setAttribute("objectCode", objectCode);

		return mapping.findForward("viewExams");
	}

	public ActionForward submitMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		return null;
	}
}