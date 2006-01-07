/*
 * Created on 24/Set/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.Iterator;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTestAdvisory;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTestAdvisory;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
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
        DistributedTest distributedTest = (DistributedTest) persistentDistributedTest.readByOID(DistributedTest.class, distributedTestId);

        persistentSuport.getIPersistentQuestion().cleanQuestions(distributedTest);
        persistentSuport.getIPersistentMetadata().cleanMetadatas();

        if (distributedTest.getTestType().getType().intValue() == TestType.EVALUATION) {
            OnlineTest onlineTest = (OnlineTest) persistentSuport.getIPersistentOnlineTest().readByDistributedTest(distributedTestId);
            Iterator<ExecutionCourse> executionCourseIterator = onlineTest.getAssociatedExecutionCoursesForOnlineTestIterator();
            while (executionCourseIterator.hasNext()) {
                executionCourseIterator.next();
                executionCourseIterator.remove();
            }

            Iterator<Mark> marksIterator = onlineTest.getMarksIterator();
            while (marksIterator.hasNext()) {
                Mark mark = marksIterator.next();
                marksIterator.remove();
                persistentSuport.getIPersistentMark().deleteByOID(Mark.class, mark.getIdInternal());
            }
            // persistentSuport.getIPersistentMark().deleteByEvaluation(onlineTest);
            distributedTest.removeOnlineTest();
            persistentSuport.getIPersistentOnlineTest().deleteByOID(OnlineTest.class, onlineTest.getIdInternal());
        }

        distributedTest.removeTestScope();

        Iterator<StudentTestQuestion> studentTestQuestionIterator = distributedTest.getDistributedTestQuestionsIterator();

        while (studentTestQuestionIterator.hasNext()) {
            StudentTestQuestion studentTestQuestion = studentTestQuestionIterator.next();
            studentTestQuestionIterator.remove();
            studentTestQuestion.delete();
        }

        Iterator<DistributedTestAdvisory> distributedTestAdvisoryIterator = distributedTest.getDistributedTestAdvisoriesIterator();
        while (distributedTestAdvisoryIterator.hasNext()) {
            DistributedTestAdvisory distributedTestAdvisory = distributedTestAdvisoryIterator.next();
            distributedTestAdvisoryIterator.remove();
            persistentSuport.getIPersistentDistributedTestAdvisory().deleteByOID(DistributedTestAdvisory.class,
                    distributedTestAdvisory.getIdInternal());
        }

        Iterator<StudentTestLog> studentTestLogsIterator = distributedTest.getStudentsLogsIterator();
        while (studentTestLogsIterator.hasNext()) {
            StudentTestLog studentTestLog = studentTestLogsIterator.next();
            studentTestLogsIterator.remove();
            studentTestLog.removeStudent();
            persistentSuport.getIPersistentStudentTestLog().deleteByOID(StudentTestLog.class, studentTestLog.getIdInternal());
        }

        persistentDistributedTest.deleteByOID(DistributedTest.class, distributedTest.getIdInternal());

    }
}