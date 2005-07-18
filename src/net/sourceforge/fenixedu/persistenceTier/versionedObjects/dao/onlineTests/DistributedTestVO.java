/*
 * Created on 19/Ago/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.ITestScope;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentDistributedTest;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Susana Fernandes
 */
public class DistributedTestVO extends VersionedObjectsBase implements IPersistentDistributedTest {

    public List<IDistributedTest> readByTestScope(String className, Integer idInternal) throws ExcepcaoPersistencia {
        List<IDistributedTest> distributedTestList = (List<IDistributedTest>) readAll(DistributedTest.class);
        List<IDistributedTest> result = new ArrayList<IDistributedTest>();
        for (IDistributedTest distributedTest : distributedTestList) {
            final ITestScope testScope = distributedTest.getTestScope();
            if (testScope != null && testScope.getClassName().equals(className) && testScope.getKeyClass().equals(idInternal)) {
                result.add(distributedTest);
            }
        }
        return result;
    }

    public List<IDistributedTest> readByStudent(Integer studentId) throws ExcepcaoPersistencia {
        List<IStudentTestQuestion> studentTestQuestionList = (List<IStudentTestQuestion>) readAll(StudentTestQuestion.class);
        List<IDistributedTest> result = new ArrayList<IDistributedTest>();
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyStudent().equals(studentId) && !result.contains(studentTestQuestion.getDistributedTest())) {
                result.add(studentTestQuestion.getDistributedTest());
            }
        }
        return result;
    }

    public List<IDistributedTest> readByStudentAndExecutionCourse(Integer studentId, Integer executionCourseId) throws ExcepcaoPersistencia {
        List<IStudentTestQuestion> studentTestQuestionList = (List<IStudentTestQuestion>) readAll(StudentTestQuestion.class);
        List<IDistributedTest> result = new ArrayList<IDistributedTest>();
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyStudent().equals(studentId)
                    && studentTestQuestion.getDistributedTest().getTestScope().getKeyClass().equals(executionCourseId)
                    && studentTestQuestion.getDistributedTest().getTestScope().getClassName().equals(ExecutionCourse.class.getName())
                    && !result.contains(studentTestQuestion.getDistributedTest())) {
                result.add(studentTestQuestion.getDistributedTest());
            }
        }
        return result;
    }

    public List<IDistributedTest> readAll() throws ExcepcaoPersistencia {
        return (List<IDistributedTest>) readAll(DistributedTest.class);
    }
}