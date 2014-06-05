/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * Created on 27/Ago/2003
 */

package net.sourceforge.fenixedu.presentationTier.Action.student.onlineTests;

import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.tests.NotAuthorizedStudentToDoTestException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests.CleanSubQuestions;
import net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests.GiveUpQuestion;
import net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests.InsertStudentTestResponses;
import net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests.ReadExecutionCoursesByStudentTests;
import net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests.ReadStudentTest;
import net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests.ReadStudentTestQuestionImage;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarDateComparator;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarHourComparator;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteStudentTestFeedback;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.RegistrationDistributedTests;
import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationSelectExecutionYearBean;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.student.StudentApplication.StudentSubmitApp;
import net.sourceforge.fenixedu.util.report.ReportsUtils;
import net.sourceforge.fenixedu.util.tests.CardinalityType;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.Response;
import net.sourceforge.fenixedu.util.tests.ResponseLID;
import net.sourceforge.fenixedu.util.tests.ResponseNUM;
import net.sourceforge.fenixedu.util.tests.ResponseSTR;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.portal.EntryPoint;
import org.fenixedu.bennu.portal.StrutsFunctionality;

import pt.ist.fenixWebFramework.struts.annotations.Forward;
import pt.ist.fenixWebFramework.struts.annotations.Forwards;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.io.BaseEncoding;

/**
 * @author Susana Fernandes
 */
@StrutsFunctionality(app = StudentSubmitApp.class, path = "tests", titleKey = "link.tests")
@Mapping(module = "student", path = "/studentTests", input = "/studentTests.do?method=prepareToDoTest",
        formBean = "studentTestForm", validate = false)
@Forwards({
        @Forward(name = "testsFirstPage", path = "/student/onlineTests/testsFirstPage_bd.jsp"),
        @Forward(name = "viewStudentExecutionCoursesWithTests",
                path = "/student/onlineTests/viewStudentExecutionCoursesWithTests_bd.jsp"),
        @Forward(name = "testError", path = "/student/onlineTests/testError_bd.jsp"),
        @Forward(name = "studentFeedback", path = "/student/onlineTests/showStudentTestFeedback_bd.jsp"),
        @Forward(name = "showTestCorrection", path = "/student/onlineTests/showTestCorrection_bd.jsp"),
        @Forward(name = "doTest", path = "/student/onlineTests/doTest_bd.jsp"),
        @Forward(name = "giveUpQuestion", path = "/student/onlineTests/giveUpQuestion.jsp") })
public class StudentTestsAction extends FenixDispatchAction {

    @EntryPoint
    public ActionForward viewStudentExecutionCoursesWithTests(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final User userView = getUserView(request);
        final Student student = userView.getPerson().getStudent();
        RegistrationSelectExecutionYearBean registrationSelectExecutionYearBean =
                new RegistrationSelectExecutionYearBean(student.getRegistrationsSet().iterator().next());
        ExecutionYear executionYear = getRenderedObject();

        if (executionYear == null) {
            executionYear = ExecutionYear.readCurrentExecutionYear();
        }
        registrationSelectExecutionYearBean.setExecutionYear(executionYear);
        request.setAttribute("registrationSelectExecutionYearBean", registrationSelectExecutionYearBean);
        Set<ExecutionCourse> studentExecutionCoursesList = ReadExecutionCoursesByStudentTests.run(student, executionYear);
        request.setAttribute("studentExecutionCoursesList", studentExecutionCoursesList);

        return mapping.findForward("viewStudentExecutionCoursesWithTests");
    }

    public ActionForward testsFirstPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final User userView = getUserView(request);
        final String objectCode = request.getParameter("objectCode");
        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(objectCode);

        final Student student = userView.getPerson().getStudent();
        Map<Registration, Set<DistributedTest>> distributedTestList =
                student.getDistributedTestsByExecutionCourse(executionCourse);

        List<RegistrationDistributedTests> tests = new ArrayList<RegistrationDistributedTests>();
        for (Registration registration : distributedTestList.keySet()) {
            List<DistributedTest> testToDoList = new ArrayList<DistributedTest>();
            List<DistributedTest> doneTestsList = new ArrayList<DistributedTest>();

            Set<DistributedTest> distributedTests = distributedTestList.get(registration);
            for (DistributedTest distributedTest : distributedTests) {
                if (testsToDo(distributedTest) && registration.isActive()) {
                    testToDoList.add(distributedTest);
                } else if (doneTests(distributedTest)) {
                    doneTestsList.add(distributedTest);
                }
            }
            tests.add(new RegistrationDistributedTests(registration, testToDoList, doneTestsList));
        }
        request.setAttribute("tests", tests);
        request.setAttribute("objectCode", objectCode);
        return mapping.findForward("testsFirstPage");
    }

    private boolean testsToDo(DistributedTest distributedTest) {
        Calendar calendar = Calendar.getInstance();
        CalendarDateComparator dateComparator = new CalendarDateComparator();
        CalendarHourComparator hourComparator = new CalendarHourComparator();

        if (dateComparator.compare(calendar, distributedTest.getBeginDate()) >= 0) {
            if (dateComparator.compare(calendar, distributedTest.getBeginDate()) == 0) {
                if (hourComparator.compare(calendar, distributedTest.getBeginHour()) < 0) {
                    return false;
                }
            }
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

    public ActionForward prepareToDoTest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        String testCode = request.getParameter("testCode");
        request.setAttribute("date", getDate());
        final DistributedTest distributedTest = FenixFramework.getDomainObject(testCode);
        if (distributedTest == null) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        }

        final Registration registration = getRegistration(request);
        if (registration == null) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        }

        List<StudentTestQuestion> studentTestQuestionList = null;
        try {
            studentTestQuestionList = ReadStudentTest.runReadStudentTestToDo(registration, distributedTest, new Boolean(true));
        } catch (NotAuthorizedException e) {
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
            StudentTestQuestion studentTestQuestion = studentTestQuestionList.get(i);

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
                    if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getCardinalityType().getType().intValue() == CardinalityType.SINGLE) {
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
        return mapping.findForward("doTest");
    }

    public ActionForward showImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String testCode = request.getParameter("testCode");
        final String exerciseId = request.getParameter("exerciseCode");
        final Integer imgCode = getRequestParameterAsInteger(request, "imgCode");
        final String imgTypeString = request.getParameter("imgType");
        final Integer feedbackId = getRequestParameterAsInteger(request, "feedbackCode");
        final Integer itemIndex = getRequestParameterAsInteger(request, "item");

        final DistributedTest distributedTest = FenixFramework.getDomainObject(testCode);
        if (distributedTest == null) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        }
        final Registration registration = getRegistration(request);
        if (registration == null) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        }
        String img = null;
        try {
            img =
                    ReadStudentTestQuestionImage.run(registration.getExternalId(), distributedTest.getExternalId(), exerciseId,
                            imgCode, feedbackId, itemIndex);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        byte[] imageData = BaseEncoding.base64().decode(img);
        try {
            response.reset();
            response.setContentType(imgTypeString);
            response.setContentLength(imageData.length);
            response.setBufferSize(imageData.length);
            StringBuilder imageName = new StringBuilder();
            imageName.append("image").append(exerciseId).append(imgCode);
            if (feedbackId != null) {
                imageName.append("_").append(feedbackId);
            }
            imageName.append(".").append(imgTypeString.substring(imgTypeString.lastIndexOf("/") + 1, imgTypeString.length()));
            response.setHeader("Content-disposition", "attachment; filename=" + imageName.toString());
            OutputStream os = response.getOutputStream();
            os.write(imageData, 0, imageData.length);
            response.flushBuffer();
        } catch (java.io.IOException e) {
            throw new FenixActionException(e);
        }
        return null;
    }

    protected Integer getRequestParameterAsInteger(HttpServletRequest request, String parameterName) {
        final String requestParameter = request.getParameter(parameterName);

        if (!StringUtils.isEmpty(requestParameter)) {
            return Integer.valueOf(requestParameter);
        } else {
            return null;
        }
    }

    public ActionForward doTest(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        final String objectCode = request.getParameter("objectCode");
        final Integer studentCode = new Integer(request.getParameter("studentCode"));

        request.setAttribute("date", getDate());

        String testCode = request.getParameter("testCode");

        final DistributedTest distributedTest = FenixFramework.getDomainObject(testCode);
        if (distributedTest == null) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        }
        request.setAttribute("distributedTest", distributedTest);

        final Registration registration = getRegistration(request);
        if (registration == null) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        }

        List<StudentTestQuestion> studentTestQuestionList;
        try {
            studentTestQuestionList = ReadStudentTest.runReadStudentTestToDo(registration, testCode, new Boolean(false));
        } catch (NotAuthorizedException e) {
            request.setAttribute("cantDoTest", new Boolean(true));
            return mapping.findForward("testError");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Response[] userResponse = new Response[studentTestQuestionList.size()];
        for (int i = 0; i < studentTestQuestionList.size(); i++) {
            StudentTestQuestion studentTestQuestion = studentTestQuestionList.get(i);
            int order = studentTestQuestion.getTestQuestionOrder().intValue() - 1;
            if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.STR) {
                String responseOp = request.getParameter("question" + order);
                ResponseSTR responseSTR = null;
                if (responseOp != null && responseOp.length() != 0) {
                    responseSTR = new ResponseSTR(responseOp);
                } else {
                    responseSTR = new ResponseSTR();
                    responseSTR.setResponsed(false);
                }
                userResponse[order] = responseSTR;
            } else if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.NUM) {
                String responseOp = request.getParameter("question" + order);
                ResponseNUM responseNUM = null;
                if (responseOp != null && responseOp.length() != 0) {
                    responseNUM = new ResponseNUM(responseOp);
                } else {
                    responseNUM = new ResponseNUM();
                    responseNUM.setResponsed(false);
                }
                userResponse[order] = responseNUM;
            } else if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.LID) {
                String[] responseOp = request.getParameterValues("question" + order);
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
            infoSiteStudentTestFeedback = InsertStudentTestResponses.run(registration, studentCode, testCode, userResponse);
            infoStudentTestQuestionList = ReadStudentTest.runReadStudentTestToDo(registration, testCode, new Boolean(false));
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
        Collections.sort(infoStudentTestQuestionList, StudentTestQuestion.COMPARATOR_BY_TEST_QUESTION_ORDER);
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("testCode", testCode);

        if (infoSiteStudentTestFeedback != null) {
            for (int i = 0; i < infoStudentTestQuestionList.size(); i++) {
                StudentTestQuestion studentTestQuestion = infoStudentTestQuestionList.get(i);

                if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.STR) {
                    if ((ResponseSTR) studentTestQuestion.getResponse() != null) {
                        request.setAttribute("question" + i, ((ResponseSTR) studentTestQuestion.getResponse()).getResponse());
                    } else {
                        request.setAttribute("question" + i, "");
                    }
                } else if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.NUM) {
                    if ((ResponseNUM) studentTestQuestion.getResponse() != null) {
                        request.setAttribute("question" + i, ((ResponseNUM) studentTestQuestion.getResponse()).getResponse());
                    } else {
                        request.setAttribute("question" + i, "");
                    }
                } else if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getType().intValue() == QuestionType.LID) {
                    if ((ResponseLID) studentTestQuestion.getResponse() != null) {
                        if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getCardinalityType().getType()
                                .intValue() == CardinalityType.SINGLE) {
                            request.setAttribute("question" + i,
                                    ((ResponseLID) studentTestQuestion.getResponse()).getResponse()[0]);
                        } else {
                            request.setAttribute("question" + i, ((ResponseLID) studentTestQuestion.getResponse()).getResponse());
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

    public ActionForward exportChecksum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        final String logId = request.getParameter("logId");
        final User userView = getUserView(request);
        if (logId != null && logId.length() != 0) {
            StudentTestLog studentTestLog = FenixFramework.getDomainObject(logId);
            if (studentTestLog.getStudent().getPerson().equals(userView.getPerson())) {
                List<StudentTestLog> studentTestLogs = new ArrayList<StudentTestLog>();
                studentTestLogs.add(studentTestLog);
                byte[] data =
                        ReportsUtils.exportToPdfFileAsByteArray(
                                "net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog.checksumReport", null,
                                studentTestLogs);
                response.setContentType("application/pdf");
                response.addHeader("Content-Disposition", "attachment; filename=" + studentTestLog.getStudent().getNumber()
                        + ".pdf");
                response.setContentLength(data.length);
                ServletOutputStream writer = response.getOutputStream();
                writer.write(data);
                writer.flush();
                writer.close();
                response.flushBuffer();
                return mapping.findForward("");
            }
        }
        return null;
    }

    public ActionForward prepareToGiveUp(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        request.setAttribute("testCode", request.getParameter("testCode"));
        request.setAttribute("exerciseCode", request.getParameter("exerciseCode"));
        request.setAttribute("item", request.getParameter("item"));
        request.setAttribute("objectCode", request.getParameter("objectCode"));
        return mapping.findForward("giveUpQuestion");
    }

    public ActionForward giveUp(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        request.setAttribute("exerciseCode", request.getParameter("exerciseCode"));
        request.setAttribute("item", request.getParameter("item"));
        final User userView = getUserView(request);

        String testCode = null;
        String exerciseCode = null;
        Integer itemCode = null;
        try {
            testCode = request.getParameter("testCode");
            exerciseCode = request.getParameter("exerciseCode");
            itemCode = new Integer(request.getParameter("item"));
        } catch (NumberFormatException e) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        }
        DistributedTest distributedTest = FenixFramework.getDomainObject(testCode);
        if (distributedTest == null) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        }

        Registration registration = Registration.readByUsername(userView.getUsername());
        try {
            GiveUpQuestion.run(registration, distributedTest, exerciseCode, itemCode);
        } catch (IllegalDataAccessException e) {
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

    public ActionForward cleanSubQuestions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        request.setAttribute("exerciseCode", request.getParameter("exerciseCode"));
        request.setAttribute("item", request.getParameter("item"));

        String testCode = null;
        String exerciseCode = null;
        Integer itemCode = null;
        try {
            testCode = request.getParameter("testCode");
            exerciseCode = request.getParameter("exerciseCode");
            itemCode = new Integer(request.getParameter("item"));
        } catch (NumberFormatException e) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        }
        DistributedTest distributedTest = FenixFramework.getDomainObject(testCode);
        if (distributedTest == null) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        }

        final Registration registration = getRegistration(request);
        if (registration == null) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        }

        try {
            CleanSubQuestions.run(registration, distributedTest, exerciseCode, itemCode);
        } catch (IllegalDataAccessException e) {
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

    public ActionForward showTestCorrection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {

        String testCode = request.getParameter("testCode");
        final DistributedTest distributedTest = FenixFramework.getDomainObject(testCode);
        if (distributedTest == null) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        }
        final Registration registration = getRegistration(request);
        if (registration == null) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        }

        List<StudentTestQuestion> studentTestQuestionList = null;
        try {
            studentTestQuestionList =
                    ReadStudentTest.runReadStudentTestForCorrection(registration, distributedTest, new Boolean(false));
        } catch (InvalidArgumentsServiceException e) {
            request.setAttribute("invalidTest", new Boolean(true));
            return mapping.findForward("testError");
        } catch (NotAuthorizedException e) {
            request.setAttribute("cantShowTestCorrection", new Boolean(true));
            return mapping.findForward("testError");
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Collections.sort(studentTestQuestionList, StudentTestQuestion.COMPARATOR_BY_TEST_QUESTION_ORDER);
        request.setAttribute("studentTestQuestionList", studentTestQuestionList);

        Double classification = new Double(0);
        for (int i = 0; i < studentTestQuestionList.size(); i++) {
            StudentTestQuestion studentTestQuestion = studentTestQuestionList.get(i);
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
                    if (studentTestQuestion.getSubQuestionByItem().getQuestionType().getCardinalityType().getType().intValue() == CardinalityType.SINGLE) {
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

            if (studentTestQuestion.getTestQuestionMark() != null) {
                classification =
                        new Double(classification.doubleValue() + studentTestQuestion.getTestQuestionMark().doubleValue());
            }
        }
        final DecimalFormat df = new DecimalFormat("#0.##");
        if (classification.doubleValue() < 0) {
            classification = new Double(0);
        }
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
        if (calendar.get(Calendar.MINUTE) < 10) {
            result += "0";
        }
        result += calendar.get(Calendar.MINUTE);
        result += ":";
        if (calendar.get(Calendar.SECOND) < 10) {
            result += "0";
        }
        result += calendar.get(Calendar.SECOND);
        return result;
    }

    private Registration getRegistration(HttpServletRequest request) {
        String registrationCode = request.getParameter("student");

        final Registration registration = FenixFramework.getDomainObject(registrationCode);
        if (registration == null) {
            return null;
        }

        Person person = Person.readPersonByUsername(getUserView(request).getUsername());
        if (!person.getStudent().equals(registration.getStudent())) {
            return null;
        }
        return registration;
    }
}