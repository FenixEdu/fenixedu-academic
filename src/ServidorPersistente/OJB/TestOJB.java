/*
 * Created on 28/Jul/2003
 *
 */
package ServidorPersistente.OJB;

import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IExecutionCourse;
import Dominio.ITest;
import Dominio.Test;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTest;

/**
 * @author Susana Fernandes
 */
public class TestOJB extends ObjectFenixOJB implements IPersistentTest {
	public TestOJB() {
	}

	public List readByExecutionCourse(IExecutionCourse executionCourse)
		throws ExcepcaoPersistencia {
		Criteria criteria = new Criteria();
		criteria.addEqualTo(
			"keyExecutionCourse",
			executionCourse.getIdInternal());
		return queryList(Test.class, criteria);
	}
	
	public void delete (ITest test) throws ExcepcaoPersistencia{
		super.delete(test);
	}
}
