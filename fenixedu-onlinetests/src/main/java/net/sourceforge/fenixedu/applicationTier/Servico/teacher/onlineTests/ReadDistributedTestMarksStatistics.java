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
 * Created on 12/Fev/2004
 *  
 */
package org.fenixedu.academic.service.services.teacher.onlineTests;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.service.filter.ExecutionCourseLecturingTeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.dto.onlineTests.InfoSiteStudentsTestMarksStatistics;
import org.fenixedu.academic.domain.onlineTests.DistributedTest;
import org.fenixedu.academic.domain.onlineTests.StudentTestQuestion;
import org.fenixedu.academic.util.tests.CorrectionFormula;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Susana Fernandes
 * 
 */
public class ReadDistributedTestMarksStatistics {

    protected InfoSiteStudentsTestMarksStatistics run(String executionCourseId, String distributedTestId)
            throws FenixServiceException {

        InfoSiteStudentsTestMarksStatistics infoSiteStudentsTestMarksStatistics = new InfoSiteStudentsTestMarksStatistics();

        DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestId);
        if (distributedTest == null) {
            throw new InvalidArgumentsServiceException();
        }

        Set<StudentTestQuestion> studentTestQuestions =
                distributedTest.findStudentTestQuestionsOfFirstStudentOrderedByTestQuestionOrder();

        List<String> correctAnswersPercentageList = new ArrayList<String>();
        List<String> partiallyCorrectAnswersPercentage = new ArrayList<String>();
        List<String> wrongAnswersPercentageList = new ArrayList<String>();
        List<String> notAnsweredPercentageList = new ArrayList<String>();
        List<String> answeredPercentageList = new ArrayList<String>();

        DecimalFormat df = new DecimalFormat("#%");
        int numOfStudent = distributedTest.countNumberOfStudents();
        for (StudentTestQuestion studentTestQuestion : studentTestQuestions) {
            if (studentTestQuestion.getCorrectionFormula().getFormula().intValue() == CorrectionFormula.FENIX) {

                correctAnswersPercentageList.add(df.format(distributedTest.countAnsweres(
                        studentTestQuestion.getTestQuestionOrder(), studentTestQuestion.getTestQuestionValue(), true)
                        * java.lang.Math.pow(numOfStudent, -1)));
                wrongAnswersPercentageList.add(df.format(distributedTest.countAnsweres(
                        studentTestQuestion.getTestQuestionOrder(), studentTestQuestion.getTestQuestionValue(), false)
                        * java.lang.Math.pow(numOfStudent, -1)));

                int partially =
                        distributedTest.countPartiallyCorrectAnswers(studentTestQuestion.getTestQuestionOrder(),
                                studentTestQuestion.getTestQuestionValue());
                if (partially != 0) {
                    partiallyCorrectAnswersPercentage.add(df.format(partially * java.lang.Math.pow(numOfStudent, -1)));
                } else {
                    partiallyCorrectAnswersPercentage.add("-");
                }

            }
            int responsed = distributedTest.countResponses(studentTestQuestion.getTestQuestionOrder(), true);

            notAnsweredPercentageList.add(df.format((numOfStudent - responsed) * java.lang.Math.pow(numOfStudent, -1)));
            answeredPercentageList.add(df.format(responsed * java.lang.Math.pow(numOfStudent, -1)));
        }
        infoSiteStudentsTestMarksStatistics.setCorrectAnswersPercentage(correctAnswersPercentageList);
        infoSiteStudentsTestMarksStatistics.setPartiallyCorrectAnswersPercentage(partiallyCorrectAnswersPercentage);
        infoSiteStudentsTestMarksStatistics.setWrongAnswersPercentage(wrongAnswersPercentageList);
        infoSiteStudentsTestMarksStatistics.setNotAnsweredPercentage(notAnsweredPercentageList);
        infoSiteStudentsTestMarksStatistics.setAnsweredPercentage(answeredPercentageList);
        infoSiteStudentsTestMarksStatistics.setDistributedTest(distributedTest);
        return infoSiteStudentsTestMarksStatistics;
    }

    // Service Invokers migrated from Berserk

    private static final ReadDistributedTestMarksStatistics serviceInstance = new ReadDistributedTestMarksStatistics();

    @Atomic
    public static InfoSiteStudentsTestMarksStatistics runReadDistributedTestMarksStatistics(String executionCourseId,
            String distributedTestId) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, distributedTestId);
    }

}