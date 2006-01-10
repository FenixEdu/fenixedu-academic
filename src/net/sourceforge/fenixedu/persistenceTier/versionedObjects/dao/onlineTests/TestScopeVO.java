/*
 * Created on 3/Fev/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests;

import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTestScope;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * 
 * @author Susana Fernandes
 * 
 */
public class TestScopeVO extends VersionedObjectsBase implements IPersistentTestScope {

    public TestScope readByDomainObject(String className, Integer idInternal) throws ExcepcaoPersistencia {
        List<TestScope> testScopeList = (List<TestScope>) readAll(TestScope.class);
        for (TestScope testScope : testScopeList) {
            if (testScope.getClassName().equals(className) && testScope.getKeyClass().equals(idInternal)) {
                return testScope;
            }
        }
        return null;
    }

}