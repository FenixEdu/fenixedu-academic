/*
 * Created on 27/Ago/2003
 */

package net.sourceforge.fenixedu.presentationTier.Action.student.onlineTests;

import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.tests.NotAuthorizedStudentToDoTestException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteStudentDistributedTests;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteStudentTestFeedback;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.tests.CardinalityType;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.Response;
import net.sourceforge.fenixedu.util.tests.ResponseLID;
import net.sourceforge.fenixedu.util.tests.ResponseNUM;
import net.sourceforge.fenixedu.util.tests.ResponseSTR;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.util.Base64;

/**
 * @author Susana Fernandes
 */
public class StudentTestsAction extends FenixDispatchAction {

    public ActionForward viewStudentExecutionCoursesWithTests(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        List studentExecutionCoursesList = null;
        try {
        	final Collection<Registration> students = userView.getPerson().getStudentsSet();
        	final Object[] args = { students.isEmpty() ? null : students.iterator().next() };
            studentExecutionCoursesList = (List) ServiceUtils.executeService(userView, "ReadExecutionCoursesByStudentTests", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("studentExecutionCoursesList", studentExecutionCoursesList);
        return mapping.findForward("viewStudentExecutionCoursesWithTests");
    }

    public ActionForward testsFirstPage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        InfoSiteStudentDistributedTests infoSiteStudentDistributedTests = null;
        String objectCode = request.getParameter("objectCode");
        try {
            infoSiteStudentDistributedTests = (InfoSiteStudentDistributedTests) ServiceUtils.executeService(userView, "ReadStudentTests",
                    new Object[] { userView.getUtilizador(), new Integer(objectCode) });
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("infoSiteStudentDistributedTests", infoSiteStudentDistributedTests);
        request.setAttribute("objectCode", objectCode);
        return mapping.findForward("testsFirstPage");
    }

    public ActionForward prepareToDoTest(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession();
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        Integer testCode = null;

        try {
            testCode = new Integer(request.getParameter("testCode"));
        } catch (NumberFormatException e) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        }

        List infoStudentTestQuestionList = null;
        String path = getServlet().getServletContext().getRealPath("/");
        try {
            infoStudentTestQuestionList = (List) ServiceUtils.executeService(userView, "ReadStudentTestToDo", new Object[] {
                    userView.getUtilizador(), testCode, new Boolean(true), path });
        } catch (NotAuthorizedFilterException e) {
            request.setAttribute("cantDoTest", new Boolean(true));
            return mapping.findForward("testError");
        } catch (InvalidArgumentsServiceException e) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Collections.sort(infoStudentTestQuestionList);
        request.setAttribute("infoStudentTestQuestionList", infoStudentTestQuestionList);
        for (int i = 0; i < infoStudentTestQuestionList.size(); i++) {
            InfoStudentTestQuestion infoStudentTestQuestion = (InfoStudentTestQuestion) infoStudentTestQuestionList.get(i);

            if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.STR) {
                request.setAttribute("question" + i, ((ResponseSTR) infoStudentTestQuestion.getResponse()).getResponse());
            } else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.NUM) {
                request.setAttribute("question" + i, ((ResponseNUM) infoStudentTestQuestion.getResponse()).getResponse());
            } else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.LID
                    && ((ResponseLID) infoStudentTestQuestion.getResponse()).getResponse() != null) {
                if (infoStudentTestQuestion.getQuestion().getQuestionType().getCardinalityType().getType().intValue() == CardinalityType.SINGLE) {
                    if (((ResponseLID) infoStudentTestQuestion.getResponse()).getResponse().length != 0) {
                        request.setAttribute("question" + i, ((ResponseLID) infoStudentTestQuestion.getResponse()).getResponse()[0]);
                    }
                } else {
                    request.setAttribute("question" + i, ((ResponseLID) infoStudentTestQuestion.getResponse()).getResponse());
                }
            }
        }

        request.setAttribute("studentTestForm", form);
        request.setAttribute("date", getDate());
        return mapping.findForward("doTest");
    }

    public ActionForward showImage(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        String testCode = request.getParameter("testCode");
        String exerciseIdString = request.getParameter("exerciseCode");
        String imgCodeString = request.getParameter("imgCode");
        String imgTypeString = request.getParameter("imgType");
        String feedbackId = request.getParameter("feedbackCode");
        String path = getServlet().getServletContext().getRealPath("/");
        String img = null;
        try {
            Object[] args = { userView.getUtilizador(), new Integer(testCode), new Integer(exerciseIdString), new Integer(imgCodeString), feedbackId,
                    path };
            img = (String) ServiceUtils.executeService(userView, "ReadStudentTestQuestionImage", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        byte[] imageData = Base64.decode(img.getBytes());
        try {
            response.reset();
            response.setContentType(imgTypeString);
            response.setContentLength(imageData.length);
            response.setBufferSize(imageData.length);
            String imageName = new String("image" + exerciseIdString + imgCodeString + "."
                    + imgTypeString.substring(imgTypeString.lastIndexOf("/") + 1, imgTypeString.length()));
            response.setHeader("Content-disposition", "attachment; filename=" + imageName);
            OutputStream os = response.getOutputStream();
            os.write(imageData, 0, imageData.length);
            response.flushBuffer();
        } catch (java.io.IOException e) {
            throw new FenixActionException(e);
        }
        return null;
    }

    public ActionForward doTest(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);
        Integer testCode = null;
        String objectCode = request.getParameter("objectCode");
        Integer studentCode = new Integer(request.getParameter("studentCode"));

        try {
            testCode = new Integer(request.getParameter("testCode"));
        } catch (NumberFormatException e) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        }

        String path = getServlet().getServletContext().getRealPath("/");

        List infoStudentTestQuestionList;

        try {
            infoStudentTestQuestionList = (List) ServiceUtils.executeService(userView, "ReadStudentTestToDo", new Object[] {
                    userView.getUtilizador(), testCode, new Boolean(false), path });
        } catch (NotAuthorizedException e) {
            request.setAttribute("cantDoTest", new Boolean(true));
            return mapping.findForward("testError");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Response[] userResponse = new Response[infoStudentTestQuestionList.size()];
        for (int i = 0; i < infoStudentTestQuestionList.size(); i++) {
            InfoStudentTestQuestion infoStudentTestQuestion = (InfoStudentTestQuestion) infoStudentTestQuestionList.get(i);
            int order = infoStudentTestQuestion.getTestQuestionOrder().intValue() - 1;
            if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.STR)
                userResponse[order] = new ResponseSTR(request.getParameter(new String("question" + order)));
            else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.NUM)
                userResponse[order] = new ResponseNUM(request.getParameter(new String("question" + order)));
            else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.LID)
                userResponse[order] = new ResponseLID(request.getParameterValues(new String("question" + order)));
        }

        InfoSiteStudentTestFeedback infoSiteStudentTestFeedback;
        try {
            infoSiteStudentTestFeedback = (InfoSiteStudentTestFeedback) ServiceUtils.executeService(userView, "InsertStudentTestResponses",
                    new Object[] { userView.getUtilizador(), studentCode, testCode, userResponse, path });
            infoStudentTestQuestionList = (List) ServiceUtils.executeService(userView, "ReadStudentTestToDo", new Object[] {
                    userView.getUtilizador(), testCode, new Boolean(false), path });
        } catch (NotAuthorizedException e) {
            request.setAttribute("cantDoTest", new Boolean(true));
            return mapping.findForward("testError");
        } catch (NotAuthorizedStudentToDoTestException e) {
            ActionErrors actionErrors = new ActionErrors();
            actionErrors.add("error.invalid.session", new ActionError("error.invalid.session"));
            saveErrors(request, actionErrors);
            return mapping.findForward("logoff");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        Collections.sort(infoStudentTestQuestionList);
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("testCode", testCode);

        if (infoSiteStudentTestFeedback != null) {
            for (int i = 0; i < infoStudentTestQuestionList.size(); i++) {
                InfoStudentTestQuestion infoStudentTestQuestion = (InfoStudentTestQuestion) infoStudentTestQuestionList.get(i);

                if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.STR
                        && ((ResponseSTR) infoStudentTestQuestion.getResponse()).getResponse() != null)
                    request.setAttribute("question" + i, ((ResponseSTR) infoStudentTestQuestion.getResponse()).getResponse());
                else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.NUM
                        && ((ResponseNUM) infoStudentTestQuestion.getResponse()).getResponse() != null)
                    request.setAttribute("question" + i, ((ResponseNUM) infoStudentTestQuestion.getResponse()).getResponse());
                else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.LID
                        && ((ResponseLID) infoStudentTestQuestion.getResponse()).getResponse() != null)
                    if (infoStudentTestQuestion.getQuestion().getQuestionType().getCardinalityType().getType().intValue() == CardinalityType.SINGLE)
                        request.setAttribute("question" + i, ((ResponseLID) infoStudentTestQuestion.getResponse()).getResponse()[0]);
                    else
                        request.setAttribute("question" + i, ((ResponseLID) infoStudentTestQuestion.getResponse()).getResponse());

            }

            request.setAttribute("infoStudentTestQuestionList", infoStudentTestQuestionList);
            request.setAttribute("infoSiteStudentTestFeedback", infoSiteStudentTestFeedback);
        }

        return mapping.findForward("studentFeedback");
    }

    public ActionForward showTestCorrection(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException, FenixFilterException {

        HttpSession session = request.getSession(false);
        IUserView userView = (IUserView) session.getAttribute(SessionConstants.U_VIEW);

        Integer testCode = null;
        try {
            testCode = new Integer(request.getParameter("testCode"));
        } catch (NumberFormatException e) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        }
        String path = getServlet().getServletContext().getRealPath("/");
        List infoStudentTestQuestionList = null;
        try {
            infoStudentTestQuestionList = (List) ServiceUtils.executeService(userView, "ReadStudentTestForCorrection", new Object[] {
                    userView.getUtilizador(), testCode, new Boolean(false), path });
        } catch (InvalidArgumentsServiceException e) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        } catch (NotAuthorizedFilterException e) {
            request.setAttribute("cantShowTestCorrection", new Boolean(true));
            return mapping.findForward("testError");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Collections.sort(infoStudentTestQuestionList);
        request.setAttribute("infoStudentTestQuestionList", infoStudentTestQuestionList);

        Double classification = new Double(0);
        for (int i = 0; i < infoStudentTestQuestionList.size(); i++) {
            InfoStudentTestQuestion infoStudentTestQuestion = (InfoStudentTestQuestion) infoStudentTestQuestionList.get(i);

            if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.STR
                    && ((ResponseSTR) infoStudentTestQuestion.getResponse()).getResponse() != null)
                request.setAttribute("question" + i, ((ResponseSTR) infoStudentTestQuestion.getResponse()).getResponse());
            else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.NUM
                    && ((ResponseNUM) infoStudentTestQuestion.getResponse()).getResponse() != null)
                request.setAttribute("question" + i, ((ResponseNUM) infoStudentTestQuestion.getResponse()).getResponse());
            else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.LID
                    && ((ResponseLID) infoStudentTestQuestion.getResponse()).getResponse() != null)
                if (infoStudentTestQuestion.getQuestion().getQuestionType().getCardinalityType().getType().intValue() == CardinalityType.SINGLE) {
                    if (((ResponseLID) infoStudentTestQuestion.getResponse()).getResponse().length != 0) {
                        request.setAttribute("question" + i, ((ResponseLID) infoStudentTestQuestion.getResponse()).getResponse()[0]);
                    }
                } else {
                    request.setAttribute("question" + i, ((ResponseLID) infoStudentTestQuestion.getResponse()).getResponse());
                }

            if (infoStudentTestQuestion.getTestQuestionMark() != null)
                classification = new Double(classification.doubleValue() + infoStudentTestQuestion.getTestQuestionMark().doubleValue());
        }
        DecimalFormat df = new DecimalFormat("#0.##");
        if (classification.doubleValue() < 0)
            classification = new Double(0);
        request.setAttribute("classification", df.format(classification));
        return mapping.findForward("showTestCorrection");
    }

    private String getDate() {
        String result = new String();
        Calendar calendar = Calendar.getInstance();
        result += calendar.get(Calendar.DAY_OF_MONTH);
        result += "/";
        result += calendar.get(Calendar.MONTH) + 1;
        result += "/";
        result += calendar.get(Calendar.YEAR);
        result += " ";
        result += calendar.get(Calendar.HOUR_OF_DAY);
        result += ":";
        if (calendar.get(Calendar.MINUTE) < 10)
            result += "0";
        result += calendar.get(Calendar.MINUTE);
        result += ":";
        if (calendar.get(Calendar.SECOND) < 10)
            result += "0";
        result += calendar.get(Calendar.SECOND);
        return result;
    }
}