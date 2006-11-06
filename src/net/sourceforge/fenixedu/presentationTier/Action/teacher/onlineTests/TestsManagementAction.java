/*
 * Created on 14/Ago/2003
 */

package net.sourceforge.fenixedu.presentationTier.Action.teacher.onlineTests;

import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.applicationTier.Filtro.exception.FenixFilterException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarDateComparator;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarHourComparator;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteDistributedTestAdvisory;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteStudentTestFeedback;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteStudentsTestMarks;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.presentationTier.Action.base.FenixDispatchAction;
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.tests.CardinalityType;
import net.sourceforge.fenixedu.util.tests.CorrectionAvailability;
import net.sourceforge.fenixedu.util.tests.CorrectionFormula;
import net.sourceforge.fenixedu.util.tests.QuestionType;
import net.sourceforge.fenixedu.util.tests.Response;
import net.sourceforge.fenixedu.util.tests.ResponseLID;
import net.sourceforge.fenixedu.util.tests.ResponseNUM;
import net.sourceforge.fenixedu.util.tests.ResponseSTR;
import net.sourceforge.fenixedu.util.tests.TestQuestionChangesType;
import net.sourceforge.fenixedu.util.tests.TestQuestionStudentsChangesType;
import net.sourceforge.fenixedu.util.tests.TestType;
import net.sourceforge.fenixedu.utilTests.ParseQuestionException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.DynaActionForm;
import org.apache.util.Base64;

import com.sun.faces.el.impl.parser.ParseException;

/**
 * @author Susana Fernandes
 */
public class TestsManagementAction extends FenixDispatchAction {

    public ActionForward testsFirstPage(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("objectCode", getCodeFromRequest(request, "objectCode"));
        return mapping.findForward("testsFirstPage");
    }

    public ActionForward prepareCreateTest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        final ExecutionCourse executionCourse = rootDomainObject
                .readExecutionCourseByOID(executionCourseId);
        final Integer availableMetadatas = executionCourse.findVisibleMetadata().size();
        request.setAttribute("availableMetadatas", availableMetadatas);
        request.setAttribute("objectCode", executionCourseId);
        return mapping.findForward("createTest");
    }

    public ActionForward createTest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        final Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        final IUserView userView = getUserView(request);
        final String title = request.getParameter("title");
        final String information = request.getParameter("information");
        final Object[] args = { executionCourseId, title, information };
        Integer testCode = null;
        try {
            testCode = (Integer) ServiceUtils.executeService(userView, "InsertTest", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("testCode", testCode);
        request.setAttribute("objectCode", executionCourseId);
        return showAvailableQuestions(mapping, form, request, response);
    }

    public ActionForward editAsNewTest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        final IUserView userView = getUserView(request);
        final Integer testCode = getCodeFromRequest(request, "testCode");
        final Object[] args = { executionCourseId, testCode };
        Integer newTestCode = null;
        try {
            newTestCode = (Integer) ServiceUtils.executeService(userView, "InsertTestAsNewTest", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("testCode", newTestCode);
        return editTest(mapping, form, request, response);
    }

    public ActionForward showAvailableQuestions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        final Integer testCode = getCodeFromRequest(request, "testCode");
        Test test = null;
        try {
            test = (Test) ServiceUtils.executeService(userView, "ReadTest", new Object[] {
                    executionCourseId, testCode });
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        final ExecutionCourse executionCourse = rootDomainObject
                .readExecutionCourseByOID(executionCourseId);
        Set<Metadata> metadataList = Metadata.findVisibleMetadataFromExecutionCourseNotOfTest(
                executionCourse, test);
        final String asc = request.getParameter("asc");
        String order = request.getParameter("order");
        if (order != null) {
            // MetadataComparator metadataComparator = new
            // MetadataComparator(order, asc);
            // Collections.sort(infoMetadataList, metadataComparator);
        } else {
            order = new String();
        }
        request.setAttribute("metadataList", metadataList);
        request.setAttribute("objectCode", executionCourseId);
        request.setAttribute("testCode", testCode);
        request.setAttribute("asc", asc);
        request.setAttribute("order", order);
        return mapping.findForward("showAvailableQuestions");
    }

    public ActionForward prepareInsertTestQuestion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        final Integer testCode = getCodeFromRequest(request, "testCode");
        final Integer metadataCode = getCodeFromRequest(request, "metadataCode");
        Integer exerciseCode = getCodeFromRequest(request, "exerciseCode");
        if (exerciseCode == null)
            exerciseCode = new Integer(-1);
        final String path = getServlet().getServletContext().getRealPath("/");
        Test test = null;
        try {
            test = (Test) ServiceUtils.executeService(userView, "ReadTest", new Object[] {
                    executionCourseId, testCode });
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        final Metadata metadata = rootDomainObject.readMetadataByOID(metadataCode);
        Question question = null;
        if (exerciseCode == null || exerciseCode.equals(new Integer(-1))) {
            question = metadata.getVisibleQuestions().get(0);
        } else {
            for (Question visibleQuestion : metadata.getVisibleQuestions()) {
                if (visibleQuestion.getIdInternal().equals(exerciseCode)) {
                    question = visibleQuestion;
                    break;
                }
            }
        }
        if (question == null) {
            throw new FenixActionException();
        }
        ParseSubQuestion parse = new ParseSubQuestion();
        try {
            question = parse.parseSubQuestion(question, path);
        } catch (ParseQuestionException e) {
            throw new FenixActionException();
        } catch (ParseException e) {
            throw new FenixActionException();
        }

        List<String> testQuestionNames = new ArrayList<String>();
        List<Integer> testQuestionValues = new ArrayList<Integer>();
        for (int i = 0; i < test.getTestQuestions().size(); i++) {
            testQuestionNames.add(new String("Pergunta " + (i + 1)));
            testQuestionValues.add(new Integer(i));
        }

        List formulas = CorrectionFormula.getFormulas();
        request.setAttribute("order", request.getParameter("order"));
        request.setAttribute("asc", request.getParameter("asc"));
        request.setAttribute("testQuestionNames", testQuestionNames);
        request.setAttribute("testQuestionValues", testQuestionValues);
        request.setAttribute("formulas", formulas);
        request.setAttribute("testCode", testCode);
        request.setAttribute("exerciseCode", exerciseCode);
        request.setAttribute("question", question);
        request.setAttribute("objectCode", executionCourseId);
        return mapping.findForward("insertTestQuestion");
    }

    public ActionForward insertTestQuestion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        final String[] metadataCodes = request.getParameterValues("metadataCode");
        final Integer questionOrder = getCodeFromRequest(request, "questionOrder");
        final Integer testCode = getCodeFromRequest(request, "testCode");
        Double questionValue = null;
        if (request.getParameter("questionValue") != null) {
            questionValue = new Double(request.getParameter("questionValue"));
        }
        final Integer formula = getCodeFromRequest(request, "formula");
        final String path = getServlet().getServletContext().getRealPath("/");

        CorrectionFormula correctionFormulas = new CorrectionFormula(CorrectionFormula.FENIX);
        if (formula != null) {
            correctionFormulas = new CorrectionFormula(formula);
        }
        Object[] arguments = { executionCourseId, testCode, metadataCodes, questionOrder, questionValue,
                correctionFormulas, path };
        try {
            ServiceUtils.executeService(userView, "InsertTestQuestion", arguments);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("order", request.getParameter("order"));
        request.setAttribute("asc", request.getParameter("asc"));
        request.setAttribute("testCode", testCode);
        return showAvailableQuestions(mapping, form, request, response);
    }

    public ActionForward prepareEditTestQuestion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        final Integer questionCode = getCodeFromRequest(request, "questionCode");
        final Integer testCode = getCodeFromRequest(request, "testCode");
        Test test = null;
        try {
            test = (Test) ServiceUtils.executeService(userView, "ReadTest", new Object[] {
                    executionCourseId, testCode });
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        final Question question = test.findQuestionByOID(questionCode);
        final TestQuestion testQuestion = test.getTestQuestion(question);
        ParseSubQuestion parse = new ParseSubQuestion();
        try {
            parse.parseSubQuestion(testQuestion.getQuestion(), getServlet().getServletContext()
                    .getRealPath("/"));
        } catch (ParseQuestionException e) {
            throw new FenixActionException();
        } catch (ParseException e) {
            throw new FenixActionException();
        }
        List<String> testQuestionNames = new ArrayList<String>();
        List<Integer> testQuestionValues = new ArrayList<Integer>();
        int questionOrder = testQuestion.getTestQuestionOrder().intValue();
        for (int i = 0; i < test.getTestQuestions().size(); i++) {
            if ((i + 1) != questionOrder && (i + 1) != questionOrder + 1) {
                testQuestionNames.add(new String("Pergunta " + (i + 1)));
                testQuestionValues.add(new Integer(i));
            }
        }
        List formulas = CorrectionFormula.getFormulas();
        ((DynaActionForm) form).set("formula", testQuestion.getCorrectionFormula().getFormula()
                .toString());
        request.setAttribute("formulas", formulas);
        request.setAttribute("testQuestionNames", testQuestionNames);
        request.setAttribute("testQuestionValues", testQuestionValues);
        request.setAttribute("testCode", testCode);
        request.setAttribute("questionCode", questionCode);
        request.setAttribute("objectCode", executionCourseId);
        request.setAttribute("testQuestion", testQuestion);
        return mapping.findForward("editTestQuestion");
    }

    public ActionForward editTestQuestion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer executionCourseCode = getCodeFromRequest(request, "objectCode");
        final Integer testCode = getCodeFromRequest(request, "testCode");
        final Integer testQuestionCode = getCodeFromRequest(request, "testQuestionCode");
        final Integer questionOrder = getCodeFromRequest(request, "testQuestionOrder");
        final Double questionValue = new Double(request.getParameter("testQuestionValue"));
        final Integer formula = getCodeFromRequest(request, "formula");
        CorrectionFormula correctionFormula = new CorrectionFormula(CorrectionFormula.FENIX);
        if (formula != null) {
            correctionFormula = new CorrectionFormula(formula);
        }
        Object[] arguments = { executionCourseCode, testQuestionCode, questionOrder, questionValue,
                correctionFormula };
        try {
            ServiceUtils.executeService(userView, "EditTestQuestion", arguments);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("testCode", testCode);
        return editTest(mapping, form, request, response);
    }

    public ActionForward deleteTestQuestion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer executionCourseCode = getCodeFromRequest(request, "objectCode");
        final Integer testCode = getCodeFromRequest(request, "testCode");
        final Integer questionCode = getCodeFromRequest(request, "questionCode");
        Object[] args = { executionCourseCode, testCode, questionCode };
        try {
            ServiceUtils.executeService(userView, "DeleteTestQuestion", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return editTest(mapping, form, request, response);
    }

    public ActionForward showTests(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        final Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        final TestScope testScope = TestScope.readByDomainObject(ExecutionCourse.class,
                executionCourseId);
        List<Test> testList = new ArrayList<Test>();
        if (testScope != null) {
            testList = testScope.getTests();
        }
        request.setAttribute("testList", testList);
        request.setAttribute("objectCode", executionCourseId);
        return mapping.findForward("showTests");
    }

    public ActionForward prepareDeleteTest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer testCode = getCodeFromRequest(request, "testCode");
        final Integer objectCode = getCodeFromRequest(request, "objectCode");
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("testCode", testCode);
        Test test = null;
        try {
            test = (Test) ServiceUtils.executeService(userView, "ReadTest", new Object[] { objectCode,
                    testCode });
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("title", test.getTitle());
        return mapping.findForward("prepareDeleteTest");
    }

    public ActionForward deleteTest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer executionCourseCode = getCodeFromRequest(request, "objectCode");
        final Integer testCode = getCodeFromRequest(request, "testCode");
        Object[] args = { executionCourseCode, testCode };
        try {
            ServiceUtils.executeService(userView, "DeleteTest", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("objectCode", executionCourseCode);
        return showTests(mapping, form, request, response);
    }

    public ActionForward showImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer exerciseCode = getCodeFromRequest(request, "exerciseCode");
        final Integer imgCode = getCodeFromRequest(request, "imgCode");
        final String imgTypeString = request.getParameter("imgType");
        final Integer studentCode = getCodeFromRequest(request, "studentCode");
        final String optionShuffle = request.getParameter("optionShuffle");
        final Integer testCode = getCodeFromRequest(request, "testCode");
        final Integer metadataCode = getCodeFromRequest(request, "metadataCode");
        final Integer itemIndex = getCodeFromRequest(request, "item");
        String feedbackCode = request.getParameter("feedbackCode");
        final String path = getServlet().getServletContext().getRealPath("/");
        String img = null;
        if (studentCode != null && testCode != null) {
            if (feedbackCode == null) {
                feedbackCode = "";
            }
            Object[] args = { studentCode, testCode, exerciseCode, imgCode, feedbackCode, itemIndex,
                    path };
            try {
                img = (String) ServiceUtils.executeService(userView, "ReadStudentTestQuestionImage",
                        args);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
        } else if (optionShuffle != null) {
            Object[] args = { testCode, exerciseCode, optionShuffle, imgCode, path };
            try {
                img = (String) ServiceUtils.executeService(userView, "ReadQuestionImage", args);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
        } else {
            Object[] args = { exerciseCode, metadataCode, imgCode, itemIndex, path };
            try {
                img = (String) ServiceUtils.executeService(userView, "ReadQuestionImage", args);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
        }
        byte[] imageData = Base64.decode(img.getBytes());
        try {
            response.reset();
            response.setContentType(imgTypeString);
            response.setContentLength(imageData.length);
            response.setBufferSize(imageData.length);
            String imageName = new String("image"
                    + exerciseCode
                    + imgCode
                    + "."
                    + imgTypeString
                            .substring(imgTypeString.lastIndexOf("/") + 1, imgTypeString.length()));
            response.setHeader("Content-disposition", "attachment; filename=" + imageName);
            DataOutputStream dataOut = new DataOutputStream(response.getOutputStream());
            dataOut.write(imageData);
            response.flushBuffer();
        } catch (java.io.IOException e) {
            throw new FenixActionException(e);
        }
        return null;
    }

    public ActionForward editTest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException, FenixFilterException {
        final Integer executionCourseCode = getCodeFromRequest(request, "objectCode");
        final Integer testCode = getCodeFromRequest(request, "testCode");
        final IUserView userView = getUserView(request);
        Test test = null;
        try {
            test = (Test) ServiceUtils.executeService(userView, "ReadTest", new Object[] {
                    executionCourseCode, testCode });
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        List<TestQuestion> testQuestionList = new ArrayList<TestQuestion>(test.getTestQuestions());
        for (TestQuestion testQuestion : testQuestionList) {
            ParseSubQuestion parse = new ParseSubQuestion();
            try {
                parse.parseSubQuestion(testQuestion.getQuestion(), getServlet().getServletContext()
                        .getRealPath("/").replace('\\', '/'));
            } catch (ParseQuestionException e) {
                throw new FenixActionException();
            } catch (ParseException e) {
                throw new FenixActionException();
            }
        }
        Collections.sort(testQuestionList, TestQuestion.COMPARATOR_BY_TEST_QUESTION_ORDER);
        request.setAttribute("test", test);
        request.setAttribute("testQuestionList", testQuestionList);
        request.setAttribute("testCode", testCode);
        request.setAttribute("objectCode", executionCourseCode);
        return mapping.findForward("editTest");
    }

    public ActionForward prepareEditTestHeader(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer executionCourseCode = getCodeFromRequest(request, "objectCode");
        final Integer testCode = getCodeFromRequest(request, "testCode");
        Test test = null;
        try {
            test = (Test) ServiceUtils.executeService(userView, "ReadTest", new Object[] {
                    executionCourseCode, testCode });
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("test", test);
        request.setAttribute("testCode", testCode);
        request.setAttribute("objectCode", executionCourseCode);
        return mapping.findForward("editTestHeader");
    }

    public ActionForward editTestHeader(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final Integer executionCourseCode = getCodeFromRequest(request, "objectCode");
        final Integer testCode = getCodeFromRequest(request, "testCode");
        final IUserView userView = getUserView(request);
        final String title = request.getParameter("title");
        final String information = request.getParameter("information");
        final Object[] args = { executionCourseCode, testCode, title, information };
        try {
            ServiceUtils.executeService(userView, "EditTest", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return editTest(mapping, form, request, response);
    }

    public ActionForward prepareDistributeTest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer objectCode = getCodeFromRequest(request, "objectCode");
        final Integer testCode = getCodeFromRequest(request, "testCode");

        Test test = null;
        try {
            test = (Test) ServiceUtils.executeService(userView, "ReadTest", new Object[] { objectCode,
                    testCode });
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("objectCode", objectCode);
        if (test.getTestQuestions().size() >= 1) {
            List testTypeList = (new TestType()).getAllTypes();
            request.setAttribute("testTypeList", testTypeList);
            List correctionAvailabilityList = (new CorrectionAvailability()).getAllAvailabilities();
            request.setAttribute("correctionAvailabilityList", correctionAvailabilityList);
            if ((((DynaActionForm) form).get("testType")).equals(""))
                ((DynaActionForm) form).set("testType", "1");
            if ((((DynaActionForm) form).get("availableCorrection")).equals(""))
                ((DynaActionForm) form).set("availableCorrection", "3");
            if ((((DynaActionForm) form).get("imsFeedback")).equals(""))
                ((DynaActionForm) form).set("imsFeedback", "true");
            if ((((DynaActionForm) form).get("testInformation")).equals("")) {
                ((DynaActionForm) form).set("testInformation", createDefaultDistributedTestInfo(test));
                ((DynaActionForm) form).set("notInquiryInformation", ((DynaActionForm) form)
                        .get("testInformation"));
            }
            if ((((DynaActionForm) form).get("inquiryInformation")).equals(""))
                ((DynaActionForm) form).set("inquiryInformation", createDefaultDistributedInquiryInfo());
            request.setAttribute("testCode", testCode);
            return mapping.findForward("distributeTest");
        }
        error(request, "InvalidDistribution", "error.distributeTest.noExercise");
        return showTests(mapping, form, request, response);

    }

    public ActionForward chooseDistributionFor(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        if (request.getParameter("shifts") != null) {
            if (!compareDates(request)) {
                return prepareDistributeTest(mapping, form, request, response);
            }
            return chooseShifts(mapping, form, request, response);
        } else if (request.getParameter("students") != null) {
            if (!compareDates(request)) {
                return prepareDistributeTest(mapping, form, request, response);
            }
            return chooseStudents(mapping, form, request, response);
        } else if (request.getParameter("addShifts") != null) {
            if (!compareDates(request)) {
                return prepareEditDistributedTest(mapping, form, request, response);
            }
            return chooseAddShifts(mapping, form, request, response);
        } else if (request.getParameter("addStudents") != null) {
            if (!compareDates(request)) {
                return prepareEditDistributedTest(mapping, form, request, response);
            }
            return chooseAddStudents(mapping, form, request, response);
        } else if (request.getParameter("save") != null) {
            if (!compareDates(request)) {
                return prepareEditDistributedTest(mapping, form, request, response);
            }
            return editDistributedTest(mapping, form, request, response);
        } else
            return showDistributedTests(mapping, form, request, response);
    }

    public ActionForward chooseShifts(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final Integer objectCode = getCodeFromRequest(request, "objectCode");
        final Integer testCode = getCodeFromRequest(request, "testCode");
        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(objectCode);
        if (executionCourse == null) {
            throw new FenixActionException();
        }
        final List<Shift> shiftList = executionCourse.getAssociatedShifts();
        // Collections.sort(shifts, new InfoShiftComparatorByLessonType());
        request.setAttribute("shiftList", shiftList);
        request.setAttribute("testCode", testCode);
        request.setAttribute("objectCode", objectCode);
        return mapping.findForward("distributeTestByShifts");
    }

    public ActionForward chooseStudents(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final Integer objectCode = getCodeFromRequest(request, "objectCode");
        final Integer testCode = getCodeFromRequest(request, "testCode");

        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(objectCode);
        final List<Registration> studentList = new ArrayList<Registration>();
        for (final Attends attends : executionCourse.getAttends()) {
            studentList.add(attends.getAluno());
        }
        Collections.sort(studentList, new BeanComparator("number"));
        request.setAttribute("studentList", studentList);
        request.setAttribute("testCode", testCode);
        request.setAttribute("objectCode", objectCode);
        return mapping.findForward("distributeTestByStudents");
    }

    public ActionForward distributeTest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer objectCode = getCodeFromRequest(request, "objectCode");
        final Integer testCode = getCodeFromRequest(request, "testCode");
        final String testInformation = request.getParameter("testInformation");
        final String testBeginDay = request.getParameter("beginDayFormatted");
        final String testBeginMonth = request.getParameter("beginMonthFormatted");
        final String testBeginYear = request.getParameter("beginYearFormatted");
        final String testBeginHour = request.getParameter("beginHourFormatted");
        final String testBeginMinute = request.getParameter("beginMinuteFormatted");
        final String testEndDay = request.getParameter("endDayFormatted");
        final String testEndMonth = request.getParameter("endMonthFormatted");
        final String testEndYear = request.getParameter("endYearFormatted");
        final String testEndHour = request.getParameter("endHourFormatted");
        final String testEndMinute = request.getParameter("endMinuteFormatted");
        final String testType = request.getParameter("testType");
        final String availableCorrection = request.getParameter("availableCorrection");
        final String imsFeedback = request.getParameter("imsFeedback");
        final Calendar beginDate = string2Date(testBeginDay, testBeginMonth, testBeginYear);
        final Calendar beginHour = string2Hour(testBeginHour, testBeginMinute);
        final Calendar endDate = string2Date(testEndDay, testEndMonth, testEndYear);
        final Calendar endHour = string2Hour(testEndHour, testEndMinute);
        final String[] selected = request.getParameterValues("selected");
        final String insertByShifts = request.getParameter("insertByShifts");

        TestType testTypeArg = new TestType(new Integer(testType));
        CorrectionAvailability correctionAvailabilityArg;
        Boolean imsFeedbackArg;
        if (testTypeArg.equals(new TestType(TestType.INQUIRY))) {
            correctionAvailabilityArg = new CorrectionAvailability(CorrectionAvailability.NEVER);
            imsFeedbackArg = new Boolean(false);
        } else {
            correctionAvailabilityArg = new CorrectionAvailability(new Integer(availableCorrection));
            imsFeedbackArg = new Boolean(imsFeedback);
        }

        List<InfoStudent> infoStudentList = null;
        Integer advisoryId = null;
        try {

            infoStudentList = (List) ServiceUtils.executeService(userView, "ReadStudentsByIdArray",
                    new Object[] { objectCode, selected, new Boolean(insertByShifts) });
            Object[] args = { objectCode, testCode, testInformation, beginDate, beginHour, endDate,
                    endHour, testTypeArg, correctionAvailabilityArg, imsFeedbackArg, infoStudentList,
                    getServlet().getServletContext().getRealPath("/") };
            advisoryId = (Integer) ServiceUtils.executeService(userView, "InsertDistributedTest", args);

        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (advisoryId == null) {
            request.setAttribute("successfulDistribution", new Boolean(false));
            return showDistributedTests(mapping, form, request, response);
        }

        for (int times = 0; times < 3; times++) {
            List<InfoStudent> studentWithoutAdvisory = new ArrayList<InfoStudent>();
            for (InfoStudent infoStudent : infoStudentList) {
                try {
                    Object[] args = { objectCode, advisoryId, infoStudent.getIdInternal() };
                    ServiceUtils.executeService(userView, "InsertStudentDistributedTestAdvisory", args);
                } catch (FenixServiceException e) {
                    studentWithoutAdvisory.add(infoStudent);
                }
            }
            infoStudentList = studentWithoutAdvisory;
            if (infoStudentList.size() == 0)
                break;
        }
        request.setAttribute("infoStudentList", infoStudentList);
        request.setAttribute("successfulDistribution", new Boolean(true));
        return showDistributedTests(mapping, form, request, response);
    }

    public ActionForward showDistributedTests(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final Integer objectCode = getCodeFromRequest(request, "objectCode");
        final TestScope testScope = TestScope.readByDomainObject(ExecutionCourse.class, objectCode);
        List<DistributedTest> distributedTestList = new ArrayList<DistributedTest>();
        if (testScope != null) {
            distributedTestList = testScope.getDistributedTests();
        }
        request.setAttribute("distributedTests", distributedTestList);
        request.setAttribute("objectCode", objectCode);
        return mapping.findForward("showDistributedTests");
    }

    public ActionForward prepareEditDistributedTest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final Integer objectCode = getCodeFromRequest(request, "objectCode");
        final Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
        final DistributedTest distributedTest = rootDomainObject
                .readDistributedTestByOID(distributedTestCode);
        if (distributedTest == null) {
            throw new FenixActionException();
        }
        final List testTypeList = (new TestType()).getAllTypes();
        request.setAttribute("testTypeList", testTypeList);
        final List correctionAvailabilityList = (new CorrectionAvailability()).getAllAvailabilities();
        request.setAttribute("correctionAvailabilityList", correctionAvailabilityList);
        ((DynaActionForm) form).set("distributedTestCode", distributedTest.getIdInternal());
        ((DynaActionForm) form).set("title", distributedTest.getTitle());
        if ((((DynaActionForm) form).get("testInformation")).equals(""))
            ((DynaActionForm) form).set("testInformation", distributedTest.getTestInformation());
        if ((((DynaActionForm) form).get("beginDayFormatted")).equals(""))
            ((DynaActionForm) form).set("beginDayFormatted", distributedTest.getBeginDayFormatted());
        if ((((DynaActionForm) form).get("beginMonthFormatted")).equals(""))
            ((DynaActionForm) form).set("beginMonthFormatted", distributedTest.getBeginMonthFormatted());
        if ((((DynaActionForm) form).get("beginYearFormatted")).equals(""))
            ((DynaActionForm) form).set("beginYearFormatted", distributedTest.getBeginYearFormatted());
        if ((((DynaActionForm) form).get("beginHourFormatted")).equals(""))
            ((DynaActionForm) form).set("beginHourFormatted", distributedTest.getBeginHourFormatted());
        if ((((DynaActionForm) form).get("beginMinuteFormatted")).equals(""))
            ((DynaActionForm) form).set("beginMinuteFormatted", distributedTest
                    .getBeginMinuteFormatted());
        if ((((DynaActionForm) form).get("endDayFormatted")).equals(""))
            ((DynaActionForm) form).set("endDayFormatted", distributedTest.getEndDayFormatted());
        if ((((DynaActionForm) form).get("endMonthFormatted")).equals(""))
            ((DynaActionForm) form).set("endMonthFormatted", distributedTest.getEndMonthFormatted());
        if ((((DynaActionForm) form).get("endYearFormatted")).equals(""))
            ((DynaActionForm) form).set("endYearFormatted", distributedTest.getEndYearFormatted());
        if ((((DynaActionForm) form).get("endHourFormatted")).equals(""))
            ((DynaActionForm) form).set("endHourFormatted", distributedTest.getEndHourFormatted());
        if ((((DynaActionForm) form).get("endMinuteFormatted")).equals(""))
            ((DynaActionForm) form).set("endMinuteFormatted", distributedTest.getEndMinuteFormatted());
        if ((((DynaActionForm) form).get("testType")).equals(""))
            ((DynaActionForm) form).set("testType", distributedTest.getTestType().getType().toString());
        if ((((DynaActionForm) form).get("availableCorrection")).equals(""))
            ((DynaActionForm) form).set("availableCorrection", (distributedTest
                    .getCorrectionAvailability().getAvailability().toString()));
        if ((((DynaActionForm) form).get("imsFeedback")).equals(""))
            ((DynaActionForm) form).set("imsFeedback", distributedTest.getImsFeedback().toString());
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("infoDistributedTest", distributedTest);
        return mapping.findForward("editDistributedTest");
    }

    public ActionForward chooseAddShifts(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final Integer objectCode = getCodeFromRequest(request, "objectCode");
        final Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");

        final DistributedTest distributedTest = rootDomainObject
                .readDistributedTestByOID(distributedTestCode);
        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(objectCode);
        if (executionCourse == null) {
            throw new FenixActionException();
        }
        final Set<Registration> students = distributedTest.findStudents();
        final List<Shift> associatedShifts = executionCourse.getAssociatedShifts();
        List<Shift> shiftList = new ArrayList<Shift>();
        for (Shift shift : associatedShifts) {
            List<Registration> shiftStudents = shift.getStudents();
            if (!students.containsAll(shiftStudents)) {
                shiftList.add(shift);
            }
        }
        // Collections.sort(shiftList, new InfoShiftComparatorByLessonType());
        request.setAttribute("shiftList", shiftList);
        request.setAttribute("distributedTestCode", distributedTestCode);
        request.setAttribute("objectCode", objectCode);
        return mapping.findForward("addShiftsToDistributedTest");
    }

    public ActionForward chooseAddStudents(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final Integer objectCode = getCodeFromRequest(request, "objectCode");
        final Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
        final List<Registration> studentList = new ArrayList<Registration>();
        final ExecutionCourse executionCourse = rootDomainObject.readExecutionCourseByOID(objectCode);
        final List<Attends> attendList = executionCourse.getAttends();
        final DistributedTest distributedTest = rootDomainObject
                .readDistributedTestByOID(distributedTestCode);
        final Set<Registration> students = distributedTest.findStudents();
        for (Attends attend : attendList) {
            if (!students.contains(attend.getAluno())) {
                studentList.add(attend.getAluno());
            }
        }

        Collections.sort(studentList, new BeanComparator("number"));
        request.setAttribute("studentList", studentList);
        request.setAttribute("distributedTestCode", distributedTestCode);
        request.setAttribute("objectCode", objectCode);
        return mapping.findForward("addStudentsToDistributedTest");
    }

    public ActionForward editDistributedTest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer objectCode = getCodeFromRequest(request, "objectCode");
        final Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
        final String testInformation = request.getParameter("testInformation");
        final String testBeginDay = request.getParameter("beginDayFormatted");
        final String testBeginMonth = request.getParameter("beginMonthFormatted");
        final String testBeginYear = request.getParameter("beginYearFormatted");
        final String testBeginHour = request.getParameter("beginHourFormatted");
        final String testBeginMinute = request.getParameter("beginMinuteFormatted");
        final String testEndDay = request.getParameter("endDayFormatted");
        final String testEndMonth = request.getParameter("endMonthFormatted");
        final String testEndYear = request.getParameter("endYearFormatted");
        final String testEndHour = request.getParameter("endHourFormatted");
        final String testEndMinute = request.getParameter("endMinuteFormatted");
        final String testType = request.getParameter("testType");
        final String availableCorrection = request.getParameter("availableCorrection");
        final String imsFeedback = request.getParameter("imsFeedback");
        final Calendar beginDate = string2Date(testBeginDay, testBeginMonth, testBeginYear);
        final Calendar beginHour = string2Hour(testBeginHour, testBeginMinute);
        final Calendar endDate = string2Date(testEndDay, testEndMonth, testEndYear);
        final Calendar endHour = string2Hour(testEndHour, testEndMinute);
        final String[] selected = request.getParameterValues("selected");
        final String insertByShifts = request.getParameter("insertByShifts");
        TestType testTypeArg = new TestType(new Integer(testType));
        CorrectionAvailability correctionAvailabilityArg;
        Boolean imsFeedbackArg;

        if (testTypeArg.equals(new TestType(TestType.INQUIRY))) {
            correctionAvailabilityArg = new CorrectionAvailability(CorrectionAvailability.NEVER);
            imsFeedbackArg = new Boolean(false);
        } else {
            correctionAvailabilityArg = new CorrectionAvailability(new Integer(availableCorrection));
            imsFeedbackArg = new Boolean(imsFeedback);
        }
        request.setAttribute("objectCode", objectCode);

        List<InfoStudent> infoStudentList = new ArrayList<InfoStudent>();
        Integer dataChangesAdvisory = null;

        try {
            Object[] args = { objectCode, distributedTestCode, testInformation, beginDate, beginHour,
                    endDate, endHour, testTypeArg, correctionAvailabilityArg, imsFeedbackArg,
                    request.getContextPath() };
            dataChangesAdvisory = (Integer) ServiceUtils.executeService(userView, "EditDistributedTest",
                    args);
        } catch (FenixServiceException e) {
            request.setAttribute("successfulEdition", new Boolean(false));
            return showDistributedTests(mapping, form, request, response);
        }

        request.setAttribute("successfulEdition", new Boolean(true));

        List<InfoStudent> infoOldStudentList = new ArrayList<InfoStudent>();
        if (dataChangesAdvisory != null) {
            try {
                infoOldStudentList = (List) ServiceUtils.executeService(userView,
                        "ReadStudentsWithDistributedTest", new Object[] { objectCode,
                                distributedTestCode });
            } catch (FenixServiceException e) {
                request.setAttribute("insuccessfulAdvisoryDistribution", new Boolean(true));
                return showDistributedTests(mapping, form, request, response);
            }
            for (int times = 0; times < 3; times++) {
                List<InfoStudent> studentWithoutAdvisory = new ArrayList<InfoStudent>();
                for (InfoStudent infoStudent : infoOldStudentList) {
                    try {
                        ServiceUtils.executeService(userView, "InsertStudentDistributedTestAdvisory",
                                new Object[] { objectCode, dataChangesAdvisory,
                                        infoStudent.getIdInternal() });
                    } catch (FenixServiceException e) {
                        studentWithoutAdvisory.add(infoStudent);
                    }
                }
                infoOldStudentList = studentWithoutAdvisory;
                if (infoOldStudentList.size() == 0)
                    break;
            }
            request.setAttribute("infoStudentList", infoOldStudentList);
        } else
            infoOldStudentList = new ArrayList<InfoStudent>();
        Integer addStudentsAdvisory = null;
        if (selected != null) {
            try {
                infoStudentList = (List) ServiceUtils.executeService(userView, "ReadStudentsByIdArray",
                        new Object[] { objectCode, selected, new Boolean(insertByShifts) });
                addStudentsAdvisory = (Integer) ServiceUtils.executeService(userView,
                        "AddStudentsToDistributedTest", new Object[] { objectCode, distributedTestCode,
                                infoStudentList, request.getContextPath() });
            } catch (FenixServiceException e) {
                request.setAttribute("successfulStudentAddition", new Boolean(false));
                return showDistributedTests(mapping, form, request, response);
            }

            if (addStudentsAdvisory != null) {

                for (int times = 0; times < 3; times++) {
                    List<InfoStudent> studentWithoutAdvisory = new ArrayList<InfoStudent>();
                    for (InfoStudent infoStudent : infoStudentList) {
                        try {
                            Object[] args = { objectCode, addStudentsAdvisory,
                                    infoStudent.getIdInternal() };
                            ServiceUtils.executeService(userView,
                                    "InsertStudentDistributedTestAdvisory", args);
                        } catch (FenixServiceException e) {
                            studentWithoutAdvisory.add(infoStudent);
                        }
                    }
                    infoStudentList = studentWithoutAdvisory;
                    if (infoStudentList.size() == 0)
                        break;
                }
            }
            infoOldStudentList.addAll(infoStudentList);
            request.setAttribute("infoStudentList", infoOldStudentList);
        }
        request.setAttribute("objectCode", objectCode);
        return showDistributedTests(mapping, form, request, response);
    }

    public ActionForward prepareDeleteDistributedTest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final Integer objectCode = getCodeFromRequest(request, "objectCode");
        final Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
        final DistributedTest distributedTest = rootDomainObject
                .readDistributedTestByOID(distributedTestCode);
        if (distributedTest == null) {
            throw new FenixActionException();
        }
        final Boolean canDelete = (!(distributedTest.getTestType().getType().intValue() == TestType.EVALUATION && distributedTest
                .countResponses(null, true) != 0));
        request.setAttribute("canDelete", canDelete);
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("distributedTestCode", distributedTestCode);
        return mapping.findForward("prepareDeleteDistributedTest");
    }

    public ActionForward deleteDistributedTest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer objectCode = getCodeFromRequest(request, "objectCode");
        final Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
        final Object[] args = { objectCode, distributedTestCode };
        Boolean successfulTestDeletion = null;
        try {
            successfulTestDeletion = (Boolean) ServiceUtils.executeService(userView,
                    "DeleteDistributedTest", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("successfulTestDeletion", successfulTestDeletion);
        return showDistributedTests(mapping, form, request, response);
    }

    public ActionForward showStudentTest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer objectCode = getCodeFromRequest(request, "objectCode");
        final Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
        final Integer studentCode = getCodeFromRequest(request, "studentCode");
        final String path = getServlet().getServletContext().getRealPath("/");
        List<StudentTestQuestion> studentTestQuestionList = new ArrayList<StudentTestQuestion>();
        try {
            final Object[] args = { objectCode, distributedTestCode, studentCode, path };
            studentTestQuestionList = (List<StudentTestQuestion>) ServiceUtils.executeService(userView,
                    "ReadStudentDistributedTest", args);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Collections.sort(studentTestQuestionList, StudentTestQuestion.COMPARATOR_BY_TEST_QUESTION_ORDER);
        request.setAttribute("studentTestQuestionList", studentTestQuestionList);
        double classification = 0;

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
            classification += studentTestQuestion.getTestQuestionMark().doubleValue();
        }
        request.setAttribute("studentTestForm", form);
        if (classification < 0) {
            classification = 0;
        }
        request.setAttribute("classification", (new DecimalFormat("#0.##").format(classification)));
        request.setAttribute("objectCode", objectCode);
        return mapping.findForward("showStudentTest");
    }

    public ActionForward showStudentTestLog(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final Integer objectCode = getCodeFromRequest(request, "objectCode");
        final Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
        final Integer studentCode = getCodeFromRequest(request, "studentCode");
        final Registration student = rootDomainObject.readRegistrationByOID(studentCode);
        if (student == null) {
            throw new FenixActionException();
        }
        final DistributedTest distributedTest = rootDomainObject
                .readDistributedTestByOID(distributedTestCode);
        if (distributedTest == null) {
            throw new FenixActionException();
        }

        List<StudentTestLog> studentTestLogList = distributedTest.getStudentTestLogs(student);
        request.setAttribute("questionNumber", distributedTest.getNumberOfQuestions());
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("studentTestLogList", studentTestLogList);
        request.setAttribute("distributedTestCode", distributedTestCode);
        return mapping.findForward("showStudentTestLog");
    }

    public ActionForward showTestMarks(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer objectCode = getCodeFromRequest(request, "objectCode");
        final Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
        final String path = getServlet().getServletContext().getRealPath("/");
        InfoSiteStudentsTestMarks infoSiteStudentsTestMarks = null;
        try {
            infoSiteStudentsTestMarks = (InfoSiteStudentsTestMarks) ServiceUtils.executeService(
                    userView, "ReadDistributedTestMarks", new Object[] { objectCode,
                            distributedTestCode, path });
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("distributedTestCode", distributedTestCode);
        request.setAttribute("infoSiteStudentsTestMarks", infoSiteStudentsTestMarks);
        return mapping.findForward("showTestMarks");
    }

    public ActionForward showTestMarksStatistics(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer objectCode = getCodeFromRequest(request, "objectCode");
        final Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
        SiteView siteView = null;
        try {
            siteView = (SiteView) ServiceUtils.executeService(userView,
                    "ReadDistributedTestMarksStatistics",
                    new Object[] { objectCode, distributedTestCode });
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("distributedTestCode", distributedTestCode);
        request.setAttribute("siteView", siteView);
        return mapping.findForward("showTestMarksStatistics");
    }

    public ActionForward showTestStatistics(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer objectCode = getCodeFromRequest(request, "objectCode");
        final Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
        SiteView siteView = null;
        try {
            siteView = (SiteView) ServiceUtils.executeService(userView, "ReadInquiryStatistics",
                    new Object[] { objectCode, distributedTestCode,
                            getServlet().getServletContext().getRealPath("/") });
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("distributedTestCode", distributedTestCode);
        request.setAttribute("siteView", siteView);
        return mapping.findForward("showTestStatistics");
    }

    public ActionForward downloadTestMarks(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer objectCode = getCodeFromRequest(request, "objectCode");
        final Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
        String result = null;
        try {
            if (distributedTestCode != null) {
                Object[] args = { objectCode, distributedTestCode };
                result = (String) ServiceUtils.executeService(userView,
                        "ReadDistributedTestMarksToString", args);
            } else {
                Object[] args = { objectCode, request.getParameterValues("distributedTestCodes") };
                result = (String) ServiceUtils.executeService(userView,
                        "ReadDistributedTestMarksToString", args);
            }
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        try {
            ServletOutputStream writer = response.getOutputStream();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment; filename=pauta.xls");
            writer.print(result);
            writer.flush();
            response.flushBuffer();
        } catch (IOException e) {
            throw new FenixActionException();
        }
        return null;
    }

    public ActionForward prepareChangeStudentTestQuestion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("objectCode", getCodeFromRequest(request, "objectCode"));
        request.setAttribute("questionCode", getCodeFromRequest(request, "questionCode"));
        request.setAttribute("distributedTestCode", getCodeFromRequest(request, "distributedTestCode"));
        request.setAttribute("studentCode", getCodeFromRequest(request, "studentCode"));
        List changesTypeList = (new TestQuestionChangesType()).getAllTypes();
        request.setAttribute("changesTypeList", changesTypeList);
        List studentsTypeList = (new TestQuestionStudentsChangesType()).getAllTypes();
        request.setAttribute("studentsTypeList", studentsTypeList);
        ((DynaActionForm) form).set("changesType", "1");
        ((DynaActionForm) form).set("deleteVariation", "true");
        ((DynaActionForm) form).set("studentsType", "4");
        return mapping.findForward("changeStudentTestQuestion");
    }

    public ActionForward chooseAnotherExercise(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        final Integer distributedTestId = getCodeFromRequest(request, "distributedTestCode");
        final ExecutionCourse executionCourse = rootDomainObject
                .readExecutionCourseByOID(executionCourseId);
        final DistributedTest distributedTest = rootDomainObject
                .readDistributedTestByOID(distributedTestId);
        Set<Metadata> metadataList = Metadata
                .findVisibleMetadataFromExecutionCourseNotOfDistributedTest(executionCourse,
                        distributedTest);
        request.setAttribute("objectCode", executionCourseId);
        request.setAttribute("questionCode", getCodeFromRequest(request, "questionCode"));
        request.setAttribute("distributedTestCode", distributedTestId);
        request.setAttribute("studentCode", getCodeFromRequest(request, "studentCode"));
        request.setAttribute("successfulChanged", request.getAttribute("successfulChanged"));
        request.setAttribute("studentsType", request.getAttribute("studentsType"));
        request.setAttribute("changesType", request.getAttribute("changesType"));
        request.setAttribute("deleteVariation", request.getAttribute("deleteVariation"));
        request.setAttribute("metadataList", metadataList);
        return mapping.findForward("chooseAnotherExercise");
    }

    public ActionForward changeStudentTestQuestion(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        final Integer questionId = getCodeFromRequest(request, "questionCode");
        final Integer distributedTestId = getCodeFromRequest(request, "distributedTestCode");
        final Integer studentId = getCodeFromRequest(request, "studentCode");
        final Integer metadataId = getCodeFromRequest(request, "metadataCode");
        final String changesType = request.getParameter("changesType");
        final String delete = request.getParameter("deleteVariation");
        final String studentsType = request.getParameter("studentsType");
        request.setAttribute("studentCode", studentId);
        request.setAttribute("distributedTestCode", distributedTestId);
        request.setAttribute("objectCode", executionCourseId);
        if (((new TestQuestionChangesType(new Integer(changesType))).getType().intValue() == 2)
                && (metadataId == null)) {
            request.setAttribute("deleteVariation", delete);
            request.setAttribute("studentsType", studentsType);
            request.setAttribute("changesType", changesType);
            return chooseAnotherExercise(mapping, form, request, response);
        }
        List<InfoSiteDistributedTestAdvisory> infoSiteDistributedTestAdvisoryList = new ArrayList<InfoSiteDistributedTestAdvisory>();
        try {
            infoSiteDistributedTestAdvisoryList = (List<InfoSiteDistributedTestAdvisory>) ServiceUtils
                    .executeService(userView, "ChangeStudentTestQuestion", new Object[] {
                            executionCourseId, distributedTestId, questionId, metadataId, studentId,
                            new TestQuestionChangesType(new Integer(changesType)), new Boolean(delete),
                            new TestQuestionStudentsChangesType(new Integer(studentsType)),
                            request.getContextPath() });
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (infoSiteDistributedTestAdvisoryList == null
                || infoSiteDistributedTestAdvisoryList.size() == 0) {
            request.setAttribute("successfulChanged", new Boolean(false));
            request.setAttribute("studentsType", studentsType);
            request.setAttribute("changesType", changesType);
            return chooseAnotherExercise(mapping, form, request, response);
        }
        // Collections.sort(result, new BeanComparator("label"));
        request.setAttribute("successfulChanged", infoSiteDistributedTestAdvisoryList);

        List<InfoStudent> infoStudentList = new ArrayList<InfoStudent>();
        for (InfoSiteDistributedTestAdvisory infoSiteDistributedTestAdvisory : infoSiteDistributedTestAdvisoryList) {
            List<InfoStudent> studentWithoutAdvisory = infoSiteDistributedTestAdvisory
                    .getInfoStudentList();
            List<InfoStudent> result = new ArrayList<InfoStudent>();
            for (int times = 0; times < 3; times++) {
                for (InfoStudent student : studentWithoutAdvisory) {
                    try {
                        ServiceUtils.executeService(userView, "InsertStudentDistributedTestAdvisory",
                                new Object[] {
                                        executionCourseId,
                                        infoSiteDistributedTestAdvisory.getInfoAdvisory()
                                                .getIdInternal(), student.getIdInternal() });
                    } catch (FenixServiceException e) {
                        result.add(student);
                    }
                }
                studentWithoutAdvisory = result;
                if (studentWithoutAdvisory.size() == 0) {
                    break;
                }
            }
            infoStudentList.addAll(studentWithoutAdvisory);
        }

        request.setAttribute("studentWithoutAdvisory", infoStudentList);
        return mapping.findForward("showStudentTest");
    }

    public ActionForward prepareChangeStudentTestQuestionValue(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("objectCode", getCodeFromRequest(request, "objectCode"));
        request.setAttribute("questionCode", getCodeFromRequest(request, "questionCode"));
        request.setAttribute("distributedTestCode", getCodeFromRequest(request, "distributedTestCode"));
        request.setAttribute("studentCode", getCodeFromRequest(request, "studentCode"));
        List studentsTypeList = (new TestQuestionStudentsChangesType()).getAllTypes();
        request.setAttribute("studentsTypeList", studentsTypeList);
        ((DynaActionForm) form).set("studentsType", "1");
        ((DynaActionForm) form).set("questionValue", request.getParameter("questionValue"));
        return mapping.findForward("changeStudentTestQuestionValue");
    }

    public ActionForward changeStudentTestQuestionValue(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer objectCode = getCodeFromRequest(request, "objectCode");
        final Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
        final Integer studentCode = getCodeFromRequest(request, "studentCode");
        final Integer questionCode = getCodeFromRequest(request, "questionCode");
        final String studentTypeString = (String) ((DynaActionForm) form).get("studentsType");
        final String questionValueString = (String) ((DynaActionForm) form).get("questionValue");

        try {
            List<InfoSiteDistributedTestAdvisory> result = (List<InfoSiteDistributedTestAdvisory>) ServiceUtils
                    .executeService(userView, "ChangeStudentTestQuestionValue", new Object[] {
                            objectCode, distributedTestCode, new Double(questionValueString),
                            questionCode, studentCode,
                            new TestQuestionStudentsChangesType(new Integer(studentTypeString)),
                            getServlet().getServletContext().getRealPath("/") });
            request.setAttribute("successfulChanged", result);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("distributedTestCode", distributedTestCode);
        request.setAttribute("studentCode", studentCode);
        return mapping.findForward("showStudentTest");
    }

    public ActionForward prepareChangeStudentTestQuestionMark(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("objectCode", getCodeFromRequest(request, "objectCode"));
        request.setAttribute("questionCode", getCodeFromRequest(request, "questionCode"));
        request.setAttribute("distributedTestCode", getCodeFromRequest(request, "distributedTestCode"));
        request.setAttribute("studentCode", getCodeFromRequest(request, "studentCode"));
        List studentsTypeList = (new TestQuestionStudentsChangesType()).getAllTypes();
        request.setAttribute("studentsTypeList", studentsTypeList);
        ((DynaActionForm) form).set("studentsType", "1");
        ((DynaActionForm) form).set("questionValue", request.getParameter("questionValue"));
        return mapping.findForward("changeStudentTestQuestionMark");
    }

    public ActionForward changeStudentTestQuestionMark(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer objectCode = getCodeFromRequest(request, "objectCode");
        final Integer distributedTestCode = getCodeFromRequest(request, "distributedTestCode");
        final Integer studentCode = getCodeFromRequest(request, "studentCode");
        final Integer questionCode = getCodeFromRequest(request, "questionCode");
        final String studentTypeString = (String) ((DynaActionForm) form).get("studentsType");
        final String questionValueString = (String) ((DynaActionForm) form).get("questionValue");

        try {
            List<InfoSiteDistributedTestAdvisory> result = (List<InfoSiteDistributedTestAdvisory>) ServiceUtils
                    .executeService(userView, "ChangeStudentTestQuestionMark", new Object[] {
                            objectCode, distributedTestCode,
                            new Double(questionValueString.replaceAll(",", ".")), questionCode,
                            studentCode,
                            new TestQuestionStudentsChangesType(new Integer(studentTypeString)),
                            getServlet().getServletContext().getRealPath("/") });
            request.setAttribute("successfulChanged", result);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("objectCode", objectCode);
        request.setAttribute("distributedTestCode", distributedTestCode);
        request.setAttribute("studentCode", studentCode);
        return mapping.findForward("showStudentTest");
    }

    public ActionForward chooseTestSimulationOptions(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        final Integer objectCode = getCodeFromRequest(request, "objectCode");
        final Integer testCode = getCodeFromRequest(request, "testCode");
        request.setAttribute("objectCode", objectCode);
        Test test = null;
        try {
            test = (Test) ServiceUtils.executeService(userView, "ReadTest", new Object[] { objectCode,
                    testCode });
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (test.getTestQuestions().size() >= 1) {
            List testTypeList = (new TestType()).getAllTypes();
            request.setAttribute("testTypeList", testTypeList);
            List correctionAvailabilityList = (new CorrectionAvailability()).getAllAvailabilities();
            request.setAttribute("correctionAvailabilityList", correctionAvailabilityList);
            if ((((DynaActionForm) form).get("testType")).equals(""))
                ((DynaActionForm) form).set("testType", "1");
            if ((((DynaActionForm) form).get("availableCorrection")).equals(""))
                ((DynaActionForm) form).set("availableCorrection", "3");
            if ((((DynaActionForm) form).get("imsFeedback")).equals(""))
                ((DynaActionForm) form).set("imsFeedback", "true");
            if ((((DynaActionForm) form).get("testInformation")).equals("")) {
                ((DynaActionForm) form).set("testInformation", createDefaultDistributedTestInfo(test));
                ((DynaActionForm) form).set("notInquiryInformation", ((DynaActionForm) form)
                        .get("testInformation"));
            }
            if ((((DynaActionForm) form).get("inquiryInformation")).equals(""))
                ((DynaActionForm) form).set("inquiryInformation", createDefaultDistributedInquiryInfo());
            request.setAttribute("testCode", testCode);
            return mapping.findForward("chooseTestSimulationOptions");
        }
        error(request, "InvalidDistribution", "error.distributeTest.noExercise");
        return showTests(mapping, form, request, response);
    }

    public ActionForward prepareSimulateTest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = getUserView(request);
        Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        Integer testId = getCodeFromRequest(request, "testCode");
        if (testId == null)
            testId = getCodeFromRequest(request, "distributedTestCode");
        String testInformation = request.getParameter("testInformation");
        String testType = request.getParameter("testType");
        String availableCorrection = request.getParameter("availableCorrection");
        String imsFeedback = request.getParameter("imsFeedback");
        TestType testTypeArg = new TestType(new Integer(testType));
        CorrectionAvailability correctionAvailabilityArg;
        Boolean imsFeedbackArg;
        if (testTypeArg.equals(new TestType(TestType.INQUIRY))) {
            correctionAvailabilityArg = new CorrectionAvailability(CorrectionAvailability.NEVER);
            imsFeedbackArg = new Boolean(false);
        } else {
            correctionAvailabilityArg = new CorrectionAvailability(new Integer(availableCorrection));
            imsFeedbackArg = new Boolean(imsFeedback);
        }

        List<InfoStudentTestQuestion> infoStudentTestQuestionList = new ArrayList();
        try {
            infoStudentTestQuestionList = (List) ServiceUtils.executeService(userView,
                    "GenetareStudentTestForSimulation", new Object[] { executionCourseId, testId,
                            getServlet().getServletContext().getRealPath("/"), testTypeArg,
                            correctionAvailabilityArg, imsFeedbackArg, testInformation });
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        for (int i = 0; i < infoStudentTestQuestionList.size(); i++) {
            InfoStudentTestQuestion infoStudentTestQuestion = infoStudentTestQuestionList.get(i);

            if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.STR
                    && ((ResponseSTR) infoStudentTestQuestion.getResponse()).getResponse() != null)
                request.setAttribute("question" + i, ((ResponseSTR) infoStudentTestQuestion
                        .getResponse()).getResponse());
            else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.NUM
                    && ((ResponseNUM) infoStudentTestQuestion.getResponse()).getResponse() != null)
                request.setAttribute("question" + i, ((ResponseNUM) infoStudentTestQuestion
                        .getResponse()).getResponse());
            else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.LID
                    && ((ResponseLID) infoStudentTestQuestion.getResponse()).getResponse() != null)
                if (infoStudentTestQuestion.getQuestion().getQuestionType().getCardinalityType()
                        .getType().intValue() == CardinalityType.SINGLE)
                    request.setAttribute("question" + i, ((ResponseLID) infoStudentTestQuestion
                            .getResponse()).getResponse()[0]);
                else
                    request.setAttribute("question" + i, ((ResponseLID) infoStudentTestQuestion
                            .getResponse()).getResponse());
        }

        request.setAttribute("studentTestForm", form);
        request.setAttribute("objectCode", executionCourseId);
        request.setAttribute("simulate", new Boolean(true));
        request.setAttribute("infoStudentTestQuestionList", infoStudentTestQuestionList);
        return mapping.findForward("doTestSimulation");
    }

    public ActionForward simulateTest(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        final IUserView userView = (IUserView) request.getSession(false).getAttribute(
                SessionConstants.U_VIEW);
        final Integer executionCourseId = getCodeFromRequest(request, "objectCode");
        final Integer distributedTestId = getCodeFromRequest(request, "distributedTestCode");

        final String testInformation = request.getParameter("testInformation");
        final String testType = request.getParameter("testType");
        final String availableCorrection = request.getParameter("availableCorrection");
        final String imsFeedback = request.getParameter("imsFeedback");
        final TestType testTypeArg = new TestType(new Integer(testType));
        CorrectionAvailability correctionAvailabilityArg = new CorrectionAvailability(new Integer(
                availableCorrection));
        Boolean imsFeedbackArg = new Boolean(imsFeedback);
        if (testTypeArg.equals(new TestType(TestType.INQUIRY))) {
            correctionAvailabilityArg = new CorrectionAvailability(CorrectionAvailability.NEVER);
            imsFeedbackArg = new Boolean(false);
        }
        final String path = getServlet().getServletContext().getRealPath("/");
        request.setAttribute("objectCode", executionCourseId);
        Test test = null;
        try {
            test = (Test) ServiceUtils.executeService(userView, "ReadTest", new Object[] {
                    executionCourseId, distributedTestId });
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        for (TestQuestion testQuestion : test.getTestQuestions()) {
            ParseSubQuestion parse = new ParseSubQuestion();
            try {
                parse.parseSubQuestion(testQuestion.getQuestion(), getServlet().getServletContext()
                        .getRealPath("/"));
            } catch (ParseQuestionException e) {
                throw new FenixActionException();
            } catch (ParseException e) {
                throw new FenixActionException();
            }
        }

        String[] questionCodes = new String[test.getTestQuestions().size()];
        String[] optionsShuffle = new String[test.getTestQuestions().size()];
        String[] questionTypes = new String[test.getTestQuestions().size()];
        for (int i = 0; i < test.getTestQuestions().size(); i++) {
            questionCodes[i] = request.getParameter("questionCode" + i);
            optionsShuffle[i] = request.getParameter("optionShuffle" + i);
            questionTypes[i] = request.getParameter("questionType" + i);
        }
        Response[] userResponse = new Response[test.getTestQuestions().size()];
        for (int i = 0; i < test.getTestQuestions().size(); i++) {
            if (new Integer(questionTypes[i]).intValue() == QuestionType.STR)
                userResponse[i] = new ResponseSTR(request.getParameter(new String("question" + i)));
            else if (new Integer(questionTypes[i]).intValue() == QuestionType.NUM)
                userResponse[i] = new ResponseNUM(request.getParameter(new String("question" + i)));
            else if (new Integer(questionTypes[i]).intValue() == QuestionType.LID)
                userResponse[i] = new ResponseLID(request.getParameterValues(new String("question" + i)));
        }
        InfoSiteStudentTestFeedback infoSiteStudentTestFeedback = null;
        try {
            infoSiteStudentTestFeedback = (InfoSiteStudentTestFeedback) ServiceUtils.executeService(
                    userView, "SimulateTest", new Object[] { executionCourseId, distributedTestId,
                            userResponse, questionCodes, optionsShuffle, testTypeArg,
                            correctionAvailabilityArg, imsFeedbackArg, testInformation, path });
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoSiteStudentTestFeedback != null) {
            List infoStudentTestQuestionList = infoSiteStudentTestFeedback.getStudentTestQuestionList();
            for (int i = 0; i < infoStudentTestQuestionList.size(); i++) {
                InfoStudentTestQuestion infoStudentTestQuestion = (InfoStudentTestQuestion) infoStudentTestQuestionList
                        .get(i);

                if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.STR
                        && ((ResponseSTR) infoStudentTestQuestion.getResponse()).getResponse() != null)
                    request.setAttribute("question" + i, ((ResponseSTR) infoStudentTestQuestion
                            .getResponse()).getResponse());
                else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.NUM
                        && ((ResponseNUM) infoStudentTestQuestion.getResponse()).getResponse() != null)
                    request.setAttribute("question" + i, ((ResponseNUM) infoStudentTestQuestion
                            .getResponse()).getResponse());
                else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.LID
                        && ((ResponseLID) infoStudentTestQuestion.getResponse()).getResponse() != null)
                    if (infoStudentTestQuestion.getQuestion().getQuestionType().getCardinalityType()
                            .getType().intValue() == CardinalityType.SINGLE)
                        request.setAttribute("question" + i, ((ResponseLID) infoStudentTestQuestion
                                .getResponse()).getResponse()[0]);
                    else
                        request.setAttribute("question" + i, ((ResponseLID) infoStudentTestQuestion
                                .getResponse()).getResponse());
                if (((InfoStudentTestQuestion) infoSiteStudentTestFeedback.getStudentTestQuestionList()
                        .get(0)).getDistributedTest().getCorrectionAvailability().getAvailability()
                        .intValue() == CorrectionAvailability.ALWAYS) {
                    request.setAttribute("infoStudentTestQuestionList", infoSiteStudentTestFeedback
                            .getStudentTestQuestionList());
                }
            }
            if (request.getAttribute("showCorrection") != null) {
                request.setAttribute("infoStudentTestQuestionList", infoSiteStudentTestFeedback
                        .getStudentTestQuestionList());
                return mapping.findForward("showSimulationCorrection");
            }
            if (request.getAttribute("doTestSimulation") != null
                    || request.getParameter("doTestSimulation") != null) {
                request.setAttribute("infoStudentTestQuestionList", infoSiteStudentTestFeedback
                        .getStudentTestQuestionList());
                return mapping.findForward("doTestSimulation");
            }

        }
        request.setAttribute("infoSiteStudentTestFeedback", infoSiteStudentTestFeedback);
        return mapping.findForward("showSimulationFeedback");
    }

    public ActionForward showSimulationCorrection(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        request.setAttribute("showCorrection", new Boolean(true));
        return simulateTest(mapping, form, request, response);
    }

    public ActionForward doTestSimulation(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws FenixActionException,
            FenixFilterException {
        request.setAttribute("doTestSimulation", new Boolean(true));
        return simulateTest(mapping, form, request, response);
    }

    private void error(HttpServletRequest request, String errorProperty, String error) {
        ActionErrors actionErrors = new ActionErrors();
        actionErrors.add(errorProperty, new ActionError(error));
        saveErrors(request, actionErrors);
    }

    private Integer getCodeFromRequest(HttpServletRequest request, String codeString) {
        Integer code = null;
        Object objectCode = request.getAttribute(codeString);
        if (objectCode != null) {
            if (objectCode instanceof String)
                code = new Integer((String) objectCode);
            else if (objectCode instanceof Integer)
                code = (Integer) objectCode;
        } else {
            String thisCodeString = request.getParameter(codeString);
            if (thisCodeString != null)
                code = new Integer(thisCodeString);
        }
        return code;
    }

    private Calendar string2Date(String day, String month, String year) {
        Calendar result = Calendar.getInstance();
        result.set(Calendar.DAY_OF_MONTH, (new Integer(day)).intValue());
        result.set(Calendar.MONTH, (new Integer(month)).intValue() - 1);
        result.set(Calendar.YEAR, (new Integer(year)).intValue());
        return result;
    }

    private Calendar string2Hour(String hour, String minute) {
        Calendar result = Calendar.getInstance();
        result.set(Calendar.HOUR_OF_DAY, (new Integer(hour)).intValue());
        result.set(Calendar.MINUTE, (new Integer(minute)).intValue());
        result.set(Calendar.SECOND, new Integer(0).intValue());
        return result;
    }

    private boolean compareDates(HttpServletRequest request) {
        CalendarDateComparator dateComparator = new CalendarDateComparator();
        CalendarHourComparator hourComparator = new CalendarHourComparator();
        String testBeginDay = request.getParameter("beginDayFormatted");
        String testBeginMonth = request.getParameter("beginMonthFormatted");
        String testBeginYear = request.getParameter("beginYearFormatted");
        String testBeginHour = request.getParameter("beginHourFormatted");
        String testBeginMinute = request.getParameter("beginMinuteFormatted");
        String testEndDay = request.getParameter("endDayFormatted");
        String testEndMonth = request.getParameter("endMonthFormatted");
        String testEndYear = request.getParameter("endYearFormatted");
        String testEndHour = request.getParameter("endHourFormatted");
        String testEndMinute = request.getParameter("endMinuteFormatted");
        Calendar beginDate = string2Date(testBeginDay, testBeginMonth, testBeginYear);
        Calendar beginHour = string2Hour(testBeginHour, testBeginMinute);
        Calendar endDate = string2Date(testEndDay, testEndMonth, testEndYear);
        Calendar endHour = string2Hour(testEndHour, testEndMinute);
        if (dateComparator.compare(beginDate, endDate) > 0) {
            error(request, "InvalidTime", "errors.lesson.invalid.time.interval");
            return false;
        }
        if (dateComparator.compare(beginDate, endDate) == 0) {
            if (hourComparator.compare(beginHour, endHour) >= 0) {
                error(request, "InvalidTime", "errors.lesson.invalid.time.interval");
                return false;
            }
        }
        return true;
    }

    private String createDefaultDistributedTestInfo(Test test) {
        ResourceBundle bundle = ResourceBundle.getBundle("resources.ApplicationResources");
        return MessageFormat.format(bundle.getString("message.distributeTest.evaluation"), new Object[] {
                test.getTitle(), test.getTestQuestions().size() });
    }

    private String createDefaultDistributedInquiryInfo() {
        ResourceBundle bundle = ResourceBundle.getBundle("resources.ApplicationResources", LanguageUtils
                .getLocale());
        return bundle.getString("message.distributeTest.inquiry");
    }
}