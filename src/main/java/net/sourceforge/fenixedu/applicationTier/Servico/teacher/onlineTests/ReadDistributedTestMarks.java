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
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Susana Fernandes
 * 
 */
public class ReadDistributedTestMarks {

    protected InfoSiteStudentsTestMarks run(Integer executionCourseId, Integer distributedTestId, String path)
            throws FenixServiceException {

        InfoSiteStudentsTestMarks infoSiteStudentsTestMarks = new InfoSiteStudentsTestMarks();

        DistributedTest distributedTest = AbstractDomainObject.fromExternalId(distributedTestId);
        if (distributedTest == null) {
            throw new InvalidArgumentsServiceException();
        }

        List<StudentTestQuestion> studentTestQuestionList =
                distributedTest.getStudentTestQuestionsSortedByStudentNumberAndTestQuestionOrder();

        HashMap<Integer, InfoStudentTestQuestionMark> infoStudentTestQuestionMarkList =
                new HashMap<Integer, InfoStudentTestQuestionMark>();
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (infoStudentTestQuestionMarkList.containsKey(studentTestQuestion.getStudent().getExternalId())) {
                InfoStudentTestQuestionMark infoStudentTestQuestionMark =
                        infoStudentTestQuestionMarkList.get(studentTestQuestion.getStudent().getExternalId());
                ParseSubQuestion parse = new ParseSubQuestion();
                Question question = studentTestQuestion.getQuestion();
                try {
                    question = parse.parseSubQuestion(studentTestQuestion.getQuestion(), path.replace('\\', '/'));
                } catch (Exception e) {
                    throw new FenixServiceException(e);
                }
                if (studentTestQuestion.getItemId() != null
                        && !studentTestQuestion.getItemId().equals(question.getSubQuestions().get(0).getItemId())) {
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
        infoSiteStudentsTestMarks.setExecutionCourse(InfoExecutionCourse.newInfoFromDomain((ExecutionCourse) distributedTest
                .getTestScope().getDomainObject()));
        infoSiteStudentsTestMarks.setInfoDistributedTest(InfoDistributedTest.newInfoFromDomain(distributedTest));

        return infoSiteStudentsTestMarks;
    }

    // Service Invokers migrated from Berserk

    private static final ReadDistributedTestMarks serviceInstance = new ReadDistributedTestMarks();

    @Service
    public static InfoSiteStudentsTestMarks runReadDistributedTestMarks(Integer executionCourseId, Integer distributedTestId,
            String path) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, distributedTestId, path);
    }

}