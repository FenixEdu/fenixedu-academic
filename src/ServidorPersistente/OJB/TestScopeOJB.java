/*
 * Created on 3/Fev/2004
 *
 */
package ServidorPersistente.OJB;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IDomainObject;
import Dominio.ITestScope;
import Dominio.TestScope;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTestScope;

/**
 *
 * @author Susana Fernandes
 *
 */
public class TestScopeOJB extends ObjectFenixOJB implements IPersistentTestScope
{
	public TestScopeOJB()
	{
	}

	public ITestScope readByDomainObject(IDomainObject object) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("className", object.getClass().getName());
		criteria.addEqualTo("keyClass", object.getIdInternal());

		return (ITestScope) queryObject(TestScope.class, criteria);
	}
}
