/*
 * Created on 19/Ago/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.DistributedTest;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.IDistributedTest;
import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IStudent;
import net.sourceforge.fenixedu.domain.IStudentTestQuestion;
import net.sourceforge.fenixedu.domain.ITestScope;
import net.sourceforge.fenixedu.domain.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.TestScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentDistributedTest;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.odmg.HasBroker;

/**
 * @author Susana Fernandes
 */
public class DistributedTestOJB extends PersistentObjectOJB implements IPersistentDistributedTest {

    public DistributedTestOJB() {
    }

    public List readByTestScopeObject(IDomainObject object) throws ExcepcaoPersistencia {

        //		 Force object materialization to obtain correct class name for query.
        IDomainObject materializedObject = materialize(object);

        Criteria criteria = new Criteria();
        criteria.addEqualTo("className", materializedObject.getClass().getName());
        criteria.addEqualTo("keyClass", object.getIdInternal());
        ITestScope scope = (ITestScope) queryObject(TestScope.class, criteria);
        if (scope == null)
            return new ArrayList();
        criteria = new Criteria();
        criteria.addEqualTo("keyTestScope", scope.getIdInternal());
        return queryList(DistributedTest.class, criteria);
    }

    public List readByStudent(IStudent student) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyStudent", student.getIdInternal());
        criteria.addEqualTo("testQuestionOrder", new Integer(1));
        PersistenceBroker pb = ((HasBroker) odmg.currentTransaction()).getBroker();
        QueryByCriteria queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria, false);
        List result = (List) pb.getCollectionByQuery(queryCriteria);
        lockRead(result);
        List distributedTestList = new ArrayList();
        Iterator iterator = result.iterator();
        while (iterator.hasNext())
            distributedTestList.add(((IStudentTestQuestion) iterator.next()).getDistributedTest());
        return distributedTestList;
    }

    public List readByStudentAndExecutionCourse(IStudent student, IExecutionCourse executionCourse)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyStudent", student.getIdInternal());
        criteria.addEqualTo("distributedTest.testScope.className", ExecutionCourse.class.getName());
        criteria.addEqualTo("distributedTest.testScope.keyClass", executionCourse.getIdInternal());
        criteria.addEqualTo("testQuestionOrder", new Integer(1));
        PersistenceBroker pb = ((HasBroker) odmg.currentTransaction()).getBroker();
        QueryByCriteria queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria, false);
        List result = (List) pb.getCollectionByQuery(queryCriteria);
        lockRead(result);
        List distributedTestList = new ArrayList();
        Iterator iterator = result.iterator();
        while (iterator.hasNext())
            distributedTestList.add(((IStudentTestQuestion) iterator.next()).getDistributedTest());
        return distributedTestList;
    }

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(DistributedTest.class, null);
    }

    public void delete(IDistributedTest distributedTest) throws ExcepcaoPersistencia {
        super.delete(distributedTest);
    }
}