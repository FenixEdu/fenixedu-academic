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
 * Created on 28/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Filtro.student.tests.ReadStudentTestForCorrectionFilter;
import net.sourceforge.fenixedu.applicationTier.Filtro.student.tests.ReadStudentTestToDoFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Susana Fernandes
 */
public class ReadStudentTest {

    @Atomic
    public static List<StudentTestQuestion> run(Registration registration, String distributedTestId, Boolean log)
            throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);
        final DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestId);
        return run(registration, distributedTest, log);
    }

    @Atomic
    public static List<StudentTestQuestion> run(Registration registration, DistributedTest distributedTest, Boolean log)
            throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);
        if (distributedTest == null) {
            throw new InvalidArgumentsServiceException();
        }
        List<StudentTestQuestion> studentTestQuestionList = new ArrayList<StudentTestQuestion>();
        Set<StudentTestQuestion> studentTestQuestions = findStudentTestQuestions(registration, distributedTest);
        for (StudentTestQuestion studentTestQuestion : studentTestQuestions) {
            ParseSubQuestion parse = new ParseSubQuestion();
            try {
                parse.parseStudentTestQuestion(studentTestQuestion);
            } catch (Exception e) {
                throw new FenixServiceException(e);
            }
            if (studentTestQuestion.getOptionShuffle() == null && studentTestQuestion.getSubQuestionByItem().getShuffle() != null) {
                studentTestQuestion.setOptionShuffle(studentTestQuestion.getSubQuestionByItem().getShuffleString());
            }
            studentTestQuestionList.add(studentTestQuestion);
        }
        if (log.booleanValue()) {
            StudentTestLog studentTestLog = new StudentTestLog(distributedTest, registration, "Ler Ficha de Trabalho");
        }
        return studentTestQuestionList;
    }

    private static Set<StudentTestQuestion> findStudentTestQuestions(Registration registration, DistributedTest distributedTest)
            throws InvalidArgumentsServiceException {
        final Set<StudentTestQuestion> studentTestQuestions =
                StudentTestQuestion.findStudentTestQuestions(registration, distributedTest);
        if (studentTestQuestions.size() == 0) {
            throw new InvalidArgumentsServiceException();
        }
        return studentTestQuestions;
    }

    // Service Invokers migrated from Berserk

    @Atomic
    public static List<StudentTestQuestion> runReadStudentTestForCorrection(Registration registration, String distributedTestId,
            Boolean log) throws FenixServiceException, NotAuthorizedException {
        ReadStudentTestForCorrectionFilter.instance.execute(distributedTestId);
        return run(registration, distributedTestId, log);
    }

    @Atomic
    public static List<StudentTestQuestion> runReadStudentTestForCorrection(Registration registration,
            DistributedTest distributedTest, Boolean log) throws FenixServiceException, NotAuthorizedException {
        ReadStudentTestForCorrectionFilter.instance.execute(distributedTest.getExternalId());
        return run(registration, distributedTest, log);
    }

    // Service Invokers migrated from Berserk

    @Atomic
    public static List<StudentTestQuestion> runReadStudentTestToDo(Registration registration, String distributedTestId,
            Boolean log) throws FenixServiceException, NotAuthorizedException {
        ReadStudentTestToDoFilter.instance.execute(distributedTestId);
        return run(registration, distributedTestId, log);
    }

    @Atomic
    public static List<StudentTestQuestion> runReadStudentTestToDo(Registration registration, DistributedTest distributedTest,
            Boolean log) throws FenixServiceException, NotAuthorizedException {
        ReadStudentTestToDoFilter.instance.execute(distributedTest.getExternalId());
        return run(registration, distributedTest, log);
    }

}