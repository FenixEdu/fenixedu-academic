/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.ITest;
import net.sourceforge.fenixedu.domain.onlineTests.ITestScope;
import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTest;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Susana Fernandes
 */
public class TestVO extends VersionedObjectsBase implements IPersistentTest {

    public List<ITest> readByTestScope(String className, Integer idInternal) throws ExcepcaoPersistencia {
        List<ITest> testList = (List<ITest>) readAll(Test.class);
        List<ITest> result = new ArrayList<ITest>();
        for (ITest test : testList) {
            final ITestScope testScope = test.getTestScope();
            if (testScope != null && testScope.getClassName().equals(className) && testScope.getKeyClass().equals(idInternal)) {
                result.add(test);
            }
        }
        return result;
    }

    public List<ITest> readAll() throws ExcepcaoPersistencia {
        return (List<ITest>) readAll(Test.class);
    }
}