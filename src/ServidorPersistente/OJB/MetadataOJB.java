/*
 * Created on 23/Jul/2003
 *
 */
package ServidorPersistente.OJB;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.odmg.HasBroker;

import Dominio.IExecutionCourse;
import Dominio.IMetadata;
import Dominio.ITest;
import Dominio.ITestQuestion;
import Dominio.Metadata;
import Dominio.Question;
import Dominio.TestQuestion;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentMetadata;

/**
 * @author Susana Fernandes
 */

public class MetadataOJB extends ObjectFenixOJB implements IPersistentMetadata
{

	public MetadataOJB()
	{
	}

	public List readByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());
		return queryList(Metadata.class, criteria);
	}

	public List readByExecutionCourseAndVisibility(IExecutionCourse executionCourse)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());
		criteria.addEqualTo("visibility", new Boolean("true"));
		return queryList(Metadata.class, criteria);
	}

	public List readByExecutionCourseAndVisibilityAndOrder(
		IExecutionCourse executionCourse,
		String order,
		String asc)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());
		criteria.addEqualTo("visibility", new Boolean("true"));
		if (asc != null && asc.equals("false"))
			criteria.addOrderBy(order, false);
		else
			criteria.addOrderBy(order, true);
		return queryList(Metadata.class, criteria);
	}

	public List readByExecutionCourseAndNotTest(
		IExecutionCourse executionCourse,
		ITest test,
		String order,
		String asc)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyTest", test.getIdInternal());
		List testQuestionsList = queryList(TestQuestion.class, criteria);
		Collection testMetadatasIdInternals =
			CollectionUtils.collect(testQuestionsList, new Transformer()
		{
			public Object transform(Object input)
			{
				ITestQuestion testQuestion = (ITestQuestion) input;
				return testQuestion.getQuestion().getMetadata().getIdInternal();
			}
		});

		criteria = new Criteria();
		criteria.addEqualTo("visibility", new Boolean("true"));
		criteria.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());
		if (testMetadatasIdInternals.size() != 0)
			criteria.addNotIn("idInternal", testMetadatasIdInternals);
		if (asc != null && asc.equals("false"))
			criteria.addOrderBy(order, false);
		else
			criteria.addOrderBy(order, true);
		return queryList(Metadata.class, criteria);
	}

	public int getNumberOfQuestions(IMetadata metadata) throws ExcepcaoPersistencia
	{
		PersistenceBroker pb = ((HasBroker) odmg.currentTransaction()).getBroker();
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyMetadata", metadata.getIdInternal());
		QueryByCriteria queryCriteria = new QueryByCriteria(Question.class, criteria);
		return pb.getCount(queryCriteria);
	}

	public int countByExecutionCourse(IExecutionCourse executionCourse) throws ExcepcaoPersistencia
	{
		PersistenceBroker pb = ((HasBroker) odmg.currentTransaction()).getBroker();
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());
		QueryByCriteria queryCriteria = new QueryByCriteria(Metadata.class, criteria);
		return pb.getCount(queryCriteria);
	}

	public void delete(IMetadata metadata) throws ExcepcaoPersistencia
	{
		super.delete(metadata);
	}
}
