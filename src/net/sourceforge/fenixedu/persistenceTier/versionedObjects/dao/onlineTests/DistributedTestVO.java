/*
 * Created on 19/Ago/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTest;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Susana Fernandes
 */
public class DistributedTestVO extends VersionedObjectsBase implements IPersistentDistributedTest {

    public List<DistributedTest> readByTestScope(String className, Integer idInternal) throws ExcepcaoPersistencia {
        List<DistributedTest> distributedTestList = (List<DistributedTest>) readAll(DistributedTest.class);
        List<DistributedTest> result = new ArrayList<DistributedTest>();
        for (DistributedTest distributedTest : distributedTestList) {
            final TestScope testScope = distributedTest.getTestScope();
            if (testScope != null && testScope.getClassName().equals(className) && testScope.getKeyClass().equals(idInternal)) {
                result.add(distributedTest);
            }
        }
        return result;
    }

    public List<DistributedTest> readByStudent(Integer studentId) throws ExcepcaoPersistencia {
        List<StudentTestQuestion> studentTestQuestionList = (List<StudentTestQuestion>) readAll(StudentTestQuestion.class);
        List<DistributedTest> result = new ArrayList<DistributedTest>();
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyStudent().equals(studentId) && !result.contains(studentTestQuestion.getDistributedTest())) {
                result.add(studentTestQuestion.getDistributedTest());
            }
        }
        return result;
    }

    public List<DistributedTest> readByStudentAndExecutionCourse(Integer studentId, Integer executionCourseId) throws ExcepcaoPersistencia {
        List<StudentTestQuestion> studentTestQuestionList = (List<StudentTestQuestion>) readAll(StudentTestQuestion.class);
        List<DistributedTest> result = new ArrayList<DistributedTest>();
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyStudent().equals(studentId)
                    && studentTestQuestion.getDistributedTest().getTestScope().getKeyClass().equals(executionCourseId)
                    && studentTestQuestion.getDistributedTest().getTestScope().getClassName().equals(ExecutionCourse.class.getName())
                    && !result.contains(studentTestQuestion.getDistributedTest())) {
                result.add(studentTestQuestion.getDistributedTest());
            }
        }
        return result;
    }

    public List<DistributedTest> readAll() throws ExcepcaoPersistencia {
        return (List<DistributedTest>) readAll(DistributedTest.class);
    }
}