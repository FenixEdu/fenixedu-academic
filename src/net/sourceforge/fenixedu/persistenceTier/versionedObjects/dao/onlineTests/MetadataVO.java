/*
 * Created on 23/Jul/2003
 *  
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentMetadata;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Susana Fernandes
 */
public class MetadataVO extends VersionedObjectsBase implements IPersistentMetadata {

    public List<Metadata> readByExecutionCourse(ExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        final List<Metadata> metadataList = (List<Metadata>) readAll(Metadata.class);
        List<Metadata> result = new ArrayList<Metadata>();
        for (Metadata metadata : metadataList) {
            if (metadata.getKeyExecutionCourse().equals(executionCourse.getIdInternal())) {
                result.add(metadata);
            }
        }
        return result;
    }

    public List<Metadata> readByExecutionCourseAndVisibility(Integer executionCourseId) throws ExcepcaoPersistencia {
        final List<Metadata> metadataList = (List<Metadata>) readAll(Metadata.class);
        List<Metadata> result = new ArrayList<Metadata>();
        for (Metadata metadata : metadataList) {
            if (metadata.getKeyExecutionCourse().equals(executionCourseId) && metadata.getVisibility().equals(true)) {
                result.add(metadata);
            }
        }
        return result;
    }

    public List<Metadata> readByExecutionCourseAndNotTest(Integer executionCourseId, Integer testId) throws ExcepcaoPersistencia {
        final List<TestQuestion> testQuestionList = (List<TestQuestion>) readAll(TestQuestion.class);
        List<Integer> metadataIdList = new ArrayList<Integer>();
        for (TestQuestion testQuestion : testQuestionList) {
            if (testQuestion.getKeyTest().equals(testId)) {
                metadataIdList.add(testQuestion.getQuestion().getKeyMetadata());
            }
        }
        List<Metadata> result = new ArrayList<Metadata>();
        final List<Metadata> metadataList = (List<Metadata>) readAll(Metadata.class);
        for (Metadata metadata : metadataList) {
            if (metadata.getVisibility().equals(true) && metadata.getKeyExecutionCourse().equals(executionCourseId)
                    && !metadataIdList.contains(metadata.getIdInternal())) {
                result.add(metadata);
            }
        }
        return result;
    }

    public List<Metadata> readByExecutionCourseAndNotDistributedTest(Integer executionCourseId, Integer distributedTestId)
            throws ExcepcaoPersistencia {
        final List<StudentTestQuestion> studentTestQuestionList = (List<StudentTestQuestion>) readAll(StudentTestQuestion.class);
        List<Integer> metadataIdList = new ArrayList<Integer>();
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyDistributedTest().equals(distributedTestId)) {
                metadataIdList.add(studentTestQuestion.getQuestion().getKeyMetadata());
            }
        }
        List<Metadata> result = new ArrayList<Metadata>();
        final List<Metadata> metadataList = (List<Metadata>) readAll(Metadata.class);
        for (Metadata metadata : metadataList) {
            if (metadata.getVisibility().equals(true) && metadata.getKeyExecutionCourse().equals(executionCourseId)
                    && !metadataIdList.contains(metadata.getIdInternal())) {
                result.add(metadata);
            }
        }
        return result;
    }

    public int getNumberOfQuestions(Metadata metadata) {
        final List<Question> questionList = (List<Question>) readAll(Question.class);
        int result = 0;
        for (Question question : questionList) {
            if (question.getKeyMetadata().equals(metadata.getIdInternal())) {
                result++;
            }
        }
        return result;
    }

    public int countByExecutionCourse(Integer executionCourseId) {
        final List<Metadata> metadataList = (List<Metadata>) readAll(Metadata.class);
        int result = 0;
        for (Metadata metadata : metadataList) {
            if (metadata.getKeyExecutionCourse().equals(executionCourseId) && metadata.getVisibility().equals(true)) {
                result++;
            }
        }
        return result;
    }

    public void cleanMetadatas() throws ExcepcaoPersistencia {
        final List<Metadata> metadataList = (List<Metadata>) readAll(Metadata.class);
        for (Metadata metadata : metadataList) {
            if (getNumberOfQuestions(metadata) == 0) {
                deleteByOID(Metadata.class, metadata.getIdInternal());
            }
        }
    }
}