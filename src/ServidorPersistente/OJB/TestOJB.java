/*
 * Created on 28/Jul/2003
 *
 */
package ServidorPersistente.OJB;

import java.util.ArrayList;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import Dominio.IDomainObject;
import Dominio.ITest;
import Dominio.ITestScope;
import Dominio.Test;
import Dominio.TestScope;
import ServidorPersistente.ExcepcaoPersistencia;
import ServidorPersistente.IPersistentTest;

/**
 * @author Susana Fernandes
 */
public class TestOJB extends ObjectFenixOJB implements IPersistentTest
{
	public TestOJB()
	{
	}

	public List readByTestScopeObject(IDomainObject object) throws ExcepcaoPersistencia
	{
		Criteria criteria = new Criteria();
		criteria.addEqualTo("className", object.getClass().getName());
		criteria.addEqualTo("keyClass", object.getIdInternal());
		ITestScope scope = (ITestScope) queryObject(TestScope.class, criteria);
		if (scope == null)
			return new ArrayList();
		criteria = new Criteria();
		criteria.addEqualTo("keyTestScope", scope.getIdInternal());
		return queryList(Test.class, criteria);
	}

	public List readAll() throws ExcepcaoPersistencia
	{
		return queryList(Test.class, null);
	}

	public void delete(ITest test) throws ExcepcaoPersistencia
	{
		super.delete(test);
	}
}
