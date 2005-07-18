/*
 * Created on 2/Fev/2004
 *
 */
package net.sourceforge.fenixedu.persistenceTier.versionedObjects.dao.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.domain.onlineTests.IOnlineTest;
import net.sourceforge.fenixedu.domain.onlineTests.OnlineTest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.onlineTests.IPersistentOnlineTest;
import net.sourceforge.fenixedu.persistenceTier.versionedObjects.VersionedObjectsBase;

/**
 * 
 * @author Susana Fernandes
 * 
 */
public class OnlineTestVO extends VersionedObjectsBase implements IPersistentOnlineTest {

    public Object readByDistributedTest(IDistributedTest distributedTest) throws ExcepcaoPersistencia {
        final List<IOnlineTest> onlineTestList = (List<IOnlineTest>) readAll(OnlineTest.class);
        List<IOnlineTest> result = new ArrayList<IOnlineTest>();
        for (IOnlineTest onlineTest : onlineTestList) {
            if (onlineTest.getKeyDistributedTest().equals(distributedTest.getIdInternal())) {
                result.add(onlineTest);
            }
        }
        return null;
    }
}