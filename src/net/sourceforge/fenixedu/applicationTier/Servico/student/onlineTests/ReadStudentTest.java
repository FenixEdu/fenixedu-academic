/*
 * Created on 28/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestionWithAll;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;

/**
 * @author Susana Fernandes
 */
public class ReadStudentTest extends Service {

    public List<InfoStudentTestQuestion> run(String userName, Integer distributedTestId, Boolean log,
            String path) throws FenixServiceException, ExcepcaoPersistencia {
        
        List<InfoStudentTestQuestion> infoStudentTestQuestionList = new ArrayList<InfoStudentTestQuestion>();
        path = path.replace('\\', '/');
        
        Student student = Student.readByUsername(userName);
        if (student == null)
            throw new FenixServiceException();
        DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(distributedTestId);
        if (distributedTest == null) {
            throw new InvalidArgumentsServiceException();
        }

        Set<StudentTestQuestion> studentTestQuestionList = StudentTestQuestion.findStudentTestQuestions(student, distributedTest);
        if (studentTestQuestionList.size() == 0)
            throw new InvalidArgumentsServiceException();

        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            InfoStudentTestQuestion infoStudentTestQuestion;
            ParseQuestion parse = new ParseQuestion();
            try {
                infoStudentTestQuestion = InfoStudentTestQuestionWithAll
                        .newInfoFromDomain(studentTestQuestion);
                infoStudentTestQuestion = parse.parseStudentTestQuestion(infoStudentTestQuestion, path);
                if (studentTestQuestion.getOptionShuffle() == null) {
                    studentTestQuestion.setOptionShuffle(infoStudentTestQuestion.getOptionShuffle());
                }
            } catch (Exception e) {
                throw new FenixServiceException(e);
            }

            infoStudentTestQuestionList.add(infoStudentTestQuestion);
        }
        if (log.booleanValue()) {
            StudentTestLog studentTestLog = new StudentTestLog();
            studentTestLog.setDistributedTest(distributedTest);
            studentTestLog.setStudent(student);
            studentTestLog.setDate(Calendar.getInstance().getTime());
            studentTestLog.setEvent(new String("Ler Ficha de Trabalho"));
        }
        return infoStudentTestQuestionList;
    }

}