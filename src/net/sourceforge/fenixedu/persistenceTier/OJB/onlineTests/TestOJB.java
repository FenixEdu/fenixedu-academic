/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.OJB.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.ITest;
import net.sourceforge.fenixedu.domain.onlineTests.ITestScope;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.OJB.PersistentObjectOJB;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTest;

import org.apache.ojb.broker.query.Criteria;

/**
 * @author Susana Fernandes
 */
public class TestOJB extends PersistentObjectOJB implements IPersistentTest {

    public List<ITest> readByTestScope(String className, Integer idInternal) throws ExcepcaoPersistencia {
        Criteria criteria = new Criteria();
        criteria.addEqualTo("className", className);
        criteria.addEqualTo("keyClass", idInternal);
        ITestScope scope = (ITestScope) queryObject(TestScope.class, criteria);
        if (scope == null)
            return new ArrayList<ITest>();
        criteria = new Criteria();
        criteria.addEqualTo("keyTestScope", scope.getIdInternal());
        return queryList(Test.class, criteria);
    }

    public List<ITest> readAll() throws ExcepcaoPersistencia {
        return queryList(Test.class, null);
    }
}