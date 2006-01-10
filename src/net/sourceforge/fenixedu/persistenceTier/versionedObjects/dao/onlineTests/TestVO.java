/*
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.Test;
import net.sourceforge.fenixedu.domain.onlineTests.TestScope;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentTest;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * @author Susana Fernandes
 */
public class TestVO extends VersionedObjectsBase implements IPersistentTest {

    public List<Test> readByTestScope(String className, Integer idInternal) throws ExcepcaoPersistencia {
        List<Test> testList = (List<Test>) readAll(Test.class);
        List<Test> result = new ArrayList<Test>();
        for (Test test : testList) {
            final TestScope testScope = test.getTestScope();
            if (testScope != null && testScope.getClassName().equals(className) && testScope.getKeyClass().equals(idInternal)) {
                result.add(test);
            }
        }
        return result;
    }

    public List<Test> readAll() throws ExcepcaoPersistencia {
        return (List<Test>) readAll(Test.class);
    }
}