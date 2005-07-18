/*
 * Created on 25/Jul/2003
 */

package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IMetadata;
import net.sourceforge.fenixedu.domain.onlineTests.IQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentQuestion;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Susana Fernandes
 */
public class QuestionVO extends VersionedObjectsBase implements IPersistentQuestion {

    public IQuestion readByFileNameAndMetadataId(String fileName, IMetadata metadata) throws ExcepcaoPersistencia {
        final List<IQuestion> questionList = (List<IQuestion>) readAll(Question.class);
        for (IQuestion question : questionList) {
            if (question.getKeyMetadata().equals(metadata.getIdInternal()) && question.getXmlFileName().equals(fileName)) {
                return question;
            }
        }
        return null;
    }

    public List<IQuestion> readByMetadata(IMetadata metadata) throws ExcepcaoPersistencia {
        final List<IQuestion> questionList = (List<IQuestion>) readAll(Question.class);
        List<IQuestion> result = new ArrayList<IQuestion>();
        for (IQuestion question : questionList) {
            if (question.getKeyMetadata().equals(metadata.getIdInternal())) {
                result.add(question);
            }
        }
        return result;
    }

    public List<IQuestion> readByMetadataAndVisibility(IMetadata metadata) throws ExcepcaoPersistencia {
        final List<IQuestion> questionList = (List<IQuestion>) readAll(Question.class);
        List<IQuestion> result = new ArrayList<IQuestion>();
        for (IQuestion question : questionList) {
            if (question.getKeyMetadata().equals(metadata.getIdInternal()) && question.getVisibility().equals(true)) {
                result.add(question);
            }
        }
        return result;
    }

    public int countByMetadata(IMetadata metadata) {
        final List<IQuestion> questionList = (List<IQuestion>) readAll(Question.class);
        int result = 0;
        for (IQuestion question : questionList) {
            if (question.getKeyMetadata().equals(metadata.getIdInternal())) {
                result++;
            }
        }
        return result;
    }

    public String correctFileName(String fileName, Integer metadataId) {
        String original = fileName.replaceAll(".xml", "");
        String newName = fileName;
        final List<IQuestion> questionList = (List<IQuestion>) readAll(Question.class);
        for (int i = 1;; i++) {
            int result = 0;
            for (IQuestion question : questionList) {
                if (question.getKeyMetadata().equals(metadataId) && question.getXmlFileName().equals(fileName)) {
                    result++;
                }
            }
            if (result == 0)
                return newName;
            newName = original.concat("(" + new Integer(i).toString() + ").xml");
        }
    }

    public void cleanQuestions(IDistributedTest distributedTest) throws ExcepcaoPersistencia {

        final List<IStudentTestQuestion> studentTestQuestionList = (List<IStudentTestQuestion>) readAll(StudentTestQuestion.class);
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyDistributedTest().equals(distributedTest.getIdInternal())
                    && studentTestQuestion.getQuestion().getVisibility().equals(false)
                    && countReferences(distributedTest.getIdInternal(), studentTestQuestion.getQuestion().getIdInternal()) == 0) {
                deleteByOID(Question.class, studentTestQuestion.getQuestion().getIdInternal());
            }
        }
    }

    private int countReferences(Integer distributedTestId, Integer questionId) {
        final List<IStudentTestQuestion> studentTestQuestionList = (List<IStudentTestQuestion>) readAll(StudentTestQuestion.class);
        int result = 0;
        for (IStudentTestQuestion studentTestQuestion : studentTestQuestionList) {
            if (studentTestQuestion.getKeyDistributedTest().equals(distributedTestId) && studentTestQuestion.getKeyQuestion().equals(questionId)) {
                result++;
            }
        }
        return result;
    }

    public void deleteByMetadata(IMetadata metadata) throws ExcepcaoPersistencia {
        final List<IQuestion> questionList = (List<IQuestion>) readAll(Question.class);
        for (IQuestion question : questionList) {
            if (question.getKeyMetadata().equals(metadata.getIdInternal())) {
                deleteByOID(Question.class, question.getIdInternal());
            }
        }
    }
}