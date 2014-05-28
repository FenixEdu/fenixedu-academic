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
 * Created on 3/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Susana Fernandes
 */
public class ReadStudentTestQuestionImage {
    @Atomic
    public static String run(String registrationId, String distributedTestId, String questionId, Integer imageId,
            Integer feedbackId, Integer itemIndex) throws FenixServiceException {
        final DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestId);
        final Registration registration = FenixFramework.getDomainObject(registrationId);
        return run(registration, distributedTest, questionId, imageId, feedbackId, itemIndex);
    }

    @Atomic
    public static String run(Registration registration, DistributedTest distributedTest, String questionId, Integer imageId,
            Integer feedbackId, Integer itemIndex) throws FenixServiceException {
        final Question question = FenixFramework.getDomainObject(questionId);
        for (StudentTestQuestion studentTestQuestion : registration.getStudentTestsQuestions()) {
            if (studentTestQuestion.getDistributedTest() == distributedTest && studentTestQuestion.getQuestion() == question) {
                ParseSubQuestion parse = new ParseSubQuestion();
                try {
                    parse.parseStudentTestQuestion(studentTestQuestion);
                } catch (Exception e) {
                    throw new FenixServiceException(e);
                }
                return studentTestQuestion.getStudentSubQuestions().get(itemIndex).getImage(imageId, feedbackId);
            }
        }
        return null;
    }

}