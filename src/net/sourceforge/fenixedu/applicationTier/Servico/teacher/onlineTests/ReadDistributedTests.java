/*
 * Created on 20/Ago/2003
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher.onlineTests;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.onlineTests.InfoDistributedTest;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.onlineTests.DistributedTest;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

/**
 * @author Susana Fernandes
 */
public class ReadDistributedTests extends Service {

    public List<InfoDistributedTest> run(Integer executionCourseId) throws ExcepcaoPersistencia {
        List<DistributedTest> distributedTests = persistentSupport.getIPersistentDistributedTest().readByTestScope(ExecutionCourse.class.getName(),
                executionCourseId);
        List<InfoDistributedTest> infoDistributedTestList = new ArrayList<InfoDistributedTest>();
        for (DistributedTest distributedTest : distributedTests) {
            infoDistributedTestList.add(InfoDistributedTest.newInfoFromDomain(distributedTest));
        }
        return infoDistributedTestList;
    }
}