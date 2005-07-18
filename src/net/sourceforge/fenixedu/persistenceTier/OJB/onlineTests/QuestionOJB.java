/*
 * Created on 25/Jul/2003
 */

package net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IMetadata;
import net.sourceforge.fenixedu.domain.onlineTests.IQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.IStudentTestQuestion;
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

    public IQuestion readByFileNameAndMetadataId(String fileName, IMetadata metadata) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyMetadata", metadata.getIdInternal());
        criteria.addEqualTo("xmlFileName", fileName);
        return (IQuestion) queryObject(Question.class, criteria);
    }

    public List<IQuestion> readByMetadata(IMetadata metadata) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyMetadata", metadata.getIdInternal());
        return queryList(Question.class, criteria);
    }

    public List<IQuestion> readByMetadataAndVisibility(IMetadata metadata) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyMetadata", metadata.getIdInternal());
        criteria.addEqualTo("visibility", new Boolean("true"));
        return queryList(Question.class, criteria);
    }

    public int countByMetadata(IMetadata metadata) {
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

    public void cleanQuestions(IDistributedTest distributedTest) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
        criteria.addEqualTo("question.visibility", new Boolean(false));
        List<IStudentTestQuestion> questions = queryList(StudentTestQuestion.class, criteria);
        for (IStudentTestQuestion studentTestQuestion : questions) {
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

    public void deleteByMetadata(IMetadata metadata) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyMetadata", metadata.getIdInternal());
        List<IQuestion> questions = queryList(Question.class, criteria);
        for (IQuestion question : questions) {
            delete(question);
        }
    }
}