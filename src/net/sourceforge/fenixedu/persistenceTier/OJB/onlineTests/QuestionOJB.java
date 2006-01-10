/*
 * Created on 25/Jul/2003
 */

package net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentQuestion;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Susana Fernandes
 */
public class QuestionOJB extends PersistentObjectOJB implements IPersistentQuestion {

    public Question readByFileNameAndMetadataId(String fileName, Metadata metadata) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyMetadata", metadata.getIdInternal());
        criteria.addEqualTo("xmlFileName", fileName);
        return (Question) queryObject(Question.class, criteria);
    }

    public List<Question> readByMetadata(Metadata metadata) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyMetadata", metadata.getIdInternal());
        return queryList(Question.class, criteria);
    }

    public List<Question> readByMetadataAndVisibility(Metadata metadata) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyMetadata", metadata.getIdInternal());
        criteria.addEqualTo("visibility", new Boolean("true"));
        return queryList(Question.class, criteria);
    }

    public int countByMetadata(Metadata metadata) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyMetadata", metadata.getIdInternal());
        return count(Question.class, criteria);
    }

    public String correctFileName(String fileName, Integer metadataId) {
        String original = fileName.replaceAll(".xml", "");
        String newName = fileName;
        for (int i = 1;; i++) {
            Criteria criteria = new Criteria();
            criteria.addEqualTo("xmlFileName", newName);
            criteria.addEqualTo("keyMetadata", metadataId);
            if (count(Question.class, criteria) == 0)
                return newName;
            newName = original.concat("(" + new Integer(i).toString() + ").xml");
        }
    }

    public void cleanQuestions(DistributedTest distributedTest) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
        criteria.addEqualTo("question.visibility", new Boolean(false));
        List<StudentTestQuestion> questions = queryList(StudentTestQuestion.class, criteria);
        for (StudentTestQuestion studentTestQuestion : questions) {
            if (countReferences(distributedTest.getIdInternal(), studentTestQuestion.getKeyQuestion()) == 0)
                delete(studentTestQuestion.getQuestion());
        }
    }

    private int countReferences(Integer distributedTestId, Integer questionId) {
        Criteria criteria = new Criteria();
        criteria.addNotEqualTo("keyDistributedTest", distributedTestId);
        criteria.addEqualTo("keyQuestion", questionId);
        return count(StudentTestQuestion.class, criteria);
    }

    public void deleteByMetadata(Metadata metadata) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyMetadata", metadata.getIdInternal());
        List<Question> questions = queryList(Question.class, criteria);
        for (Question question : questions) {
            delete(question);
        }
    }
}