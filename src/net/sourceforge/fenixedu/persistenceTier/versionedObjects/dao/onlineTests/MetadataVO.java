/*
 * Created on 23/Jul/2003
 *  
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.IMetadata;
import net.sourceforge.fenixedu.domain.onlineTests.IQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.ITestQuestion;
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

    public List<IMetadata> readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        final List<IMetadata> metadataList = (List<IMetadata>) readAll(Metadata.class);
        List<IMetadata> result = new ArrayList<IMetadata>();
        for (IMetadata metadata : metadataList) {
            if (metadata.getKeyExecutionCourse().equals(executionCourse.getIdInternal())) {
                result.add(metadata);
            }
        }
        return result;
    }

    public List<IMetadata> readByExecutionCourseAndVisibility(Integer executionCourseId) throws ExcepcaoPersistencia {
        final List<IMetadata> metadataList = (List<IMetadata>) readAll(Metadata.class);
        List<IMetadata> result = new ArrayList<IMetadata>();
        for (IMetadata metadata : metadataList) {
            if (metadata.getKeyExecutionCourse().equals(executionCourseId) && metadata.getVisibility().equals(true)) {
                result.add(metadata);
            }
        }
        return result;
    }

    public List<IMetadata> readByExecutionCourseAndNotTest(Integer executionCourseId, Integer testId) throws ExcepcaoPersistencia {
        final List<ITestQuestion> testQuestionList = (List<ITestQuestion>) readAll(TestQuestion.class);
        List<Integer> metadataIdList = new ArrayList<Integer>();
        for (ITestQuestion testQuestion : testQuestionList) {
            if (testQuestion.getKeyTest().equals(testId)) {
                metadataIdList.add(testQuestion.getQuestion().getKeyMetadata());
            }
        }
        List<IMetadata> result = new ArrayList<IMetadata>();
        final List<IMetadata> metadataList = (List<IMetadata>) readAll(Metadata.class);
        for (IMetadata metadata : metadataList) {
            if (metadata.getVisibility().equals(true) && metadata.getKeyExecutionCourse().equals(executionCourseId)
                    && !metadataIdList.contains(metadata.getIdInternal())) {
                result.add(metadata);
            }
        }
        return result;
    }

    public List<IMetadata> readByExecutionCourseAndNotDistributedTest(Integer executionCourseId, Integer distributedTestId)
            throws ExcepcaoPersistencia {
        final List<IStudentTestQuestion> studentTestQuestionList = (List<IStudentTestQuestion>) readAll(StudentTestQuestion.class);
        List<Integer> metadataIdList = new ArrayList<Integer>();
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyDistributedTest().equals(distributedTestId)) {
                metadataIdList.add(studentTestQuestion.getQuestion().getKeyMetadata());
            }
        }
        List<IMetadata> result = new ArrayList<IMetadata>();
        final List<IMetadata> metadataList = (List<IMetadata>) readAll(Metadata.class);
        for (IMetadata metadata : metadataList) {
            if (metadata.getVisibility().equals(true) && metadata.getKeyExecutionCourse().equals(executionCourseId)
                    && !metadataIdList.contains(metadata.getIdInternal())) {
                result.add(metadata);
            }
        }
        return result;
    }

    public int getNumberOfQuestions(IMetadata metadata) {
        final List<IQuestion> questionList = (List<IQuestion>) readAll(Question.class);
        int result = 0;
        for (IQuestion question : questionList) {
            if (question.getKeyMetadata().equals(metadata.getIdInternal())) {
                result++;
            }
        }
        return result;
    }

    public int countByExecutionCourse(Integer executionCourseId) {
        final List<IMetadata> metadataList = (List<IMetadata>) readAll(Metadata.class);
        int result = 0;
        for (IMetadata metadata : metadataList) {
            if (metadata.getKeyExecutionCourse().equals(executionCourseId) && metadata.getVisibility().equals(true)) {
                result++;
            }
        }
        return result;
    }

    public void cleanMetadatas() throws ExcepcaoPersistencia {
        final List<IMetadata> metadataList = (List<IMetadata>) readAll(Metadata.class);
        for (IMetadata metadata : metadataList) {
            if (getNumberOfQuestions(metadata) == 0) {
                deleteByOID(Metadata.class, metadata.getIdInternal());
            }
        }
    }
}