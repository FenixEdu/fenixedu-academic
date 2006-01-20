/*
 * Created on 24/Set/2003
 */

package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.Iterator;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTestAdvisory;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTest;
import net.sourceforge.fenixedu.util.tests.TestType;
import net.sourceforge.fenixedu.applicationTier.Service;

/**
 * @author Susana Fernandes
 */
public class DeleteDistributedTest extends Service {
    public void run(Integer executionCourseId, Integer distributedTestId) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSupport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        IPersistentDistributedTest persistentDistributedTest = persistentSupport.getIPersistentDistributedTest();
        DistributedTest distributedTest = (DistributedTest) persistentDistributedTest.readByOID(DistributedTest.class, distributedTestId);

        persistentSupport.getIPersistentQuestion().cleanQuestions(distributedTest);
        persistentSupport.getIPersistentMetadata().cleanMetadatas();

        if (distributedTest.getTestType().getType().intValue() == TestType.EVALUATION) {
            OnlineTest onlineTest = (OnlineTest) persistentSupport.getIPersistentOnlineTest().readByDistributedTest(distributedTestId);
            Iterator<ExecutionCourse> executionCourseIterator = onlineTest.getAssociatedExecutionCoursesForOnlineTestIterator();
            while (executionCourseIterator.hasNext()) {
                executionCourseIterator.next();
                executionCourseIterator.remove();
            }

            Iterator<Mark> marksIterator = onlineTest.getMarksIterator();
            while (marksIterator.hasNext()) {
                Mark mark = marksIterator.next();
                marksIterator.remove();
                persistentSupport.getIPersistentMark().deleteByOID(Mark.class, mark.getIdInternal());
            }
            // persistentSupport.getIPersistentMark().deleteByEvaluation(onlineTest);
            distributedTest.removeOnlineTest();
            persistentSupport.getIPersistentOnlineTest().deleteByOID(OnlineTest.class, onlineTest.getIdInternal());
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
            persistentSupport.getIPersistentDistributedTestAdvisory().deleteByOID(DistributedTestAdvisory.class,
                    distributedTestAdvisory.getIdInternal());
        }

        Iterator<StudentTestLog> studentTestLogsIterator = distributedTest.getStudentsLogsIterator();
        while (studentTestLogsIterator.hasNext()) {
            StudentTestLog studentTestLog = studentTestLogsIterator.next();
            studentTestLogsIterator.remove();
            studentTestLog.removeStudent();
            persistentSupport.getIPersistentStudentTestLog().deleteByOID(StudentTestLog.class, studentTestLog.getIdInternal());
        }

        persistentDistributedTest.deleteByOID(DistributedTest.class, distributedTest.getIdInternal());

    }
}