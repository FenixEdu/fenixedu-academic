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
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteStudentTestFeedback;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.util.tests.CorrectionAvailability;
import net.sourceforge.fenixedu.util.tests.Response;
import net.sourceforge.fenixedu.util.tests.ResponseProcessing;
import net.sourceforge.fenixedu.util.tests.TestType;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class SimulateTest {

    private final String path = new String();

    protected InfoSiteStudentTestFeedback run(String executionCourseId, String testId, Response[] responses,
            String[] questionCodes, String[] optionShuffle, TestType testType, CorrectionAvailability correctionAvailability,
            Boolean imsfeedback, String testInformation, String path) throws FenixServiceException {

        // InfoSiteStudentTestFeedback infoSiteStudentTestFeedback = new
        // InfoSiteStudentTestFeedback();
        // this.path = path.replace('\\', '/');
        // Test test = FenixFramework.getDomainObject(testId);
        // if (test == null)
        // throw new FenixServiceException();
        //
        // double totalMark = 0;
        // int responseNumber = 0;
        // int notResponseNumber = 0;
        // List<String> errors = new ArrayList<String>();
        //
        // TestScope testScope =
        // TestScope.readByDomainObject(ExecutionCourse.class,
        // executionCourseId);
        //
        // if (testScope == null) {
        // ExecutionCourse executionCourse =
        // FenixFramework.getDomainObject(executionCourseId);
        // if (executionCourse == null)
        // throw new InvalidArgumentsServiceException();
        // testScope = DomainFactory.makeTestScope(executionCourse);
        // }
        //
        // InfoDistributedTest infoDistributedTest = new InfoDistributedTest();
        // infoDistributedTest.setExternalId(testId);
        // infoDistributedTest.setInfoTestScope(InfoTestScope.newInfoFromDomain(
        // testScope));
        // infoDistributedTest.setTestType(testType);
        // infoDistributedTest.setCorrectionAvailability(correctionAvailability);
        // infoDistributedTest.setImsFeedback(imsfeedback);
        // infoDistributedTest.setTestInformation(testInformation);
        // infoDistributedTest.setTitle(test.getTitle());
        // infoDistributedTest.setNumberOfQuestions(test.getTestQuestionsCount())
        // ;
        //
        // List<InfoStudentTestQuestion> infoStudentTestQuestionList =
        // getInfoStudentTestQuestionList(questionCodes, optionShuffle,
        // responses, infoDistributedTest, testId);
        // if (infoStudentTestQuestionList.size() == 0)
        // return null;
        // for (InfoStudentTestQuestion infoStudentTestQuestion :
        // infoStudentTestQuestionList) {
        // if (infoStudentTestQuestion.getResponse().isResponsed()) {
        // responseNumber++;
        //
        // IQuestionCorrectionStrategyFactory questionCorrectionStrategyFactory
        // = QuestionCorrectionStrategyFactory.getInstance();
        // IQuestionCorrectionStrategy questionCorrectionStrategy =
        // questionCorrectionStrategyFactory
        // .getQuestionCorrectionStrategy(infoStudentTestQuestion);
        //
        // String error =
        // questionCorrectionStrategy.validResponse(infoStudentTestQuestion);
        // if (error == null) {
        // if ((!infoDistributedTest.getTestType().equals(new
        // TestType(TestType.INQUIRY)))
        // &&
        // infoStudentTestQuestion.getQuestion().getResponseProcessingInstructions
        // ().size() != 0) {
        //
        // infoStudentTestQuestion =
        // questionCorrectionStrategy.getMark(infoStudentTestQuestion);
        // }
        // totalMark +=
        // infoStudentTestQuestion.getTestQuestionMark().doubleValue();
        // } else {
        // notResponseNumber++;
        // responseNumber--;
        // errors.add(error);
        // if
        // (infoStudentTestQuestion.getQuestion().getQuestionType().getType().
        // intValue() == QuestionType.LID)
        // infoStudentTestQuestion.setResponse(new ResponseLID());
        // else if
        // (infoStudentTestQuestion.getQuestion().getQuestionType().getType
        // ().intValue() == QuestionType.STR)
        // infoStudentTestQuestion.setResponse(new ResponseSTR());
        // else if
        // (infoStudentTestQuestion.getQuestion().getQuestionType().getType
        // ().intValue() == QuestionType.NUM)
        // infoStudentTestQuestion.setResponse(new ResponseNUM());
        // }
        // } else
        // notResponseNumber++;
        // }
        //
        // infoSiteStudentTestFeedback.setResponseNumber(Integer.valueOf(
        // responseNumber));
        // infoSiteStudentTestFeedback.setNotResponseNumber(Integer.valueOf(
        // notResponseNumber));
        // infoSiteStudentTestFeedback.setErrors(errors);
        //
        // infoSiteStudentTestFeedback.setInfoStudentTestQuestionList(
        // infoStudentTestQuestionList);
        // return infoSiteStudentTestFeedback;
        return null;
    }

    private InfoQuestion correctQuestionValues(InfoQuestion infoQuestion, Double questionValue) {
        Double maxValue = Double.valueOf(0);
        for (ResponseProcessing responseProcessing : infoQuestion.getResponseProcessingInstructions()) {
            if (responseProcessing.getAction().intValue() == ResponseProcessing.SET
                    || responseProcessing.getAction().intValue() == ResponseProcessing.ADD) {
                if (maxValue.compareTo(responseProcessing.getResponseValue()) < 0) {
                    maxValue = responseProcessing.getResponseValue();
                }
            }
        }
        if (maxValue.compareTo(questionValue) != 0) {
            double difValue = questionValue * Math.pow(maxValue, -1);
            for (ResponseProcessing responseProcessing : infoQuestion.getResponseProcessingInstructions()) {
                responseProcessing.setResponseValue(Double.valueOf(responseProcessing.getResponseValue() * difValue));
            }
        }

        return infoQuestion;
    }

    private List<InfoStudentTestQuestion> getInfoStudentTestQuestionList(String[] questionCodes, String[] optionShuffle,
            Response[] responses, InfoDistributedTest infoDistributedTest, String testId)
            throws InvalidArgumentsServiceException, FenixServiceException {
        List<InfoStudentTestQuestion> infoStudentTestQuestionList = new ArrayList<InfoStudentTestQuestion>();

        Test test = FenixFramework.getDomainObject(testId);
        List<TestQuestion> testQuestionList = new ArrayList<TestQuestion>(test.getTestQuestions());
        Collections.sort(testQuestionList, new BeanComparator("testQuestionOrder"));
        for (int i = 0; i < testQuestionList.size(); i++) {
            TestQuestion testQuestionExample = testQuestionList.get(i);
            InfoStudentTestQuestion infoStudentTestQuestion = new InfoStudentTestQuestion();
            // infoStudentTestQuestion.setDistributedTest(distributedTest);
            infoStudentTestQuestion.setTestQuestionOrder(testQuestionExample.getTestQuestionOrder());
            infoStudentTestQuestion.setTestQuestionValue(testQuestionExample.getTestQuestionValue());
            infoStudentTestQuestion.setOldResponse(Integer.valueOf(0));
            infoStudentTestQuestion.setCorrectionFormula(testQuestionExample.getCorrectionFormula());
            infoStudentTestQuestion.setTestQuestionMark(Double.valueOf(0));
            infoStudentTestQuestion.setResponse(null);
            Question question = FenixFramework.getDomainObject(questionCodes[i]);
            if (question == null) {
                throw new InvalidArgumentsServiceException();
            }
            infoStudentTestQuestion.setQuestion(InfoQuestion.newInfoFromDomain(question));
            // ParseQuestion parse = new ParseQuestion();
            // try {
            // infoStudentTestQuestion.setOptionShuffle(optionShuffle[i]);
            // infoStudentTestQuestion =
            // parse.parseStudentTestQuestion(infoStudentTestQuestion,
            // this.path);
            // infoStudentTestQuestion.setQuestion(correctQuestionValues(
            // infoStudentTestQuestion.getQuestion(),
            // Double.valueOf(infoStudentTestQuestion.getTestQuestionValue())));
            // infoStudentTestQuestion.setResponse(responses[i]);
            // } catch (Exception e) {
            // throw new FenixServiceException(e);
            // }
            infoStudentTestQuestionList.add(infoStudentTestQuestion);
        }
        return infoStudentTestQuestionList;
    }

    // Service Invokers migrated from Berserk

    private static final SimulateTest serviceInstance = new SimulateTest();

    @Atomic
    public static InfoSiteStudentTestFeedback runSimulateTest(String executionCourseId, String testId, Response[] responses,
            String[] questionCodes, String[] optionShuffle, TestType testType, CorrectionAvailability correctionAvailability,
            Boolean imsfeedback, String testInformation, String path) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, testId, responses, questionCodes, optionShuffle, testType,
                correctionAvailability, imsfeedback, testInformation, path);
    }

}