/*
 * Created on 29/Jul/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.ITestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTestQuestion;

import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;

/**
 * @author Susana Fernandes
 */
public class TestQuestionOJB extends PersistentObjectOJB implements IPersistentTestQuestion {

    public List<ITestQuestion> readByTest(Integer testId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTest", testId);
        QueryByCriteria queryCriteria = new QueryByCriteria(TestQuestion.class, criteria, false);
        queryCriteria.addOrderBy("testQuestionOrder", true);
        return queryList(queryCriteria);
    }

    public ITestQuestion readByTestAndQuestion(Integer testId, Integer questionId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTest", testId);
        criteria.addEqualTo("keyQuestion", questionId);
        return (ITestQuestion) queryObject(TestQuestion.class, criteria);
    }

    public List<ITestQuestion> readByQuestion(Integer questionId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyQuestion", questionId);
        return queryList(TestQuestion.class, criteria);
    }

}