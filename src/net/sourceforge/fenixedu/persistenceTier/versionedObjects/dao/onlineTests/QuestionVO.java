/*
 * Created on 25/Jul/2003
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentQuestion;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Susana Fernandes
 */
public class QuestionVO extends VersionedObjectsBase implements IPersistentQuestion {

    public Question readByFileNameAndMetadataId(String fileName, Metadata metadata) throws ExcepcaoPersistencia {
        final List<Question> questionList = (List<Question>) readAll(Question.class);
        for (Question question : questionList) {
            if (question.getKeyMetadata().equals(metadata.getIdInternal()) && question.getXmlFileName().equals(fileName)) {
                return question;
            }
        }
        return null;
    }

    public List<Question> readByMetadata(Metadata metadata) throws ExcepcaoPersistencia {
        final List<Question> questionList = (List<Question>) readAll(Question.class);
        List<Question> result = new ArrayList<Question>();
        for (Question question : questionList) {
            if (question.getKeyMetadata().equals(metadata.getIdInternal())) {
                result.add(question);
            }
        }
        return result;
    }

    public List<Question> readByMetadataAndVisibility(Metadata metadata) throws ExcepcaoPersistencia {
        final List<Question> questionList = (List<Question>) readAll(Question.class);
        List<Question> result = new ArrayList<Question>();
        for (Question question : questionList) {
            if (question.getKeyMetadata().equals(metadata.getIdInternal()) && question.getVisibility().equals(true)) {
                result.add(question);
            }
        }
        return result;
    }

    public int countByMetadata(Metadata metadata) {
        final List<Question> questionList = (List<Question>) readAll(Question.class);
        int result = 0;
        for (Question question : questionList) {
            if (question.getKeyMetadata().equals(metadata.getIdInternal())) {
                result++;
            }
        }
        return result;
    }

    public String correctFileName(String fileName, Integer metadataId) {
        String original = fileName.replaceAll(".xml", "");
        String newName = fileName;
        final List<Question> questionList = (List<Question>) readAll(Question.class);
        for (int i = 1;; i++) {
            int result = 0;
            for (Question question : questionList) {
                if (question.getKeyMetadata().equals(metadataId) && question.getXmlFileName().equals(fileName)) {
                    result++;
                }
            }
            if (result == 0)
                return newName;
            newName = original.concat("(" + new Integer(i).toString() + ").xml");
        }
    }

    public void cleanQuestions(DistributedTest distributedTest) throws ExcepcaoPersistencia {

        final List<StudentTestQuestion> studentTestQuestionList = (List<StudentTestQuestion>) readAll(StudentTestQuestion.class);
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyDistributedTest().equals(distributedTest.getIdInternal())
                    && studentTestQuestion.getQuestion().getVisibility().equals(false)
                    && countReferences(distributedTest.getIdInternal(), studentTestQuestion.getQuestion().getIdInternal()) == 0) {
                deleteByOID(Question.class, studentTestQuestion.getQuestion().getIdInternal());
            }
        }
    }

    private int countReferences(Integer distributedTestId, Integer questionId) {
        final List<StudentTestQuestion> studentTestQuestionList = (List<StudentTestQuestion>) readAll(StudentTestQuestion.class);
        int result = 0;
        for (StudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyDistributedTest().equals(distributedTestId) && studentTestQuestion.getKeyQuestion().equals(questionId)) {
                result++;
            }
        }
        return result;
    }

    public void deleteByMetadata(Metadata metadata) throws ExcepcaoPersistencia {
        final List<Question> questionList = (List<Question>) readAll(Question.class);
        for (Question question : questionList) {
            if (question.getKeyMetadata().equals(metadata.getIdInternal())) {
                deleteByOID(Question.class, question.getIdInternal());
            }
        }
    }
}