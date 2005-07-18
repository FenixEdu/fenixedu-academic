/*
 * Created on 28/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student.onlineTests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestion;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoStudentTestQuestionWithAll;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.util.tests.TestType;
import net.sourceforge.fenixedu.utilTests.ParseQuestion;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadStudentTest implements IService {

    private String path = new String();

    public Object run(String userName, Integer distributedTestId, Boolean log, String path) throws FenixServiceException {
        List<InfoStudentTestQuestion> infoStudentTestQuestionList = new ArrayList<InfoStudentTestQuestion>();
        this.path = path.replace('\\', '/');
        try {
            ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
            IStudent student = persistentSuport.getIPersistentStudent().readByUsername(userName);
            if (student == null)
                throw new FenixServiceException();
            IDistributedTest distributedTest = (IDistributedTest) persistentSuport.getIPersistentDistributedTest().readByOID(DistributedTest.class,
                    distributedTestId);
            if (distributedTest == null) {
                throw new InvalidArgumentsServiceException();
            }

            List<IStudentTestQuestion> studentTestQuestionList = persistentSuport.getIPersistentStudentTestQuestion()
                    .readByStudentAndDistributedTest(student.getIdInternal(), distributedTest.getIdInternal());
            if (studentTestQuestionList.size() == 0)
                throw new InvalidArgumentsServiceException();

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
            if (log.booleanValue()) {
                IStudentTestLog studentTestLog = new StudentTestLog();
                persistentSuport.getIPersistentStudentTestLog().simpleLockWrite(studentTestLog);
                studentTestLog.setDistributedTest(distributedTest);
                studentTestLog.setStudent(student);
                studentTestLog.setDate(Calendar.getInstance().getTime());
                studentTestLog.setEvent(new String("Ler Ficha de Trabalho"));
            }
        } catch (ExcepcaoPersistencia e) {
            throw new FenixServiceException(e);
        }
        return infoStudentTestQuestionList;
    }

}