/*
 * Created on 27/Ago/2003
 */

package net.sourceforge.fenixedu.presentationTier.Action.student.onlineTests;

import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.NotAuthorizedFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.tests.NotAuthorizedStudentToDoTestException;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarDateComparator;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarHourComparator;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteStudentTestFeedback;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
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

    public ActionForward viewStudentExecutionCoursesWithTests(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        final IUserView userView = getUserView(request);
        List studentExecutionCoursesList = null;
        try {
            final Collection<Registration> students = userView.getPerson().getStudentsSet();
            final Object[] args = { students.isEmpty() ? null : students.iterator().next() };
            studentExecutionCoursesList = (List) ServiceUtils.executeService(userView,
                    "ReadExecutionCoursesByStudentTests", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("studentExecutionCoursesList", studentExecutionCoursesList);
        return mapping.findForward("viewStudentExecutionCoursesWithTests");
    }

    public ActionForward testsFirstPage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer objectCode = new Integer(request.getParameter("objectCode"));
        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(objectCode);

        Registration registration = Registration.readByUsername(userView.getUtilizador());
        Set<DistributedTest> distributedTestList = registration
                .getDistributedTestsByExecutionCourse(executionCourse);
        List<DistributedTest> testToDoList = new ArrayList<DistributedTest>();
        List<DistributedTest> doneTestsList = new ArrayList<DistributedTest>();
        for (DistributedTest distributedTest : distributedTestList) {
            if (testsToDo(distributedTest)) {
                testToDoList.add(distributedTest);
            } else if (doneTests(distributedTest)) {
                doneTestsList.add(distributedTest);
            }
        }
        request.setAttribute("testToDoList", testToDoList);
        request.setAttribute("doneTestsList", doneTestsList);
        request.setAttribute("objectCode", objectCode);
        return mapping.findForward("testsFirstPage");
    }

    private boolean testsToDo(DistributedTest distributedTest) {
        Calendar calendar = Calendar.getInstance();
        CalendarDateComparator dateComparator = new CalendarDateComparator();
        CalendarHourComparator hourComparator = new CalendarHourComparator();

        if (dateComparator.compare(calendar, distributedTest.getBeginDate()) >= 0) {
            if (dateComparator.compare(calendar, distributedTest.getBeginDate()) == 0)
                if (hourComparator.compare(calendar, distributedTest.getBeginHour()) < 0)
                    return false;
            if (dateComparator.compare(calendar, distributedTest.getEndDate()) <= 0) {
                if (dateComparator.compare(calendar, distributedTest.getEndDate()) == 0) {
                    if (hourComparator.compare(calendar, distributedTest.getEndHour()) <= 0) {
                        return true;
                    }
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    private boolean doneTests(DistributedTest distributedTest) {
        Calendar calendar = Calendar.getInstance();
        CalendarDateComparator dateComparator = new CalendarDateComparator();
        CalendarHourComparator hourComparator = new CalendarHourComparator();
        if (dateComparator.compare(calendar, distributedTest.getEndDate()) <= 0) {
            if (dateComparator.compare(calendar, distributedTest.getEndDate()) == 0) {
                if (hourComparator.compare(calendar, distributedTest.getEndHour()) <= 0) {
                    return false;
                }

                return true;
            }
            return false;
        }
        return true;
    }

    public ActionForward prepareToDoTest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);

        Integer testCode = null;
        try {
            testCode = new Integer(request.getParameter("testCode"));
        } catch (NumberFormatException e) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        }
        final DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(testCode);
        if (distributedTest == null) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        }

        final Registration registration = Registration.readByUsername(userView.getUtilizador());

        List<StudentTestQuestion> studentTestQuestionList = null;
        try {
            studentTestQuestionList = (List<StudentTestQuestion>) ServiceUtils.executeService(userView,
                    "ReadStudentTestToDo", new Object[] { registration, distributedTest,
                            new Boolean(true), getServlet().getServletContext().getRealPath("/") });
        } catch (NotAuthorizedFilterException e) {
            request.setAttribute("cantDoTest", new Boolean(true));
            return mapping.findForward("testError");
        } catch (InvalidArgumentsServiceException e) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Collections.sort(studentTestQuestionList, StudentTestQuestion.COMPARATOR_BY_TEST_QUESTION_ORDER);
        request.setAttribute("studentTestQuestionList", studentTestQuestionList);
        for (int i = 0; i < studentTestQuestionList.size(); i++) {
            StudentTestQuestion studentTestQuestion = (StudentTestQuestion) studentTestQuestionList
                    .get(i);

            if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.STR) {
                ResponseSTR responseSTR = new ResponseSTR();
                responseSTR.setResponsed(false);
                if (studentTestQuestion.getResponse() != null
                        && ((ResponseSTR) studentTestQuestion.getResponse()).getResponse() != null) {
                    responseSTR = ((ResponseSTR) studentTestQuestion.getResponse());
                }
                request.setAttribute("question" + i, responseSTR.getResponse());
            } else if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.NUM) {
                ResponseNUM responseNUM = new ResponseNUM();
                responseNUM.setResponsed(false);
                if (studentTestQuestion.getResponse() != null
                        && ((ResponseNUM) studentTestQuestion.getResponse()).getResponse() != null) {
                    responseNUM = (ResponseNUM) studentTestQuestion.getResponse();
                }
                request.setAttribute("question" + i, responseNUM.getResponse());
            } else if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.LID) {
                ResponseLID responseLID = new ResponseLID();
                if (studentTestQuestion.getResponse() != null
                        && ((ResponseLID) studentTestQuestion.getResponse()).getResponse() != null) {
                    responseLID = (ResponseLID) studentTestQuestion.getResponse();
                    if (studentTestQuestion.getSubQuestionByItem().getQuestionType()
                            .getCardinalityType().getType().intValue() == CardinalityType.SINGLE) {
                        if (((ResponseLID) studentTestQuestion.getResponse()).getResponse().length != 0) {
                            request.setAttribute("question" + i, responseLID.getResponse()[0]);
                        }
                    } else {
                        request.setAttribute("question" + i, responseLID.getResponse());
                    }
                } else {
                    request.setAttribute("question" + i, null);
                }

            }
        }

        request.setAttribute("studentTestForm", form);
        request.setAttribute("date", getDate());
        return mapping.findForward("doTest");
    }

    public ActionForward showImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        final IUserView userView = getUserView(request);

        final String testCode = request.getParameter("testCode");
        final String exerciseIdString = request.getParameter("exerciseCode");
        final String imgCodeString = request.getParameter("imgCode");
        final String imgTypeString = request.getParameter("imgType");
        String feedbackId = request.getParameter("feedbackCode");
        final Integer itemIndex = new Integer(request.getParameter("item"));
        final String path = getServlet().getServletContext().getRealPath("/");

        final DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(new Integer(
                testCode));
        if (distributedTest == null) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        }
        final Registration registration = Registration.readByUsername(userView.getUtilizador());

        String img = null;
        try {
            if (feedbackId == null) {
                feedbackId = "";
            }
            Object[] args = { registration.getIdInternal(), distributedTest.getIdInternal(),
                    new Integer(exerciseIdString), new Integer(imgCodeString), feedbackId, itemIndex,
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
            String imageName = new String("image"
                    + exerciseIdString
                    + imgCodeString
                    + "."
                    + imgTypeString
                            .substring(imgTypeString.lastIndexOf("/") + 1, imgTypeString.length()));
            response.setHeader("Content-disposition", "attachment; filename=" + imageName);
            OutputStream os = response.getOutputStream();
            os.write(imageData, 0, imageData.length);
            response.flushBuffer();
        } catch (java.io.IOException e) {
            throw new FenixActionException(e);
        }
        return null;
    }

    public ActionForward doTest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        final IUserView userView = getUserView(request);
        final String objectCode = request.getParameter("objectCode");
        final Integer studentCode = new Integer(request.getParameter("studentCode"));
        final String path = getServlet().getServletContext().getRealPath("/");

        Integer testCode = null;
        try {
            testCode = new Integer(request.getParameter("testCode"));
        } catch (NumberFormatException e) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        }

        final Registration registration = Registration.readByUsername(userView.getUtilizador());

        List studentTestQuestionList;
        try {
            studentTestQuestionList = (List) ServiceUtils.executeService(userView,
                    "ReadStudentTestToDo", new Object[] { registration, testCode, new Boolean(false),
                            path });
        } catch (NotAuthorizedException e) {
            request.setAttribute("cantDoTest", new Boolean(true));
            return mapping.findForward("testError");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Response[] userResponse = new Response[studentTestQuestionList.size()];
        for (int i = 0; i < studentTestQuestionList.size(); i++) {
            StudentTestQuestion studentTestQuestion = (StudentTestQuestion) studentTestQuestionList
                    .get(i);
            int order = studentTestQuestion.getTestQuestionOrder().intValue() - 1;
            if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.STR) {
                String responseOp = request.getParameter(new String("question" + order));
                ResponseSTR responseSTR = null;
                if (responseOp != null && responseOp.length() != 0) {
                    responseSTR = new ResponseSTR(responseOp);
                } else {
                    responseSTR = new ResponseSTR();
                    responseSTR.setResponsed(false);
                }
                userResponse[order] = responseSTR;
            } else if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.NUM) {
                String responseOp = request.getParameter(new String("question" + order));
                ResponseNUM responseNUM = null;
                if (responseOp != null && responseOp.length() != 0) {
                    responseNUM = new ResponseNUM(responseOp);
                } else {
                    responseNUM = new ResponseNUM();
                    responseNUM.setResponsed(false);
                }
                userResponse[order] = responseNUM;
            } else if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.LID) {
                String[] responseOp = request.getParameterValues(new String("question" + order));
                ResponseLID responseLID = null;
                if (responseOp != null && responseOp.length != 0) {
                    responseLID = new ResponseLID(responseOp);
                } else {
                    responseLID = new ResponseLID();
                    responseLID.setResponsed(false);
                }
                userResponse[order] = responseLID;
            }
        }

        InfoSiteStudentTestFeedback infoSiteStudentTestFeedback;
        List<StudentTestQuestion> infoStudentTestQuestionList;
        try {
            infoSiteStudentTestFeedback = (InfoSiteStudentTestFeedback) ServiceUtils.executeService(
                    userView, "InsertStudentTestResponses", new Object[] { registration, studentCode,
                            testCode, userResponse, path });
            infoStudentTestQuestionList = (List) ServiceUtils.executeService(userView,
                    "ReadStudentTestToDo", new Object[] { registration, testCode, new Boolean(false),
                            path });
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
        Collections.sort(infoStudentTestQuestionList,
                StudentTestQuestion.COMPARATOR_BY_TEST_QUESTION_ORDER);
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("testCode", testCode);

        if (infoSiteStudentTestFeedback != null) {
            for (int i = 0; i < infoStudentTestQuestionList.size(); i++) {
                StudentTestQuestion studentTestQuestion = (StudentTestQuestion) infoStudentTestQuestionList
                        .get(i);

                if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.STR) {
                    if ((ResponseSTR) studentTestQuestion.getResponse() != null) {
                        request.setAttribute("question" + i, ((ResponseSTR) studentTestQuestion
                                .getResponse()).getResponse());
                    } else {
                        request.setAttribute("question" + i, "");
                    }
                } else if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType()
                        .intValue() == QuestionType.NUM) {
                    if ((ResponseNUM) studentTestQuestion.getResponse() != null) {
                        request.setAttribute("question" + i, ((ResponseNUM) studentTestQuestion
                                .getResponse()).getResponse());
                    } else {
                        request.setAttribute("question" + i, "");
                    }
                } else if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType()
                        .intValue() == QuestionType.LID) {
                    if ((ResponseLID) studentTestQuestion.getResponse() != null) {
                        if (studentTestQuestion.getSubQuestionByItem().getQuestionType()
                                .getCardinalityType().getType().intValue() == CardinalityType.SINGLE) {
                            request.setAttribute("question" + i, ((ResponseLID) studentTestQuestion
                                    .getResponse()).getResponse()[0]);
                        } else {
                            request.setAttribute("question" + i, ((ResponseLID) studentTestQuestion
                                    .getResponse()).getResponse());
                        }
                    } else {
                        request.setAttribute("question" + i, "");
                    }
                }

            }

            infoSiteStudentTestFeedback.setStudentTestQuestionList(infoStudentTestQuestionList);
            request.setAttribute("infoSiteStudentTestFeedback", infoSiteStudentTestFeedback);
        }

        return mapping.findForward("studentFeedback");
    }

    public ActionForward prepareToGiveUp(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        request.setAttribute("testCode", request.getParameter("testCode"));
        request.setAttribute("exerciseCode", request.getParameter("exerciseCode"));
        request.setAttribute("item", request.getParameter("item"));
        request.setAttribute("objectCode", request.getParameter("objectCode"));
        return mapping.findForward("giveUpQuestion");
    }

    public ActionForward giveUp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        request.setAttribute("exerciseCode", request.getParameter("exerciseCode"));
        request.setAttribute("item", request.getParameter("item"));
        final IUserView userView = getUserView(request);

        Integer testCode = null;
        Integer exerciseCode = null;
        Integer itemCode = null;
        try {
            testCode = new Integer(request.getParameter("testCode"));
            exerciseCode = new Integer(request.getParameter("exerciseCode"));
            itemCode = new Integer(request.getParameter("item"));
        } catch (NumberFormatException e) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        }
        DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(testCode);
        if (distributedTest == null) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        }

        Registration registration = Registration.readByUsername(userView.getUtilizador());

        List<StudentTestQuestion> studentTestQuestionList = null;
        try {
            studentTestQuestionList = (List<StudentTestQuestion>) ServiceUtils.executeService(userView,
                    "GiveUpQuestion", new Object[] { registration, distributedTest, exerciseCode,
                            itemCode, getServlet().getServletContext().getRealPath("/") });
        } catch (NotAuthorizedFilterException e) {
            request.setAttribute("cantDoTest", new Boolean(true));
            return mapping.findForward("testError");
        } catch (InvalidArgumentsServiceException e) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        return prepareToDoTest(mapping, form, request, response);
    }

    public ActionForward showTestCorrection(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        final String path = getServlet().getServletContext().getRealPath("/");

        Integer testCode = null;
        try {
            testCode = new Integer(request.getParameter("testCode"));
        } catch (NumberFormatException e) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        }
        final DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(new Integer(
                testCode));
        if (distributedTest == null) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        }
        final Registration registration = Registration.readByUsername(userView.getUtilizador());

        List<StudentTestQuestion> studentTestQuestionList = null;
        try {
            studentTestQuestionList = (List<StudentTestQuestion>) ServiceUtils.executeService(userView,
                    "ReadStudentTestForCorrection", new Object[] { registration, distributedTest,
                            new Boolean(false), path });
        } catch (InvalidArgumentsServiceException e) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        } catch (NotAuthorizedFilterException e) {
            request.setAttribute("cantShowTestCorrection", new Boolean(true));
            return mapping.findForward("testError");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Collections.sort(studentTestQuestionList, StudentTestQuestion.COMPARATOR_BY_TEST_QUESTION_ORDER);
        request.setAttribute("studentTestQuestionList", studentTestQuestionList);

        Double classification = new Double(0);
        for (int i = 0; i < studentTestQuestionList.size(); i++) {
            StudentTestQuestion studentTestQuestion = (StudentTestQuestion) studentTestQuestionList
                    .get(i);
            if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.STR) {
                ResponseSTR responseSTR = new ResponseSTR();
                responseSTR.setResponsed(false);
                if (studentTestQuestion.getResponse() != null
                        && ((ResponseSTR) studentTestQuestion.getResponse()).getResponse() != null) {
                    responseSTR = ((ResponseSTR) studentTestQuestion.getResponse());
                }
                request.setAttribute("question" + i, responseSTR.getResponse());
            } else if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.NUM) {
                ResponseNUM responseNUM = new ResponseNUM();
                responseNUM.setResponsed(false);
                if (studentTestQuestion.getResponse() != null
                        && ((ResponseNUM) studentTestQuestion.getResponse()).getResponse() != null) {
                    responseNUM = (ResponseNUM) studentTestQuestion.getResponse();
                }
                request.setAttribute("question" + i, responseNUM.getResponse());
            } else if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.LID) {
                ResponseLID responseLID = new ResponseLID();
                if (studentTestQuestion.getResponse() != null
                        && ((ResponseLID) studentTestQuestion.getResponse()).getResponse() != null) {
                    responseLID = (ResponseLID) studentTestQuestion.getResponse();
                    if (studentTestQuestion.getSubQuestionByItem().getQuestionType()
                            .getCardinalityType().getType().intValue() == CardinalityType.SINGLE) {
                        if (((ResponseLID) studentTestQuestion.getResponse()).getResponse().length != 0) {
                            request.setAttribute("question" + i, responseLID.getResponse()[0]);
                        }
                    } else {
                        request.setAttribute("question" + i, responseLID.getResponse());
                    }
                } else {
                    request.setAttribute("question" + i, null);
                }
            }

            if (studentTestQuestion.getTestQuestionMark() != null)
                classification = new Double(classification.doubleValue()
                        + studentTestQuestion.getTestQuestionMark().doubleValue());
        }
        final DecimalFormat df = new DecimalFormat("#0.##");
        if (classification.doubleValue() < 0)
            classification = new Double(0);
        request.setAttribute("classification", df.format(classification));
        return mapping.findForward("showTestCorrection");
    }

    private String getDate() {
        String result = new String();
        final Calendar calendar = Calendar.getInstance();
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