/*
 * Created on 23/Jul/2003
 *  
 */

package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.domain.IDistributedTest;
import net.sourceforge.fenixedu.domain.IExecutionCourse;
import net.sourceforge.fenixedu.domain.IMetadata;
import net.sourceforge.fenixedu.domain.IStudentTestQuestion;
import net.sourceforge.fenixedu.domain.ITest;
import net.sourceforge.fenixedu.domain.ITestQuestion;
import net.sourceforge.fenixedu.domain.Metadata;
import net.sourceforge.fenixedu.domain.Question;
import net.sourceforge.fenixedu.domain.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.TestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentMetadata;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.odmg.HasBroker;

/**
 * @author Susana Fernandes
 */
public class MetadataOJB extends PersistentObjectOJB implements IPersistentMetadata {

    public MetadataOJB() {
    }

    public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());
        return queryList(Metadata.class, criteria);
    }

    public List readByExecutionCourseAndVisibility(IExecutionCourse executionCourse)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());
        criteria.addEqualTo("visibility", new Boolean("true"));
        return queryList(Metadata.class, criteria);
    }

    public List readByExecutionCourseAndVisibilityAndOrder(IExecutionCourse executionCourse,
            String order, String asc) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());
        criteria.addEqualTo("visibility", new Boolean("true"));

        PersistenceBroker pb = ((HasBroker) odmg.currentTransaction()).getBroker();
        QueryByCriteria queryCriteria = new QueryByCriteria(Metadata.class, criteria, false);

        if (asc != null && asc.equals("false"))
            queryCriteria.addOrderBy(order, false);
        else
            queryCriteria.addOrderBy(order, true);
        return (List) pb.getCollectionByQuery(queryCriteria);
    }

    public List readByExecutionCourseAndNotTest(IExecutionCourse executionCourse, ITest test,
            String order, String asc) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTest", test.getIdInternal());
        List testQuestionsList = queryList(TestQuestion.class, criteria);
        Collection testMetadatasIdInternals = CollectionUtils.collect(testQuestionsList,
                new Transformer() {

                    public Object transform(Object input) {
                        ITestQuestion testQuestion = (ITestQuestion) input;
                        return testQuestion.getQuestion().getMetadata().getIdInternal();
                    }
                });
        criteria = new Criteria();
        criteria.addEqualTo("visibility", new Boolean("true"));
        criteria.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());
        if (testMetadatasIdInternals.size() != 0)
            criteria.addNotIn("idInternal", testMetadatasIdInternals);

        PersistenceBroker pb = ((HasBroker) odmg.currentTransaction()).getBroker();
        QueryByCriteria queryCriteria = new QueryByCriteria(Metadata.class, criteria, false);
        if (asc != null && asc.equals("false"))
            queryCriteria.addOrderBy(order, false);
        else
            queryCriteria.addOrderBy(order, true);
        return (List) pb.getCollectionByQuery(queryCriteria);
    }

    public List readByExecutionCourseAndNotDistributedTest(IDistributedTest distributedTest)
            throws ExcepcaoPersistencia {
        PersistenceBroker pb = ((HasBroker) odmg.currentTransaction()).getBroker();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyDistributedTest", distributedTest.getIdInternal());
        QueryByCriteria queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria, true);
        queryCriteria.addGroupBy("keyQuestion");
        List studentTestQuestionList = (List) pb.getCollectionByQuery(queryCriteria);
        Collection metadatasIds = CollectionUtils.collect(studentTestQuestionList, new Transformer() {

            public Object transform(Object input) {
                IStudentTestQuestion studentTestQuestion = (IStudentTestQuestion) input;
                return studentTestQuestion.getQuestion().getMetadata().getIdInternal();
            }
        });
        criteria = new Criteria();
        criteria.addEqualTo("visibility", new Boolean("true"));
        criteria.addEqualTo("keyExecutionCourse", distributedTest.getTestScope().getDomainObject()
                .getIdInternal());
        if (metadatasIds.size() != 0)
            criteria.addNotIn("idInternal", metadatasIds);
        List result = queryList(Metadata.class, criteria);
        return result;
    }

    public int getNumberOfQuestions(IMetadata metadata) throws ExcepcaoPersistencia {
        PersistenceBroker pb = ((HasBroker) odmg.currentTransaction()).getBroker();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyMetadata", metadata.getIdInternal());
        QueryByCriteria queryCriteria = new QueryByCriteria(Question.class, criteria);
        return pb.getCount(queryCriteria);
    }

    public int countByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        PersistenceBroker pb = ((HasBroker) odmg.currentTransaction()).getBroker();
        Criteria criteria = new Criteria();
        criteria.addEqualTo("visibility", new Boolean(true));
        criteria.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());
        QueryByCriteria queryCriteria = new QueryByCriteria(Metadata.class, criteria);
        return pb.getCount(queryCriteria);
    }

    public void cleanMetadatas() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("visibility", new Boolean(false));
        List metadatas = queryList(Metadata.class, criteria);
        Iterator it = metadatas.iterator();
        while (it.hasNext()) {
            IMetadata metadata = (IMetadata) it.next();
            if (getNumberOfQuestions(metadata) == 0)
                delete(metadata);
        }
    }

    public void delete(IMetadata metadata) throws ExcepcaoPersistencia {
        super.delete(metadata);
    }
}