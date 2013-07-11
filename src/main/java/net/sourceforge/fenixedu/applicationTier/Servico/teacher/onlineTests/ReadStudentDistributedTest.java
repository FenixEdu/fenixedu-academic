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
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Susana Fernandes
 */
public class ReadStudentDistributedTest {
    public List<StudentTestQuestion> run(String executionCourseId, String distributedTestId, String studentId, String path)
            throws FenixServiceException {

        List<StudentTestQuestion> studentTestQuestionList = new ArrayList<StudentTestQuestion>();
        Registration registration = FenixFramework.getDomainObject(studentId);
        if (registration == null) {
            throw new FenixServiceException();
        }

        final DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestId);
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

    @Atomic
    public static List<StudentTestQuestion> runReadStudentDistributedTest(String executionCourseId, String distributedTestId,
            String studentId, String path) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseId);
        return serviceInstance.run(executionCourseId, distributedTestId, studentId, path);
    }

}