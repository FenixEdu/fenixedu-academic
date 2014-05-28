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
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteExercise {

    protected void run(ExecutionCourse executionCourseId, String metadataId) throws FenixServiceException {
        Metadata metadata = FenixFramework.getDomainObject(metadataId);
        if (metadata == null) {
            throw new InvalidArgumentsServiceException();
        }

        Collection<Question> questionList = metadata.getQuestions();
        boolean delete = true;
        for (Question question : questionList) {
            Collection<TestQuestion> testQuestionList = question.getTestQuestions();
            for (TestQuestion testQuestion : testQuestionList) {
                removeTestQuestionFromTest(testQuestion);
            }
            if (!question.hasAnyStudentTestsQuestions()) {
                question.delete();
            } else {
                question.setVisibility(Boolean.FALSE);
                delete = false;
            }
        }
        if (delete) {
            metadata.delete();
        } else {
            metadata.setVisibility(Boolean.FALSE);
        }
    }

    private void removeTestQuestionFromTest(TestQuestion testQuestion) throws FenixServiceException {
        Test test = testQuestion.getTest();
        if (test == null) {
            throw new FenixServiceException();
        }

        List<TestQuestion> testQuestionList = new ArrayList<TestQuestion>(test.getTestQuestions());
        Collections.sort(testQuestionList, new BeanComparator("testQuestionOrder"));

        Integer questionOrder = testQuestion.getTestQuestionOrder();
        for (TestQuestion iterTestQuestion : testQuestionList) {
            Integer iterQuestionOrder = iterTestQuestion.getTestQuestionOrder();
            if (questionOrder.compareTo(iterQuestionOrder) <= 0) {
                iterTestQuestion.setTestQuestionOrder(iterQuestionOrder - 1);
            }
        }
        testQuestion.delete();
        test.setLastModifiedDate(Calendar.getInstance().getTime());
    }

    // Service Invokers migrated from Berserk

    private static final DeleteExercise serviceInstance = new DeleteExercise();

    @Atomic
    public static void runDeleteExercise(ExecutionCourse executionCourseId, String metadataId) throws FenixServiceException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        serviceInstance.run(executionCourseId, metadataId);
    }

}