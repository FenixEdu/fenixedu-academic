/*
 * Created on 25/Jul/2003
 */

package ServidorPersistente.OJB;

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.odmg.HasBroker;

import Dominio.IDistributedTest;
import Dominio.IMetadata;
import Dominio.IQuestion;
import Dominio.IStudentTestQuestion;
import Dominio.Question;
import Dominio.StudentTestQuestion;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentQuestion;

/**
 * @author Susana Fernandes
 */
public class QuestionOJB extends ObjectFenixOJB implements IPersistentQuestion
{
    public QuestionOJB() {
    }

    public Question readByFileNameAndMetadataId(String fileName, IMetadata metadata)
            throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyMetadata", metadata.getIdInternal());
        criteria.addEqualTo("xmlFileName", fileName);
        return (Question) queryObject(Question.class, criteria);
    }

    public List readByMetadata(IMetadata metadata) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyMetadata", metadata.getIdInternal());
        return queryList(Question.class, criteria);
    }

    public List readByMetadataAndVisibility(IMetadata metadata) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyMetadata", metadata.getIdInternal());
        criteria.addEqualTo("visibility", new Boolean("true"));
        return queryList(Question.class, criteria);
    }

    public int countByMetadata(IMetadata metadata) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyMetadata", metadata.getIdInternal());
        PersistenceBroker pb = ((HasBroker) odmg.currentTransaction()).getBroker();
        QueryByCriteria queryCriteria = new QueryByCriteria(Question.class, criteria, false);
        return pb.getCount(queryCriteria);
    }

    public void cleanQuestions(IDistributedTest distributedTest) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
        criteria.addEqualTo("question.visibility", new Boolean(false));
        List questions = queryList(StudentTestQuestion.class, criteria);
        Iterator it = questions.iterator();
        while (it.hasNext())
        {
            IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) it.next();
            if (countReferences(distributedTest.getIdInternal(), studentTestQuestion.getKeyQuestion()) == 0)
                delete(studentTestQuestion.getQuestion());
        }
    }

    private int countReferences(Integer distributedTestId, Integer questionId)
    {
        Criteria criteria = new Criteria();
        criteria.addNotEqualTo("keyDistributedTest", distributedTestId);
        criteria.addEqualTo("keyQuestion", questionId);
        PersistenceBroker pb = ((HasBroker) odmg.currentTransaction()).getBroker();
        QueryByCriteria queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria, false);
        return pb.getCount(queryCriteria);
    }

    public void deleteByMetadata(IMetadata metadata) throws ExcepcaoPersistencia
    {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyMetadata", metadata.getIdInternal());
        List questions = queryList(Question.class, criteria);
        Iterator it = questions.iterator();
        while (it.hasNext())
        {
            delete((IQuestion) it.next());
        }
    }

    public void delete(IQuestion question) throws ExcepcaoPersistencia
    {
        super.delete(question);
    }
}