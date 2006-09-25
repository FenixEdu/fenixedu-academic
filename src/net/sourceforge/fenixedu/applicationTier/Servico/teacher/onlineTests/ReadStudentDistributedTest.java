/*
 * Created on 8/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.utils.ParseSubQuestion;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 */
public class ReadStudentDistributedTest extends Service {
    public List<StudentTestQuestion> run(Integer executionCourseId, Integer distributedTestId,
            Integer studentId, String path) throws FenixServiceException, ExcepcaoPersistencia {

        List<StudentTestQuestion> studentTestQuestionList = new ArrayList<StudentTestQuestion>();
        Registration registration = rootDomainObject.readRegistrationByOID(studentId);
        if (registration == null)
            throw new FenixServiceException();

        final DistributedTest distributedTest = rootDomainObject
                .readDistributedTestByOID(distributedTestId);
        if (distributedTest == null) {
            throw new FenixServiceException();
        }

        Set<StudentTestQuestion> studentTestQuestions = StudentTestQuestion.findStudentTestQuestions(
                registration, distributedTest);
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

        return studentTestQuestionList;
    }
}