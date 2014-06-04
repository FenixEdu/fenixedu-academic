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
 * Created on 14/Ago/2003
 */

package net.sourceforge.fenixedu.presentationTier.Action.teacher.onlineTests;

import java.io.DataOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests.ReadStudentTestQuestionImage;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.AddStudentsToDistributedTest;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.ChangeStudentTestQuestion;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.ChangeStudentTestQuestionMark;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.ChangeStudentTestQuestionValue;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.DeleteDistributedTest;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.DeleteTest;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.DeleteTestQuestion;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.EditDistributedTest;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.EditTest;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.EditTestQuestion;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.GenetareStudentTestForSimulation;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.InsertDistributedTest;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.InsertTest;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.InsertTestAsNewTest;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.InsertTestQuestion;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.ReadDistributedTestMarks;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.ReadDistributedTestMarksStatistics;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.ReadDistributedTestMarksToString;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.ReadInquiryStatistics;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.ReadQuestionImage;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.ReadStudentDistributedTest;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.ReadStudentsByIdArray;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests.SimulateTest;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.dataTransferObject.SiteView;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarDateComparator;
import net.sourceforge.fenixedu.dataTransferObject.comparators.CalendarHourComparator;
import net.sourceforge.fenixedu.dataTransferObject.comparators.MetadataComparator;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoInquiryStatistics;
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
import net.sourceforge.fenixedu.presentationTier.Action.exceptions.FenixActionException;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.ManageExecutionCourseDA;
import net.sourceforge.fenixedu.presentationTier.Action.teacher.executionCourse.ExecutionCourseBaseAction;
import net.sourceforge.fenixedu.util.Bundle;
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
import org.apache.struts.util.LabelValueBean;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter;
import pt.ist.fenixWebFramework.struts.annotations.Input;
import pt.ist.fenixWebFramework.struts.annotations.Mapping;
import pt.ist.fenixframework.FenixFramework;

import com.google.common.base.Strings;
import com.google.common.io.BaseEncoding;

/**
 * @author Susana Fernandes
 */
@Mapping(path = "/testsManagement", module = "teacher", formBean = "testForm", functionality = ManageExecutionCourseDA.class)
public class TestsManagementAction extends ExecutionCourseBaseAction {

    private ActionForward doForward(HttpServletRequest request, String testsPath) {
        request.setAttribute("teacher$actual$page", "/teacher/onlineTests/" + testsPath + ".jsp");
        return new ActionForward("/onlineTests/testsFrame.jsp");
    }

    public ActionForward testsFirstPage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        return doForward(request, "testsFirstPage");
    }

    @Input
    public ActionForward prepareCreateTest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final ExecutionCourse executionCourse = getExecutionCourse(request);
        final Integer availableMetadatas = executionCourse.findVisibleMetadata().size();
        request.setAttribute("availableMetadatas", availableMetadatas);
        return doForward(request, "createTest");
    }

    public ActionForward createTest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String title = request.getParameter("title");
        final String information = request.getParameter("information");
        String testCode = null;
        try {
            testCode = InsertTest.runInsertTest(getExecutionCourse(request), title, information);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("testCode", testCode);
        return showAvailableQuestions(mapping, form, request, response);
    }

    public ActionForward editAsNewTest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String testCode = getStringFromRequest(request, "testCode");
        String newTestCode = null;
        try {
            newTestCode = InsertTestAsNewTest.runInsertTestAsNewTest(getExecutionCourse(request).getExternalId(), testCode);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("testCode", newTestCode);
        return editTest(mapping, form, request, response);
    }

    public ActionForward showAvailableQuestions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        Test test = getDomainObject(request, "testCode");
        final ExecutionCourse executionCourse = getExecutionCourse(request);
        List<Metadata> metadataList =
                new ArrayList<Metadata>(Metadata.findVisibleMetadataFromExecutionCourseNotOfTest(executionCourse, test));
        String order = request.getParameter("order");
        final String asc = request.getParameter("asc");

        if (order != null) {
            MetadataComparator metadataComparator = new MetadataComparator(order, asc);
            Collections.sort(metadataList, metadataComparator);
        } else {
            order = new String();
        }

        request.setAttribute("metadataList", metadataList);
        request.setAttribute("testCode", test.getExternalId());
        request.setAttribute("asc", asc);
        request.setAttribute("order", order);
        return doForward(request, "showAvailableQuestions");
    }

    public ActionForward prepareInsertTestQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String metadataCode = getStringFromRequest(request, "metadataCode");
        String exerciseCode = getStringFromRequest(request, "exerciseCode");
        Test test = getDomainObject(request, "testCode");

        final Metadata metadata = FenixFramework.getDomainObject(metadataCode);
        Question question = null;
        if (exerciseCode == null || exerciseCode.equals(new Integer(-1))) {
            question = metadata.getVisibleQuestions().iterator().next();
        } else {
            for (Question visibleQuestion : metadata.getVisibleQuestions()) {
                if (visibleQuestion.getExternalId().equals(exerciseCode)) {
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
            question = parse.parseSubQuestion(question);
        } catch (ParseQuestionException e) {
            throw new FenixActionException();
        }

        List<String> testQuestionNames = new ArrayList<String>();
        List<Integer> testQuestionValues = new ArrayList<Integer>();
        for (int i = 0; i < test.getTestQuestionsSet().size(); i++) {
            testQuestionNames.add("Pergunta " + (i + 1));
            testQuestionValues.add(new Integer(i));
        }

        List<LabelValueBean> formulas = CorrectionFormula.getFormulas();
        request.setAttribute("order", request.getParameter("order"));
        request.setAttribute("asc", request.getParameter("asc"));
        request.setAttribute("testQuestionNames", testQuestionNames);
        request.setAttribute("testQuestionValues", testQuestionValues);
        request.setAttribute("formulas", formulas);
        request.setAttribute("testCode", test.getExternalId());
        request.setAttribute("exerciseCode", exerciseCode);
        request.setAttribute("question", question);
        return doForward(request, "insertTestQuestion");
    }

    public ActionForward insertTestQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String[] metadataCodes = request.getParameterValues("metadataCode");
        final Integer questionOrder = getCodeFromRequest(request, "questionOrder");
        final String testCode = getStringFromRequest(request, "testCode");
        Double questionValue = null;
        if (request.getParameter("questionValue") != null) {
            questionValue = new Double(request.getParameter("questionValue"));
        }
        final Integer formula = getCodeFromRequest(request, "formula");

        CorrectionFormula correctionFormulas = new CorrectionFormula(CorrectionFormula.FENIX);
        if (formula != null) {
            correctionFormulas = new CorrectionFormula(formula);
        }
        try {
            InsertTestQuestion.runInsertTestQuestion(getExecutionCourse(request).getExternalId(), testCode, metadataCodes,
                    questionOrder, questionValue, correctionFormulas);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("order", request.getParameter("order"));
        request.setAttribute("asc", request.getParameter("asc"));
        request.setAttribute("testCode", testCode);
        return showAvailableQuestions(mapping, form, request, response);
    }

    public ActionForward prepareEditTestQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String questionCode = getStringFromRequest(request, "questionCode");
        Test test = getDomainObject(request, "testCode");
        final Question question = test.findQuestionByOID(questionCode);
        final TestQuestion testQuestion = test.getTestQuestion(question);
        ParseSubQuestion parse = new ParseSubQuestion();
        try {
            parse.parseSubQuestion(testQuestion.getQuestion());
        } catch (ParseQuestionException e) {
            throw new FenixActionException();
        }
        List<String> testQuestionNames = new ArrayList<String>();
        List<Integer> testQuestionValues = new ArrayList<Integer>();
        int questionOrder = testQuestion.getTestQuestionOrder().intValue();
        for (int i = 0; i < test.getTestQuestionsSet().size(); i++) {
            if ((i + 1) != questionOrder && (i + 1) != questionOrder + 1) {
                testQuestionNames.add("Pergunta " + (i + 1));
                testQuestionValues.add(new Integer(i));
            }
        }
        List<LabelValueBean> formulas = CorrectionFormula.getFormulas();
        ((DynaActionForm) form).set("formula", testQuestion.getCorrectionFormula().getFormula().toString());
        request.setAttribute("formulas", formulas);
        request.setAttribute("testQuestionNames", testQuestionNames);
        request.setAttribute("testQuestionValues", testQuestionValues);
        request.setAttribute("testCode", test.getExternalId());
        request.setAttribute("questionCode", questionCode);
        request.setAttribute("testQuestion", testQuestion);
        return doForward(request, "editTestQuestion");
    }

    public ActionForward editTestQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final Object testCode = getFromRequest(request, "testCode");
        final String testQuestionCode = getStringFromRequest(request, "testQuestionCode");
        final Integer questionOrder = getCodeFromRequest(request, "testQuestionOrder");
        final Double questionValue = new Double(request.getParameter("testQuestionValue"));
        final Integer formula = getCodeFromRequest(request, "formula");
        CorrectionFormula correctionFormula = new CorrectionFormula(CorrectionFormula.FENIX);
        if (formula != null) {
            correctionFormula = new CorrectionFormula(formula);
        }
        try {
            EditTestQuestion.runEditTestQuestion(getExecutionCourse(request).getExternalId(), testQuestionCode, questionOrder,
                    questionValue, correctionFormula);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("testCode", testCode);
        return editTest(mapping, form, request, response);
    }

    public ActionForward deleteTestQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String testCode = getStringFromRequest(request, "testCode");
        final String questionCode = getStringFromRequest(request, "questionCode");
        try {
            DeleteTestQuestion.runDeleteTestQuestion(getExecutionCourse(request).getExternalId(), testCode, questionCode);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return editTest(mapping, form, request, response);
    }

    public ActionForward showTests(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        ExecutionCourse executionCourse = FenixFramework.getDomainObject(getExecutionCourse(request).getExternalId());
        final TestScope testScope = executionCourse.getTestScope();
        List<Test> testList = new ArrayList<Test>();
        if (testScope != null) {
            testList = new ArrayList<>(testScope.getTestsSet());
        }
        request.setAttribute("testList", testList);
        return doForward(request, "showTests");
    }

    public ActionForward prepareDeleteTest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String testCode = getStringFromRequest(request, "testCode");
        request.setAttribute("testCode", testCode);
        Test test = FenixFramework.getDomainObject(testCode);
        request.setAttribute("title", test.getTitle());
        return doForward(request, "prepareDeleteTest");
    }

    public ActionForward deleteTest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String testCode = getStringFromRequest(request, "testCode");
        try {
            DeleteTest.runDeleteTest(getExecutionCourse(request).getExternalId(), testCode);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return showTests(mapping, form, request, response);
    }

    public ActionForward showImage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String exerciseCode = getStringFromRequest(request, "exerciseCode");
        final Integer imgCode = getCodeFromRequest(request, "imgCode");
        final String imgTypeString = request.getParameter("imgType");
        final String studentCode = getStringFromRequest(request, "studentCode");
        final String optionShuffle = request.getParameter("optionShuffle");
        final String testCode = getStringFromRequest(request, "testCode");
        final Integer itemIndex = getCodeFromRequest(request, "item");
        final Integer feedbackCode = getCodeFromRequest(request, "feedbackCode");
        String img = null;
        if (studentCode != null && testCode != null) {
            try {
                img = ReadStudentTestQuestionImage.run(studentCode, testCode, exerciseCode, imgCode, feedbackCode, itemIndex);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
        } else if (optionShuffle != null && !Strings.isNullOrEmpty(testCode)) {
            try {
                img = ReadQuestionImage.run(testCode, exerciseCode, optionShuffle, imgCode, feedbackCode);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
        } else {

            try {
                img = ReadQuestionImage.run(exerciseCode, imgCode, feedbackCode, itemIndex);
            } catch (FenixServiceException e) {
                throw new FenixActionException(e);
            }
        }
        byte[] imageData = BaseEncoding.base64().decode(img);
        try {
            response.reset();
            response.setContentType(imgTypeString);
            response.setContentLength(imageData.length);
            response.setBufferSize(imageData.length);
            String imageName =
                    "image" + exerciseCode + imgCode + "."
                            + imgTypeString.substring(imgTypeString.lastIndexOf("/") + 1, imgTypeString.length());
            response.setHeader("Content-disposition", "attachment; filename=" + imageName);
            DataOutputStream dataOut = new DataOutputStream(response.getOutputStream());
            dataOut.write(imageData);
            response.flushBuffer();
        } catch (java.io.IOException e) {
            throw new FenixActionException(e);
        }
        return null;
    }

    public ActionForward editTest(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
            throws FenixActionException {
        final String testCode = getStringFromRequest(request, "testCode");
        Test test = FenixFramework.getDomainObject(testCode);

        List<TestQuestion> testQuestionList = new ArrayList<TestQuestion>(test.getTestQuestionsSet());
        for (TestQuestion testQuestion : testQuestionList) {
            ParseSubQuestion parse = new ParseSubQuestion();
            try {
                parse.parseSubQuestion(testQuestion.getQuestion());
            } catch (ParseQuestionException e) {
                throw new FenixActionException();
            }
        }
        Collections.sort(testQuestionList, TestQuestion.COMPARATOR_BY_TEST_QUESTION_ORDER);
        request.setAttribute("test", test);
        request.setAttribute("testQuestionList", testQuestionList);
        request.setAttribute("testCode", testCode);
        return doForward(request, "editTest");
    }

    public ActionForward prepareEditTestHeader(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String testCode = getStringFromRequest(request, "testCode");
        Test test = FenixFramework.getDomainObject(testCode);
        request.setAttribute("test", test);
        request.setAttribute("testCode", testCode);
        return doForward(request, "editTestHeader");
    }

    public ActionForward editTestHeader(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String testCode = getStringFromRequest(request, "testCode");
        final String title = request.getParameter("title");
        final String information = request.getParameter("information");
        try {
            EditTest.runEditTest(getExecutionCourse(request).getExternalId(), testCode, title, information);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        return editTest(mapping, form, request, response);
    }

    public ActionForward prepareDistributeTest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String testCode = getStringFromRequest(request, "testCode");

        Test test = FenixFramework.getDomainObject(testCode);
        if (test.getTestQuestionsSet().size() >= 1) {
            List<LabelValueBean> testTypeList = TestType.getAllTypes();
            request.setAttribute("testTypeList", testTypeList);
            List<LabelValueBean> correctionAvailabilityList = CorrectionAvailability.getAllAvailabilities();
            request.setAttribute("correctionAvailabilityList", correctionAvailabilityList);
            if ((((DynaActionForm) form).get("testType")).equals("")) {
                ((DynaActionForm) form).set("testType", "1");
            }
            if ((((DynaActionForm) form).get("availableCorrection")).equals("")) {
                ((DynaActionForm) form).set("availableCorrection", "3");
            }
            if ((((DynaActionForm) form).get("imsFeedback")).equals("")) {
                ((DynaActionForm) form).set("imsFeedback", "true");
            }
            if ((((DynaActionForm) form).get("testInformation")).equals("")) {
                ((DynaActionForm) form).set("testInformation", createDefaultDistributedTestInfo(test));
                ((DynaActionForm) form).set("notInquiryInformation", ((DynaActionForm) form).get("testInformation"));
            }
            if ((((DynaActionForm) form).get("inquiryInformation")).equals("")) {
                ((DynaActionForm) form).set("inquiryInformation", createDefaultDistributedInquiryInfo());
            }

            ((DynaActionForm) form).set("evaluationTitle", test.getTitle());
            request.setAttribute("testCode", testCode);
            return doForward(request, "distributeTest");
        }
        error(request, "InvalidDistribution", "error.distributeTest.noExercise");
        return showTests(mapping, form, request, response);

    }

    public ActionForward chooseDistributionFor(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
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
        } else {
            return showDistributedTests(mapping, form, request, response);
        }
    }

    public ActionForward chooseShifts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String testCode = getStringFromRequest(request, "testCode");
        final ExecutionCourse executionCourse = getExecutionCourse(request);
        if (executionCourse == null) {
            throw new FenixActionException();
        }
        final Set<Shift> shiftList = executionCourse.getAssociatedShifts();
        // Collections.sort(shifts, new InfoShiftComparatorByLessonType());
        request.setAttribute("shiftList", shiftList);
        request.setAttribute("testCode", testCode);
        return doForward(request, "distributeTestByShifts");
    }

    public ActionForward chooseStudents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String testCode = getStringFromRequest(request, "testCode");

        final ExecutionCourse executionCourse = getExecutionCourse(request);
        final List<Registration> studentList = new ArrayList<Registration>();
        for (final Attends attends : executionCourse.getAttends()) {
            if (!studentList.contains(attends.getRegistration()) && attends.getRegistration().isActive()) {
                studentList.add(attends.getRegistration());
            }
        }
        Collections.sort(studentList, new BeanComparator("number"));
        request.setAttribute("studentList", studentList);
        request.setAttribute("testCode", testCode);
        return doForward(request, "distributeTestByStudents");
    }

    public ActionForward distributeTest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String testCode = getStringFromRequest(request, "testCode");
        final String testInformation = request.getParameter("testInformation");
        final String evaluationTitle = request.getParameter("evaluationTitle");
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

        String executionCourseID = getExecutionCourse(request).getExternalId();

        try {
            List<InfoStudent> infoStudentList =
                    ReadStudentsByIdArray.runReadStudentsByIdArray(executionCourseID, selected, new Boolean(insertByShifts));
            InsertDistributedTest.runInsertDistributedTest(executionCourseID, testCode, testInformation, evaluationTitle,
                    beginDate, beginHour, endDate, endHour, testTypeArg, correctionAvailabilityArg, imsFeedbackArg,
                    infoStudentList);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        request.setAttribute("successfulDistribution", new Boolean(true));
        final ActionForward actionForward = new ActionForward();
        actionForward.setRedirect(true);
        final String path =
                request.getContextPath() + "/teacher/testDistribution.do?method=showDistributedTests&amp;executionCourseID="
                        + executionCourseID;
        final String requestPath =
                "/testDistribution.do?method=showDistributedTests&executionCourseID=" + executionCourseID
                        + "&_request_checksum_=" + GenericChecksumRewriter.calculateChecksum(path, request.getSession());
        actionForward.setPath(requestPath);
        return actionForward;
    }

    public ActionForward showDistributedTests(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        ExecutionCourse executionCourse = getExecutionCourse(request);
        final TestScope testScope = executionCourse.getTestScope();
        List<DistributedTest> distributedTestList = new ArrayList<DistributedTest>();
        if (testScope != null) {
            distributedTestList = new ArrayList<>(testScope.getDistributedTests());
        }
        request.setAttribute("distributedTests", distributedTestList);
        return doForward(request, "showDistributedTests");
    }

    public ActionForward prepareEditDistributedTest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String distributedTestCode = getStringFromRequest(request, "distributedTestCode");
        final DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestCode);
        if (distributedTest == null) {
            throw new FenixActionException();
        }
        final List<LabelValueBean> testTypeList = TestType.getAllTypes();
        request.setAttribute("testTypeList", testTypeList);
        final List<LabelValueBean> correctionAvailabilityList = CorrectionAvailability.getAllAvailabilities();
        request.setAttribute("correctionAvailabilityList", correctionAvailabilityList);
        ((DynaActionForm) form).set("distributedTestCode", distributedTest.getExternalId());
        ((DynaActionForm) form).set("title", distributedTest.getTitle());
        if ((((DynaActionForm) form).get("testInformation")).equals("")) {
            ((DynaActionForm) form).set("testInformation", distributedTest.getTestInformation());
        }
        ((DynaActionForm) form).set("evaluationTitle", distributedTest.getEvaluationTitle());
        if ((((DynaActionForm) form).get("beginDayFormatted")).equals("")) {
            ((DynaActionForm) form).set("beginDayFormatted", distributedTest.getBeginDayFormatted());
        }
        if ((((DynaActionForm) form).get("beginMonthFormatted")).equals("")) {
            ((DynaActionForm) form).set("beginMonthFormatted", distributedTest.getBeginMonthFormatted());
        }
        if ((((DynaActionForm) form).get("beginYearFormatted")).equals("")) {
            ((DynaActionForm) form).set("beginYearFormatted", distributedTest.getBeginYearFormatted());
        }
        if ((((DynaActionForm) form).get("beginHourFormatted")).equals("")) {
            ((DynaActionForm) form).set("beginHourFormatted", distributedTest.getBeginHourFormatted());
        }
        if ((((DynaActionForm) form).get("beginMinuteFormatted")).equals("")) {
            ((DynaActionForm) form).set("beginMinuteFormatted", distributedTest.getBeginMinuteFormatted());
        }
        if ((((DynaActionForm) form).get("endDayFormatted")).equals("")) {
            ((DynaActionForm) form).set("endDayFormatted", distributedTest.getEndDayFormatted());
        }
        if ((((DynaActionForm) form).get("endMonthFormatted")).equals("")) {
            ((DynaActionForm) form).set("endMonthFormatted", distributedTest.getEndMonthFormatted());
        }
        if ((((DynaActionForm) form).get("endYearFormatted")).equals("")) {
            ((DynaActionForm) form).set("endYearFormatted", distributedTest.getEndYearFormatted());
        }
        if ((((DynaActionForm) form).get("endHourFormatted")).equals("")) {
            ((DynaActionForm) form).set("endHourFormatted", distributedTest.getEndHourFormatted());
        }
        if ((((DynaActionForm) form).get("endMinuteFormatted")).equals("")) {
            ((DynaActionForm) form).set("endMinuteFormatted", distributedTest.getEndMinuteFormatted());
        }
        if ((((DynaActionForm) form).get("testType")).equals("")) {
            ((DynaActionForm) form).set("testType", distributedTest.getTestType().getType().toString());
        }
        if ((((DynaActionForm) form).get("availableCorrection")).equals("")) {
            ((DynaActionForm) form).set("availableCorrection",
                    (distributedTest.getCorrectionAvailability().getAvailability().toString()));
        }
        if ((((DynaActionForm) form).get("imsFeedback")).equals("")) {
            ((DynaActionForm) form).set("imsFeedback", distributedTest.getImsFeedback().toString());
        }
        request.setAttribute("infoDistributedTest", distributedTest);
        return doForward(request, "editDistributedTest");
    }

    public ActionForward chooseAddShifts(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String distributedTestCode = getStringFromRequest(request, "distributedTestCode");

        final DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestCode);
        final ExecutionCourse executionCourse = getExecutionCourse(request);
        if (executionCourse == null) {
            throw new FenixActionException();
        }
        final Set<Registration> students = distributedTest.findStudents();
        final Set<Shift> associatedShifts = executionCourse.getAssociatedShifts();
        List<Shift> shiftList = new ArrayList<Shift>();
        for (Shift shift : associatedShifts) {
            Collection<Registration> shiftStudents = shift.getStudents();
            if (!students.containsAll(shiftStudents)) {
                shiftList.add(shift);
            }
        }
        // Collections.sort(shiftList, new InfoShiftComparatorByLessonType());
        request.setAttribute("shiftList", shiftList);
        request.setAttribute("distributedTestCode", distributedTestCode);
        return doForward(request, "addShiftsToDistributedTest");
    }

    public ActionForward chooseAddStudents(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String distributedTestCode = getStringFromRequest(request, "distributedTestCode");
        final List<Registration> studentList = new ArrayList<Registration>();
        final ExecutionCourse executionCourse = getExecutionCourse(request);
        final Collection<Attends> attendList = executionCourse.getAttends();
        final DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestCode);
        final Set<Registration> students = distributedTest.findStudents();
        for (Attends attend : attendList) {
            if (!students.contains(attend.getRegistration()) && attend.getRegistration().isActive()) {
                studentList.add(attend.getRegistration());
            }
        }

        Collections.sort(studentList, new BeanComparator("number"));
        request.setAttribute("studentList", studentList);
        request.setAttribute("distributedTestCode", distributedTestCode);
        return doForward(request, "addStudentsToDistributedTest");
    }

    public ActionForward editDistributedTest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String distributedTestCode = getStringFromRequest(request, "distributedTestCode");
        final String testInformation = request.getParameter("testInformation");
        final String evaluationTitle = request.getParameter("evaluationTitle");
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

        List<InfoStudent> infoStudentList = new ArrayList<InfoStudent>();
        try {
            EditDistributedTest.runEditDistributedTest(getExecutionCourse(request).getExternalId(), distributedTestCode,
                    testInformation, evaluationTitle, beginDate, beginHour, endDate, endHour, testTypeArg,
                    correctionAvailabilityArg, imsFeedbackArg);
        } catch (FenixServiceException e) {
            request.setAttribute("successfulEdition", new Boolean(false));
            return showDistributedTests(mapping, form, request, response);
        }

        request.setAttribute("successfulEdition", new Boolean(true));

        if (selected != null) {
            try {
                infoStudentList =
                        ReadStudentsByIdArray.runReadStudentsByIdArray(getExecutionCourse(request).getExternalId(),
                                distributedTestCode, selected, new Boolean(insertByShifts));
                AddStudentsToDistributedTest.runAddStudentsToDistributedTest(getExecutionCourse(request).getExternalId(),
                        distributedTestCode, infoStudentList);
            } catch (Exception e) {
                request.setAttribute("successfulStudentAddition", new Boolean(false));
                return showDistributedTests(mapping, form, request, response);
            }

        }
        return showDistributedTests(mapping, form, request, response);
    }

    public ActionForward prepareDeleteDistributedTest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String distributedTestCode = getStringFromRequest(request, "distributedTestCode");
        final DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestCode);
        if (distributedTest == null) {
            throw new FenixActionException();
        }
        final Boolean canDelete =
                (!(distributedTest.getTestType().getType().intValue() == TestType.EVALUATION && distributedTest.countResponses(
                        null, true) != 0));
        request.setAttribute("canDelete", canDelete);
        request.setAttribute("distributedTestCode", distributedTestCode);
        return doForward(request, "prepareDeleteDistributedTest");
    }

    public ActionForward deleteDistributedTest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String distributedTestCode = getStringFromRequest(request, "distributedTestCode");
        Boolean successfulTestDeletion = null;
        try {
            DeleteDistributedTest.runDeleteDistributedTest(getExecutionCourse(request).getExternalId(), distributedTestCode);
            successfulTestDeletion = true;
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("successfulTestDeletion", successfulTestDeletion);
        return showDistributedTests(mapping, form, request, response);
    }

    public ActionForward showStudentTest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String distributedTestCode = getStringFromRequest(request, "distributedTestCode");
        final String studentCode = getStringFromRequest(request, "studentCode");
        List<StudentTestQuestion> studentTestQuestionList = new ArrayList<StudentTestQuestion>();
        try {
            studentTestQuestionList =
                    ReadStudentDistributedTest.runReadStudentDistributedTest(getExecutionCourse(request).getExternalId(),
                            distributedTestCode, studentCode);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        Collections.sort(studentTestQuestionList, StudentTestQuestion.COMPARATOR_BY_TEST_QUESTION_ORDER);
        request.setAttribute("studentTestQuestionList", studentTestQuestionList);
        double classification = 0;

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
            classification += studentTestQuestion.getTestQuestionMark().doubleValue();
        }
        request.setAttribute("studentTestForm", form);
        if (classification < 0) {
            classification = 0;
        }
        request.setAttribute("classification", (new DecimalFormat("#0.##").format(classification)));
        return doForward(request, "showStudentTestCorrection");
    }

    public ActionForward showStudentTestLog(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String distributedTestCode = getStringFromRequest(request, "distributedTestCode");
        final String studentCode = getStringFromRequest(request, "studentCode");
        final Registration student = FenixFramework.getDomainObject(studentCode);
        if (student == null) {
            throw new FenixActionException();
        }
        final DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestCode);
        if (distributedTest == null) {
            throw new FenixActionException();
        }

        List<StudentTestLog> studentTestLogList = new ArrayList<StudentTestLog>(distributedTest.getStudentTestLogs(student));
        Collections.sort(studentTestLogList, new BeanComparator("dateDateTime"));

        request.setAttribute("questionNumber", distributedTest.getNumberOfQuestions());
        request.setAttribute("studentTestLogList", studentTestLogList);
        request.setAttribute("distributedTestCode", distributedTestCode);
        return doForward(request, "showStudentTestLog");
    }

    public ActionForward prepareValidateTestChecksum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String distributedTestCode = getStringFromRequest(request, "distributedTestCode");
        final String studentCode = getStringFromRequest(request, "studentCode");
        final Registration student = FenixFramework.getDomainObject(studentCode);
        if (student == null) {
            throw new FenixActionException();
        }
        final DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestCode);
        if (distributedTest == null) {
            throw new FenixActionException();
        }

        request.setAttribute("registration", student);
        request.setAttribute("distributedTest", distributedTest);
        return doForward(request, "validateStudentTestChecksum");
    }

    public ActionForward validateTestChecksum(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String distributedTestCode = getStringFromRequest(request, "distributedTestCode");
        final String studentCode = getStringFromRequest(request, "studentCode");

        final Registration student = FenixFramework.getDomainObject(studentCode);
        if (student == null) {
            throw new FenixActionException();
        }
        final DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestCode);
        if (distributedTest == null) {
            throw new FenixActionException();
        }

        request.setAttribute("registration", student);
        request.setAttribute("distributedTest", distributedTest);

        final String dateString = request.getParameter("date");
        final DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss");
        try {
            final DateTime datetime = dateTimeFormat.parseDateTime(dateString);
            final String checksum = StudentTestLog.getChecksum(distributedTest, student, datetime);
            request.setAttribute("checksum", checksum);
        } catch (final IllegalArgumentException ex) {
            error(request, "date", "error.date.invalid");
            return doForward(request, "validateStudentTestChecksum");
        }
        return doForward(request, "validateStudentTestChecksum");
    }

    public ActionForward showTestMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String distributedTestCode = getStringFromRequest(request, "distributedTestCode");
        InfoSiteStudentsTestMarks infoSiteStudentsTestMarks = null;
        try {
            infoSiteStudentsTestMarks =
                    ReadDistributedTestMarks.runReadDistributedTestMarks(getExecutionCourse(request).getExternalId(),
                            distributedTestCode);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("distributedTestCode", distributedTestCode);
        request.setAttribute("infoSiteStudentsTestMarks", infoSiteStudentsTestMarks);
        return doForward(request, "showTestMarks");
    }

    public ActionForward showTestMarksStatistics(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String distributedTestCode = getStringFromRequest(request, "distributedTestCode");
        SiteView siteView = null;
        try {
            siteView =
                    ReadDistributedTestMarksStatistics.runReadDistributedTestMarksStatistics(getExecutionCourse(request)
                            .getExternalId(), distributedTestCode);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("distributedTestCode", distributedTestCode);
        request.setAttribute("siteView", siteView);
        return doForward(request, "showTestMarksStatistics");
    }

    public ActionForward showTestStatistics(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String distributedTestCode = getStringFromRequest(request, "distributedTestCode");
        List<InfoInquiryStatistics> infoInquiryStatisticsList = null;
        try {
            infoInquiryStatisticsList =
                    ReadInquiryStatistics.runReadInquiryStatistics(getExecutionCourse(request).getExternalId(),
                            distributedTestCode);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("distributedTestCode", distributedTestCode);
        request.setAttribute("infoInquiryStatisticsList", infoInquiryStatisticsList);
        return doForward(request, "showTestStatistics");
    }

    public ActionForward downloadTestMarks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String distributedTestCode = getStringFromRequest(request, "distributedTestCode");
        String result = null;
        try {
            if (distributedTestCode != null) {
                result =
                        ReadDistributedTestMarksToString.runReadDistributedTestMarksToString(getExecutionCourse(request)
                                .getExternalId(), distributedTestCode);
            } else {
                result =
                        ReadDistributedTestMarksToString.runReadDistributedTestMarksToString(getExecutionCourse(request)
                                .getExternalId(), request.getParameterValues("distributedTestCodes"));
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

    public ActionForward prepareChangeStudentTestQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("questionCode", getFromRequest(request, "questionCode"));
        request.setAttribute("distributedTestCode", getFromRequest(request, "distributedTestCode"));
        request.setAttribute("studentCode", getFromRequest(request, "studentCode"));
        List<LabelValueBean> changesTypeList = (new TestQuestionChangesType()).getAllTypes();
        request.setAttribute("changesTypeList", changesTypeList);
        List<LabelValueBean> studentsTypeList = (new TestQuestionStudentsChangesType()).getAllTypes();
        request.setAttribute("studentsTypeList", studentsTypeList);
        ((DynaActionForm) form).set("changesType", "1");
        ((DynaActionForm) form).set("deleteVariation", "true");
        ((DynaActionForm) form).set("studentsType", "4");
        return doForward(request, "changeStudentTestQuestion");
    }

    public ActionForward chooseAnotherExercise(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String distributedTestId = getStringFromRequest(request, "distributedTestCode");
        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(getExecutionCourse(request).getExternalId());
        final DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestId);
        Set<Metadata> metadataList =
                Metadata.findVisibleMetadataFromExecutionCourseNotOfDistributedTest(executionCourse, distributedTest);
        request.setAttribute("questionCode", getFromRequest(request, "questionCode"));
        request.setAttribute("distributedTestCode", distributedTestId);
        request.setAttribute("studentCode", getFromRequest(request, "studentCode"));
        request.setAttribute("successfulChanged", request.getAttribute("successfulChanged"));
        request.setAttribute("studentsType", request.getAttribute("studentsType"));
        request.setAttribute("changesType", request.getAttribute("changesType"));
        request.setAttribute("deleteVariation", request.getAttribute("deleteVariation"));
        request.setAttribute("metadataList", metadataList);
        return doForward(request, "chooseAnotherExercise");
    }

    public ActionForward changeStudentTestQuestion(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String questionId = getStringFromRequest(request, "questionCode");
        final String distributedTestId = getStringFromRequest(request, "distributedTestCode");
        final String studentId = getStringFromRequest(request, "studentCode");
        final String metadataId = getStringFromRequest(request, "metadataCode");
        final String changesType = request.getParameter("changesType");
        final String delete = request.getParameter("deleteVariation");
        final String studentsType = request.getParameter("studentsType");
        request.setAttribute("studentCode", studentId);
        request.setAttribute("distributedTestCode", distributedTestId);
        if (((new TestQuestionChangesType(new Integer(changesType))).getType().intValue() == 2) && (metadataId == null)) {
            request.setAttribute("deleteVariation", delete);
            request.setAttribute("studentsType", studentsType);
            request.setAttribute("changesType", changesType);
            return chooseAnotherExercise(mapping, form, request, response);
        }
        Boolean result = Boolean.FALSE;
        try {
            result =
                    ChangeStudentTestQuestion.runChangeStudentTestQuestion(getExecutionCourse(request), distributedTestId,
                            questionId, metadataId, studentId, new TestQuestionChangesType(new Integer(changesType)),
                            new Boolean(delete), new TestQuestionStudentsChangesType(new Integer(studentsType)),
                            request.getContextPath());
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        if (!result.booleanValue()) {
            request.setAttribute("successfulChanged", new Boolean(false));
            request.setAttribute("studentsType", studentsType);
            request.setAttribute("changesType", changesType);
            return chooseAnotherExercise(mapping, form, request, response);
        }
        return doForward(request, "showStudentTestCorrection");
    }

    public ActionForward prepareChangeStudentTestQuestionValue(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute("questionCode", getFromRequest(request, "questionCode"));
        request.setAttribute("distributedTestCode", getFromRequest(request, "distributedTestCode"));
        request.setAttribute("studentCode", getFromRequest(request, "studentCode"));
        List<LabelValueBean> studentsTypeList = (new TestQuestionStudentsChangesType()).getAllTypes();
        request.setAttribute("studentsTypeList", studentsTypeList);
        ((DynaActionForm) form).set("studentsType", "1");
        ((DynaActionForm) form).set("questionValue", request.getParameter("questionValue"));
        return doForward(request, "changeStudentTestQuestionValue");
    }

    public ActionForward changeStudentTestQuestionValue(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String distributedTestCode = getStringFromRequest(request, "distributedTestCode");
        final String studentCode = getStringFromRequest(request, "studentCode");
        final String questionCode = getStringFromRequest(request, "questionCode");
        final String studentTypeString = (String) ((DynaActionForm) form).get("studentsType");
        final String questionValueString = (String) ((DynaActionForm) form).get("questionValue");

        try {
            ChangeStudentTestQuestionValue.runChangeStudentTestQuestionValue(getExecutionCourse(request).getExternalId(),
                    distributedTestCode, new Double(questionValueString), questionCode, studentCode,
                    new TestQuestionStudentsChangesType(new Integer(studentTypeString)));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("distributedTestCode", distributedTestCode);
        request.setAttribute("studentCode", studentCode);
        return doForward(request, "showStudentTestCorrection");
    }

    public ActionForward prepareChangeStudentTestQuestionMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) {
        request.setAttribute("questionCode", getFromRequest(request, "questionCode"));
        request.setAttribute("distributedTestCode", getFromRequest(request, "distributedTestCode"));
        request.setAttribute("studentCode", getFromRequest(request, "studentCode"));
        List<LabelValueBean> studentsTypeList = (new TestQuestionStudentsChangesType()).getAllTypes();
        request.setAttribute("studentsTypeList", studentsTypeList);
        ((DynaActionForm) form).set("studentsType", "1");
        ((DynaActionForm) form).set("questionValue", request.getParameter("questionValue"));
        return doForward(request, "changeStudentTestQuestionMark");
    }

    public ActionForward changeStudentTestQuestionMark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String distributedTestCode = getStringFromRequest(request, "distributedTestCode");
        final String studentCode = getStringFromRequest(request, "studentCode");
        final String questionCode = getStringFromRequest(request, "questionCode");
        final String studentTypeString = (String) ((DynaActionForm) form).get("studentsType");
        final String questionValueString = (String) ((DynaActionForm) form).get("questionValue");

        try {
            ChangeStudentTestQuestionMark.runChangeStudentTestQuestionMark(getExecutionCourse(request).getExternalId(),
                    distributedTestCode, new Double(questionValueString.replaceAll(",", ".")), questionCode, studentCode,
                    new TestQuestionStudentsChangesType(new Integer(studentTypeString)));
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        request.setAttribute("distributedTestCode", distributedTestCode);
        request.setAttribute("studentCode", studentCode);
        return doForward(request, "showStudentTestCorrection");
    }

    public ActionForward chooseTestSimulationOptions(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String testCode = getStringFromRequest(request, "testCode");
        Test test = FenixFramework.getDomainObject(testCode);
        if (test.getTestQuestionsSet().size() >= 1) {
            List<LabelValueBean> testTypeList = TestType.getAllTypes();
            request.setAttribute("testTypeList", testTypeList);
            List<LabelValueBean> correctionAvailabilityList = CorrectionAvailability.getAllAvailabilities();
            request.setAttribute("correctionAvailabilityList", correctionAvailabilityList);
            if ((((DynaActionForm) form).get("testType")).equals("")) {
                ((DynaActionForm) form).set("testType", "1");
            }
            if ((((DynaActionForm) form).get("availableCorrection")).equals("")) {
                ((DynaActionForm) form).set("availableCorrection", "3");
            }
            if ((((DynaActionForm) form).get("imsFeedback")).equals("")) {
                ((DynaActionForm) form).set("imsFeedback", "true");
            }
            if ((((DynaActionForm) form).get("testInformation")).equals("")) {
                ((DynaActionForm) form).set("testInformation", createDefaultDistributedTestInfo(test));
                ((DynaActionForm) form).set("notInquiryInformation", ((DynaActionForm) form).get("testInformation"));
            }
            if ((((DynaActionForm) form).get("inquiryInformation")).equals("")) {
                ((DynaActionForm) form).set("inquiryInformation", createDefaultDistributedInquiryInfo());
            }
            request.setAttribute("testCode", testCode);
            return doForward(request, "chooseTestSimulationOptions");
        }
        error(request, "InvalidDistribution", "error.distributeTest.noExercise");
        return showTests(mapping, form, request, response);
    }

    public ActionForward prepareSimulateTest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        String testId = getStringFromRequest(request, "testCode");
        if (testId == null) {
            testId = getStringFromRequest(request, "distributedTestCode");
        }
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

        List<InfoStudentTestQuestion> infoStudentTestQuestionList = new ArrayList<InfoStudentTestQuestion>();
        try {
            infoStudentTestQuestionList =
                    GenetareStudentTestForSimulation.runGenetareStudentTestForSimulation(getExecutionCourse(request)
                            .getExternalId(), testId, getServlet().getServletContext().getRealPath("/"), testTypeArg,
                            correctionAvailabilityArg, imsFeedbackArg, testInformation);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }
        for (int i = 0; i < infoStudentTestQuestionList.size(); i++) {
            InfoStudentTestQuestion infoStudentTestQuestion = infoStudentTestQuestionList.get(i);

            if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.STR
                    && ((ResponseSTR) infoStudentTestQuestion.getResponse()).getResponse() != null) {
                request.setAttribute("question" + i, ((ResponseSTR) infoStudentTestQuestion.getResponse()).getResponse());
            } else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.NUM
                    && ((ResponseNUM) infoStudentTestQuestion.getResponse()).getResponse() != null) {
                request.setAttribute("question" + i, ((ResponseNUM) infoStudentTestQuestion.getResponse()).getResponse());
            } else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.LID
                    && ((ResponseLID) infoStudentTestQuestion.getResponse()).getResponse() != null) {
                if (infoStudentTestQuestion.getQuestion().getQuestionType().getCardinalityType().getType().intValue() == CardinalityType.SINGLE) {
                    request.setAttribute("question" + i, ((ResponseLID) infoStudentTestQuestion.getResponse()).getResponse()[0]);
                } else {
                    request.setAttribute("question" + i, ((ResponseLID) infoStudentTestQuestion.getResponse()).getResponse());
                }
            }
        }

        request.setAttribute("studentTestForm", form);
        request.setAttribute("simulate", new Boolean(true));
        request.setAttribute("infoStudentTestQuestionList", infoStudentTestQuestionList);
        return doForward(request, "doTestSimulation");
    }

    public ActionForward simulateTest(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        final String distributedTestId = getStringFromRequest(request, "distributedTestCode");

        final String testInformation = request.getParameter("testInformation");
        final String testType = request.getParameter("testType");
        final String availableCorrection = request.getParameter("availableCorrection");
        final String imsFeedback = request.getParameter("imsFeedback");
        final TestType testTypeArg = new TestType(new Integer(testType));
        CorrectionAvailability correctionAvailabilityArg = new CorrectionAvailability(new Integer(availableCorrection));
        Boolean imsFeedbackArg = new Boolean(imsFeedback);
        if (testTypeArg.equals(new TestType(TestType.INQUIRY))) {
            correctionAvailabilityArg = new CorrectionAvailability(CorrectionAvailability.NEVER);
            imsFeedbackArg = new Boolean(false);
        }
        final String path = getServlet().getServletContext().getRealPath("/");
        Test test = FenixFramework.getDomainObject(distributedTestId);

        for (TestQuestion testQuestion : test.getTestQuestionsSet()) {
            ParseSubQuestion parse = new ParseSubQuestion();
            try {
                parse.parseSubQuestion(testQuestion.getQuestion());
            } catch (ParseQuestionException e) {
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
            if (new Integer(questionTypes[i]).intValue() == QuestionType.STR) {
                userResponse[i] = new ResponseSTR(request.getParameter("question" + i));
            } else if (new Integer(questionTypes[i]).intValue() == QuestionType.NUM) {
                userResponse[i] = new ResponseNUM(request.getParameter("question" + i));
            } else if (new Integer(questionTypes[i]).intValue() == QuestionType.LID) {
                userResponse[i] = new ResponseLID(request.getParameterValues("question" + i));
            }
        }
        InfoSiteStudentTestFeedback infoSiteStudentTestFeedback = null;
        try {
            infoSiteStudentTestFeedback =
                    SimulateTest.runSimulateTest(getExecutionCourse(request).getExternalId(), distributedTestId, userResponse,
                            questionCodes, optionsShuffle, testTypeArg, correctionAvailabilityArg, imsFeedbackArg,
                            testInformation, path);
        } catch (FenixServiceException e) {
            throw new FenixActionException(e);
        }

        if (infoSiteStudentTestFeedback != null) {
            List infoStudentTestQuestionList = infoSiteStudentTestFeedback.getStudentTestQuestionList();
            for (int i = 0; i < infoStudentTestQuestionList.size(); i++) {
                InfoStudentTestQuestion infoStudentTestQuestion = (InfoStudentTestQuestion) infoStudentTestQuestionList.get(i);

                if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.STR
                        && ((ResponseSTR) infoStudentTestQuestion.getResponse()).getResponse() != null) {
                    request.setAttribute("question" + i, ((ResponseSTR) infoStudentTestQuestion.getResponse()).getResponse());
                } else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.NUM
                        && ((ResponseNUM) infoStudentTestQuestion.getResponse()).getResponse() != null) {
                    request.setAttribute("question" + i, ((ResponseNUM) infoStudentTestQuestion.getResponse()).getResponse());
                } else if (infoStudentTestQuestion.getQuestion().getQuestionType().getType().intValue() == QuestionType.LID
                        && ((ResponseLID) infoStudentTestQuestion.getResponse()).getResponse() != null) {
                    if (infoStudentTestQuestion.getQuestion().getQuestionType().getCardinalityType().getType().intValue() == CardinalityType.SINGLE) {
                        request.setAttribute("question" + i,
                                ((ResponseLID) infoStudentTestQuestion.getResponse()).getResponse()[0]);
                    } else {
                        request.setAttribute("question" + i, ((ResponseLID) infoStudentTestQuestion.getResponse()).getResponse());
                    }
                }
                if (((InfoStudentTestQuestion) infoSiteStudentTestFeedback.getStudentTestQuestionList().iterator().next())
                        .getDistributedTest().getCorrectionAvailability().getAvailability().intValue() == CorrectionAvailability.ALWAYS) {
                    request.setAttribute("infoStudentTestQuestionList", infoSiteStudentTestFeedback.getStudentTestQuestionList());
                }
            }
            if (request.getAttribute("showCorrection") != null) {
                request.setAttribute("infoStudentTestQuestionList", infoSiteStudentTestFeedback.getStudentTestQuestionList());
                return doForward(request, "showSimulationCorrection");
            }
            if (request.getAttribute("doTestSimulation") != null || request.getParameter("doTestSimulation") != null) {
                request.setAttribute("infoStudentTestQuestionList", infoSiteStudentTestFeedback.getStudentTestQuestionList());
                return doForward(request, "doTestSimulation");
            }

        }
        request.setAttribute("infoSiteStudentTestFeedback", infoSiteStudentTestFeedback);
        return doForward(request, "showSimulationFeedback");
    }

    public ActionForward showSimulationCorrection(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
        request.setAttribute("showCorrection", new Boolean(true));
        return simulateTest(mapping, form, request, response);
    }

    public ActionForward doTestSimulation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
            HttpServletResponse response) throws FenixActionException {
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
            if (objectCode instanceof String) {
                code = new Integer((String) objectCode);
            } else if (objectCode instanceof Integer) {
                code = (Integer) objectCode;
            }
        } else {
            String thisCodeString = request.getParameter(codeString);
            if (thisCodeString != null) {
                code = new Integer(thisCodeString);
            }
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
        return MessageFormat.format(BundleUtil.getString(Bundle.APPLICATION, "message.distributeTest.evaluation"), new Object[] {
                test.getTitle(), test.getTestQuestions().size() });
    }

    private String createDefaultDistributedInquiryInfo() {
        return BundleUtil.getString(Bundle.APPLICATION, "message.distributeTest.inquiry");
    }
}