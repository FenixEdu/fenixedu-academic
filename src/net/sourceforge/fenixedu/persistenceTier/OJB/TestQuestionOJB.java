/*
 * Created on 29/Jul/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.IQuestion;
import net.sourceforge.fenixedu.domain.ITest;
import net.sourceforge.fenixedu.domain.ITestQuestion;
import net.sourceforge.fenixedu.domain.TestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTestQuestion;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.odmg.HasBroker;

/**
 * @author Susana Fernandes
 */
public class TestQuestionOJB extends PersistentObjectOJB implements IPersistentTestQuestion {

    public TestQuestionOJB() {
    }

    public List readByTest(ITest test) {
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