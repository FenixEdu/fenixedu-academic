/*
 * Created on 29/Jul/2003
 *
 */
package ServidorPersistente.OJB;

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.odmg.HasBroker;

import Dominio.IQuestion;
import Dominio.ITest;
import Dominio.ITestQuestion;
import Dominio.TestQuestion;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTestQuestion;

/**
 * @author Susana Fernandes
 */
public class TestQuestionOJB extends PersistentObjectOJB implements IPersistentTestQuestion {

    public TestQuestionOJB() {
    }

    public List readByTest(ITest test) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTest", test.getIdInternal());

        PersistenceBroker pb = ((HasBroker) odmg.currentTransaction()).getBroker();
        QueryByCriteria queryCriteria = new QueryByCriteria(TestQuestion.class, criteria, false);
        queryCriteria.addOrderBy("testQuestionOrder", true);
        return (List) pb.getCollectionByQuery(queryCriteria);
    }

    public ITestQuestion readByTestAndQuestion(ITest test, IQuestion question)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTest", test.getIdInternal());
        criteria.addEqualTo("keyQuestion", question.getIdInternal());
        return (ITestQuestion) queryObject(TestQuestion.class, criteria);
    }

    public List readByQuestion(IQuestion question) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyQuestion", question.getIdInternal());
        return queryList(TestQuestion.class, criteria);
    }

    public void deleteByTest(ITest test) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTest", test.getIdInternal());
        List testQuestions = queryList(TestQuestion.class, criteria);
        Iterator it = testQuestions.iterator();
        while (it.hasNext()) {
            delete((ITestQuestion) it.next());
        }
    }

    public void delete(ITestQuestion testQuestion) throws ExcepcaoPersistencia {
        super.delete(testQuestion);
    }
}