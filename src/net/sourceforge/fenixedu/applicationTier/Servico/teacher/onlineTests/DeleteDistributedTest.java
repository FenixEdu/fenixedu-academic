package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Mark;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTestAdvisory;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestLog;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.tests.TestType;

public class DeleteDistributedTest extends Service {
    public void run(Integer executionCourseId, final Integer distributedTestId) throws ExcepcaoPersistencia {
        final DistributedTest distributedTest = rootDomainObject.readDistributedTestByOID(distributedTestId);

        distributedTest.cleanQuestions();
        persistentSupport.getIPersistentMetadata().cleanMetadatas();

        if (distributedTest.getTestType().getType().intValue() == TestType.EVALUATION) {
            OnlineTest onlineTest = distributedTest.getOnlineTest();
            Iterator<ExecutionCourse> executionCourseIterator = onlineTest.getAssociatedExecutionCoursesIterator();
            while (executionCourseIterator.hasNext()) {
                executionCourseIterator.next();
                executionCourseIterator.remove();
            }

            Iterator<Mark> marksIterator = onlineTest.getMarksIterator();
            while (marksIterator.hasNext()) {
                Mark mark = marksIterator.next();
                marksIterator.remove();
                persistentObject.deleteByOID(Mark.class, mark.getIdInternal());
            }
            // persistentSupport.getIPersistentMark().deleteByEvaluation(onlineTest);
            distributedTest.removeOnlineTest();
            persistentObject.deleteByOID(OnlineTest.class, onlineTest.getIdInternal());
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
            persistentObject.deleteByOID(DistributedTestAdvisory.class, distributedTestAdvisory.getIdInternal());
        }

        Iterator<StudentTestLog> studentTestLogsIterator = distributedTest.getStudentsLogsIterator();
        while (studentTestLogsIterator.hasNext()) {
            StudentTestLog studentTestLog = studentTestLogsIterator.next();
            studentTestLogsIterator.remove();
            studentTestLog.removeStudent();
            persistentObject.deleteByOID(StudentTestLog.class, studentTestLog.getIdInternal());
        }

        persistentObject.deleteByOID(DistributedTest.class, distributedTest.getIdInternal());

    }

}
