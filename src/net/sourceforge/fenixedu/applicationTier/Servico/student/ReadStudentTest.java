/*
 * Created on 28/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentTestQuestionWithAll;
import net.sourceforge.fenixedu.domain.DistributedTest;
import net.sourceforge.fenixedu.domain.IDistributedTest;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentTestLog;
import net.sourceforge.fenixedu.domain.IStudentTestQuestion;
import net.sourceforge.fenixedu.domain.StudentTestLog;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentStudentTestLog;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.tests.TestType;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadStudentTest implements IService {

    private String path = new String();

    public ReadStudentTest() {
    }

    public Object run(String userName, Integer distributedTestId, Boolean log, String path)
            throws FenixServiceException {
        List infoStudentTestQuestionList = new ArrayList();
        this.path = path.replace('\\', '/');
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IStudent student = persistentSuport.getIPersistentStudent().readByUsername(userName);
            if (student == null)
                throw new FenixServiceException();
            IDistributedTest distributedTest = (IDistributedTest) persistentSuport
                    .getIPersistentDistributedTest().readByOID(DistributedTest.class, distributedTestId);
            if (distributedTest == null) {
                throw new InvalidArgumentsServiceException();
            }

            List studentTestQuestionList = persistentSuport.getIPersistentStudentTestQuestion()
                    .readByStudentAndDistributedTest(student, distributedTest);
            if (studentTestQuestionList.size() == 0)
                throw new InvalidArgumentsServiceException();
            Iterator it = studentTestQuestionList.iterator();
            while (it.hasNext()) {
                IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it.next();
                InfoStudentTestQuestion infoStudentTestQuestion;
                ParseQuestion parse = new ParseQuestion();
                try {

                    if (studentTestQuestion.getOptionShuffle() == null) {
                        persistentSuport.getIPersistentStudentTestQuestion().simpleLockWrite(
                                studentTestQuestion);
                        boolean shuffle = true;
                        if (distributedTest.getTestType().equals(new TestType(3))) //INQUIRY
                            shuffle = false;
                        studentTestQuestion.setOptionShuffle(parse.shuffleQuestionOptions(
                                studentTestQuestion.getQuestion().getXmlFile(), shuffle, this.path));
                    }
                    infoStudentTestQuestion = InfoStudentTestQuestionWithAll
                            .newInfoFromDomain(studentTestQuestion);
                    infoStudentTestQuestion = parse.parseStudentTestQuestion(infoStudentTestQuestion,
                            this.path);
                } catch (Exception e) {
                    throw new FenixServiceException(e);
                }

                infoStudentTestQuestionList.add(infoStudentTestQuestion);
            }
            if (log.booleanValue()) {
                IPersistentStudentTestLog persistentStudentTestLog = persistentSuport
                        .getIPersistentStudentTestLog();

                IStudentTestLog studentTestLog = new StudentTestLog();
                studentTestLog.setDistributedTest(distributedTest);
                studentTestLog.setStudent(student);
                studentTestLog.setDate(Calendar.getInstance().getTime());
                studentTestLog.setEvent(new String("Ler Ficha de Trabalho"));

                persistentStudentTestLog.simpleLockWrite(studentTestLog);
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return infoStudentTestQuestionList;
    }

}