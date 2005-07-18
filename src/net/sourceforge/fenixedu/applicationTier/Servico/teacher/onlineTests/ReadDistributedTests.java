/*
 * Created on 20/Ago/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.IDistributedTest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.persistenceTier.ISuportePersistente;
import net.sourceforge.fenixedu.persistenceTier.PersistenceSupportFactory;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Susana Fernandes
 */
public class ReadDistributedTests implements IService {

    public List<InfoDistributedTest> run(Integer executionCourseId) throws ExcepcaoPersistencia {
        ISuportePersistente persistentSuport = PersistenceSupportFactory.getDefaultPersistenceSupport();
        List<IDistributedTest> distributedTests = persistentSuport.getIPersistentDistributedTest().readByTestScope(ExecutionCourse.class.getName(),
                executionCourseId);
        List<InfoDistributedTest> infoDistributedTestList = new ArrayList<InfoDistributedTest>();
        for (IDistributedTest distributedTest : distributedTests) {
            infoDistributedTestList.add(InfoDistributedTest.newInfoFromDomain(distributedTest));
        }
        return infoDistributedTestList;
    }
}