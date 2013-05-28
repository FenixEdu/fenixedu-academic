/*
 * Created on 8/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author Susana Fernandes
 */
public class ReadStudentDistributedTest {
    public List<StudentTestQuestion> run(Integer executionCourseId, Integer distributedTestId, Integer studentId, String path)
            throws FenixServiceException {

        List<StudentTestQuestion> studentTestQuestionList = new ArrayList<StudentTestQuestion>();
        Registration registration = AbstractDomainObject.fromExternalId(studentId);
        if (registration == null) {
            throw new FenixServiceException();
        }

        final DistributedTest distributedTest = AbstractDomainObject.fromExternalId(distributedTestId);
        if (distributedTest == null) {
            throw new FenixServiceException();
        }

        Set<StudentTestQuestion> studentTestQuestions =
                StudentTestQuestion.findStudentTestQuestions(registration, distributedTest);
        for (StudentTestQuestion studentTestQuestion : studentTestQuestions) {
            ParseSubQuestion parse = new ParseSubQuestion();
            try {
                parse.parseStudentTestQuestion(studentTestQuestion, path.replace('\\', '/'));
            } catch (Exception e) {
                throw new FenixServiceException(e);
            }
            if (studentTestQuestion.getOptionShuffle() == null && studentTestQuestion.getSubQuestionByItem().getShuffle() != null) {
                studentTestQuestion.setOptionShuffle(studentTestQuestion.getSubQuestionByItem().getShuffleString());
            }
            studentTestQuestionList.add(studentTestQuestion);
        }

        return studentTestQuestionList;
    }

    // Service Invokers migrated from Berserk

    private static final ReadStudentDistributedTest serviceInstance = new ReadStudentDistributedTest();

    @Service
    public static List<StudentTestQuestion> runReadStudentDistributedTest(Integer executionCourseId, Integer distributedTestId,
            Integer studentId, String path) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, distributedTestId, studentId, path);
    }

}