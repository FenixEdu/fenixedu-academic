/*
 * Created on 14/Ago/2003
 *
 */
package ServidorApresentacao.Action.teacher;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.util.LabelValueBean;
import org.dbunit.util.Base64;

import DataBeans.ISiteComponent;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteDistributedTest;
import DataBeans.InfoSiteTest;
import DataBeans.InfoSiteTestQuestion;
import DataBeans.InfoStudentTestQuestion;
import DataBeans.SiteView;
import DataBeans.TeacherAdministrationSiteView;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.CorrectionAvailability;
import Util.TestType;

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

		request.setAttribute("siteView", readSiteView(request));
		return mapping.findForward("testsFirstPage");
	}

	public ActionForward prepareCreateTest(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		Integer executionCourseId = getCodeFromRequest(request, "objectCode");
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

		Integer executionCourseId = getCodeFromRequest(request, "objectCode");
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

	public ActionForward editAsNewTest(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		Integer executionCourseId = getCodeFromRequest(request, "objectCode");
		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer testCode = getCodeFromRequest(request, "testCode");

		Object[] args = { executionCourseId, testCode };
		Integer newTestCode = null;
		try {
			newTestCode =
				(Integer) ServiceUtils.executeService(
					userView,
					"InsertTestAsNewTest",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		request.setAttribute("testCode", newTestCode);
		return editTest(mapping, form, request, response);
	}
	public ActionForward showAvailableQuestions(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		Integer executionCourseId = getCodeFromRequest(request, "objectCode");
		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		SiteView siteView = null;
		Integer testCode = getCodeFromRequest(request, "testCode");
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
		Integer executionCourseId = getCodeFromRequest(request, "objectCode");
		Integer testCode = getCodeFromRequest(request, "testCode");
		Integer metadataCode = getCodeFromRequest(request, "metadataCode");

		String exerciceIdString = request.getParameter("exerciceCode");
		Integer exerciceCode = null;
		if (exerciceIdString == null)
			exerciceCode = new Integer(-1);
		else
			exerciceCode = new Integer(exerciceIdString);
		SiteView siteView = null;
		try {
			Object[] args = { executionCourseId, metadataCode, exerciceCode };
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
			Object[] args = { executionCourseId, testCode };
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
		request.setAttribute("testCode", testCode);
		request.setAttribute("exerciceCode", exerciceCode);
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
		Integer executionCourseId = getCodeFromRequest(request, "objectCode");
		Integer metadataCode = getCodeFromRequest(request, "metadataCode");
		Integer questionOrder = getCodeFromRequest(request, "questionOrder");
		Integer testCode = getCodeFromRequest(request, "testCode");
		Integer questionValue = getCodeFromRequest(request, "questionValue");

		Object[] arguments =
			{
				executionCourseId,
				testCode,
				metadataCode,
				questionOrder,
				questionValue };
		try {
			ServiceUtils.executeService(
				userView,
				"InsertTestQuestion",
				arguments);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		request.setAttribute("testCode", testCode);
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
		Integer executionCourseId = getCodeFromRequest(request, "objectCode");
		Integer questionCode = getCodeFromRequest(request, "questionCode");
		Integer testCode = getCodeFromRequest(request, "testCode");

		SiteView siteView = null;
		try {
			Object[] args = { executionCourseId, testCode, questionCode };
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
			Object[] args = { executionCourseId, testCode };
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
		request.setAttribute("testCode", testCode);
		request.setAttribute("questionCode", questionCode);
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

		Integer executionCourseCode = getCodeFromRequest(request, "objectCode");
		Integer testCode = getCodeFromRequest(request, "testCode");
		Integer testQuestionCode =
			getCodeFromRequest(request, "testQuestionCode");
		Integer questionOrder =
			getCodeFromRequest(request, "testQuestionOrder");
		Integer questionValue =
			getCodeFromRequest(request, "testQuestionValue");
		Object[] arguments =
			{
				executionCourseCode,
				testCode,
				testQuestionCode,
				questionOrder,
				questionValue };
		try {
			ServiceUtils.executeService(
				userView,
				"EditTestQuestion",
				arguments);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		request.setAttribute("testCode", testCode);
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

		Integer executionCourseCode = getCodeFromRequest(request, "objectCode");
		Integer testCode = getCodeFromRequest(request, "testCode");
		Integer questionCode = getCodeFromRequest(request, "questionCode");
		Object[] args = { executionCourseCode, testCode, questionCode };
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

		Integer executionCourseId = getCodeFromRequest(request, "objectCode");
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

	public ActionForward prepareDeleteTest(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer executionCourseCode = getCodeFromRequest(request, "objectCode");
		Integer testCode = getCodeFromRequest(request, "testCode");
		Object[] args = { executionCourseCode, testCode };
		List result = null;
		try {
			result =
				(List) ServiceUtils.executeService(
					userView,
					"ReadDistributedTestByTestId",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		if (result.size() == 0) {
			return deleteTest(mapping, form, request, response);
		}
		request.setAttribute("testCode", testCode);
		request.setAttribute("objectCode", executionCourseCode);
		request.setAttribute("siteView", readSiteView(request));
		return mapping.findForward("chooseDeleteTestAction");
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
		Integer executionCourseCode = getCodeFromRequest(request, "objectCode");
		Integer testCode = getCodeFromRequest(request, "testCode");

		Object[] args = { executionCourseCode, testCode };
		try {
			ServiceUtils.executeService(userView, "DeleteTest", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		request.setAttribute("objectCode", executionCourseCode);
		return showTests(mapping, form, request, response);
	}

	public ActionForward chooseTestAction(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		String option = request.getParameter("button");
		if (option.equals("sim"))
			return deleteTest(mapping, form, request, response);
		else if (option.equals("Alterar"))
			return editTest(mapping, form, request, response);
		else if (option.equals("Guardar"))
			return editAsNewTest(mapping, form, request, response);
		else
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

		Integer exerciceCode = getCodeFromRequest(request, "exerciceCode");
		Integer imgCode = getCodeFromRequest(request, "imgCode");
		String imgTypeString = request.getParameter("imgType");

		String studentCode = request.getParameter("studentCode");
		Integer testCode = getCodeFromRequest(request, "testCode");

		String img = null;

		if (studentCode != null && testCode != null) {
			Object[] args = { studentCode, testCode, exerciceCode, imgCode };
			try {
				img =
					(String) ServiceUtils.executeService(
						userView,
						"ReadStudentTestQuestionImage",
						args);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}
		} else {
			Object[] args = { exerciceCode, imgCode };
			try {
				img =
					(String) ServiceUtils.executeService(
						userView,
						"ReadQuestionImage",
						args);
			} catch (FenixServiceException e) {
				throw new FenixActionException(e);
			}
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

	public ActionForward prepareEditTest(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer executionCourseCode = getCodeFromRequest(request, "objectCode");
		Integer testCode = getCodeFromRequest(request, "testCode");
		Object[] args = { executionCourseCode, testCode };
		List result = null;
		try {
			result =
				(List) ServiceUtils.executeService(
					userView,
					"ReadDistributedTestByTestId",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		request.setAttribute(
			"distributedTestListSize",
			new Integer(result.size()));
		request.setAttribute("testCode", testCode);
		request.setAttribute("objectCode", executionCourseCode);
		request.setAttribute("siteView", readSiteView(request));
		return mapping.findForward("chooseEditTestAction");
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

		Integer executionCourseCode = getCodeFromRequest(request, "objectCode");
		Integer testCode = getCodeFromRequest(request, "testCode");
		Object[] args = { executionCourseCode, testCode };
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
		request.setAttribute("testCode", testCode);
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

		Integer executionCourseCode = getCodeFromRequest(request, "objectCode");
		Integer testCode = getCodeFromRequest(request, "testCode");
		Object[] args = { executionCourseCode, testCode };
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
		request.setAttribute("testCode", testCode);
		request.setAttribute("siteView", siteView);
		return mapping.findForward("editTestHeader");
	}

	public ActionForward editTestHeader(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		Integer executionCourseCode = getCodeFromRequest(request, "objectCode");
		Integer testCode = getCodeFromRequest(request, "testCode");

		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);
		String title = request.getParameter("title");
		String information = request.getParameter("information");
		Object[] args = { executionCourseCode, testCode, title, information };
		try {
			ServiceUtils.executeService(userView, "EditTest", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		return editTest(mapping, form, request, response);
	}

	public ActionForward prepareDistributeTest(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = getSession(request);
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer objectCode = getCodeFromRequest(request, "objectCode");
		Integer testCode = getCodeFromRequest(request, "testCode");

		List shifts = null;
		try {
			Object[] args = { objectCode };
			shifts =
				(List) ServiceUtils.executeService(
					userView,
					"ReadShiftsByExecutionCourseCode",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		List testTypeList = (new TestType()).getAllTypes();
		request.setAttribute("testTypeList", testTypeList);
		List correctionAvailabilityList =
			(new CorrectionAvailability()).getAllAvailabilities();
		request.setAttribute(
			"correctionAvailabilityList",
			correctionAvailabilityList);

		List shitfTypes = new ArrayList();

		shitfTypes.add(new LabelValueBean("Teórica", "T"));
		shitfTypes.add(new LabelValueBean("Prática", "P"));
		shitfTypes.add(new LabelValueBean("Teórico-prática", "TP"));
		shitfTypes.add(new LabelValueBean("Laboratorial", "L"));

		request.setAttribute("shiftTypes", shitfTypes);
		request.setAttribute("shifts", shifts);
		request.setAttribute("siteView", readSiteView(request));
		request.setAttribute("testCode", testCode);
		request.setAttribute("objectCode", objectCode);
		return mapping.findForward("distributeTest");
	}

	public ActionForward distributeTest(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = getSession(request);
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer objectCode = getCodeFromRequest(request, "objectCode");
		Integer testCode = getCodeFromRequest(request, "testCode");

		String testInformation = request.getParameter("testInformation");
		String testBeginDate = request.getParameter("testBeginDate");
		String testBeginHour = request.getParameter("testBeginHour");
		String testEndDate = request.getParameter("testEndDate");
		String testEndHour = request.getParameter("testEndHour");
		String testType = request.getParameter("testType");
		String availableCorrection =
			request.getParameter("availableCorrection");
		String studentFeedback = request.getParameter("studentFeedback");

		Calendar beginDate = string2Date(testBeginDate);
		Calendar beginHour = string2Hour(testBeginHour);
		Calendar endDate = string2Date(testEndDate);
		Calendar endHour = string2Hour(testEndHour);

		String[] selectedShifts = request.getParameterValues("selectedShifts");

		Object[] args =
			{
				objectCode,
				testCode,
				testInformation,
				beginDate,
				beginHour,
				endDate,
				endHour,
				new TestType(new Integer(testType)),
				new CorrectionAvailability(new Integer(availableCorrection)),
				new Boolean(studentFeedback),
				selectedShifts };

		SiteView siteView = null;
		try {
			ServiceUtils.executeService(
				userView,
				"InsertDistributedTest",
				args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		return showDistributedTests(mapping, form, request, response);
	}

	private Calendar string2Date(String date) {
		String[] dateTokens = date.split("/");
		Calendar result = Calendar.getInstance();
		result.set(
			Calendar.DAY_OF_MONTH,
			(new Integer(dateTokens[0])).intValue());
		result.set(Calendar.MONTH, (new Integer(dateTokens[1])).intValue() - 1);
		result.set(Calendar.YEAR, (new Integer(dateTokens[2])).intValue());
		return result;
	}

	private Calendar string2Hour(String hour) {
		String[] hourTokens = hour.split(":");
		Calendar result = Calendar.getInstance();
		result.set(
			Calendar.HOUR_OF_DAY,
			(new Integer(hourTokens[0])).intValue());
		result.set(Calendar.MINUTE, (new Integer(hourTokens[1])).intValue());
		result.set(Calendar.SECOND, new Integer(0).intValue());
		return result;
	}

	public ActionForward showDistributedTests(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = getSession(request);
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer objectCode = getCodeFromRequest(request, "objectCode");

		Object[] args = { objectCode };
		SiteView siteView = null;
		try {
			siteView =
				(SiteView) ServiceUtils.executeService(
					userView,
					"ReadDistributedTests",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		request.setAttribute("siteView", siteView);
		return mapping.findForward("showDistributedTests");
	}

	public ActionForward prepareEditDistributedTest(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = getSession(request);
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer objectCode = getCodeFromRequest(request, "objectCode");
		Integer distributedTestCode =
			getCodeFromRequest(request, "distributedTestCode");
		Object[] args = { objectCode, distributedTestCode };
		SiteView siteView = null;
		try {
			siteView =
				(SiteView) ServiceUtils.executeService(
					userView,
					"ReadDistributedTest",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		List testTypeList = (new TestType()).getAllTypes();
		for (int i = 0; i < testTypeList.size(); i++) {
			if (((LabelValueBean) testTypeList.get(i))
				.getLabel()
				.equalsIgnoreCase(
					((InfoSiteDistributedTest) siteView.getComponent())
						.getInfoDistributedTest()
						.getTestType()
						.getTypeString()))
				testTypeList.remove(testTypeList.get(i));
		}
		request.setAttribute("testTypeList", testTypeList);

		List correctionAvailabilityList =
			(new CorrectionAvailability()).getAllAvailabilities();
		for (int i = 0; i < correctionAvailabilityList.size(); i++) {
			if (((LabelValueBean) correctionAvailabilityList.get(i))
				.getLabel()
				.equalsIgnoreCase(
					((InfoSiteDistributedTest) siteView.getComponent())
						.getInfoDistributedTest()
						.getCorrectionAvailability()
						.getTypeString()))
				correctionAvailabilityList.remove(
					correctionAvailabilityList.get(i));
		}
		request.setAttribute(
			"correctionAvailabilityList",
			correctionAvailabilityList);

		List studentFeedbackList = new ArrayList();
		studentFeedbackList.add(new LabelValueBean("Sim", "true"));
		if (((InfoSiteDistributedTest) siteView.getComponent())
			.getInfoDistributedTest()
			.getStudentFeedback()
			.booleanValue()
			== true)
			studentFeedbackList.add(1, new LabelValueBean("Não", "false"));
		else
			studentFeedbackList.add(0, new LabelValueBean("Não", "false"));
		request.setAttribute("studentFeedbackList", studentFeedbackList);

		request.setAttribute("distributedTestCode", distributedTestCode);
		request.setAttribute("siteView", siteView);
		return mapping.findForward("editDistributedTest");
	}

	public ActionForward editDistributedTest(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		HttpSession session = getSession(request);
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer objectCode = getCodeFromRequest(request, "objectCode");
		Integer distributedTestCode =
			getCodeFromRequest(request, "distributedTestCode");
		String testInformation = request.getParameter("testInformation");
		String testBeginDate = request.getParameter("beginDateFormatted");
		String testBeginHour = request.getParameter("beginHourFormatted");
		String testEndDate = request.getParameter("endDateFormatted");
		String testEndHour = request.getParameter("endHourFormatted");
		String testType = request.getParameter("testType");
		String availableCorrection =
			request.getParameter("availableCorrection");
		String studentFeedback = request.getParameter("studentFeedback");

		Calendar beginDate = string2Date(testBeginDate);
		Calendar beginHour = string2Hour(testBeginHour);
		Calendar endDate = string2Date(testEndDate);
		Calendar endHour = string2Hour(testEndHour);

		Object[] args =
			{
				objectCode,
				distributedTestCode,
				testInformation,
				beginDate,
				beginHour,
				endDate,
				endHour,
				new TestType(new Integer(testType)),
				new CorrectionAvailability(new Integer(availableCorrection)),
				new Boolean(studentFeedback)};

		try {
			ServiceUtils.executeService(userView, "EditDistributedTest", args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		request.setAttribute("objectCode", objectCode);
		return showDistributedTests(mapping, form, request, response);
	}

	public ActionForward showDistributedTestStudents(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = getSession(request);
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer objectCode = getCodeFromRequest(request, "objectCode");
		Integer distributedTestCode =
			getCodeFromRequest(request, "distributedTestCode");
		Object[] args = { objectCode, distributedTestCode };
		List infoStudentList = null;
		try {
			infoStudentList =
				(List) ServiceUtils.executeService(
					userView,
					"ReadStudentsByDistributedTest",
					args);
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		request.setAttribute("distributedTestCode", distributedTestCode);
		request.setAttribute("infoStudentList", infoStudentList);
		readSiteView(request);
		return mapping.findForward("showDistributedTestStudents");
	}

	public ActionForward showStudentTest(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = getSession(request);
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer objectCode = getCodeFromRequest(request, "objectCode");
		Integer distributedTestCode =
			getCodeFromRequest(request, "distributedTestCode");
		Integer studentCode = getCodeFromRequest(request, "studentCode");

		List infoStudentTestQuestionList = null;
		try {
			infoStudentTestQuestionList =
				(List) ServiceUtils.executeService(
					userView,
					"ReadStudentDistributedTest",
					new Object[] {
						objectCode,
						distributedTestCode,
						studentCode });
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		Collections.sort(infoStudentTestQuestionList);
		request.setAttribute(
			"infoStudentTestQuestionList",
			infoStudentTestQuestionList);

		int numQuestions =
			((InfoStudentTestQuestion) infoStudentTestQuestionList.get(0))
				.getDistributedTest()
				.getInfoTest()
				.getNumberOfQuestions()
				.intValue();

		Iterator it = infoStudentTestQuestionList.iterator();
		String[] option = new String[numQuestions];
		while (it.hasNext()) {
			InfoStudentTestQuestion infoStudentTestQuestion =
				(InfoStudentTestQuestion) it.next();
			option[infoStudentTestQuestion.getTestQuestionOrder().intValue()
				- 1] =
				infoStudentTestQuestion.getResponse().toString();
		}
		((DynaActionForm) form).set("option", option);
		readSiteView(request);
		return mapping.findForward("showStudentTest");
	}

	private SiteView readSiteView(HttpServletRequest request)
		throws FenixActionException {

		HttpSession session = getSession(request);
		UserView userView =
			(UserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer objectCode = getCodeFromRequest(request, "objectCode");
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
			request.setAttribute("objectCode", objectCode);
			return siteView;
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
	}

	private Integer getCodeFromRequest(
		HttpServletRequest request,
		String codeString) {
		Integer code = null;
		String thisCodeString = request.getParameter(codeString);
		if (thisCodeString == null) {
			Object o = request.getAttribute(codeString);
			if (o instanceof String)
				thisCodeString = (String) o;
			else if (o instanceof Integer)
				code = (Integer) o;
		} else if (thisCodeString != null) {
			code = new Integer(thisCodeString);
		}

		return code;
	}

}
