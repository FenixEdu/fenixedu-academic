/*
 * Created on 8/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestionWithAll;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadStudentDistributedTest implements IService {
    public List run(Integer executionCourseId, Integer distributedTestId, Integer studentId, String path) throws FenixServiceException,
            ExcepcaoPersistencia {
        path = path.replace('\\', '/');
        List<InfoStudentTestQuestion> infoStudentTestQuestionList = new ArrayList<InfoStudentTestQuestion>();
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        Student student = (Student) persistentSuport.getIPersistentStudent().readByOID(Student.class, studentId);
        if (student == null)
            throw new FenixServiceException();
        DistributedTest distributedTest = (DistributedTest) persistentSuport.getIPersistentDistributedTest().readByOID(DistributedTest.class,
                distributedTestId);
        if (distributedTest == null)
            throw new FenixServiceException();
        List<StudentTestQuestion> studentTestQuestionList = persistentSuport.getIPersistentStudentTestQuestion().readByStudentAndDistributedTest(
                student.getIdInternal(), distributedTest.getIdInternal());
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            InfoStudentTestQuestion infoStudentTestQuestion;
            ParseQuestion parse = new ParseQuestion();
            try {
                infoStudentTestQuestion = InfoStudentTestQuestionWithAll.newInfoFromDomain(studentTestQuestion);
                infoStudentTestQuestion = parse.parseStudentTestQuestion(infoStudentTestQuestion, path);
                if (studentTestQuestion.getOptionShuffle() == null) {
                    studentTestQuestion.setOptionShuffle(infoStudentTestQuestion.getOptionShuffle());
                }
            } catch (Exception e) {
                throw new FenixServiceException(e);
            }

            infoStudentTestQuestionList.add(infoStudentTestQuestion);
        }
        return infoStudentTestQuestionList;
    }
}