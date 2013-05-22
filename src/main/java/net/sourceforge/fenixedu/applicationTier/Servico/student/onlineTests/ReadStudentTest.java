/*
 * Created on 28/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.FenixService;
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
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

/**
 * @author Susana Fernandes
 */
public class ReadStudentTest extends FenixService {

    @Checked("RolePredicates.STUDENT_PREDICATE")
    @Service
    public static List<StudentTestQuestion> run(Registration registration, Integer distributedTestId, Boolean log, String path)
            throws FenixServiceException {
        final DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(distributedTestId);
        return run(registration, distributedTest, log, path);
    }

    @Checked("RolePredicates.STUDENT_PREDICATE")
    @Service
    public static List<StudentTestQuestion> run(Registration registration, DistributedTest distributedTest, Boolean log,
            String path) throws FenixServiceException {
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

    private static final ReadStudentTest serviceInstance = new ReadStudentTest();

    @Service
    public static List<StudentTestQuestion> runReadStudentTestForCorrection(Registration registration, Integer distributedTestId,
            Boolean log, String path) throws FenixServiceException, NotAuthorizedException {
        ReadStudentTestForCorrectionFilter.instance.execute(distributedTestId);
        return serviceInstance.run(registration, distributedTestId, log, path);
    }

    // Service Invokers migrated from Berserk

    @Service
    public static List<StudentTestQuestion> runReadStudentTestToDo(Registration registration, Integer distributedTestId,
            Boolean log, String path) throws FenixServiceException, NotAuthorizedException {
        ReadStudentTestToDoFilter.instance.execute(distributedTestId);
        return serviceInstance.run(registration, distributedTestId, log, path);
    }

}