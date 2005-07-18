/*
 * Created on 8/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestionWithAll;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.Student;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.tests.TestType;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadStudentDistributedTest implements IService {

    private String path = new String();

    public List run(Integer executionCourseId, Integer distributedTestId, Integer studentId, String path) throws FenixServiceException {
        this.path = path.replace('\\', '/');
        List<InfoStudentTestQuestion> infoStudentTestQuestionList = new ArrayList<InfoStudentTestQuestion>();
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IStudent student = (IStudent) persistentSuport.getIPersistentStudent().readByOID(Student.class, studentId);
            if (student == null)
                throw new FenixServiceException();
            IDistributedTest distributedTest = (IDistributedTest) persistentSuport.getIPersistentDistributedTest().readByOID(DistributedTest.class,
                    distributedTestId);
            if (distributedTest == null)
                throw new FenixServiceException();
            List<IStudentTestQuestion> studentTestQuestionList = persistentSuport.getIPersistentStudentTestQuestion()
                    .readByStudentAndDistributedTest(student.getIdInternal(), distributedTest.getIdInternal());
            for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
                InfoStudentTestQuestion infoStudentTestQuestion;
                ParseQuestion parse = new ParseQuestion();
                try {
                    if (studentTestQuestion.getOptionShuffle() == null) {
                        persistentSuport.getIPersistentStudentTestQuestion().simpleLockWrite(studentTestQuestion);
                        boolean shuffle = true;
                        if (distributedTest.getTestType().equals(new TestType(3))) // INQUIRY
                            shuffle = false;
                        studentTestQuestion.setOptionShuffle(parse.shuffleQuestionOptions(studentTestQuestion.getQuestion().getXmlFile(), shuffle,
                                this.path));
                    }
                    infoStudentTestQuestion = InfoStudentTestQuestionWithAll.newInfoFromDomain(studentTestQuestion);

                    infoStudentTestQuestion = parse.parseStudentTestQuestion(infoStudentTestQuestion, this.path);
                } catch (Exception e) {
                    throw new FenixServiceException(e);
                }

                infoStudentTestQuestionList.add(infoStudentTestQuestion);
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return infoStudentTestQuestionList;
    }
}