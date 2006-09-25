/*
 * Created on 28/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

import org.joda.time.DateTime;

/**
 * @author Susana Fernandes
 */
public class ReadStudentTest extends Service {

    public List<StudentTestQuestion> run(Registration registration, Integer distributedTestId,
            Boolean log, String path) throws FenixServiceException, ExcepcaoPersistencia {
        final DistributedTest distributedTest = rootDomainObject
                .readDistributedTestByOID(distributedTestId);
        if (distributedTest == null) {
            throw new InvalidArgumentsServiceException();
        }
        return run(registration, distributedTest, log, path);
    }

    public List<StudentTestQuestion> run(Registration registration, DistributedTest distributedTest,
            Boolean log, String path) throws FenixServiceException, ExcepcaoPersistencia {
        List<StudentTestQuestion> studentTestQuestionList = new ArrayList<StudentTestQuestion>();
        if (registration == null) {
            throw new FenixServiceException();
        }
        if (distributedTest == null) {
            throw new InvalidArgumentsServiceException();
        }
        Set<StudentTestQuestion> studentTestQuestions = StudentTestQuestion.findStudentTestQuestions(
                registration, distributedTest);
        if (studentTestQuestions.size() == 0) {
            throw new InvalidArgumentsServiceException();
        }
        for (StudentTestQuestion studentTestQuestion : studentTestQuestions) {
            ParseSubQuestion parse = new ParseSubQuestion();
            try {
                parse.parseStudentTestQuestion(studentTestQuestion, path.replace('\\', '/'));
            } catch (Exception e) {
                throw new FenixServiceException(e);
            }
            if (studentTestQuestion.getOptionShuffle() == null
                    && studentTestQuestion.getSubQuestionByItem().getShuffle() != null) {
                studentTestQuestion.setOptionShuffle(studentTestQuestion.getSubQuestionByItem()
                        .getShuffleString());
            }
            studentTestQuestionList.add(studentTestQuestion);
        }
        if (log.booleanValue()) {
            StudentTestLog studentTestLog = new StudentTestLog();
            studentTestLog.setDistributedTest(distributedTest);
            studentTestLog.setStudent(registration);
            studentTestLog.setDateDateTime(new DateTime());
            studentTestLog.setEvent(new String("Ler Ficha de Trabalho"));
        }
        return studentTestQuestionList;
    }

}