/*
 * Created on 23/Jul/2003
 *
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.PersistenceBroker;
import org.apache.ojb.broker.query.Criteria;
import org.apache.ojb.broker.query.QueryByCriteria;
import org.apache.ojb.odmg.HasBroker;

import Dominio.IExecutionCourse;
import Dominio.IMetadata;
import Dominio.Metadata;
import Dominio.Question;
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
		String order)
		throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyExecutionCourse", executionCourse.getIdInternal());
		criteria.addEqualTo("visibility", new Boolean("true"));
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

	public void delete(IMetadata metadata) throws ExcepcaoPersistencia
	{
		super.delete(metadata);
	}
}
