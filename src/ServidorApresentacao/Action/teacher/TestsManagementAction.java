/*
 * Created on 14/Ago/2003
 *
 */
package ServidorApresentacao.Action.teacher;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.dbunit.util.Base64;

import DataBeans.ISiteComponent;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteTest;
import DataBeans.InfoSiteTestQuestion;
import DataBeans.SiteView;
import DataBeans.TeacherAdministrationSiteView;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;

/**
 * @author Susana Fernandes
 */
public class TestsManagementAction extends FenixDispatchAction {

	public ActionForward testsFirstPage(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = getSession(request);
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);

		String objectCodeString = request.getParameter("objectCode");
		if (objectCodeString == null) {
			objectCodeString = (String) request.getAttribute("objectCode");
		}

		Integer objectCode = new Integer(objectCodeString);
		ISiteComponent commonComponent = new InfoSiteCommon();
		Object[] args =
			{ objectCode, commonComponent, null, objectCode, null, null };

		try {
			TeacherAdministrationSiteView siteView =
				(TeacherAdministrationSiteView) ServiceUtils.executeService(
					userView,
					"TeacherAdministrationSiteComponentService",
					args);
			request.setAttribute("siteView", siteView);
			request.setAttribute(
				"objectCode",
				((InfoSiteCommon) siteView.getCommonComponent())
					.getExecutionCourse()
					.getIdInternal());
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		return mapping.findForward("testsFirstPage");
	}

	public ActionForward prepareCreateTest(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		String executionCourseIdString = request.getParameter("objectCode");
		Integer executionCourseId = new Integer(executionCourseIdString);
		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		SiteView siteView = null;
		try {
			Object[] args = { executionCourseId };
			siteView =
				(SiteView) ServiceUtils.executeService(
					userView,
					"ReadMetadatas",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		request.setAttribute("siteView", siteView);
		return mapping.findForward("createTest");
	}

	public ActionForward createTest(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		String executionCourseIdString = request.getParameter("objectCode");
		Integer executionCourseId = new Integer(executionCourseIdString);
		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		String title = request.getParameter("title");
		String information = request.getParameter("information");
		Object[] args = { executionCourseId, title, information };
		Integer testCode = null;
		try {
			testCode =
				(Integer) ServiceUtils.executeService(
					userView,
					"InsertTest",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		request.setAttribute("testCode", testCode);
		return showAvailableQuestions(mapping, form, request, response);
	}

	public ActionForward showAvailableQuestions(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		String executionCourseIdString = request.getParameter("objectCode");
		Integer executionCourseId = new Integer(executionCourseIdString);
		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		SiteView siteView = null;
		Integer testCode = (Integer) request.getAttribute("testCode");
		if (testCode == null)
			testCode = new Integer(request.getParameter("testCode"));
		try {
			Object[] args = { executionCourseId, testCode };
			siteView =
				(SiteView) ServiceUtils.executeService(
					userView,
					"ReadMetadatasByTest",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		request.setAttribute("siteView", siteView);
		request.setAttribute("testCode", testCode);
		return mapping.findForward("showAvailableQuestions");
	}

	public ActionForward prepareInsertTestQuestion(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		String executionCourseIdString = request.getParameter("objectCode");
		Integer executionCourseId = new Integer(executionCourseIdString);
		String exerciceIdString = request.getParameter("exerciceCode");
		String testCode = request.getParameter("testCode");
		String metadataCode = request.getParameter("metadataCode");
		Integer metadataId = new Integer(metadataCode);
		Integer exerciceId = null;
		if (exerciceIdString == null)
			exerciceId = new Integer(-1);
		else
			exerciceId = new Integer(exerciceIdString);
		SiteView siteView = null;
		try {
			Object[] args = { executionCourseId, metadataId, exerciceId };
			siteView =
				(SiteView) ServiceUtils.executeService(
					userView,
					"ReadQuestion",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		SiteView siteViewAux = null;
		try {
			Object[] args = { executionCourseId, new Integer(testCode)};
			siteViewAux =
				(SiteView) ServiceUtils.executeService(
					userView,
					"ReadTest",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		List testQuestionList = new ArrayList();
		testQuestionList =
			((InfoSiteTest) siteViewAux.getComponent()).getInfoTestQuestions();
		Collections.sort(testQuestionList);
		List testQuestionNames = new ArrayList();
		List testQuestionValues = new ArrayList();

		for (int i = 0; i < testQuestionList.size(); i++) {
			testQuestionNames.add(new String("Pergunta " + (i + 1)));
			testQuestionValues.add(new Integer(i));
		}
		request.setAttribute("testQuestionNames", testQuestionNames);
		request.setAttribute("testQuestionValues", testQuestionValues);
		request.setAttribute("testCode", new Integer(testCode));
		request.setAttribute("exerciceCode", exerciceId);
		request.setAttribute("siteView", siteView);
		return mapping.findForward("insertTestQuestion");
	}

	public ActionForward insertTestQuestion(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		String metadataCode = request.getParameter("metadataCode");
		String questionOrder = request.getParameter("questionOrder");
		String testCode = request.getParameter("testCode");
		String questionValue = request.getParameter("questionValue");

		Object[] arguments =
			{
				new Integer(testCode),
				new Integer(metadataCode),
				new Integer(questionOrder),
				new Integer(questionValue)};
		try {
			ServiceUtils.executeService(
				userView,
				"InsertTestQuestion",
				arguments);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		request.setAttribute("testCode", new Integer(testCode));
		return showAvailableQuestions(mapping, form, request, response);
	}

	public ActionForward prepareEditTestQuestion(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		String executionCourseIdString = request.getParameter("objectCode");
		Integer executionCourseId = new Integer(executionCourseIdString);
		String questionCode = request.getParameter("questionCode");
		String testCode = request.getParameter("testCode");

		SiteView siteView = null;
		try {
			Object[] args =
				{
					executionCourseId,
					new Integer(testCode),
					new Integer(questionCode)};
			siteView =
				(SiteView) ServiceUtils.executeService(
					userView,
					"ReadTestQuestion",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		SiteView siteViewAux = null;
		try {
			Object[] args = { executionCourseId, new Integer(testCode)};
			siteViewAux =
				(SiteView) ServiceUtils.executeService(
					userView,
					"ReadTest",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		List testQuestionList = new ArrayList();
		testQuestionList =
			((InfoSiteTest) siteViewAux.getComponent()).getInfoTestQuestions();
		Collections.sort(testQuestionList);
		List testQuestionNames = new ArrayList();
		List testQuestionValues = new ArrayList();

		int questionOrder =
			((InfoSiteTestQuestion) siteView.getComponent())
				.getInfoTestQuestion()
				.getTestQuestionOrder()
				.intValue();

		for (int i = 0; i < testQuestionList.size(); i++) {
			if ((i + 1) != questionOrder && (i + 1) != questionOrder + 1) {
				testQuestionNames.add(new String("Pergunta " + (i + 1)));
				testQuestionValues.add(new Integer(i));
			}
		}
		request.setAttribute("testQuestionNames", testQuestionNames);
		request.setAttribute("testQuestionValues", testQuestionValues);
		request.setAttribute("testCode", new Integer(testCode));
		request.setAttribute("questionCode", new Integer(questionCode));
		request.setAttribute("objectCode", executionCourseId);
		request.setAttribute("siteView", siteView);
		return mapping.findForward("editTestQuestion");
	}

	public ActionForward editTestQuestion(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		String testCode = request.getParameter("testCode");
		String testQuestionCode = request.getParameter("testQuestionCode");
		String questionOrder = request.getParameter("testQuestionOrder");
		String questionValue = request.getParameter("testQuestionValue");
		Object[] arguments =
			{
				new Integer(testCode),
				new Integer(testQuestionCode),
				new Integer(questionOrder),
				new Integer(questionValue)};
		try {
			ServiceUtils.executeService(
				userView,
				"EditTestQuestion",
				arguments);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		request.setAttribute("testCode", new Integer(testCode));
		return editTest(mapping, form, request, response);
	}

	public ActionForward deleteTestQuestion(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		String testCode = request.getParameter("testCode");
		String questionCode = request.getParameter("questionCode");
		Object[] args = { new Integer(testCode), new Integer(questionCode)};
		try {
			ServiceUtils.executeService(userView, "DeleteTestQuestion", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		return editTest(mapping, form, request, response);
	}

	public ActionForward showTests(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		String executionCourseIdString = request.getParameter("objectCode");
		Integer executionCourseId = new Integer(executionCourseIdString);
		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		SiteView siteView = null;
		try {
			Object[] args = { executionCourseId };
			siteView =
				(SiteView) ServiceUtils.executeService(
					userView,
					"ReadTests",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		request.setAttribute("siteView", siteView);
		return mapping.findForward("showTests");
	}

	public ActionForward deleteTest(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		String testCodeString = request.getParameter("testCode");
		Object[] args = { new Integer(testCodeString)};
		try {
			ServiceUtils.executeService(userView, "DeleteTest", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		return showTests(mapping, form, request, response);
	}

	public ActionForward showImage(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		String exerciceIdString = request.getParameter("exerciceCode");
		String imgCodeString = request.getParameter("imgCode");
		String imgTypeString = request.getParameter("imgType");
		Object[] args =
			{ new Integer(exerciceIdString), new Integer(imgCodeString)};
		String img = null;
		try {
			img =
				(String) ServiceUtils.executeService(
					userView,
					"ReadQuestionImage",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		byte[] imageData = Base64.decode(img);
		try {
			response.reset();
			response.setContentType(imgTypeString);
			response.setContentLength(imageData.length);
			response.setBufferSize(imageData.length);
			OutputStream os = response.getOutputStream();
			os.write(imageData, 0, imageData.length);
			response.flushBuffer();
		} catch (java.io.IOException e) {
			throw new FenixActionException(e);
		}
		return null;
	}

	public ActionForward editTest(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		String testCodeString = request.getParameter("testCode");
		String executionCourseIdString = request.getParameter("objectCode");
		Object[] args =
			{
				new Integer(executionCourseIdString),
				new Integer(testCodeString)};
		SiteView siteView = null;
		try {
			siteView =
				(SiteView) ServiceUtils.executeService(
					userView,
					"ReadTest",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		Collections.sort(
			((InfoSiteTest) siteView.getComponent()).getInfoTestQuestions());
		request.setAttribute("testCode", new Integer(testCodeString));
		request.setAttribute("siteView", siteView);
		return mapping.findForward("editTest");
	}

	public ActionForward prepareEditTestHeader(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		String testCodeString = request.getParameter("testCode");
		String executionCourseIdString = request.getParameter("objectCode");
		Object[] args =
			{
				new Integer(executionCourseIdString),
				new Integer(testCodeString)};
		SiteView siteView = null;
		try {
			siteView =
				(SiteView) ServiceUtils.executeService(
					userView,
					"ReadTest",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		request.setAttribute("testCode", new Integer(testCodeString));
		request.setAttribute("siteView", siteView);
		return mapping.findForward("editTestHeader");
	}

	public ActionForward editTestHeader(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		String executionCourseIdString = request.getParameter("objectCode");
		String testCodeString = request.getParameter("testCode");
		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		String title = request.getParameter("title");
		String information = request.getParameter("information");
		Object[] args = { new Integer(testCodeString), title, information };
		try {
			ServiceUtils.executeService(userView, "EditTest", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		return editTest(mapping, form, request, response);
	}

}
