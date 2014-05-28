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
 * Created on 6/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.util.tests.CorrectionFormula;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Susana Fernandes
 */
public class EditTestQuestion {

    protected void run(String executionCourseId, String testQuestionId, Integer testQuestionOrder, Double testQuestionValue,
            CorrectionFormula formula) throws FenixServiceException {
        TestQuestion testQuestion = FenixFramework.getDomainObject(testQuestionId);
        if (testQuestion == null) {
            throw new InvalidArgumentsServiceException();
        }
        if (testQuestionOrder == -1) {
            testQuestionOrder = testQuestion.getTest().getTestQuestions().size();
        } else if (testQuestionOrder == -2) {
            testQuestionOrder = testQuestion.getTestQuestionOrder();
        }
        if (testQuestionOrder.compareTo(testQuestion.getTestQuestionOrder()) < 0) {
            testQuestionOrder++;
        }

        testQuestion.editTestQuestion(testQuestionOrder, testQuestionValue, formula);
    }

    // Service Invokers migrated from Berserk

    private static final EditTestQuestion serviceInstance = new EditTestQuestion();

    @Atomic
    public static void runEditTestQuestion(String executionCourseId, String testQuestionId, Integer testQuestionOrder,
            Double testQuestionValue, CorrectionFormula formula) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        serviceInstance.run(executionCourseId, testQuestionId, testQuestionOrder, testQuestionValue, formula);
    }

}