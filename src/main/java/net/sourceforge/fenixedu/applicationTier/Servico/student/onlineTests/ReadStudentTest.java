/*
 * Created on 28/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests;

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
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Susana Fernandes
 */
public class ReadStudentTest {

    @Atomic
    public static List<StudentTestQuestion> run(Registration registration, String distributedTestId, Boolean log, String path)
            throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);
        final DistributedTest distributedTest = FenixFramework.getDomainObject(distributedTestId);
        return run(registration, distributedTest, log, path);
    }

    @Atomic
    public static List<StudentTestQuestion> run(Registration registration, DistributedTest distributedTest, Boolean log,
            String path) throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);
        if (distributedTest == null) {
            throw new InvalidArgumentsServiceException();
        }
        List<StudentTestQuestion> studentTestQuestionList = new ArrayList<StudentTestQuestion>();
        Set<StudentTestQuestion> studentTestQuestions = findStudentTestQuestions(registration, distributedTest);
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
            Boolean log, String path) throws FenixServiceException, NotAuthorizedException {
        ReadStudentTestForCorrectionFilter.instance.execute(distributedTestId);
        return run(registration, distributedTestId, log, path);
    }

    @Atomic
    public static List<StudentTestQuestion> runReadStudentTestForCorrection(Registration registration,
            DistributedTest distributedTest, Boolean log, String path) throws FenixServiceException, NotAuthorizedException {
        ReadStudentTestForCorrectionFilter.instance.execute(distributedTest.getExternalId());
        return run(registration, distributedTest, log, path);
    }

    // Service Invokers migrated from Berserk

    @Atomic
    public static List<StudentTestQuestion> runReadStudentTestToDo(Registration registration, String distributedTestId,
            Boolean log, String path) throws FenixServiceException, NotAuthorizedException {
        ReadStudentTestToDoFilter.instance.execute(distributedTestId);
        return run(registration, distributedTestId, log, path);
    }

    @Atomic
    public static List<StudentTestQuestion> runReadStudentTestToDo(Registration registration, DistributedTest distributedTest,
            Boolean log, String path) throws FenixServiceException, NotAuthorizedException {
        ReadStudentTestToDoFilter.instance.execute(distributedTest.getExternalId());
        return run(registration, distributedTest, log, path);
    }

}