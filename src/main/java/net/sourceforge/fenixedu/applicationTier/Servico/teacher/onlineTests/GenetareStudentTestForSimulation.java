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
import java.util.Random;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoTestScope;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.util.tests.CorrectionAvailability;
import net.sourceforge.fenixedu.util.tests.TestType;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class GenetareStudentTestForSimulation {
    protected List run(String executionCourseId, String testId, String path, TestType testType,
            CorrectionAvailability correctionAvailability, Boolean imsfeedback, String testInformation)
            throws FenixServiceException {
        List<InfoStudentTestQuestion> infoStudentTestQuestionList = new ArrayList<InfoStudentTestQuestion>();
        path = path.replace('\\', '/');
        final Test test = FenixFramework.getDomainObject(testId);
        if (test == null) {
            throw new InvalidArgumentsServiceException();
        }

        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);
        if (executionCourse == null) {
            throw new InvalidArgumentsServiceException();
        }

        TestScope testScope = executionCourse.getTestScope();
        if (testScope == null) {
            testScope = new TestScope(executionCourse);
        }

        InfoDistributedTest infoDistributedTest = new InfoDistributedTest();
        infoDistributedTest.setExternalId(testId);
        infoDistributedTest.setInfoTestScope(InfoTestScope.newInfoFromDomain(testScope));
        infoDistributedTest.setTestType(testType);
        infoDistributedTest.setCorrectionAvailability(correctionAvailability);
        infoDistributedTest.setImsFeedback(imsfeedback);
        infoDistributedTest.setTitle(test.getTitle());
        infoDistributedTest.setTestInformation(testInformation);
        infoDistributedTest.setNumberOfQuestions(test.getTestQuestionsSet().size());

        List<TestQuestion> testQuestionList = new ArrayList<TestQuestion>(test.getTestQuestions());
        Collections.sort(testQuestionList, new BeanComparator("testQuestionOrder"));
        for (TestQuestion testQuestionExample : testQuestionList) {
            List<Question> questionList = new ArrayList<Question>();
            questionList.addAll(testQuestionExample.getQuestion().getMetadata().getVisibleQuestions());

            InfoStudentTestQuestion infoStudentTestQuestion = new InfoStudentTestQuestion();
            // infoStudentTestQuestion.setDistributedTest(distributedTest);
            infoStudentTestQuestion.setTestQuestionOrder(testQuestionExample.getTestQuestionOrder());
            infoStudentTestQuestion.setTestQuestionValue(testQuestionExample.getTestQuestionValue());
            infoStudentTestQuestion.setOldResponse(Integer.valueOf(0));
            infoStudentTestQuestion.setCorrectionFormula(testQuestionExample.getCorrectionFormula());
            infoStudentTestQuestion.setTestQuestionMark(Double.valueOf(0));
            infoStudentTestQuestion.setResponse(null);

            if (questionList.size() == 0) {
                questionList.addAll(testQuestionExample.getQuestion().getMetadata().getVisibleQuestions());
            }
            Question question = getStudentQuestion(questionList);
            if (question == null) {
                throw new InvalidArgumentsServiceException();
            }
            infoStudentTestQuestion.setQuestion(InfoQuestion.newInfoFromDomain(question));
            ParseSubQuestion parse = new ParseSubQuestion();
            // try {
            // // studentTestQuestion =
            // parse.parseStudentTestQuestion(studentTestQuestion, path);
            // } catch (Exception e) {
            // throw new FenixServiceException(e);
            // }
            infoStudentTestQuestionList.add(infoStudentTestQuestion);
            questionList.remove(question);
        }

        return infoStudentTestQuestionList;
    }

    private Question getStudentQuestion(List<Question> questions) {
        Question question = null;
        if (questions.size() != 0) {
            Random r = new Random();
            int questionIndex = r.nextInt(questions.size());
            question = questions.get(questionIndex);
        }
        return question;
    }

    // Service Invokers migrated from Berserk

    private static final GenetareStudentTestForSimulation serviceInstance = new GenetareStudentTestForSimulation();

    @Atomic
    public static List runGenetareStudentTestForSimulation(String executionCourseId, String testId, String path,
            TestType testType, CorrectionAvailability correctionAvailability, Boolean imsfeedback, String testInformation)
            throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, testId, path, testType, correctionAvailability, imsfeedback,
                testInformation);
    }

}