/*
 * Created on 27/Ago/2003
 *
 */
package ServidorApresentacao.Action.student;

import java.io.OutputStream;
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
import org.dbunit.util.Base64;

import DataBeans.InfoSiteDistributedTests;
import DataBeans.InfoStudentTestQuestion;
import ServidorAplicacao.IUserView;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorApresentacao.Action.base.FenixDispatchAction;
import ServidorApresentacao.Action.exceptions.FenixActionException;
import ServidorApresentacao.Action.sop.utils.ServiceUtils;
import ServidorApresentacao.Action.sop.utils.SessionConstants;
import Util.CorrectionAvailability;

/**
 * @author Susana Fernandes
 */
public class StudentTestsAction extends FenixDispatchAction {

	public ActionForward testsFirstPage(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {
		request.setAttribute("objectCode", request.getParameter("objectCode"));
		return mapping.findForward("testsFirstPage");
	}

	public ActionForward viewDoneTests(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession();
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoSiteDistributedTests infoSiteDistributedTests = null;
		String objectCode = request.getParameter("objectCode");

		try {
			infoSiteDistributedTests =
				(InfoSiteDistributedTests) ServiceUtils.executeService(
					userView,
					"ReadStudentDoneTests",
					new Object[] {
						userView.getUtilizador(),
						new Integer(objectCode)});
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		request.setAttribute(
			"infoSiteDistributedTests",
			infoSiteDistributedTests);

		request.setAttribute("objectCode", objectCode);
		return mapping.findForward("viewDoneTests");
	}

	public ActionForward viewTestsToDo(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession();
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);

		InfoSiteDistributedTests infoSiteDistributedTests = null;
		String objectCode = request.getParameter("objectCode");

		try {
			infoSiteDistributedTests =
				(InfoSiteDistributedTests) ServiceUtils.executeService(
					userView,
					"ReadStudentTestsToDo",
					new Object[] {
						userView.getUtilizador(),
						new Integer(objectCode)});
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		request.setAttribute(
			"infoSiteDistributedTests",
			infoSiteDistributedTests);

		request.setAttribute("objectCode", objectCode);

		return mapping.findForward("viewTestsToDo");
	}

	public ActionForward prepareToDoTest(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession();
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);

		String testCode = request.getParameter("testCode");
		List infoStudentTestQuestionList = null;
		try {
			infoStudentTestQuestionList =
				(List) ServiceUtils.executeService(
					userView,
					"ReadStudentTest",
					new Object[] {
						userView.getUtilizador(),
						new Integer(testCode)});
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
			option[infoStudentTestQuestion.getTestQuestionOrder().intValue()- 1]=infoStudentTestQuestion.getResponse().toString();
		}

		((DynaActionForm) form).set("option", option);
		return mapping.findForward("doTest");
	}

	public ActionForward chooseAction(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		String button = request.getParameter("button");

		if (button.equals("Voltar"))
			return testsFirstPage(mapping, form, request, response);
		else if (button.equals("Submeter Ficha"))
			return doTest(mapping, form, request, response);
		return viewTestsToDo(mapping, form, request, response);
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

		String testCode = request.getParameter("testCode");
		String exerciceIdString = request.getParameter("exerciceCode");
		String imgCodeString = request.getParameter("imgCode");
		String imgTypeString = request.getParameter("imgType");

		String img = null;
		try {
			Object[] args =
				{
					userView.getUtilizador(),
					new Integer(testCode),
					new Integer(exerciceIdString),
					new Integer(imgCodeString)};
			img =
				(String) ServiceUtils.executeService(
					userView,
					"ReadStudentTestQuestionImage",
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

	public ActionForward doTest(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);

		String[] options= (String[]) ((DynaActionForm)form).get("option");
		String testCode = request.getParameter("testCode");
		String objectCode = request.getParameter("objectCode");

		List infoStudentTestQuestionList;
		Boolean sent = new Boolean(false);
		try {
			sent =
				(Boolean) ServiceUtils.executeService(
					userView,
					"InsertStudentTestResponses",
					new Object[] {
						userView.getUtilizador(),
						new Integer(testCode),
						options });
			infoStudentTestQuestionList =
				(List) ServiceUtils.executeService(
					userView,
					"ReadStudentTest",
					new Object[] {
						userView.getUtilizador(),
						new Integer(testCode)});
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}
		Collections.sort(infoStudentTestQuestionList);
		request.setAttribute("objectCode", objectCode);
		request.setAttribute("sent", sent);

		if (sent.booleanValue()) {
			Iterator it = infoStudentTestQuestionList.iterator();
			int correctResponseNumber = 0;
			int incorrectResponseNumber = 0;
			int responseNumber = 0;
			Integer numberOfQuestions = new Integer(0);
			Boolean studentFeedback = new Boolean(false);
			CorrectionAvailability correction = null;
			
			while (it.hasNext()) {

				InfoStudentTestQuestion infoStudentTestQuestion = (InfoStudentTestQuestion) it.next();
				correction = infoStudentTestQuestion.getDistributedTest().getCorrectionAvailability();
				studentFeedback = infoStudentTestQuestion.getDistributedTest().getStudentFeedback();
				if (studentFeedback.booleanValue()) {
					numberOfQuestions =	infoStudentTestQuestion.getDistributedTest().getInfoTest().getNumberOfQuestions();
					studentFeedback = infoStudentTestQuestion.getDistributedTest().getStudentFeedback();
					if (infoStudentTestQuestion.getResponse().intValue() != 0) {
						responseNumber++;
						if (infoStudentTestQuestion.getQuestion().getCorrectResponse().contains(infoStudentTestQuestion.getResponse()))
								correctResponseNumber++;
						else
							incorrectResponseNumber++;
					}
				} else
					break;
			}
			request.setAttribute("studentFeedback", studentFeedback);
			if (studentFeedback.booleanValue()) {
				request.setAttribute(
					"correctResponseNumber",
					new Integer(correctResponseNumber));
				request.setAttribute(
					"incorrectResponseNumber",
					new Integer(incorrectResponseNumber));
				request.setAttribute(
					"responseNumber",
					new Integer(responseNumber));
				request.setAttribute(
					"notResponseNumber",
					new Integer(numberOfQuestions.intValue() - responseNumber));
			}
			if (correction.getTypeString().equals("Sempre"))
				request.setAttribute("infoStudentTestQuestionList", infoStudentTestQuestionList);
		}

		return mapping.findForward("studentFeedback");
	}

	public ActionForward showTestCorrection(
		ActionMapping mapping,
		ActionForm form,
		HttpServletRequest request,
		HttpServletResponse response)
		throws FenixActionException {

		HttpSession session = request.getSession(false);
		IUserView userView =
			(IUserView) session.getAttribute(SessionConstants.U_VIEW);

		String testCode = request.getParameter("testCode");
		List infoStudentTestQuestionList = null;
		try {
			infoStudentTestQuestionList =
				(List) ServiceUtils.executeService(
					userView,
					"ReadStudentTest",
					new Object[] {
						userView.getUtilizador(),
						new Integer(testCode)});
		} catch (FenixServiceException e) {
			throw new FenixActionException(e);
		}

		Collections.sort(infoStudentTestQuestionList);
		request.setAttribute("infoStudentTestQuestionList",infoStudentTestQuestionList);

		int numQuestions =((InfoStudentTestQuestion) infoStudentTestQuestionList.get(0)).getDistributedTest().getInfoTest().getNumberOfQuestions().intValue();

		Iterator it = infoStudentTestQuestionList.iterator();
		String[] option = new String[numQuestions];
		while (it.hasNext()) {
			InfoStudentTestQuestion infoStudentTestQuestion = (InfoStudentTestQuestion) it.next();
			option[infoStudentTestQuestion.getTestQuestionOrder().intValue()- 1] =	infoStudentTestQuestion.getResponse().toString();
		}
		((DynaActionForm) form).set("option", option);
		return mapping.findForward("showTestCorrection");
	}

}