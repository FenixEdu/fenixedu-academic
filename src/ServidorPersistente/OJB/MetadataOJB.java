/*
 * Created on 23/Jul/2003
 *
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IExecutionCourse;
import Dominio.IMetadata;
import Dominio.Metadata;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentMetadata;

/**
 * @author Susana Fernandes
 */

public class MetadataOJB
	extends ObjectFenixOJB
	implements IPersistentMetadata {

	public MetadataOJB() {
	}

	public List readByExecutionCourse(IExecutionCourse executionCourse)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"keyExecutionCourse",
			executionCourse.getIdInternal());
		return queryList(Metadata.class, criteria);
	}

	public List readByExecutionCourseAndVisibility(IExecutionCourse executionCourse)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"keyExecutionCourse",
			executionCourse.getIdInternal());
		criteria.addEqualTo("visibility", new Boolean("true"));
		return queryList(Metadata.class, criteria);
	}

	public void delete(IMetadata metadata) throws ExcepcaoPersistencia {
		super.delete(metadata);
	}
}
