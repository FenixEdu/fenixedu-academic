/*
 * Created on 14/Ago/2003
 *
 */
package ServidorApresentacao.Action.teacher;

import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;
import org.apache.util.Base64;

import DataBeans.ISiteComponent;
import DataBeans.InfoSiteCommon;
import DataBeans.InfoSiteDistributedTest;
import DataBeans.InfoSiteStudents;
import DataBeans.InfoSiteTest;
import DataBeans.InfoSiteTestQuestion;
import DataBeans.InfoStudentTestLog;
import DataBeans.InfoStudentTestQuestion;
import DataBeans.SiteView;
import DataBeans.TeacherAdministrationSiteView;
import DataBeans.comparators.CalendarDateComparator;
import DataBeans.comparators.CalendarHourComparator;
import DataBeans.comparators.InfoShiftComparatorByLessonType;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.UserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.Servico.exceptions.NotExecuteException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.CorrectionAvailability;
import Util.QuestionDifficultyType;
import Util.TestQuestionChangesType;
import Util.TestQuestionStudentsChangesType;
import Util.TestType;

/**
 * @author Susana Fernandes
 */
public class TestsManagementAction extends FenixDispatchAction
{

	public ActionForward testsFirstPage(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		request.setAttribute("siteView", readSiteView(request));
		return mapping.findForward("testsFirstPage");
	}

	public ActionForward prepareCreateTest(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		Integer executionCourseId = getCodeFromRequest(request, "objectCode");
		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer availableMetadatas = null;

		try
		{
			Object[] args = { executionCourseId };
			availableMetadatas =
				(Integer) ServiceUtils.executeService(userView, "CountMetadatasByExecutionCourse", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		request.setAttribute("availableMetadatas", availableMetadatas);
		request.setAttribute("siteView", readSiteView(request));
		return mapping.findForward("createTest");
	}

	public ActionForward createTest(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		Integer executionCourseId = getCodeFromRequest(request, "objectCode");
		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		String title = request.getParameter("title");
		String information = request.getParameter("information");
		Object[] args = { executionCourseId, title, information };
		Integer testCode = null;
		try
		{
			testCode = (Integer) ServiceUtils.executeService(userView, "InsertTest", args);
		}
		catch (FenixServiceException e)
		{
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
		throws FenixActionException
	{

		Integer executionCourseId = getCodeFromRequest(request, "objectCode");
		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer testCode = getCodeFromRequest(request, "testCode");

		Object[] args = { executionCourseId, testCode };
		Integer newTestCode = null;
		try
		{
			newTestCode = (Integer) ServiceUtils.executeService(userView, "InsertTestAsNewTest", args);
		}
		catch (FenixServiceException e)
		{
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
		throws FenixActionException
	{

		Integer executionCourseId = getCodeFromRequest(request, "objectCode");
		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		SiteView siteView = null;
		Integer testCode = getCodeFromRequest(request, "testCode");
		String order = request.getParameter("order");
		String asc = request.getParameter("asc");

		try
		{
			Object[] args = { executionCourseId, testCode, order, asc };
			siteView = (SiteView) ServiceUtils.executeService(userView, "ReadMetadatasByTest", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		request.setAttribute("siteView", siteView);
		request.setAttribute("testCode", testCode);
		request.setAttribute("order", order);
		request.setAttribute("asc", asc);
		return mapping.findForward("showAvailableQuestions");
	}

	public ActionForward prepareInsertTestQuestion(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer executionCourseId = getCodeFromRequest(request, "objectCode");
		Integer testCode = getCodeFromRequest(request, "testCode");
		Integer metadataCode = getCodeFromRequest(request, "metadataCode");

		String exerciseIdString = request.getParameter("exerciseCode");
		Integer exerciseCode = null;
		if (exerciseIdString == null)
			exerciseCode = new Integer(-1);
		else
			exerciseCode = new Integer(exerciseIdString);
		SiteView siteView = null;
		String path = getServlet().getServletContext().getRealPath("/");
		try
		{
			Object[] args = { executionCourseId, metadataCode, exerciseCode, path };
			siteView = (SiteView) ServiceUtils.executeService(userView, "ReadQuestion", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		SiteView siteViewAux = null;
		try
		{
			Object[] args = { executionCourseId, testCode, path };
			siteViewAux = (SiteView) ServiceUtils.executeService(userView, "ReadTest", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		List testQuestionList = new ArrayList();
		testQuestionList = ((InfoSiteTest) siteViewAux.getComponent()).getInfoTestQuestions();
		Collections.sort(testQuestionList);
		List testQuestionNames = new ArrayList();
		List testQuestionValues = new ArrayList();

		for (int i = 0; i < testQuestionList.size(); i++)
		{
			testQuestionNames.add(new String("Pergunta " + (i + 1)));
			testQuestionValues.add(new Integer(i));
		}
		request.setAttribute("order", request.getParameter("order"));
		request.setAttribute("asc", request.getParameter("asc"));
		request.setAttribute("testQuestionNames", testQuestionNames);
		request.setAttribute("testQuestionValues", testQuestionValues);
		request.setAttribute("testCode", testCode);
		request.setAttribute("exerciseCode", exerciseCode);
		request.setAttribute("siteView", siteView);
		return mapping.findForward("insertTestQuestion");
	}

	public ActionForward insertTestQuestion(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer executionCourseId = getCodeFromRequest(request, "objectCode");
		Integer metadataCode = getCodeFromRequest(request, "metadataCode");
		Integer questionOrder = getCodeFromRequest(request, "questionOrder");
		Integer testCode = getCodeFromRequest(request, "testCode");
		Integer questionValue = getCodeFromRequest(request, "questionValue");
		String path = getServlet().getServletContext().getRealPath("/");
		Object[] arguments =
			{ executionCourseId, testCode, metadataCode, questionOrder, questionValue, path };
		try
		{
			ServiceUtils.executeService(userView, "InsertTestQuestion", arguments);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		request.setAttribute("order", request.getParameter("order"));
		request.setAttribute("asc", request.getParameter("asc"));
		request.setAttribute("testCode", testCode);
		return showAvailableQuestions(mapping, form, request, response);
	}

	public ActionForward prepareEditTestQuestion(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer executionCourseId = getCodeFromRequest(request, "objectCode");
		Integer questionCode = getCodeFromRequest(request, "questionCode");
		Integer testCode = getCodeFromRequest(request, "testCode");
		String path = getServlet().getServletContext().getRealPath("/");
		SiteView siteView = null;
		try
		{
			Object[] args = { executionCourseId, testCode, questionCode, path };
			siteView = (SiteView) ServiceUtils.executeService(userView, "ReadTestQuestion", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		SiteView siteViewAux = null;
		try
		{
			Object[] args = { executionCourseId, testCode, path };
			siteViewAux = (SiteView) ServiceUtils.executeService(userView, "ReadTest", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		List testQuestionList = new ArrayList();
		testQuestionList = ((InfoSiteTest) siteViewAux.getComponent()).getInfoTestQuestions();
		Collections.sort(testQuestionList);
		List testQuestionNames = new ArrayList();
		List testQuestionValues = new ArrayList();

		int questionOrder =
			((InfoSiteTestQuestion) siteView.getComponent())
				.getInfoTestQuestion()
				.getTestQuestionOrder()
				.intValue();

		for (int i = 0; i < testQuestionList.size(); i++)
		{
			if ((i + 1) != questionOrder && (i + 1) != questionOrder + 1)
			{
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
		throws FenixActionException
	{

		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer executionCourseCode = getCodeFromRequest(request, "objectCode");
		Integer testCode = getCodeFromRequest(request, "testCode");
		Integer testQuestionCode = getCodeFromRequest(request, "testQuestionCode");
		Integer questionOrder = getCodeFromRequest(request, "testQuestionOrder");
		Integer questionValue = getCodeFromRequest(request, "testQuestionValue");
		Object[] arguments =
			{ executionCourseCode, testCode, testQuestionCode, questionOrder, questionValue };
		try
		{
			ServiceUtils.executeService(userView, "EditTestQuestion", arguments);
		}
		catch (FenixServiceException e)
		{
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
		throws FenixActionException
	{

		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer executionCourseCode = getCodeFromRequest(request, "objectCode");
		Integer testCode = getCodeFromRequest(request, "testCode");
		Integer questionCode = getCodeFromRequest(request, "questionCode");
		Object[] args = { executionCourseCode, testCode, questionCode };
		try
		{
			ServiceUtils.executeService(userView, "DeleteTestQuestion", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		return editTest(mapping, form, request, response);
	}

	public ActionForward showTests(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		Integer executionCourseId = getCodeFromRequest(request, "objectCode");
		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		SiteView siteView = null;
		try
		{
			Object[] args = { executionCourseId };
			siteView = (SiteView) ServiceUtils.executeService(userView, "ReadTests", args);
		}
		catch (FenixServiceException e)
		{
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
		throws FenixActionException
	{
		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer objectCode = getCodeFromRequest(request, "objectCode");
		Integer testCode = getCodeFromRequest(request, "testCode");
		request.setAttribute("objectCode", objectCode);
		request.setAttribute("testCode", testCode);
		String path = getServlet().getServletContext().getRealPath("/");
		SiteView siteView = null;
		try
		{
			siteView =
				(SiteView) ServiceUtils.executeService(
					userView,
					"ReadTest",
					new Object[] { objectCode, testCode, path });

		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		request.setAttribute("title", ((InfoSiteTest) siteView.getComponent()).getInfoTest().getTitle());
		request.setAttribute("siteView", readSiteView(request));
		return mapping.findForward("prepareDeleteTest");
	}

	public ActionForward deleteTest(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer executionCourseCode = getCodeFromRequest(request, "objectCode");
		Integer testCode = getCodeFromRequest(request, "testCode");

		Object[] args = { executionCourseCode, testCode };
		try
		{
			ServiceUtils.executeService(userView, "DeleteTest", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		request.setAttribute("objectCode", executionCourseCode);
		return showTests(mapping, form, request, response);
	}

	public ActionForward showImage(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer exerciseCode = getCodeFromRequest(request, "exerciseCode");
		Integer imgCode = getCodeFromRequest(request, "imgCode");
		String imgTypeString = request.getParameter("imgType");

		String studentCode = request.getParameter("studentCode");
		Integer testCode = getCodeFromRequest(request, "testCode");
		String path = getServlet().getServletContext().getRealPath("/");

		String img = null;
		if (studentCode != null && testCode != null)
		{
			Object[] args = { studentCode, testCode, exerciseCode, imgCode, path };
			try
			{
				img =
					(String) ServiceUtils.executeService(userView, "ReadStudentTestQuestionImage", args);
			}
			catch (FenixServiceException e)
			{
				throw new FenixActionException(e);
			}
		}
		else
		{
			Object[] args = { exerciseCode, imgCode, path };
			try
			{
				img = (String) ServiceUtils.executeService(userView, "ReadQuestionImage", args);
			}
			catch (FenixServiceException e)
			{
				throw new FenixActionException(e);
			}
		}
		byte[] imageData = Base64.decode(img.getBytes());

		try
		{
			response.reset();
			response.setContentType(imgTypeString);
			response.setContentLength(imageData.length);
			response.setBufferSize(imageData.length);
			String imageName =
				new String(
					"image"
						+ exerciseCode
						+ imgCode
						+ "."
						+ imgTypeString.substring(
							imgTypeString.lastIndexOf("/") + 1,
							imgTypeString.length()));
			response.setHeader("Content-disposition", "attachment; filename=" + imageName);
			DataOutputStream dataOut = new DataOutputStream(response.getOutputStream());
			dataOut.write(imageData);
			response.flushBuffer();
		}
		catch (java.io.IOException e)
		{
			throw new FenixActionException(e);
		}
		return null;
	}

	public ActionForward editTest(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer executionCourseCode = getCodeFromRequest(request, "objectCode");
		Integer testCode = getCodeFromRequest(request, "testCode");
		String path = getServlet().getServletContext().getRealPath("/");
		Object[] args = { executionCourseCode, testCode, path };
		SiteView siteView = null;
		try
		{
			siteView = (SiteView) ServiceUtils.executeService(userView, "ReadTest", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		Collections.sort(((InfoSiteTest) siteView.getComponent()).getInfoTestQuestions());
		request.setAttribute("testCode", testCode);
		request.setAttribute("siteView", siteView);
		return mapping.findForward("editTest");
	}

	public ActionForward prepareEditTestHeader(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer executionCourseCode = getCodeFromRequest(request, "objectCode");
		Integer testCode = getCodeFromRequest(request, "testCode");
		String path = getServlet().getServletContext().getRealPath("/");
		Object[] args = { executionCourseCode, testCode, path };
		SiteView siteView = null;
		try
		{
			siteView = (SiteView) ServiceUtils.executeService(userView, "ReadTest", args);
		}
		catch (FenixServiceException e)
		{
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
		throws FenixActionException
	{

		Integer executionCourseCode = getCodeFromRequest(request, "objectCode");
		Integer testCode = getCodeFromRequest(request, "testCode");

		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		String title = request.getParameter("title");
		String information = request.getParameter("information");
		Object[] args = { executionCourseCode, testCode, title, information };
		try
		{
			ServiceUtils.executeService(userView, "EditTest", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		return editTest(mapping, form, request, response);
	}

	public ActionForward prepareDistributeTest(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		HttpSession session = getSession(request);
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer objectCode = getCodeFromRequest(request, "objectCode");
		Integer testCode = getCodeFromRequest(request, "testCode");

		List testTypeList = (new TestType()).getAllTypes();
		request.setAttribute("testTypeList", testTypeList);
		List correctionAvailabilityList = (new CorrectionAvailability()).getAllAvailabilities();
		request.setAttribute("correctionAvailabilityList", correctionAvailabilityList);
		if ((((DynaActionForm) form).get("testType")).equals(""))
			 ((DynaActionForm) form).set("testType", "1");
		if ((((DynaActionForm) form).get("availableCorrection")).equals(""))
			 ((DynaActionForm) form).set("availableCorrection", "3");
		if ((((DynaActionForm) form).get("studentFeedback")).equals(""))
			 ((DynaActionForm) form).set("studentFeedback", "true");

		request.setAttribute("siteView", readSiteView(request));

		if ((((DynaActionForm) form).get("testInformation")).equals(""))
		{
			String path = getServlet().getServletContext().getRealPath("/");
			Object[] args = { objectCode, testCode, path };
			SiteView siteView = null;
			try
			{
				siteView = (SiteView) ServiceUtils.executeService(userView, "ReadTest", args);
			}
			catch (FenixServiceException e)
			{
				throw new FenixActionException(e);
			}

			((DynaActionForm) form).set("testInformation", createDefaultDistributedTestInfo(siteView));
		}
		request.setAttribute("testCode", testCode);
		request.setAttribute("objectCode", objectCode);

		return mapping.findForward("distributeTest");
	}

	public ActionForward chooseDistributionFor(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		if (request.getParameter("shifts") != null)
		{
			if (!compareDates(request))
			{
				return prepareDistributeTest(mapping, form, request, response);
			}
			return chooseShifts(mapping, form, request, response);
		}
		else if (request.getParameter("students") != null)
		{
			if (!compareDates(request))
			{
				return prepareDistributeTest(mapping, form, request, response);
			}
			return chooseStudents(mapping, form, request, response);
		}
		else if (request.getParameter("addShifts") != null)
		{
			if (!compareDates(request))
			{
				return prepareEditDistributedTest(mapping, form, request, response);
			}

			return chooseAddShifts(mapping, form, request, response);
		}
		else if (request.getParameter("addStudents") != null)
		{
			if (!compareDates(request))
			{
				return prepareEditDistributedTest(mapping, form, request, response);
			}
			return chooseAddStudents(mapping, form, request, response);
		}
		else if (request.getParameter("save") != null)
		{
			if (!compareDates(request))
			{
				return prepareEditDistributedTest(mapping, form, request, response);
			}
			return editDistributedTest(mapping, form, request, response);
		}
		else
			return showDistributedTests(mapping, form, request, response);
	}

	public ActionForward chooseShifts(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		HttpSession session = getSession(request);
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer objectCode = getCodeFromRequest(request, "objectCode");
		Integer testCode = getCodeFromRequest(request, "testCode");

		List shifts = null;
		try
		{
			Object[] args = { objectCode, null };
			shifts = (List) ServiceUtils.executeService(userView, "ReadShiftsByDistributedTest", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		Collections.sort(shifts, new InfoShiftComparatorByLessonType());
		request.setAttribute("siteView", readSiteView(request));
		request.setAttribute("shifts", shifts);
		request.setAttribute("testCode", testCode);
		request.setAttribute("objectCode", objectCode);
		return mapping.findForward("distributeTestByShifts");
	}

	public ActionForward chooseStudents(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		HttpSession session = getSession(request);
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer objectCode = getCodeFromRequest(request, "objectCode");
		Integer testCode = getCodeFromRequest(request, "testCode");
		TeacherAdministrationSiteView siteView = null;
		Object[] args = { objectCode, null };
		try
		{
			siteView =
				(TeacherAdministrationSiteView) ServiceUtils.executeService(
					userView,
					"ReadStudentsByCurricularCourse",
					args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		InfoSiteStudents infoSiteStudents = (InfoSiteStudents) siteView.getComponent();
		Collections.sort(infoSiteStudents.getStudents(), new BeanComparator("number"));

		request.setAttribute("siteView", siteView);
		request.setAttribute("testCode", testCode);
		request.setAttribute("objectCode", objectCode);
		return mapping.findForward("distributeTestByStudents");
	}

	public ActionForward distributeTest(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		HttpSession session = getSession(request);
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer objectCode = getCodeFromRequest(request, "objectCode");
		Integer testCode = getCodeFromRequest(request, "testCode");

		String testInformation = request.getParameter("testInformation");
		String testBeginDate = request.getParameter("beginDateFormatted");
		String testBeginHour = request.getParameter("beginHourFormatted");
		String testEndDate = request.getParameter("endDateFormatted");
		String testEndHour = request.getParameter("endHourFormatted");
		String testType = request.getParameter("testType");
		String availableCorrection = request.getParameter("availableCorrection");
		String studentFeedback = request.getParameter("studentFeedback");

		Calendar beginDate = string2Date(testBeginDate);
		Calendar beginHour = string2Hour(testBeginHour);
		Calendar endDate = string2Date(testEndDate);
		Calendar endHour = string2Hour(testEndHour);

		String[] selected = request.getParameterValues("selected");
		String insertByShifts = request.getParameter("insertByShifts");
		String path = getServlet().getServletContext().getRealPath("/");

		TestType testTypeArg = new TestType(new Integer(testType));
		CorrectionAvailability correctionAvailabilityArg;
		Boolean studentFeedbackArg;
		if (testTypeArg.equals(new TestType(TestType.INQUIRY)))
		{
			correctionAvailabilityArg = new CorrectionAvailability(CorrectionAvailability.NEVER);
			studentFeedbackArg = new Boolean(false);
		}
		else
		{
			correctionAvailabilityArg = new CorrectionAvailability(new Integer(availableCorrection));
			studentFeedbackArg = new Boolean(studentFeedback);
		}

		Object[] args =
			{
				objectCode,
				testCode,
				testInformation,
				beginDate,
				beginHour,
				endDate,
				endHour,
				testTypeArg,
				correctionAvailabilityArg,
				studentFeedbackArg,
				selected,
				new Boolean(insertByShifts),
				path };

		Boolean result = new Boolean(false);
		try
		{
			result = (Boolean) ServiceUtils.executeService(userView, "InsertDistributedTest", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		request.setAttribute("successfulDistribution", result);
		return showDistributedTests(mapping, form, request, response);
	}

	public ActionForward showDistributedTests(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		HttpSession session = getSession(request);
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer objectCode = getCodeFromRequest(request, "objectCode");

		Object[] args = { objectCode };
		SiteView siteView = null;
		try
		{
			siteView = (SiteView) ServiceUtils.executeService(userView, "ReadDistributedTests", args);
		}
		catch (FenixServiceException e)
		{
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
		throws FenixActionException
	{

		HttpSession session = getSession(request);
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer objectCode = getCodeFromRequest(request, "objectCode");
		Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
		Object[] args = { objectCode, distributedTestCode };
		SiteView siteView = null;
		try
		{
			siteView = (SiteView) ServiceUtils.executeService(userView, "ReadDistributedTest", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		List testTypeList = (new TestType()).getAllTypes();
		request.setAttribute("testTypeList", testTypeList);
		List correctionAvailabilityList = (new CorrectionAvailability()).getAllAvailabilities();
		request.setAttribute("correctionAvailabilityList", correctionAvailabilityList);

		if ((((DynaActionForm) form).get("testInformation")).equals(""))
			((DynaActionForm) form).set(
				"testInformation",
				((InfoSiteDistributedTest) siteView.getComponent())
					.getInfoDistributedTest()
					.getTestInformation());
		if ((((DynaActionForm) form).get("beginDateFormatted")).equals(""))
			((DynaActionForm) form).set(
				"beginDateFormatted",
				((InfoSiteDistributedTest) siteView.getComponent())
					.getInfoDistributedTest()
					.getBeginDateFormatted());
		if ((((DynaActionForm) form).get("beginHourFormatted")).equals(""))
			((DynaActionForm) form).set(
				"beginHourFormatted",
				((InfoSiteDistributedTest) siteView.getComponent())
					.getInfoDistributedTest()
					.getBeginHourFormatted());

		if ((((DynaActionForm) form).get("endDateFormatted")).equals(""))
			((DynaActionForm) form).set(
				"endDateFormatted",
				((InfoSiteDistributedTest) siteView.getComponent())
					.getInfoDistributedTest()
					.getEndDateFormatted());

		if ((((DynaActionForm) form).get("endHourFormatted")).equals(""))
			((DynaActionForm) form).set(
				"endHourFormatted",
				((InfoSiteDistributedTest) siteView.getComponent())
					.getInfoDistributedTest()
					.getEndHourFormatted());

		if ((((DynaActionForm) form).get("testType")).equals(""))
			((DynaActionForm) form).set(
				"testType",
				((InfoSiteDistributedTest) siteView.getComponent())
					.getInfoDistributedTest()
					.getTestType()
					.getType()
					.toString());
		if ((((DynaActionForm) form).get("availableCorrection")).equals(""))
			((DynaActionForm) form).set(
				"availableCorrection",
				((InfoSiteDistributedTest) siteView.getComponent())
					.getInfoDistributedTest()
					.getCorrectionAvailability()
					.getAvailability()
					.toString());
		if ((((DynaActionForm) form).get("studentFeedback")).equals(""))
			((DynaActionForm) form).set(
				"studentFeedback",
				((InfoSiteDistributedTest) siteView.getComponent())
					.getInfoDistributedTest()
					.getStudentFeedback()
					.toString());

		request.setAttribute("distributedTestCode", distributedTestCode);
		request.setAttribute("siteView", siteView);
		return mapping.findForward("editDistributedTest");
	}

	public ActionForward chooseAddShifts(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		HttpSession session = getSession(request);
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer objectCode = getCodeFromRequest(request, "objectCode");
		Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");

		List shifts = null;
		try
		{
			Object[] args = { objectCode, distributedTestCode };
			shifts = (List) ServiceUtils.executeService(userView, "ReadShiftsByDistributedTest", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		Collections.sort(shifts, new InfoShiftComparatorByLessonType());
		request.setAttribute("siteView", readSiteView(request));
		request.setAttribute("shifts", shifts);
		request.setAttribute("distributedTestCode", distributedTestCode);
		request.setAttribute("objectCode", objectCode);
		return mapping.findForward("addShiftsToDistributedTest");
	}
	public ActionForward chooseAddStudents(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		HttpSession session = getSession(request);
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer objectCode = getCodeFromRequest(request, "objectCode");
		Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");

		List students = null;
		try
		{
			Object[] args = { objectCode, distributedTestCode };
			students =
				(List) ServiceUtils.executeService(userView, "ReadStudentsWithoutDistributedTest", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		Collections.sort(students, new BeanComparator("number"));
		request.setAttribute("siteView", readSiteView(request));
		request.setAttribute("students", students);
		request.setAttribute("distributedTestCode", distributedTestCode);
		request.setAttribute("objectCode", objectCode);
		return mapping.findForward("addStudentsToDistributedTest");
	}

	public ActionForward editDistributedTest(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		HttpSession session = getSession(request);
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer objectCode = getCodeFromRequest(request, "objectCode");
		Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
		String testInformation = request.getParameter("testInformation");
		String testBeginDate = request.getParameter("beginDateFormatted");
		String testBeginHour = request.getParameter("beginHourFormatted");
		String testEndDate = request.getParameter("endDateFormatted");
		String testEndHour = request.getParameter("endHourFormatted");
		String testType = request.getParameter("testType");
		String availableCorrection = request.getParameter("availableCorrection");
		String studentFeedback = request.getParameter("studentFeedback");

		Calendar beginDate = string2Date(testBeginDate);
		Calendar beginHour = string2Hour(testBeginHour);
		Calendar endDate = string2Date(testEndDate);
		Calendar endHour = string2Hour(testEndHour);
		String[] selected = request.getParameterValues("selected");
		String insertByShifts = request.getParameter("insertByShifts");
		String path = getServlet().getServletContext().getRealPath("/");

		TestType testTypeArg = new TestType(new Integer(testType));
		CorrectionAvailability correctionAvailabilityArg;
		Boolean studentFeedbackArg;
		if (testTypeArg.equals(new TestType(TestType.INQUIRY)))
		{
			correctionAvailabilityArg = new CorrectionAvailability(CorrectionAvailability.NEVER);
			studentFeedbackArg = new Boolean(false);
		}
		else
		{
			correctionAvailabilityArg = new CorrectionAvailability(new Integer(availableCorrection));
			studentFeedbackArg = new Boolean(studentFeedback);
		}
		Object[] args =
			{
				objectCode,
				distributedTestCode,
				testInformation,
				beginDate,
				beginHour,
				endDate,
				endHour,
				testTypeArg,
				correctionAvailabilityArg,
				studentFeedbackArg,
				selected,
				new Boolean(insertByShifts),
				path };
		Boolean result = new Boolean(false);
		try
		{
			result = (Boolean) ServiceUtils.executeService(userView, "EditDistributedTest", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		request.setAttribute("objectCode", objectCode);
		request.setAttribute("successfulEdition", result);
		return showDistributedTests(mapping, form, request, response);
	}

	public ActionForward showDistributedTestStudents(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		HttpSession session = getSession(request);
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer objectCode = getCodeFromRequest(request, "objectCode");
		Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
		Object[] args = { objectCode, distributedTestCode };
		List infoStudentList = null;
		try
		{
			infoStudentList =
				(List) ServiceUtils.executeService(userView, "ReadStudentsByDistributedTest", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		Collections.sort(infoStudentList, new BeanComparator("number"));
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
		throws FenixActionException
	{

		HttpSession session = getSession(request);
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer objectCode = getCodeFromRequest(request, "objectCode");
		Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
		Integer studentCode = getCodeFromRequest(request, "studentCode");
		String path = getServlet().getServletContext().getRealPath("/");
		List infoStudentTestQuestionList = null;
		try
		{
			infoStudentTestQuestionList =
				(List) ServiceUtils.executeService(
					userView,
					"ReadStudentDistributedTest",
					new Object[] { objectCode, distributedTestCode, studentCode, path });
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		Collections.sort(infoStudentTestQuestionList);
		request.setAttribute("infoStudentTestQuestionList", infoStudentTestQuestionList);

		int numQuestions =
			((InfoStudentTestQuestion) infoStudentTestQuestionList.get(0))
				.getDistributedTest()
				.getNumberOfQuestions()
				.intValue();

		Iterator it = infoStudentTestQuestionList.iterator();
		String[] option = new String[numQuestions];
		double classification = 0;
		while (it.hasNext())
		{
			InfoStudentTestQuestion infoStudentTestQuestion = (InfoStudentTestQuestion) it.next();
			option[infoStudentTestQuestion.getTestQuestionOrder().intValue() - 1] =
				infoStudentTestQuestion.getResponse().toString();
			classification += infoStudentTestQuestion.getTestQuestionMark().doubleValue();
		}
		if (classification < 0)
			classification = 0;
		request.setAttribute("classification", (new DecimalFormat("#0.##").format(classification)));
		((DynaActionForm) form).set("option", option);
		readSiteView(request);
		return mapping.findForward("showStudentTest");
	}

	public ActionForward showStudentTestLog(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		HttpSession session = getSession(request);
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer objectCode = getCodeFromRequest(request, "objectCode");
		Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
		Integer studentCode = getCodeFromRequest(request, "studentCode");

		List infoStudentTestLogList = null;
		try
		{
			infoStudentTestLogList =
				(List) ServiceUtils.executeService(
					userView,
					"ReadStudentTestLog",
					new Object[] { objectCode, distributedTestCode, studentCode });
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		Iterator it = infoStudentTestLogList.iterator();
		int maxQuestionNumber = 0;
		while (it.hasNext())
		{
			InfoStudentTestLog infoStudentTestLog = (InfoStudentTestLog) it.next();
			if (maxQuestionNumber < infoStudentTestLog.getEventList().size())
				maxQuestionNumber = infoStudentTestLog.getEventList().size();
		}
		request.setAttribute("questionNumber", new Integer(maxQuestionNumber));
		readSiteView(request);
		request.setAttribute("infoStudentTestLogList", infoStudentTestLogList);
		request.setAttribute("distributedTestCode", distributedTestCode);
		return mapping.findForward("showStudentTestLog");
	}

	public ActionForward showTestMarks(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		HttpSession session = getSession(request);
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer objectCode = getCodeFromRequest(request, "objectCode");
		Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
		Object[] args = { objectCode, distributedTestCode };

		SiteView siteView = null;
		try
		{
			siteView =
				(SiteView) ServiceUtils.executeService(userView, "ReadDistributedTestMarks", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		request.setAttribute("objectCode", objectCode);
		request.setAttribute("distributedTestCode", distributedTestCode);
		request.setAttribute("siteView", siteView);
		return mapping.findForward("showTestMarks");
	}

	public ActionForward showTestMarksStatistics(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		HttpSession session = getSession(request);
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer objectCode = getCodeFromRequest(request, "objectCode");
		Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
		String path = getServlet().getServletContext().getRealPath("/");
		Object[] args = { objectCode, distributedTestCode };

		SiteView siteView = null;
		try
		{
			siteView =
				(SiteView) ServiceUtils.executeService(
					userView,
					"ReadDistributedTestMarksStatistics",
					args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		request.setAttribute("objectCode", objectCode);
		request.setAttribute("distributedTestCode", distributedTestCode);
		request.setAttribute("siteView", siteView);
		return mapping.findForward("showTestMarksStatistics");
	}

	public ActionForward showTestStatistics(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		HttpSession session = getSession(request);
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer objectCode = getCodeFromRequest(request, "objectCode");
		Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
		String path = getServlet().getServletContext().getRealPath("/");
		Object[] args = { objectCode, distributedTestCode, path };

		SiteView siteView = null;
		try
		{
			siteView = (SiteView) ServiceUtils.executeService(userView, "ReadInquiryStatistics", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		request.setAttribute("objectCode", objectCode);
		request.setAttribute("distributedTestCode", distributedTestCode);
		request.setAttribute("siteView", siteView);
		return mapping.findForward("showTestStatistics");
	}

	public ActionForward downloadTestMarks(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		HttpSession session = getSession(request);
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);
		Integer objectCode = getCodeFromRequest(request, "objectCode");
		Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
		Object[] args = { objectCode, distributedTestCode };

		String result = null;
		try
		{
			result =
				(String) ServiceUtils.executeService(userView, "ReadDistributedTestMarksToString", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		try
		{
			ServletOutputStream writer = response.getOutputStream();
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment; filename=pauta.xls");
			writer.print(result);
			writer.flush();
			response.flushBuffer();
		}
		catch (IOException e)
		{
			throw new FenixActionException();
		}
		return null;
	}

	public ActionForward exercisesFirstPage(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		Integer executionCourseId = getCodeFromRequest(request, "objectCode");
		List badXmls = (List) request.getAttribute("badXmls");

		HttpSession session = request.getSession(false);
		IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
		SiteView siteView = null;
		String order = request.getParameter("order");
		String asc = request.getParameter("asc");
		String path = getServlet().getServletContext().getRealPath("/");
		try
		{
			Object[] args = { executionCourseId, order, asc, path };
			siteView = (SiteView) ServiceUtils.executeService(userView, "ReadMetadatas", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		request.setAttribute("badXmls", badXmls);
		request.setAttribute("siteView", siteView);
		request.setAttribute("order", order);
		request.setAttribute("asc", asc);
		return mapping.findForward("exercisesFirstPage");
	}

	public ActionForward insertNewExercise(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		request.setAttribute("siteView", readSiteView(request));
		request.setAttribute("order", request.getParameter("order"));
		request.setAttribute("asc", request.getParameter("asc"));
		return mapping.findForward("insertNewExercise");
	}

	public ActionForward loadExerciseFiles(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws Exception, NotExecuteException
	{

		IUserView userView = (IUserView) request.getSession(false).getAttribute(SessionConstants.U_VIEW);
		FormFile metadataFile = (FormFile) ((DynaActionForm) form).get("metadataFile");
		FormFile xmlZipFile = (FormFile) ((DynaActionForm) form).get("xmlZipFile");

		Integer executionCourseId = getCodeFromRequest(request, "objectCode");
		request.setAttribute("siteView", readSiteView(request));

		if (metadataFile != null)
			if ((metadataFile.getFileData().length != 0)
				&& !(metadataFile.getContentType().equals("text/xml")))
			{
				error(request, "FileNotExist", "error.badMetadataFile");
				return mapping.findForward("insertNewExercise");
			}

		if (xmlZipFile==null || xmlZipFile.getFileData().length == 0){
			error(request, "FileNotExist", "error.nullXmlZipFile");
			return mapping.findForward("insertNewExercise");
		}else if (!(xmlZipFile.getContentType().equals("application/x-zip-compressed")
			|| xmlZipFile.getContentType().equals("text/xml")
			|| (xmlZipFile.getContentType().equals("application/zip"))))
		{
			error(request, "FileNotExist", "error.badXmlZipFile");
			return mapping.findForward("insertNewExercise");
		}
		String path = getServlet().getServletContext().getRealPath("/");
		List badXmls = null;
		try
		{
			Object[] args = { executionCourseId, metadataFile, xmlZipFile, path };
			badXmls = (List) ServiceUtils.executeService(userView, "InsertExercice", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		request.setAttribute("badXmls", badXmls);
		request.setAttribute("order", request.getParameter("order"));
		request.setAttribute("asc", request.getParameter("asc"));
		return exercisesFirstPage(mapping, form, request, response);
	}

	public ActionForward prepareRemoveExercise(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		request.setAttribute("objectCode", getCodeFromRequest(request, "objectCode"));
		request.setAttribute("exerciseCode", getCodeFromRequest(request, "exerciseCode"));
		request.setAttribute("siteView", readSiteView(request));
		request.setAttribute("order", request.getParameter("order"));
		request.setAttribute("asc", request.getParameter("asc"));
		return mapping.findForward("prepareRemoveExercise");
	}

	public ActionForward removeExercise(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		IUserView userView = (IUserView) request.getSession(false).getAttribute(SessionConstants.U_VIEW);
		Integer executionCourseId = getCodeFromRequest(request, "objectCode");
		Integer metadataId = getCodeFromRequest(request, "exerciseCode");
		String path = getServlet().getServletContext().getRealPath("/");
		try
		{
			Object[] args = { executionCourseId, metadataId, path };
			ServiceUtils.executeService(userView, "DeleteExercice", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
		request.setAttribute("successfulDeletion", "true");
		request.setAttribute("order", request.getParameter("order"));
		request.setAttribute("asc", request.getParameter("asc"));
		return exercisesFirstPage(mapping, form, request, response);
	}

	public ActionForward prepareEditExercise(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		IUserView userView = (IUserView) request.getSession(false).getAttribute(SessionConstants.U_VIEW);
		Integer executionCourseId = getCodeFromRequest(request, "objectCode");
		Integer exerciseId = getCodeFromRequest(request, "exerciseCode");
		Integer variationCode = getCodeFromRequest(request, "variationCode");
		String path = getServlet().getServletContext().getRealPath("/");

		if (variationCode == null)
			variationCode = new Integer(-1);
		SiteView siteView = null;
		try
		{
			Object[] args = { executionCourseId, exerciseId, path };
			siteView = (SiteView) ServiceUtils.executeService(userView, "ReadExercise", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		request.setAttribute("objectCode", executionCourseId);
		request.setAttribute("exerciseCode", exerciseId);
		request.setAttribute("variationCode", variationCode);
		request.setAttribute("siteView", siteView);
		request.setAttribute("order", request.getParameter("order"));
		request.setAttribute("asc", request.getParameter("asc"));
		List questionDifficultyList = (new QuestionDifficultyType()).getAllTypes();
		request.setAttribute("questionDifficultyList", questionDifficultyList);
		return mapping.findForward("editExercise");
	}

	public ActionForward editExercise(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		IUserView userView = (IUserView) request.getSession(false).getAttribute(SessionConstants.U_VIEW);
		Integer executionCourseId = getCodeFromRequest(request, "objectCode");
		Integer exerciseId = getCodeFromRequest(request, "exerciseCode");
		//String path = getServlet().getServletContext().getRealPath("/");
		String author = request.getParameter("author");
		String description = request.getParameter("description");
		String difficulty = request.getParameter("difficulty");
		String learningTime = request.getParameter("learningTimeFormatted");
		String level = request.getParameter("level");
		String mainSubject = request.getParameter("mainSubject");
		String secondarySubject = request.getParameter("secondarySubject");

		if (!difficulty.equals("-1"))
			difficulty = new QuestionDifficultyType(new Integer(difficulty)).getTypeString();
		Boolean result = null;
		try
		{
			Object[] args =
				{
					executionCourseId,
					exerciseId,
					author,
					description,
					difficulty,
					string2Hour(learningTime),
					level,
					mainSubject,
					secondarySubject };
			result = (Boolean) ServiceUtils.executeService(userView, "EditExercise", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		request.setAttribute("objectCode", executionCourseId);
		request.setAttribute("successfulEdition", result);
		request.setAttribute("order", request.getParameter("order"));
		request.setAttribute("asc", request.getParameter("asc"));
		return exercisesFirstPage(mapping, form, request, response);
	}

	public ActionForward prepareChangeStudentTestQuestion(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{
		request.setAttribute("objectCode", getCodeFromRequest(request, "objectCode"));
		request.setAttribute("questionCode", getCodeFromRequest(request, "questionCode"));
		request.setAttribute("distributedTestCode", getCodeFromRequest(request, "distributedTestCode"));
		request.setAttribute("studentCode", getCodeFromRequest(request, "studentCode"));

		request.setAttribute("siteView", readSiteView(request));

		List changesTypeList = (new TestQuestionChangesType()).getAllTypes();
		request.setAttribute("changesTypeList", changesTypeList);
		List studentsTypeList = (new TestQuestionStudentsChangesType()).getAllTypes();
		request.setAttribute("studentsTypeList", studentsTypeList);

		((DynaActionForm) form).set("changesType", "1");
		((DynaActionForm) form).set("deleteVariation", "true");
		((DynaActionForm) form).set("studentsType", "3");

		return mapping.findForward("changeStudentTestQuestion");
	}

	public ActionForward chooseAnotherExercise(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		IUserView userView = (IUserView) request.getSession(false).getAttribute(SessionConstants.U_VIEW);
		Integer executionCourseId = getCodeFromRequest(request, "objectCode");
		Integer distributedTestId = getCodeFromRequest(request, "distributedTestCode");

		String path = getServlet().getServletContext().getRealPath("/");
		SiteView siteView = null;
		try
		{
			Object[] args = { executionCourseId, distributedTestId, path };
			siteView =
				(SiteView) ServiceUtils.executeService(userView, "ReadMetadatasByDistributedTest", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		request.setAttribute("objectCode", executionCourseId);
		request.setAttribute("questionCode", getCodeFromRequest(request, "questionCode"));
		request.setAttribute("distributedTestCode", distributedTestId);
		request.setAttribute("studentCode", getCodeFromRequest(request, "studentCode"));
		request.setAttribute("successfulChanged", request.getAttribute("successfulChanged"));
		request.setAttribute("studentsType", request.getAttribute("studentsType"));
		request.setAttribute("changesType", request.getAttribute("changesType"));
		request.setAttribute("deleteVariation", request.getAttribute("deleteVariation"));
		request.setAttribute("siteView", siteView);
		return mapping.findForward("chooseAnotherExercise");
	}

	public ActionForward changeStudentTestQuestion(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException
	{

		IUserView userView = (IUserView) request.getSession(false).getAttribute(SessionConstants.U_VIEW);

		Integer executionCourseId = getCodeFromRequest(request, "objectCode");
		Integer questionId = getCodeFromRequest(request, "questionCode");
		Integer distributedTestId = getCodeFromRequest(request, "distributedTestCode");
		Integer studentId = getCodeFromRequest(request, "studentCode");
		Integer metadataId = getCodeFromRequest(request, "metadataCode");
		String changesType = request.getParameter("changesType");
		String delete = request.getParameter("deleteVariation");
		String studentsType = request.getParameter("studentsType");
		request.setAttribute("siteView", readSiteView(request));
		request.setAttribute("studentCode", studentId);
		request.setAttribute("distributedTestCode", distributedTestId);
		request.setAttribute("objectCode", executionCourseId);

		if (((new TestQuestionChangesType(new Integer(changesType))).getType().intValue() == 2)
			&& (metadataId == null))
		{
			request.setAttribute("deleteVariation", delete);
			request.setAttribute("studentsType", studentsType);
			request.setAttribute("changesType", changesType);
			return chooseAnotherExercise(mapping, form, request, response);
		}
		String path = getServlet().getServletContext().getRealPath("/");
		List result;
		try
		{
			Object[] args =
				{
					executionCourseId,
					distributedTestId,
					questionId,
					metadataId,
					studentId,
					new TestQuestionChangesType(new Integer(changesType)),
					new Boolean(delete),
					new TestQuestionStudentsChangesType(new Integer(studentsType)),
					path };
			result = (List) ServiceUtils.executeService(userView, "ChangeStudentTestQuestion", args);
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}

		if (result == null || result.size() == 0)
		{
			request.setAttribute("successfulChanged", new Boolean(false));
			request.setAttribute("deleteVariation", delete);
			request.setAttribute("studentsType", studentsType);
			request.setAttribute("changesType", changesType);
			return chooseAnotherExercise(mapping, form, request, response);
		}
		else
		{
			Collections.sort(result, new BeanComparator("label"));
			request.setAttribute("successfulChanged", result);
		}

		return showStudentTest(mapping, form, request, response);
	}

	private void error(HttpServletRequest request, String errorProperty, String error)
	{
		ActionErrors actionErrors = new ActionErrors();
		actionErrors.add(errorProperty, new ActionError(error));
		saveErrors(request, actionErrors);
	}

	private SiteView readSiteView(HttpServletRequest request) throws FenixActionException
	{

		HttpSession session = getSession(request);
		UserView userView = (UserView) session.getAttribute(SessionConstants.U_VIEW);

		Integer objectCode = getCodeFromRequest(request, "objectCode");
		ISiteComponent commonComponent = new InfoSiteCommon();
		Object[] args = { objectCode, commonComponent, null, objectCode, null, null };

		try
		{
			TeacherAdministrationSiteView siteView =
				(TeacherAdministrationSiteView) ServiceUtils.executeService(
					userView,
					"TeacherAdministrationSiteComponentService",
					args);
			request.setAttribute("siteView", siteView);
			request.setAttribute("objectCode", objectCode);
			return siteView;
		}
		catch (FenixServiceException e)
		{
			throw new FenixActionException(e);
		}
	}

	private Integer getCodeFromRequest(HttpServletRequest request, String codeString)
	{

		Integer code = null;

		Object objectCode = request.getAttribute(codeString);
		if (objectCode != null)
		{
			if (objectCode instanceof String)
				code = new Integer((String) objectCode);
			else if (objectCode instanceof Integer)
				code = (Integer) objectCode;
		}
		else
		{
			String thisCodeString = request.getParameter(codeString);
			if (thisCodeString != null)
				code = new Integer(thisCodeString);
		}
		return code;
	}

	private Calendar string2Date(String date)
	{
		String[] dateTokens = date.split("/");
		Calendar result = Calendar.getInstance();
		result.set(Calendar.DAY_OF_MONTH, (new Integer(dateTokens[0])).intValue());
		result.set(Calendar.MONTH, (new Integer(dateTokens[1])).intValue() - 1);
		result.set(Calendar.YEAR, (new Integer(dateTokens[2])).intValue());
		return result;
	}

	private Calendar string2Hour(String hour)
	{
		if (hour.equals(""))
			return null;
		String[] hourTokens = hour.split(":");
		Calendar result = Calendar.getInstance();
		result.set(Calendar.HOUR_OF_DAY, (new Integer(hourTokens[0])).intValue());
		result.set(Calendar.MINUTE, (new Integer(hourTokens[1])).intValue());
		result.set(Calendar.SECOND, new Integer(0).intValue());
		return result;
	}

	private boolean compareDates(HttpServletRequest request)
	{
		CalendarDateComparator dateComparator = new CalendarDateComparator();
		CalendarHourComparator hourComparator = new CalendarHourComparator();
		String testBeginDate = request.getParameter("beginDateFormatted");
		String testBeginHour = request.getParameter("beginHourFormatted");
		String testEndDate = request.getParameter("endDateFormatted");
		String testEndHour = request.getParameter("endHourFormatted");

		Calendar beginDate = string2Date(testBeginDate);
		Calendar beginHour = string2Hour(testBeginHour);
		Calendar endDate = string2Date(testEndDate);
		Calendar endHour = string2Hour(testEndHour);
		if (dateComparator.compare(beginDate, endDate) > 0)
		{
			error(request, "InvalidTime", "errors.lesson.invalid.time.interval");
			return false;
		}
		if (dateComparator.compare(beginDate, endDate) == 0)
		{
			if (hourComparator.compare(beginHour, endHour) >= 0)
			{
				error(request, "InvalidTime", "errors.lesson.invalid.time.interval");
				return false;
			}
		}
		return true;
	}

	private String createDefaultDistributedTestInfo(SiteView siteView)
	{
		Integer numberOfQuestions =
			((InfoSiteTest) siteView.getComponent()).getInfoTest().getNumberOfQuestions();
		String title = ((InfoSiteTest) siteView.getComponent()).getInfoTest().getTitle();
		return new String(
			"A '"
				+ title
				+ "' é constituida por "
				+ numberOfQuestions
				+ " pergunta(s). Uma pergunta certa vale a cotação indicada. Uma pergunta errada desconta 1/(número de opções-1) da cotação da pergunta.");

	}

}
