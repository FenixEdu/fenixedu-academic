/*
 * Created on 19/Ago/2003
 *
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.DistributedTest;
import Dominio.IExecutionCourse;
import Dominio.IDistributedTest;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentDistributedTest;

/**
 * @author Susana Fernandes
 */
public class DistributedTestOJB
	extends ObjectFenixOJB
	implements IPersistentDistributedTest {

	public DistributedTestOJB() {
	}

	public List readByExecutionCourse(IExecutionCourse executionCourse)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"keyExecutionCourse",
			executionCourse.getIdInternal());
		return queryList(DistributedTest.class, criteria);
	}

	public void delete(IDistributedTest distributedTest)
		throws ExcepcaoPersistencia {
		super.delete(distributedTest);
	}
}
