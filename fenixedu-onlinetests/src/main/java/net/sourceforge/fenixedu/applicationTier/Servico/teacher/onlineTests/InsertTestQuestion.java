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
package org.fenixedu.academic.service.services.teacher.onlineTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.fenixedu.academic.service.filter.ExecutionCourseLecturingTeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.dto.onlineTests.InfoQuestion;
import org.fenixedu.academic.domain.onlineTests.Metadata;
import org.fenixedu.academic.domain.onlineTests.Question;
import org.fenixedu.academic.domain.onlineTests.SubQuestion;
import org.fenixedu.academic.domain.onlineTests.Test;
import org.fenixedu.academic.domain.onlineTests.TestQuestion;
import org.fenixedu.academic.domain.onlineTests.utils.ParseSubQuestion;
import org.fenixedu.academic.util.tests.CorrectionFormula;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class InsertTestQuestion {

    protected void run(String executionCourseId, String testId, String[] metadataId, Integer questionOrder, Double questionValue,
            CorrectionFormula formula) throws FenixServiceException {

        for (String element : metadataId) {
            Metadata metadata = FenixFramework.getDomainObject(element);
            if (metadata == null) {
                throw new InvalidArgumentsServiceException();
            }
            Question question = null;
            if (metadata.getVisibleQuestions() != null && metadata.getVisibleQuestions().size() != 0) {
                question = metadata.getVisibleQuestions().iterator().next();
            } else {
                throw new InvalidArgumentsServiceException();
            }
            if (question == null) {
                throw new InvalidArgumentsServiceException();
            }
            Test test = FenixFramework.getDomainObject(testId);
            if (test == null) {
                throw new InvalidArgumentsServiceException();
            }
            List<TestQuestion> testQuestionList = new ArrayList<TestQuestion>(test.getTestQuestionsSet());
            Collections.sort(testQuestionList, new BeanComparator("testQuestionOrder"));
            if (testQuestionList != null) {
                if (questionOrder == null || questionOrder.equals(Integer.valueOf(-1))) {
                    questionOrder = Integer.valueOf(testQuestionList.size() + 1);
                } else {
                    questionOrder = Integer.valueOf(questionOrder.intValue() + 1);
                }
                for (TestQuestion iterTestQuestion : testQuestionList) {
                    Integer iterQuestionOrder = iterTestQuestion.getTestQuestionOrder();
                    if (questionOrder.compareTo(iterQuestionOrder) <= 0) {
                        iterTestQuestion.setTestQuestionOrder(Integer.valueOf(iterQuestionOrder.intValue() + 1));
                    }
                }
            }
            Double thisQuestionValue = questionValue;
            if (questionValue == null) {
                ParseSubQuestion parseQuestion = new ParseSubQuestion();
                if (thisQuestionValue == null) {
                    thisQuestionValue = new Double(0);
                }
                try {
                    InfoQuestion infoQuestion = InfoQuestion.newInfoFromDomain(question);
                    question = parseQuestion.parseSubQuestion(question);
                    if (infoQuestion.getQuestionValue() != null) {
                        thisQuestionValue = infoQuestion.getQuestionValue();
                    } else {
                        for (SubQuestion subQuestion : question.getSubQuestions()) {
                            if (subQuestion.getQuestionValue() != null) {
                                thisQuestionValue =
                                        new Double(thisQuestionValue.doubleValue() + subQuestion.getQuestionValue().doubleValue());
                            }
                        }
                    }

                } catch (Exception e) {
                    throw new FenixServiceException(e);
                }

            }
            TestQuestion testQuestion = new TestQuestion();
            test.setLastModifiedDate(Calendar.getInstance().getTime());
            testQuestion.setQuestion(question);
            testQuestion.setTest(test);
            testQuestion.setTestQuestionOrder(questionOrder);
            testQuestion.setTestQuestionValue(thisQuestionValue);
            testQuestion.setCorrectionFormula(formula);
        }
    }

    // Service Invokers migrated from Berserk

    private static final InsertTestQuestion serviceInstance = new InsertTestQuestion();

    @Atomic
    public static void runInsertTestQuestion(String executionCourseId, String testId, String[] metadataId, Integer questionOrder,
            Double questionValue, CorrectionFormula formula) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        serviceInstance.run(executionCourseId, testId, metadataId, questionOrder, questionValue, formula);
    }

}