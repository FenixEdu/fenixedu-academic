/*
 * Created on 23/Jul/2003
 *  
 */

package net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests;

import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.Metadata;
import net.sourceforge.fenixedu.domain.onlineTests.Question;
import net.sourceforge.fenixedu.domain.onlineTests.StudentTestQuestion;
import net.sourceforge.fenixedu.domain.onlineTests.TestQuestion;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentMetadata;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;

/**
 * @author Susana Fernandes
 */
public class MetadataOJB extends PersistentObjectOJB implements IPersistentMetadata {

    public List<Metadata> readByExecutionCourse(ExecutionCourse executionCourse) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());
        return queryList(Metadata.class, criteria);
    }

    public List<Metadata> readByExecutionCourseAndVisibility(Integer executionCourseId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyExecutionCourse", executionCourseId);
        criteria.addEqualTo("visibility", new Boolean("true"));
        return queryList(Metadata.class, criteria);
    }

    public List<Metadata> readByExecutionCourseAndNotTest(Integer executionCourseId, Integer testId) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyTest", testId);
        List testQuestionsList = queryList(TestQuestion.class, criteria);
        Collection testMetadatasIdInternals = CollectionUtils.collect(testQuestionsList, new Transformer() {

            public Object transform(Object input) {
                TestQuestion testQuestion = (TestQuestion) input;
                return testQuestion.getQuestion().getMetadata().getIdInternal();
            }
        });
        criteria = new Criteria();
        criteria.addEqualTo("visibility", new Boolean("true"));
        criteria.addEqualTo("keyExecutionCourse", executionCourseId);
        if (testMetadatasIdInternals.size() != 0)
            criteria.addNotIn("idInternal", testMetadatasIdInternals);
        QueryByCriteria queryCriteria = new QueryByCriteria(Metadata.class, criteria, false);
        return queryList(queryCriteria);
    }

    public List<Metadata> readByExecutionCourseAndNotDistributedTest(Integer executionCourseId, Integer distributedTestId)
            throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyDistributedTest", distributedTestId);
        QueryByCriteria queryCriteria = new QueryByCriteria(StudentTestQuestion.class, criteria, true);
        queryCriteria.addGroupBy("keyQuestion");
        List studentTestQuestionList = queryList(queryCriteria);
        Collection metadatasIds = CollectionUtils.collect(studentTestQuestionList, new Transformer() {

            public Object transform(Object input) {
                StudentTestQuestion studentTestQuestion = (StudentTestQuestion) input;
                return studentTestQuestion.getQuestion().getMetadata().getIdInternal();
            }
        });
        criteria = new Criteria();
        criteria.addEqualTo("visibility", new Boolean("true"));
        criteria.addEqualTo("keyExecutionCourse", executionCourseId);
        if (metadatasIds.size() != 0)
            criteria.addNotIn("idInternal", metadatasIds);
        return queryList(Metadata.class, criteria);
    }

    public int getNumberOfQuestions(Metadata metadata) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("keyMetadata", metadata.getIdInternal());
        return count(Question.class, criteria);
    }

    public int countByExecutionCourse(Integer executionCourseId) {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("visibility", new Boolean(true));
        criteria.addEqualTo("keyExecutionCourse", executionCourseId);
        return count(Metadata.class, criteria);
    }

    public void cleanMetadatas() throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("visibility", new Boolean(false));
        List<Metadata> metadatas = queryList(Metadata.class, criteria);
        for (Metadata metadata : metadatas) {
            if (getNumberOfQuestions(metadata) == 0)
                delete(metadata);
        }
    }
}