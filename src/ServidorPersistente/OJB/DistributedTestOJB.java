/*
 * Created on 19/Ago/2003
 *
 */
package ServidorPersistente.OJB;

import java.util.Iterator;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.DistributedTest;
import Dominio.IDisciplinaExecucao;
import Dominio.IDistributedTest;
import Dominio.ITest;
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

	public List readByExecutionCourse(IDisciplinaExecucao executionCourse)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"test.keyExecutionCourse",
			executionCourse.getIdInternal());
		return queryList(DistributedTest.class, criteria);
	}

	public List readByTest(ITest test) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("test.idInternal", test.getIdInternal());
		return queryList(DistributedTest.class, criteria);
	}

	public void deleteByTest(ITest test) throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo("keyTest", test.getIdInternal());
		List distributedTests = queryList(DistributedTest.class, criteria);
		Iterator it = distributedTests.iterator();
		while (it.hasNext()) {
			delete((IDistributedTest) it.next());
		}
	}

	public void delete(IDistributedTest distributedTest)
		throws ExcepcaoPersistencia {
		super.delete(distributedTest);
	}
}
