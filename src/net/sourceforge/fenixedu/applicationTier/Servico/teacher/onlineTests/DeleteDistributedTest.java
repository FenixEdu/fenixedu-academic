/*
 * Created on 24/Set/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.Iterator;

import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IMark;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTestAdvisory;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTestAdvisory;
import net.sourceforge.fenixedu.domain.onlineTests.IOnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTest;
import net.sourceforge.fenixedu.util.tests.TestType;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class DeleteDistributedTest implements IService {
    public void run(Integer executionCourseId, Integer distributedTestId) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentDistributedTest persistentDistributedTest = persistentSuport.getIPersistentDistributedTest();
        IDistributedTest distributedTest = (IDistributedTest) persistentDistributedTest.readByOID(DistributedTest.class, distributedTestId);

        persistentSuport.getIPersistentQuestion().cleanQuestions(distributedTest);
        persistentSuport.getIPersistentMetadata().cleanMetadatas();

        if (distributedTest.getTestType().getType().intValue() == TestType.EVALUATION) {
            IOnlineTest onlineTest = (IOnlineTest) persistentSuport.getIPersistentOnlineTest().readByDistributedTest(distributedTestId);
            Iterator<IExecutionCourse> executionCourseIterator = onlineTest.getAssociatedExecutionCoursesForOnlineTestIterator();
            while (executionCourseIterator.hasNext()) {
                IExecutionCourse executionCourse = executionCourseIterator.next();
                executionCourseIterator.remove();
            }

            Iterator<IMark> marksIterator = onlineTest.getMarksIterator();
            while (marksIterator.hasNext()) {
                IMark mark = marksIterator.next();
                marksIterator.remove();
                persistentSuport.getIPersistentMark().deleteByOID(Mark.class, mark.getIdInternal());
            }
            // persistentSuport.getIPersistentMark().deleteByEvaluation(onlineTest);
            distributedTest.removeOnlineTest();
            persistentSuport.getIPersistentOnlineTest().deleteByOID(OnlineTest.class, onlineTest.getIdInternal());
        }

        distributedTest.removeTestScope();

        Iterator<IStudentTestQuestion> studentTestQuestionIterator = distributedTest.getDistributedTestQuestionsIterator();

        while (studentTestQuestionIterator.hasNext()) {
            IStudentTestQuestion studentTestQuestion = studentTestQuestionIterator.next();
            studentTestQuestionIterator.remove();
            studentTestQuestion.delete();
        }

        Iterator<IDistributedTestAdvisory> distributedTestAdvisoryIterator = distributedTest.getDistributedTestAdvisoriesIterator();
        while (distributedTestAdvisoryIterator.hasNext()) {
            IDistributedTestAdvisory distributedTestAdvisory = distributedTestAdvisoryIterator.next();
            distributedTestAdvisoryIterator.remove();
            persistentSuport.getIPersistentDistributedTestAdvisory().deleteByOID(DistributedTestAdvisory.class,
                    distributedTestAdvisory.getIdInternal());
        }

        Iterator<IStudentTestLog> studentTestLogsIterator = distributedTest.getStudentsLogsIterator();
        while (studentTestLogsIterator.hasNext()) {
            IStudentTestLog studentTestLog = studentTestLogsIterator.next();
            studentTestLogsIterator.remove();
            studentTestLog.removeStudent();
            persistentSuport.getIPersistentStudentTestLog().deleteByOID(StudentTestLog.class, studentTestLog.getIdInternal());
        }

        persistentDistributedTest.deleteByOID(DistributedTest.class, distributedTest.getIdInternal());

    }
}