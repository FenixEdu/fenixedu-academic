/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB;

import java.util.ArrayList;
import java.util.List;

import org.apache.ojb.broker.query.Criteria;

import net.sourceforge.fenixedu.domain.IDomainObject;
import net.sourceforge.fenixedu.domain.ITest;
import net.sourceforge.fenixedu.domain.ITestScope;
import net.sourceforge.fenixedu.domain.Test;
import net.sourceforge.fenixedu.domain.TestScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.IPersistentTest;

/**
 * @author Susana Fernandes
 */
public class TestOJB extends PersistentObjectOJB implements IPersistentTest {
    public TestOJB() {
    }

    public List readByTestScopeObject(IDomainObject object) throws ExcepcaoPersistencia {
        // Force object materialization to obtain correct class name for query.
        IDomainObject materializedObject = materialize(object);

        Criteria criteria = new Criteria();
        criteria.addEqualTo("className", materializedObject.getClass().getName());
        criteria.addEqualTo("keyClass", object.getIdInternal());
        ITestScope scope = (ITestScope) queryObject(TestScope.class, criteria);
        if (scope == null)
            return new ArrayList();
        criteria = new Criteria();
        criteria.addEqualTo("keyTestScope", scope.getIdInternal());
        return queryList(Test.class, criteria);
    }

    public List readAll() throws ExcepcaoPersistencia {
        return queryList(Test.class, null);
    }

    public void delete(ITest test) throws ExcepcaoPersistencia {
        super.delete(test);
    }
}