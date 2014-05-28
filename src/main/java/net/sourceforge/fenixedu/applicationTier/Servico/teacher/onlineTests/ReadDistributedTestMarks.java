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
 * Created on Oct 14, 2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionCourse;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoSiteStudentsTestMarks;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestionMark;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Susana Fernandes
 * 
 */
public class ReadDistributedTestMarks {

    protected InfoSiteStudentsTestMarks run(String executionCourseId, String distributedTestId) throws FenixServiceException {

        InfoSiteStudentsTestMarks infoSiteStudentsTestMarks = new InfoSiteStudentsTestMarks();

        DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestId);
        if (distributedTest == null) {
            throw new InvalidArgumentsServiceException();
        }

        List<StudentTestQuestion> studentTestQuestionList =
                distributedTest.getStudentTestQuestionsSortedByStudentNumberAndTestQuestionOrder();

        HashMap<String, InfoStudentTestQuestionMark> infoStudentTestQuestionMarkList =
                new HashMap<String, InfoStudentTestQuestionMark>();
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (infoStudentTestQuestionMarkList.containsKey(studentTestQuestion.getStudent().getExternalId())) {
                InfoStudentTestQuestionMark infoStudentTestQuestionMark =
                        infoStudentTestQuestionMarkList.get(studentTestQuestion.getStudent().getExternalId());
                ParseSubQuestion parse = new ParseSubQuestion();
                Question question = studentTestQuestion.getQuestion();
                try {
                    question = parse.parseSubQuestion(studentTestQuestion.getQuestion());
                } catch (Exception e) {
                    throw new FenixServiceException(e);
                }
                if (studentTestQuestion.getItemId() != null
                        && !studentTestQuestion.getItemId().equals(question.getSubQuestions().iterator().next().getItemId())) {
                    infoStudentTestQuestionMark.addTestQuestionMark(
                            infoStudentTestQuestionMark.getTestQuestionMarks().size() - 1,
                            studentTestQuestion.getTestQuestionMark());
                } else {
                    infoStudentTestQuestionMark.addTestQuestionMark(studentTestQuestion.getTestQuestionMark());
                }
                infoStudentTestQuestionMark.addToMaximumMark(studentTestQuestion.getTestQuestionValue());
            } else {
                infoStudentTestQuestionMarkList.put(studentTestQuestion.getStudent().getExternalId(),
                        InfoStudentTestQuestionMark.newInfoFromDomain(studentTestQuestion));
            }
        }

        List<InfoStudentTestQuestionMark> infoStudentTestQuestionList =
                new ArrayList<InfoStudentTestQuestionMark>(infoStudentTestQuestionMarkList.values());
        Collections.sort(infoStudentTestQuestionList, new BeanComparator("studentNumber"));
        infoSiteStudentsTestMarks.setInfoStudentTestQuestionList(infoStudentTestQuestionList);
        infoSiteStudentsTestMarks.setExecutionCourse(InfoExecutionCourse.newInfoFromDomain(distributedTest.getTestScope()
                .getExecutionCourse()));
        infoSiteStudentsTestMarks.setInfoDistributedTest(InfoDistributedTest.newInfoFromDomain(distributedTest));

        return infoSiteStudentsTestMarks;
    }

    // Service Invokers migrated from Berserk

    private static final ReadDistributedTestMarks serviceInstance = new ReadDistributedTestMarks();

    @Atomic
    public static InfoSiteStudentsTestMarks runReadDistributedTestMarks(String executionCourseId, String distributedTestId)
            throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, distributedTestId);
    }

}