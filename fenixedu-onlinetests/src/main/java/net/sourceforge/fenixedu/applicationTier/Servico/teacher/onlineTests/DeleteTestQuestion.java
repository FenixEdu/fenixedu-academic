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

import org.fenixedu.academic.service.filter.ExecutionCourseLecturingTeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.domain.onlineTests.Test;
import org.fenixedu.academic.domain.onlineTests.TestQuestion;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteTestQuestion {

    protected void run(String executionCourseId, String testId, final String questionId) throws InvalidArgumentsServiceException {
        Test test = FenixFramework.getDomainObject(testId);
        if (test == null) {
            throw new InvalidArgumentsServiceException();
        }

        TestQuestion testQuestion = (TestQuestion) CollectionUtils.find(test.getTestQuestionsSet(), new Predicate() {
            @Override
            public boolean evaluate(Object arg0) {
                final TestQuestion testQuestion = (TestQuestion) arg0;
                return testQuestion.getQuestion().getExternalId().equals(questionId);
            }
        });
        if (testQuestion == null) {
            throw new InvalidArgumentsServiceException();
        }

        test.deleteTestQuestion(testQuestion);
    }

    // Service Invokers migrated from Berserk

    private static final DeleteTestQuestion serviceInstance = new DeleteTestQuestion();

    @Atomic
    public static void runDeleteTestQuestion(String executionCourseId, String testId, String questionId)
            throws InvalidArgumentsServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        serviceInstance.run(executionCourseId, testId, questionId);
    }

}